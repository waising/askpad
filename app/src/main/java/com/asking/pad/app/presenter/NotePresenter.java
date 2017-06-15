package com.asking.pad.app.presenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.mvp.BasePresenter;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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


    private static UploadManager uploadManager;

    public void initQiNiuUpload() {
        if (uploadManager == null) {
            uploadManager = new UploadManager();
        }
    }

    public void qiNiuUploadFile(final String path, final String key,final ApiRequestListener mListener) {
        baseReq(mModel.qiniutoken(), "content", new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                JSONObject jsonRes = JSON.parseObject(res);
                String token = jsonRes.getString("token");
                qiNiuUpload(path,key,token,mListener);
            }

            @Override
            public void onResultFail() {
                mListener.onResultFail();
            }
        });
    }

    public void qiNiuUpload(final String path, final String key, final String token, final ApiRequestListener mListener) {
        initQiNiuUpload();
        mRxManager.add(Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(final Subscriber<? super String> subscriber) {
                        uploadManager.put(path, key, token, new UpCompletionHandler() {
                            @Override
                            public void complete(String keys, ResponseInfo info, org.json.JSONObject response) {
                                if (info.isOK()) {
                                    subscriber.onNext(key);
                                    subscriber.onCompleted();
                                } else {
                                    subscriber.onError(new Throwable(info.statusCode + ""));
                                }
                            }
                        }, null);
                    }
                }).retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> observable) {
                        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                            @Override
                            public Observable<?> call(Throwable throwable) {
                                String code = throwable.toString();
                                if (code.equals("-4") || code.equals("-5")) {//token失效、过期
                                    return mModel.qiniutoken();
                                } else {
                                    mListener.onResultFail();
                                    return null;
                                }
                            }
                        });

                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String result) {
                                mListener.onResultSuccess(result);
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                mListener.onResultFail();
                            }
                        })
        );
    }


}
