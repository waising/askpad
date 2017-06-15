package com.asking.pad.app.commom;

/**
 * Created by jswang on 2017/2/21.
 */

public class AppLoginEvent {

    public static final int LOGIN_SUCCESS = 0X0;
    public static final int LOGIN_OUT = 0X1;

    /**
     * 0-退出，1-登录
     */
    public int  type;
    public Object[]  values;

    public AppLoginEvent(int type, Object...  value){
        this.type = type;
        this.values = value;
    }

    public AppLoginEvent(int type){
        this.type = type;
    }

}
