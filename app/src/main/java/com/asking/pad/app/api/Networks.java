package com.asking.pad.app.api;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.asking.pad.app.AppContext;
import com.asking.pad.app.BuildConfig;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jswang on 2017/4/6.
 */

public class Networks {
    private static final int DEFAULT_TIMEOUT = 10;

    private static Retrofit retrofit;

    private static Networks mNetworks;

    public static Networks getInstance() {
        if (mNetworks == null) {
            mNetworks = new Networks();
        }
        return mNetworks;
    }


    //--------------------- 模块api--------------
    //---------------------BEGIN--------------

    private static UserApi mUserApi;

    //---------------------END--------------

    //------------------------get -----------------

    public UserApi getUserApi() {
        return mUserApi == null ? configRetrofit(UserApi.class) : mUserApi;
    }

    //---------------------基础配置-----------------
    private <T> T configRetrofit(Class<T> service) {
        retrofit = retrofitBuilder();

        return retrofit.create(service);
    }

    private Retrofit retrofitBuilder(){
        if (retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.API_URL)
                    .client(configClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private final ArrayMap<String, List<Cookie>> cookieStore = new ArrayMap<>();

    private OkHttpClient configClient() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        //token拦截器
//        okHttpClient.addNetworkInterceptor(new TokenInterceptor());
        //为所有请求添加头部 Header 配置的拦截器
        Interceptor headerIntercept = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                builder.addHeader("X-Client-Platform", "Android");
                builder.addHeader("X-Client-Version", BuildConfig.VERSION_NAME);
                builder.addHeader("X-Client-Build", String.valueOf(BuildConfig.VERSION_CODE));

                builder.removeHeader("Accept");
                builder.addHeader("Accept", "*/*");
                builder.addHeader("Authorization", AppContext.getInstance().getToken());
                builder.addHeader("x-requested-with"," x-requested-with");

                Request request = builder.build();

                return chain.proceed(request);
            }
        };

        // Log信息拦截器
        if (BuildConfig.LOG_DEBUG) {
            Interceptor loggingIntercept = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    ResponseBody responseBody = response.body();
                    BufferedSource source = responseBody.source();
                    source.request(Long.MAX_VALUE); // Buffer the entire body.
                    Buffer buffer = source.buffer();
                    Charset UTF8 = Charset.forName("UTF-8");
                    Log.i("REQUEST_URL", request.toString());
                    Log.i("REQUEST_JSON", buffer.clone().readString(UTF8));
//                    Object o = buffer.clone().readString(UTF8);
//                    JLog.logi("REQUEST_JSON2", o.toString());
                    return response;
                }
            };
            okHttpClient.addInterceptor(loggingIntercept);
        }

        okHttpClient.dns(OkHttpDns.getInstance(AppContext.getInstance()));

        okHttpClient.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.addNetworkInterceptor(headerIntercept);
        okHttpClient.addNetworkInterceptor(new AuthInterceptor());

        //自动管理cookie
        okHttpClient.cookieJar(new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                cookieStore.put(httpUrl.host(), list);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                List<Cookie> cookies = cookieStore.get(httpUrl.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        });

        return okHttpClient.build();
    }
}
