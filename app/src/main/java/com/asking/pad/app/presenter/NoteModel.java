package com.asking.pad.app.presenter;

import com.asking.pad.app.api.Networks;
import com.asking.pad.app.mvp.BaseModel;
import com.asking.pad.app.mvp.RxSchedulers;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * 笔记请求model
 * create by linbin
 */

public class NoteModel extends BaseModel {

    // 获取我的笔记

    public Observable<ResponseBody> modelMyNote(String ticket, int start, int limit) {
        return Networks.getInstance().getUserApi()
                .getMyNote(ticket, start, limit).compose(RxSchedulers.<ResponseBody>io_main());
    }
    // 保存我的笔记
    public Observable<ResponseBody> saveNode(String title,String content,String imgurl) {
        return Networks.getInstance().getUserApi()
                .saveNode(title,content,imgurl).compose(RxSchedulers.<ResponseBody>io_main());
    }

    // 修改我的笔记
    public Observable<ResponseBody> modelAlterMyNote(String content, String id) {
        return Networks.getInstance().getUserApi()
                .alterMyNote(content, id).compose(RxSchedulers.<ResponseBody>io_main());
    }
    // 删除我的笔记
    public Observable<ResponseBody> modelDelMyNote(String ticket, String id) {
        return Networks.getInstance().getUserApi()
                .delMyNote(ticket, id).compose(RxSchedulers.<ResponseBody>io_main());
    }
}
