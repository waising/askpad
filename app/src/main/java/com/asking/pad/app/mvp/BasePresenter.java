package com.asking.pad.app.mvp;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.api.HttpCodeConstant;
import com.asking.pad.app.api.Networks;
import com.asking.pad.app.commom.AESHelper;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.FileUtils;
import com.asking.pad.app.commom.NetworkUtils;
import com.asking.pad.app.ui.downbook.db.DbBookHelper;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jswang on 2017/4/6.
 */

public abstract class BasePresenter<M> {
    public Context mContext;
    public M mModel;
    public RxManager mRxManager = new RxManager();

    public void setModel(M m) {
        this.mModel = m;
    }


    public void onDestroy() {
        mRxManager.clear();
    }

    public void countDownPresenter(int time, final OnCountDownListener mListener) {
        mRxManager.add(RxCountDown.countdown(time)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())// 指定Subscriber的回调发生在UI线程
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        mListener.onComplete();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        mListener.onNext(integer);
                    }
                })

        );
    }

    public void countUpPresenter(int time, final OnCountDownListener mListener) {
        mRxManager.add(RxCountDown.countUp(time)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())// 指定Subscriber的回调发生在UI线程
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        mListener.onComplete();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        mListener.onNext(integer);
                    }
                })

        );
    }

    public void baseReq(Observable<ResponseBody> mObservable, final String valueKey, final ApiRequestListener mListener) {
        mRxManager.add(mObservable
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())// 指定Subscriber的回调发生在UI线程
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody obj) {
                        try {
                            resSuccess(valueKey, obj.string(), mListener);
                        } catch (Exception e) {
                            mListener.onResultFail();
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mListener.onResultFail();
                        String msg = HttpCodeConstant.getErrorMsg(CommonUtil.getRequestEntity(throwable));
                        if (!TextUtils.isEmpty(msg)) {
                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                })

        );
    }

    public void baseReq1(Observable<ResponseBody> mObservable, final String valueKey, final ApiRequestListener mListener) {
        mRxManager.add(mObservable
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody obj) {
                        try {
                            resSuccess1(valueKey, obj.string(), mListener);
                        } catch (Exception e) {
                            mListener.onResultFail();
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mListener.onResultFail();
                        String msg = HttpCodeConstant.getErrorMsg(CommonUtil.getRequestEntity(throwable));
                        if (!TextUtils.isEmpty(msg)) {
                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                })

        );
    }

    public void baseReqStr(Observable<ResponseBody> mObservable, final ApiRequestListener mListener) {
        mRxManager.add(mObservable
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody obj) {
                        try {
                            String result = obj.string();
                            mListener.onResultSuccess(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mListener.onResultFail();
                        String msg = HttpCodeConstant.getErrorMsg(CommonUtil.getRequestEntity(throwable));
                        if (!TextUtils.isEmpty(msg)) {
                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                })

        );
    }

    public void baseReqStr1(Observable<String> mObservable, final ApiRequestListener mListener) {
        mRxManager.add(mObservable
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String result) {
                        try {
                            mListener.onResultSuccess(result);
                        } catch (Exception e) {
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mListener.onResultFail();
                        String msg = HttpCodeConstant.getErrorMsg(CommonUtil.getRequestEntity(throwable));
                        if (!TextUtils.isEmpty(msg)) {
                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                })

        );
    }

    private void resSuccess(String valueKey, String resStr, ApiRequestListener mListener) {
        try {
            JSONObject jsonRes = JSON.parseObject(resStr);
            String flag = jsonRes.getString("flag");
            if (TextUtils.equals("0", flag)) {//0表示返回成功
                if (!TextUtils.isEmpty(valueKey)) {
                    String result = jsonRes.getString(valueKey);
                    mListener.onResultSuccess(result);
                } else {
                    mListener.onResultSuccess(jsonRes);
                }
            } else {
                Toast.makeText(mContext, jsonRes.getString("msg"), Toast.LENGTH_SHORT).show();
                mListener.onResultFail();
            }
        } catch (Exception e) {
            mListener.onResultFail();
            e.printStackTrace();
        }
    }

    private void resSuccess1(String valueKey, String resStr, ApiRequestListener mListener) {
        try {
            JSONObject jsonRes = JSON.parseObject(resStr);
            String flag = jsonRes.getString("flag");
            if (TextUtils.equals("1", flag)) {//1表示返回成功
                if (!TextUtils.isEmpty(valueKey)) {
                    String result = jsonRes.getString(valueKey);
                    mListener.onResultSuccess(result);
                } else {
                    mListener.onResultSuccess(jsonRes);
                }
            } else {
                Toast.makeText(mContext, jsonRes.getString("msg"), Toast.LENGTH_SHORT).show();
                mListener.onResultFail();
            }
        } catch (Exception e) {
            mListener.onResultFail();
            e.printStackTrace();
        }
    }

    public void newThread(Observable<Object> mObservable, final ApiRequestListener mListener) {
        mRxManager.add(mObservable
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())// 指定Subscriber的回调发生在UI线程
                .subscribe(new rx.Observer<Object>() {
                    @Override
                    public void onNext(Object obj) {
                        mListener.onResultSuccess(obj);
                    }

                    @Override
                    public void onCompleted() {
                        mListener.onResultSuccess("");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mListener.onResultFail();
                    }
                })

        );
    }

    public void baseReqStrDB(Observable mObservable,boolean isReadDB,String dbName,String pathId
            ,final ApiRequestListener mListener) {
        baseReqDB(mObservable,isReadDB,dbName,pathId,2,"",mListener);
    }

    public void baseReqFlag0DB(Observable mObservable,boolean isReadDB, String dbName,String pathId
            ,String valueKey, final ApiRequestListener mListener) {
        baseReqDB(mObservable,isReadDB,dbName,pathId,0,valueKey,mListener);
    }

    public void baseReqFlag1DB(Observable mObservable,boolean isReadDB, String dbName,String pathId
            ,String valueKey, final ApiRequestListener mListener) {
        baseReqDB(mObservable,isReadDB,dbName,pathId,1,valueKey,mListener);
    }

    public Observable getDBObservable(final String dbName,final String pathId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                try {
//                    BookTable mTable = DbBookHelper.getInstance().setDatabase(dbName).getBookTable(pathId);
//                    String value = mTable.getValue();
                    String value = DbBookHelper.getInstance().setDatabase(dbName).getBookTableValue(pathId);
                    String res = AESHelper.decode(value);
                    subscriber.onNext(res);
                } catch (Exception e) {
                    subscriber.onError(e);
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @param mObservable
     * @param resType     0-flag(成功为0) 1-flag(成功为1) 2-不做处理返回字符串
     * @param valueKey 如：content
     * @param dbName 数据库名
     * @param mListener
     */
    public void baseReqDB(Observable mObservable, final boolean isReadDB,String dbName,String pathId
            ,final int resType,final String valueKey, final ApiRequestListener mListener) {
        if (isReadDB) {
            mObservable = getDBObservable(dbName,pathId);
        }
        setObservableByResponseBodyOrString(mObservable,resType,valueKey,mListener);
    }

    @SuppressWarnings("unchecked")
    public void setObservableByResponseBodyOrString(Observable mObservable,final int resType,final String valueKey, final ApiRequestListener mListener) {
        mRxManager.add(mObservable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<Object, String>() {
                    @Override
                    public String call(Object resBody) {
                        String resStr = "";
                        try {
                            if (resBody instanceof ResponseBody) {
                                resStr = ((ResponseBody) resBody).string();
                            } else {
                                resStr = (String) resBody;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return resStr;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String resStr) {
                        switch (resType) {
                            case 0:
                                resSuccess(valueKey, resStr, mListener);
                                break;
                            case 1:
                                resSuccess1(valueKey, resStr, mListener);
                                break;
                            case 2:
                                mListener.onResultSuccess(resStr);
                                break;
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mListener.onResultFail();
                        String msg = HttpCodeConstant.getErrorMsg(CommonUtil.getRequestEntity(throwable));
                        if (!TextUtils.isEmpty(msg)) {
                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                })

        );
    }


    public void baseReqStrCache(Observable mObservable, final String cacheKey, final ApiRequestListener mListener) {
        baseReqCache(mObservable, 2, "", cacheKey, mListener);
    }

    public void baseReqFlag0Cache(Observable mObservable, final String valueKey, final String cacheKey, final ApiRequestListener mListener) {
        baseReqCache(mObservable, 0, valueKey, cacheKey, mListener);
    }

    public void baseReqFlag1Cache(Observable mObservable, final String valueKey, final String cacheKey, final ApiRequestListener mListener) {
        baseReqCache(mObservable, 1, valueKey, cacheKey, mListener);
    }

    /**
     * @param mObservable
     * @param resType     0-flag(成功为0) 1-flag(成功为1) 2-不做处理返回字符串
     * @param valueKey
     * @param cacheKey
     * @param mListener
     */
    @SuppressWarnings("unchecked")
    public void baseReqCache(Observable mObservable, final int resType, final String valueKey, final String cacheKey, final ApiRequestListener mListener) {
        if (!NetworkUtils.isNetworkAvailable()) {
            mObservable = getCacheObservable(cacheKey);
        }
        mRxManager.add(mObservable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<Object, String>() {
                    @Override
                    public String call(Object resBody) {
                        String resStr = "";
                        try {
                            if (resBody instanceof ResponseBody) {
                                resStr = ((ResponseBody) resBody).string();
                                if (!TextUtils.isEmpty(cacheKey) && !TextUtils.isEmpty(resStr)) {
                                    AppContext.getInstance().setPreferencesValue(cacheKey, resStr);
                                }
                            } else {
                                resStr = (String) resBody;
                            }
                        } catch (Exception e) {
                        }
                        return resStr;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String resStr) {
                        switch (resType) {
                            case 0:
                                resSuccess(valueKey, resStr, mListener);
                                break;
                            case 1:
                                resSuccess1(valueKey, resStr, mListener);
                                break;
                            case 2:
                                mListener.onResultSuccess(resStr);
                                break;
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mListener.onResultFail();
                        String msg = HttpCodeConstant.getErrorMsg(CommonUtil.getRequestEntity(throwable));
                        if (!TextUtils.isEmpty(msg)) {
                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                })

        );
    }

    public Observable getCacheObservable(final String cacheKey) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                try {
                    String res = AppContext.getInstance().getPreferencesStr(cacheKey);
                    subscriber.onNext(res);
                } catch (Exception e) {
                }
            }
        });
    }

    public Observable getFileObservable(final String filePath) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                try {
                    String res = FileUtils.readFileStr(filePath);
                    subscriber.onNext(res);
                } catch (Exception e) {}
            }
        });
    }

    public void baseReqFlag0File(Observable mObservable, final String valueKey, final String cacheKey, final ApiRequestListener mListener) {
        baseReqFile(mObservable, 0, valueKey, cacheKey, mListener);
    }

    public void baseReqFlag1File(Observable mObservable, final String valueKey, final String cacheKey, final ApiRequestListener mListener) {
        baseReqFile(mObservable, 1, valueKey, cacheKey, mListener);
    }

    public void baseReqStrFile(Observable mObservable, final String filePath, final ApiRequestListener mListener) {
        baseReqFile(mObservable, 2, "", filePath, mListener);
    }

    @SuppressWarnings("unchecked")
    public void baseReqFile(Observable mObservable, final int resType, final String valueKey, final String filePath, final ApiRequestListener mListener) {
        //mObservable = getFileObservable(filePath);
        mRxManager.add(mObservable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*读取下载写入文件*/
                .map(new Func1<Object, String>() {
                    @Override
                    public String call(Object resBody) {
                        String resStr = "";
                        try {
                            if (resBody instanceof ResponseBody) {
                                resStr = ((ResponseBody) resBody).string();
                            } else {
                                resStr = (String) resBody;
                            }
                        } catch (Exception e) {
                        }
                        return resStr;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String resStr) {
                        switch (resType) {
                            case 0:
                                resSuccess(valueKey, resStr, mListener);
                                break;
                            case 1:
                                resSuccess1(valueKey, resStr, mListener);
                                break;
                            case 2:
                                mListener.onResultSuccess(resStr);
                                break;
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mListener.onResultFail();
                        String msg = HttpCodeConstant.getErrorMsg(CommonUtil.getRequestEntity(throwable));
                        if (!TextUtils.isEmpty(msg)) {
                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                })

        );
    }


    private static UploadManager uploadManager;

    public void initQiNiuUpload() {
        if (uploadManager == null) {
            uploadManager = new UploadManager();
        }
    }

    public void qiNiuUploadFile(final String path, final String key,final ApiRequestListener mListener) {
        Observable<ResponseBody> qiniutokenObservable = Networks.getInstance().getUserApi().qiniutoken().compose(RxSchedulers.<ResponseBody>io_main());
        baseReqStr(qiniutokenObservable, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                qiNiuUpload(path,key,res,mListener);
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
                                    Observable<ResponseBody> qiniutokenObservable = Networks.getInstance().getUserApi().qiniutoken().compose(RxSchedulers.<ResponseBody>io_main());
                                    return qiniutokenObservable;
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
