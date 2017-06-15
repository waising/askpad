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

public class CollectionModel extends BaseModel {

    // 请求我的收藏里的知识点和咨询接口

    public Observable<ResponseBody> modelMyFavor(int requestType, String ticket, int type, int start, int limit) {
        return Networks.getInstance().getUserApi()
                .getMyFavor(ticket, type, start, limit).compose(RxSchedulers.<ResponseBody>io_main());
    }

    // 获取我的收藏里的题目列表及详细解析
    public Observable<ResponseBody> modelGetMyFavSubjectDetail(String km, String objId) {
        return Networks.getInstance().getUserApi()
                .getMyFavSubjectDetail(km, objId).compose(RxSchedulers.<ResponseBody>io_main());
    }


    // 删除我的收藏
    public Observable<ResponseBody> modelDelMyFavor(int requestType, String ticket, String id) {
        return Networks.getInstance().getUserApi()
                .delMyFavor(ticket, id).compose(RxSchedulers.<ResponseBody>io_main());
    }
    // 获取题目接口
    public Observable<ResponseBody> modelMySubjectList(int start,int limit) {
        return Networks.getInstance().getUserApi()
                .getMySubject(start, limit).compose(RxSchedulers.<ResponseBody>io_main());
    }
}
