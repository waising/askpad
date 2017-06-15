package com.asking.pad.app.ui.downbook.download.DownLoadListener;

import com.asking.pad.app.entity.BookInfo;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 成功回调处理
 * Created by jswang on 2016/10/20.
 */
public class DownloadInterceptor implements Interceptor {

    private BookInfo mBookInfo;

    public DownloadInterceptor(BookInfo mBookInfo) {
        this.mBookInfo = mBookInfo;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        return originalResponse.newBuilder()
                .body(new DownloadResponseBody(originalResponse.body(), mBookInfo))
                .build();
    }
}
