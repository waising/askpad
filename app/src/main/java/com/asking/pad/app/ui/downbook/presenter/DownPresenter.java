package com.asking.pad.app.ui.downbook.presenter;

import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.mvp.BasePresenter;

/**
 * Created by jswang on 2017/5/2.
 */

public class DownPresenter extends BasePresenter<DownModel> {

    public void finListdWithDownloadUrl(String courseTypeId,int start,int limit,ApiRequestListener mListener) {
        baseReq(mModel.finListdWithDownloadUrl(courseTypeId,start,limit),"content", mListener);
    }

}
