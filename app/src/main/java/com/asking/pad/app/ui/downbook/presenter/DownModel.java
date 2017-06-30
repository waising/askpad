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

    public Observable<ResponseBody> finListdWithDownloadUrl(String courseTypeId,int start,int limit) {
        return Networks.getInstance().getUserApi().finListdWithDownloadUrl(courseTypeId,start,limit).compose(RxSchedulers.<ResponseBody>io_main());
    }

}
