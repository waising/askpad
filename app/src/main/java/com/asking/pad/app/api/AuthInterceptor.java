package com.asking.pad.app.api;

import com.asking.pad.app.AppContext;
import com.asking.pad.app.commom.AppLoginEvent;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.ToastUtil;
import com.asking.pad.app.ui.login.LoginActivity;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jswang on 2017/4/6.
 */

public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        if(response.code()==401 && -1 == request.url().encodedPath().indexOf("applogin")){
            ToastUtil.showMessage("你还没有登录，请登录后在操作!");
            AppContext.getInstance().clearLoginInfo();
            CommonUtil.openActivity(LoginActivity.class);
            EventBus.getDefault().post(new AppLoginEvent(AppLoginEvent.LOGIN_OUT));
        }

        return response;
    }

}
