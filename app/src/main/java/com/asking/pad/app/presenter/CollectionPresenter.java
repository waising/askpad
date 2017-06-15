package com.asking.pad.app.presenter;

import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.mvp.BasePresenter;

/**
 * 收藏的p层
 */

public class CollectionPresenter extends BasePresenter<CollectionModel> {

    /**
     * 获取我的收藏请求
     *
     * @param ticket
     * @param start
     * @param limit
     * @param mListener
     */
    public void presenterMyFavor(final int requestType, String ticket, final int type, int start, int limit, ApiRequestListener mListener) {
        baseReqStr(mModel.modelMyFavor(requestType, ticket, type, start, limit), mListener);
    }


    /**
     * 获取我的收藏里的题目列表及详细解析
     */
    public void presenterGetMyFavSubjectDetail(final int requestType, String km, String objId, ApiRequestListener mListener) {
        baseReqStr(mModel.modelGetMyFavSubjectDetail(km, objId), mListener);
    }

    /**
     * 删除我的收藏
     */
    public void presenterDelMyFavor(final int requestType, String ticket, String id, ApiRequestListener mListener) {
        baseReq(mModel.modelDelMyFavor(requestType, ticket, id), "", mListener);
    }

    /**
     * 获取题目接口
     *
     * @param mListener
     */
    public void presenterMySubject( int start, int limit, ApiRequestListener mListener) {
        baseReqStr(mModel.modelMySubjectList(start, limit), mListener);
    }


}
