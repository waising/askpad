package com.asking.pad.app.ui.downbook.download;

import com.asking.pad.app.AppContext;
import com.asking.pad.app.BuildConfig;
import com.asking.pad.app.entity.BookInfo;
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

import de.greenrobot.event.EventBus;
import im.amomo.andun7z.AndUn7z;
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
    private Set<BookInfo> BookInfos;
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
    public void startDown(final BookInfo info) {
        /*正在下载不处理*/
        if (info == null || subMap.get(info.getUrl()) != null) {
            return;
        }
        info.setUserId(AppContext.getInstance().getUserId());
        /*添加回调处理类*/
        ProgressDownSubscriber subscriber = new ProgressDownSubscriber(info);
        /*记录回调sub*/
        subMap.put(info.getUrl(), subscriber);
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
        Observable<ResponseBody> mObservable = httpService.download("bytes=" + info.getReadLength() + "-", info.getUrl());
                /*指定线程*/
        mObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                   /*失败后的retry配置*/
                .retryWhen(new RetryWhenNetworkException())
                /*读取下载写入文件*/
                .map(new Func1<ResponseBody, BookInfo>() {
                    @Override
                    public BookInfo call(ResponseBody responseBody) {
                        File mFile = new File(info.getSavePath());
                        try {
                            writeCache(responseBody, mFile, info);
                        } catch (IOException e) {
                        }
                        if (mFile.exists() && info.getReadLength() > 0 && info.getReadLength() == info.getCountLength()) {
                            info.setDownState(DownState.UN7ZIP);
                            EventBus.getDefault().post(info);
                            AndUn7z.extract7z(mFile.getAbsolutePath(), info.getBookUnZipPath());
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
    public void stopDown(BookInfo info) {
        if (info == null) return;
        info.setDownState(DownState.STOP);
        if (subMap.containsKey(info.getUrl())) {
            ProgressDownSubscriber subscriber = subMap.get(info.getUrl());
            subscriber.unsubscribe();
            subMap.remove(info.getUrl());
        }
        /*同步数据库*/
    }


    /**
     * 删除
     *
     * @param info
     */
    public void deleteDown(BookInfo info) {
        DbHelper.getInstance().deleteByIdBookInfo(info.getBookId());
        stopDown(info);
         /*删除数据库信息和本地文件*/
    }


    /**
     * 暂停下载
     *
     * @param info
     */
    public void pause(BookInfo info) {
        try{
            if (info == null) return;
            info.setDownState(DownState.PAUSE);
            if (subMap.containsKey(info.getUrl())) {
                ProgressDownSubscriber subscriber = subMap.get(info.getUrl());
                subscriber.unsubscribe();
                subMap.remove(info.getUrl());
            }
        }catch (Exception e){}

        /*这里需要讲info信息写入到数据中，可自由扩展，用自己项目的数据库*/
    }

    /**
     * 停止全部下载
     */
    public void stopAllDown() {
        for (BookInfo BookInfo : BookInfos) {
            stopDown(BookInfo);
        }
        subMap.clear();
        BookInfos.clear();
    }

    /**
     * 暂停全部下载
     */
    public void pauseAll() {
        for (BookInfo BookInfo : BookInfos) {
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
    public Set<BookInfo> getBookInfos() {
        return BookInfos;
    }


    /**
     * 写入文件
     *
     * @param file
     * @param info
     * @throws IOException
     */
    public void writeCache(ResponseBody responseBody, File file, BookInfo info) throws IOException {
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
