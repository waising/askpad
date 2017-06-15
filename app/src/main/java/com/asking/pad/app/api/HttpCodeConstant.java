package com.asking.pad.app.api;

import com.asking.pad.app.entity.RequestEntity;

/**
 * Created by jswang on 2017/4/6.
 */

public class HttpCodeConstant {


    public final static int TIME_OUT = 999;
    public final static int CONNECT_FAIL = 1000;//连接失败

    public final static int CONNECT_404 = 404;//
    public final static int CONNECT_401= 401;//

    public static final int HTTP_NO_NETWORK = 999;//无网络标识
    public static final int APP_ERROR = 99999;//未知错误

    public static String getErrorMsg(RequestEntity requestEntity){
        String errMsg = "";
        switch (requestEntity.getCode()){
            case CONNECT_404:
                errMsg = "服务接口访问失败";
                break;
            case CONNECT_401:
                break;
            case TIME_OUT:
                errMsg = "访问服务器超时";
                break;
            case CONNECT_FAIL:
                errMsg = "服务接口访问失败";
                break;
            case APP_ERROR:
                //errMsg = "未知错误";
                break;
//            default:
//                errMsg = "当前网络不可用，请检查网络情况";
//                break;
        }

        return errMsg;
    }

}
