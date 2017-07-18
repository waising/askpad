package com.asking.pad.app.presenter;

import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.mvp.BasePresenter;

/**
 * 共享空间的p层
 */

public class SharePresenter extends BasePresenter<ShareModel> {


    /**
     * 共享之星历史记录分页查询，可查询到上一周第一名
     */
    public void presenterShareHistory(int start, int limit, ApiRequestListener mListener) {
        baseReq(mModel.modelShareHistory(start, limit), "content", mListener);
    }

    /**
     * 共享之星排行查询
     */
    public void presenterShareRank(int limit, ApiRequestListener mListener) {
        baseReq(mModel.modelShareRank(limit), "content", mListener);
    }

    /**
     * 获取头像图片
     */

    public void presenterUserAvator(String userId, ApiRequestListener mListener) {
        baseReq(mModel.modelUserAvatar(userId), "content", mListener);
    }

}
