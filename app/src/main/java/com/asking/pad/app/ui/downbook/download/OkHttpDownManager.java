package com.asking.pad.app.ui.downbook.download;

import com.asking.pad.app.AppContext;
import com.asking.pad.app.BuildConfig;
import com.asking.pad.app.entity.book.BookDownInfo;
import com.asking.pad.app.ui.downbook.db.DbHelper;
import com.asking.pad.app.ui.downbook.download.DownLoadListener.DownloadInterceptor;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jswang on 2017/5/2.
 */

public class OkHttpDownManager {

    private final int DEFAULT_TIMEOUT = 10;

    /*记录下载数据*/
    private Set<BookDownInfo> BookInfos;
    /*回调sub队列*/
    private HashMap<String, ProgressDownSubscriber> subMap;
    /*单利对象*/
    private volatile static OkHttpDownManager INSTANCE;

    private OkHttpDownManager() {
        BookInfos = new HashSet<>();
        subMap = new HashMap<>();
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static OkHttpDownManager getInstance() {
        if (INSTANCE == null) {
            synchronized (OkHttpDownManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new OkHttpDownManager();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * 开始下载
     */
    public void startDown(final BookDownInfo info) {
        /*正在下载不处理*/
        if (info == null || subMap.get(info.getDownloadUrl()) != null) {
            return;
        }
        info.setUserId(AppContext.getInstance().getUserId());
        /*添加回调处理类*/
        ProgressDownSubscriber subscriber = new ProgressDownSubscriber(info);
        /*记录回调sub*/
        subMap.put(info.getDownloadUrl(), subscriber);
        /*获取service，多次请求公用一个sercie*/
        DownLoadApi httpService;

        DownloadInterceptor interceptor = new DownloadInterceptor(info);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //手动创建一个OkHttpClient并设置超时时间
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BuildConfig.API_URL)
                .build();
        httpService = retrofit.create(DownLoadApi.class);

        /*得到rx对象-上一次下載的位置開始下載*/
        Observable<ResponseBody> mObservable = httpService.download("bytes=" + info.getReadLength() + "-", info.getDownloadUrl());
                /*指定线程*/
        mObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                   /*失败后的retry配置*/
                .retryWhen(new RetryWhenNetworkException())
                /*读取下载写入文件*/
                .map(new Func1<ResponseBody, BookDownInfo>() {
                    @Override
                    public BookDownInfo call(ResponseBody responseBody) {
                        File mFile = new File(info.getSavePath());
                        try {
                            writeCache(responseBody, mFile, info);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return info;
                    }
                })
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*数据回调*/
                .subscribe(subscriber);
    }


    /**
     * 停止下载
     */
    public void stopDown(BookDownInfo info) {
        if (info == null) return;
        info.setDownState(DownState.STOP);
        if (subMap.containsKey(info.getDownloadUrl())) {
            ProgressDownSubscriber subscriber = subMap.get(info.getDownloadUrl());
            subscriber.unsubscribe();
            subMap.remove(info.getDownloadUrl());
        }
        /*同步数据库*/
    }


    /**
     * 删除
     *
     * @param info
     */
    public void deleteDown(BookDownInfo info) {
        DbHelper.getInstance().deleteByIdBookInfo(info.getCommodityId());
        stopDown(info);
         /*删除数据库信息和本地文件*/
    }


    /**
     * 暂停下载
     *
     * @param info
     */
    public void pause(BookDownInfo info) {
        try{
            if (info == null) return;
            info.setDownState(DownState.PAUSE);
            if (subMap.containsKey(info.getDownloadUrl())) {
                ProgressDownSubscriber subscriber = subMap.get(info.getDownloadUrl());
                subscriber.unsubscribe();
                subMap.remove(info.getDownloadUrl());
            }
        }catch (Exception e){}

        /*这里需要讲info信息写入到数据中，可自由扩展，用自己项目的数据库*/
    }

    /**
     * 停止全部下载
     */
    public void stopAllDown() {
        for (BookDownInfo BookInfo : BookInfos) {
            stopDown(BookInfo);
        }
        subMap.clear();
        BookInfos.clear();
    }

    /**
     * 暂停全部下载
     */
    public void pauseAll() {
        for (BookDownInfo BookInfo : BookInfos) {
            pause(BookInfo);
        }
        subMap.clear();
        BookInfos.clear();
    }


    /**
     * 返回全部正在下载的数据
     *
     * @return
     */
    public Set<BookDownInfo> getBookInfos() {
        return BookInfos;
    }


    /**
     * 写入文件
     *
     * @param file
     * @param info
     * @throws IOException
     */
    public void writeCache(ResponseBody responseBody, File file, BookDownInfo info) throws IOException {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        long allLength;
        if (info.getCountLength() == 0) {
            allLength = responseBody.contentLength();
        } else {
            allLength = info.getCountLength();
        }
        FileChannel channelOut = null;
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
        channelOut = randomAccessFile.getChannel();
        MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE,
                info.getReadLength(), allLength - info.getReadLength());
        byte[] buffer = new byte[1024 * 8];
        int len;
        while ((len = responseBody.byteStream().read(buffer)) != -1) {
            mappedBuffer.put(buffer, 0, len);
        }
        responseBody.byteStream().close();
        if (channelOut != null) {
            channelOut.close();
        }
        if (randomAccessFile != null) {
            randomAccessFile.close();
        }
    }

}
