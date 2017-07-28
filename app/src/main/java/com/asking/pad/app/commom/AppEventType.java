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

    public static final int RE_CLASSIFY_REQUEST = 0X27;
    public static final int QUESTION_REF = 0X28;
    public static final int QUESTION_ASK = 0X29;

    public static final int RE_SHARESPACE_TEAATTEN_REQUEST = 0X30;

    public static final int RE_SHARESPACE_SPECIALFAVOR_REQUEST = 0X31;

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
