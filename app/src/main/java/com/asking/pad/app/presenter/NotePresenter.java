package com.asking.pad.app.presenter;

import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.mvp.BasePresenter;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;

import rx.Observable;
import rx.Subscriber;

/**
 * 笔记p层
 */

public class NotePresenter extends BasePresenter<NoteModel> {


    /**
     * 获取笔记请求
     *
     * @param ticket
     * @param start
     * @param limit
     * @param mListener
     */
    public void presenterMyNote(String ticket, int start, int limit, ApiRequestListener mListener) {
        baseReqStr(mModel.modelMyNote(ticket, start, limit), mListener);
    }

    /**
     * 保存笔记请求
     */
    public void saveNode(String title,String content,String imgurl, ApiRequestListener mListener) {
        baseReq(mModel.saveNode(title,content,imgurl), "", mListener);
    }


    /**
     * 修改我的笔记
     */
    public void presenterAlterMyNote(String content, String id, ApiRequestListener mListener) {
        baseReq(mModel.modelAlterMyNote(content, id), "", mListener);
    }

    /**
     * 删除笔记请求
     *
     * @param ticket
     * @param id
     */
    public void presenterDelMyNote(String ticket, String id, ApiRequestListener mListener) {
        baseReq(mModel.modelDelMyNote(ticket, id), "", mListener);
    }

    public void loadCameraPic(final String filePath,final ApiRequestListener mListener) {
        Observable<String> mObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                try {
                    BitmapUtil.createImageThumbnail(mContext, filePath, filePath, 500, 80);
                    subscriber.onNext(filePath);
                } catch (Exception e) {
                }
            }
        });
        baseReqStr1(mObservable, mListener);
    }
}
