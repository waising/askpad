package com.asking.pad.app.presenter;

import com.asking.pad.app.api.Networks;
import com.asking.pad.app.mvp.BaseModel;
import com.asking.pad.app.mvp.RxSchedulers;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * 收藏请求model
 * create by linbin
 */

public class ShareModel extends BaseModel {


    /**
     * 共享之星历史记录分页查询
     */
    public Observable<ResponseBody> modelShareHistory(int start, int limit) {
        return Networks.getInstance().getUserApi()
                .ShareHistory(start, limit).compose(RxSchedulers.<ResponseBody>io_main());
    }

    /**
     * 共享之星排行查询
     */
    public Observable<ResponseBody> modelShareRank(int limit) {
        return Networks.getInstance().getUserApi()
                .ShareRank(limit).compose(RxSchedulers.<ResponseBody>io_main());
    }

    /**
     * 共享之星排行查询
     */
    public Observable<ResponseBody> modelUserAvatar(String userId) {
        return Networks.getInstance().getUserApi()
                .userAvator(userId).compose(RxSchedulers.<ResponseBody>io_main());
    }

    /**
     * 关注列表查询
     */
    public Observable<ResponseBody> modelAttentionList(int start, int limit, String userName) {
        return Networks.getInstance().getUserApi()
                .AttentionList(start, limit, userName).compose(RxSchedulers.<ResponseBody>io_main());
    }


    /**
     * 关注教师
     */
    public Observable<ResponseBody> modelAttention(String userName, String account) {
        return Networks.getInstance().getUserApi()
                .Attention(userName, account).compose(RxSchedulers.<ResponseBody>io_main());
    }

    /**
     * 取消关注教师
     */
    public Observable<ResponseBody> modelCancelAttention(String userName, String account) {
        return Networks.getInstance().getUserApi()
                .cancelAttention(userName, account).compose(RxSchedulers.<ResponseBody>io_main());
    }

}
