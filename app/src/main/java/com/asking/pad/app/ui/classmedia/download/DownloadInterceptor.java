package com.asking.pad.app.ui.classmedia.download;

import com.asking.pad.app.entity.classmedia.ClassMediaTable;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 成功回调处理
 * Created by jswang on 2016/10/20.
 */
public class DownloadInterceptor implements Interceptor {

    private ClassMediaTable info;

    public DownloadInterceptor(ClassMediaTable info) {
        this.info = info;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        return originalResponse.newBuilder()
                .body(new DownloadResponseBody(originalResponse.body(), info))
                .build();
    }
}
