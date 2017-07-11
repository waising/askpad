package com.asking.pad.app.ui.classmedia.download;

import com.asking.pad.app.AppContext;
import com.asking.pad.app.BuildConfig;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.entity.classmedia.ClassMediaTable;
import com.asking.pad.app.ui.downbook.db.DbHelper;
import com.asking.pad.app.ui.downbook.download.DownLoadApi;
import com.asking.pad.app.ui.downbook.download.DownState;
import com.asking.pad.app.ui.downbook.download.RetryWhenNetworkException;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
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
 * Created by jswang on 2017/6/12.
 */

public class ClassDownloadManager {
    private final int DEFAULT_TIMEOUT = 10;

    /*记录下载数据*/
    private Set<ClassMediaTable> mDownList;
    /*回调sub队列*/
    private HashMap<String, ProgressDownSubscriber> subMap;
    /*单利对象*/
    private volatile static ClassDownloadManager INSTANCE;

    private ClassDownloadManager() {
        mDownList = new HashSet<>();
        subMap = new HashMap<>();
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static ClassDownloadManager getInstance() {
        if (INSTANCE == null) {
            synchronized (ClassDownloadManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ClassDownloadManager();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * 开始下载
     */
    public void startDown(final ClassMediaTable info) {
        /*正在下载不处理*/
        if (info == null || subMap.get(info.getVideoUrl()) != null) {
            return;
        }
        info.setUserId(AppContext.getInstance().getUserId());
        /*添加回调处理类*/
        ProgressDownSubscriber subscriber = new ProgressDownSubscriber(info);
        /*记录回调sub*/
        subMap.put(info.getVideoUrl(), subscriber);
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
        Observable<ResponseBody> mObservable = httpService.download("bytes=" + info.getReadLength() + "-", info.getVideoUrl());
                /*指定线程*/
        mObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                   /*失败后的retry配置*/
                .retryWhen(new RetryWhenNetworkException())
                /*读取下载写入文件*/
                .map(new Func1<ResponseBody, ClassMediaTable>() {
                    @Override
                    public ClassMediaTable call(ResponseBody responseBody) {
                        File mFile = new File(Constants.getVideoPath(info.getVideoUrl()));
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
    public void stopDown(ClassMediaTable info) {
        if (info == null) return;
        info.setDownState(DownState.STOP);
        if (subMap.containsKey(info.getVideoUrl())) {
            ProgressDownSubscriber subscriber = subMap.get(info.getVideoUrl());
            subscriber.unsubscribe();
            subMap.remove(info.getVideoUrl());
        }
        /*同步数据库*/
    }


    /**
     * 删除
     *
     * @param info
     */
    public void deleteDown(ClassMediaTable info) {
        DbHelper.getInstance().deleteByIdClassMedia(info.getCourseDataId());
        stopDown(info);
         /*删除数据库信息和本地文件*/
    }


    /**
     * 暂停下载
     *
     * @param info
     */
    public void pause(ClassMediaTable info) {
        try{
            if (info == null) return;
            info.setDownState(DownState.PAUSE);
            if (subMap.containsKey(info.getVideoUrl())) {
                ProgressDownSubscriber subscriber = subMap.get(info.getVideoUrl());
                subscriber.unsubscribe();
                subMap.remove(info.getVideoUrl());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        /*这里需要讲info信息写入到数据中，可自由扩展，用自己项目的数据库*/
    }

    /**
     * 停止全部下载
     */
    public void stopAllDown() {
        for (ClassMediaTable e : mDownList) {
            stopDown(e);
        }
        subMap.clear();
        mDownList.clear();
    }

    /**
     * 暂停全部下载
     */
    public void pauseAll() {
        for (ClassMediaTable e : mDownList) {
            pause(e);
        }
        subMap.clear();
        mDownList.clear();
    }


    /**
     * 返回全部正在下载的数据
     *
     * @return
     */
    public Set<ClassMediaTable> getClassMediaTables() {
        return mDownList;
    }


    /**
     * 写入文件
     *
     * @param file
     * @param info
     * @throws IOException
     */
    public void writeCache(ResponseBody responseBody, File file, ClassMediaTable info) throws IOException {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");

        randomAccessFile.seek(info.getReadLength());

        byte[] buffer = new byte[1024 * 8];
        int len;
        while ((len = responseBody.byteStream().read(buffer)) != -1) {
            randomAccessFile.write(buffer, 0, len);
        }
        responseBody.byteStream().close();

        if (randomAccessFile != null) {
            randomAccessFile.close();
        }
    }
}
