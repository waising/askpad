package com.asking.pad.app.ui.downbook.presenter;

import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.mvp.BasePresenter;

/**
 * Created by jswang on 2017/5/2.
 */

public class DownPresenter extends BasePresenter<DownModel> {

    public void versionsForUser(ApiRequestListener mListener) {
        baseReq(mModel.versionsForUser(),"content", mListener);
    }

}
