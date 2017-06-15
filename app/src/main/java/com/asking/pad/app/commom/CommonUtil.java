package com.asking.pad.app.commom;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.HttpCodeConstant;
import com.asking.pad.app.base.BaseActivity;
import com.asking.pad.app.entity.RequestEntity;
import com.asking.pad.app.ui.login.LoginActivity;
import com.asking.pad.app.ui.mine.UserInfoActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by jswang on 2017/4/6.
 */

public class CommonUtil {

    public static void openActivity(Class<? extends BaseActivity> toActivity, Bundle bundle) {
        Intent intent = new Intent(AppContext.getInstance().getApplicationContext(), toActivity);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        AppContext.getInstance().startActivity(intent);
    }

    public static void openActivity(Class<? extends BaseActivity> toActivity) {
        openActivity(toActivity, null);
    }

    public static void startActivity(Class toActivity, Bundle bundle) {
        Intent intent = new Intent(AppContext.getInstance().getApplicationContext(), toActivity);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        AppContext.getInstance().startActivity(intent);
    }

    public static void startActivity(Class toActivity) {
        openActivity(toActivity, null);
    }

    public static void Toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static RequestEntity getRequestEntity(Throwable throwable) {
        return getRequestEntity(throwable, null);
    }

    public static RequestEntity getRequestEntity(Throwable throwable, String tag) {
        RequestEntity requestEntity = new RequestEntity();
        try {
            requestEntity.setTag(tag == null ? "" : tag);
            if (throwable.getMessage() != null && throwable.getMessage().indexOf("No address associated with hostname") != -1) {
                requestEntity.setDetailMessage(throwable.getMessage());
                requestEntity.setCode(((HttpException) throwable).code());
                requestEntity.setMessage(((HttpException) throwable).message());
            } else if (throwable instanceof HttpException) {
                if (((HttpException) throwable).code() == 404)
                    requestEntity.setCode(HttpCodeConstant.CONNECT_404);
            } else if (throwable instanceof java.net.ConnectException) {
                requestEntity.setCode(HttpCodeConstant.CONNECT_FAIL);
            } else if (throwable instanceof SocketTimeoutException) {
                requestEntity.setCode(HttpCodeConstant.TIME_OUT);
            } else {
                requestEntity.setCode(HttpCodeConstant.APP_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestEntity;
    }

    /**
     * String obj 转 list
     *
     * @param
     * @param <T>
     * @return
     */
    public static <T> T parseDataToList(ResponseBody body, TypeToken<T> token) {
        Gson gson = new Gson();
        T list = null;
        try {
            list = gson.fromJson(body.string(), token.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static <T> T parseDataToList(String res, TypeToken<T> token) {
        Gson gson = new Gson();
        T list = null;
        try {
            list = gson.fromJson(res, token.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

        public static String isEmpty(String str,String defStr) {
        if (!TextUtils.isEmpty(str))
            return str;
        return defStr;
    }

    public static boolean isEnglish(String str){
        if(!TextUtils.isEmpty(str))
            return str.matches(".*[a-zA-z$\\\\%].*");
        return false;
    }

    public static RequestBody getRequestBody(String value) {
        if (TextUtils.isEmpty(value)) {
            value = "";
        }
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    public static String decode1(String str) {
        str = str.replaceAll("\\\\u([0-9a-zA-Z]{4})", "");
        return str;
    }

    public static String getReplaceStr(String str) {
        str = str.replaceAll("\\\\documentstyle\\[12pt\\]\\{article\\} \\n \\\\begin\\{document\\} \\n \\\\begin\\{displaymath\\} \\n ", "");
        str = str.replaceAll("\\\\end\\{displaymath\\} \\n \\\\end\\{document\\}", "");
        return str;
    }

    public static String getReplaceStr(String res, String... regexs) {
        for (int i = 0; i < regexs.length; i++) {
            String regex = regexs[i];
            res = res.replaceAll(regex, "");
        }
        return res;
    }

    public static String changePathImg(String content) {
        if (!NetworkUtils.isNetworkAvailable()) {
            String addPath = String.format("file://%sresources/", Constants.getBookPath(""));
            String imgReg = "<img[^>]*src=['\"]([^'\"]+)[^>]*>";
            Pattern imgPattern = Pattern.compile(imgReg);
            Matcher m = imgPattern.matcher(content);
            while (m.find()) {
                String img = m.group();
                String src = img.replaceAll("<img[^>]*src=['\"]", "");
                src = src.replaceAll("['\"][^>]*>", "");
                String path = src.replaceAll("([a-zA-z]+://[a-zA-Z0-9.]+/)", "");
                String newImg = img.replace(src, addPath + path);
                content = content.replace(img, newImg);
            }
        }
        return content;
    }

    public static String changeNetFile(String content) {
        if (!TextUtils.isEmpty(content)) {
            content = content.substring(1,content.length()-1);
            String ss = content.replaceAll("([a-zA-z]+://[a-zA-Z0-9.]+/)", "");
            content = String.format("%sresources/%s", Constants.getBookPath(""), ss);
        }
        return content;
    }


    /**
     * 获取app版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersionName(Context context) {

        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = context.getString(R.string.version_name, info.versionName);
        return version;
    }

    public static void openAuthActivity(Class<? extends BaseActivity> toActivity) {
        openAuthActivity(toActivity, null);
    }

    public static void openAuthActivity(Class<? extends BaseActivity> toActivity, Bundle parameter) {
        //判断登陆状态
        if (AppContext.getInstance().getUserEntity() != null) {
            openActivity(toActivity, parameter);
        } else {
            //跳转登陆
            openActivity(LoginActivity.class);
        }
    }

    public static void openAuthUserDataActivity(Class<? extends BaseActivity> toActivity) {
        openAuthUserDataActivity(toActivity, null);
    }

    public static void openAuthUserDataActivity(Class<? extends BaseActivity> toActivity, Bundle parameter) {
        //查看资料是否完善
        if (AppContext.getInstance().isUserDataPerfect()) {
            openActivity(toActivity, parameter);
        } else {
            openActivity(UserInfoActivity.class);
        }
    }
}
