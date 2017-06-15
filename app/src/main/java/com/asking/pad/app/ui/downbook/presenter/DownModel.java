package com.asking.pad.app.ui.downbook.presenter;

import com.asking.pad.app.api.Networks;
import com.asking.pad.app.mvp.BaseModel;
import com.asking.pad.app.mvp.RxSchedulers;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by jswang on 2017/5/2.
 */

public class DownModel extends BaseModel {

    public Observable<ResponseBody> versionsForUser() {
        return Networks.getInstance().getUserApi().versionsForUser().compose(RxSchedulers.<ResponseBody>io_main());
    }

}
