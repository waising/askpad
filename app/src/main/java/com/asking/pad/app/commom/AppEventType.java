package com.asking.pad.app.commom;

/**
 * Created by jswang on 2017/2/21.
 */

public class AppEventType {
    public static final int WEB_VIEW_PROTOCOL_REQUEST = 0X12;

    public static final int CLASSIFY_REQUEST = 0X15;

    public static final int HOME_CAMERA_REQUEST = 0X16;

    public static final int OTO_QA_AS_CAMERA_REQUEST = 0X17;
    public static final int SET_PER_MODI_CAMERA_REQUEST = 0X18;
    public static final int RE_CAMERA_QUS_REQUEST = 0X19;
    public static final int RE_USER_INFO_REQUEST = 0X20;
    public static final int NOTE_CAMERA_REQUEST = 0X22;

    public static final int BOOK_DWON_FINISH_REQUEST = 0X23;
    public static final int BOOK_OPEN_REQUEST = 0X24;

    public static final int PAY_SUCCESSS_REQUEST = 0X25;

    public static final int RE_STU_PROGRESSS_SUCCESSS_REQUEST = 0X26;

    public static final int RE_CLASSIFY_REQUEST = 0X27;

    public int  type;
    public Object[]  values;

    public AppEventType(int type, Object...  value){
        this.type = type;
        this.values = value;
    }

    public AppEventType(int type){
        this.type = type;
    }

}
