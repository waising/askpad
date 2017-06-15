package com.asking.pad.app.mvp;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by jswang on 2017/4/6.
 */

public class RxManager {

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();// 管理订阅者者

    public void add(Subscription m) {
        mCompositeSubscription.add(m);
    }

    public void clear() {
        mCompositeSubscription.unsubscribe();// 取消订阅
    }

    public void countDownPresenter(int time, final OnCountDownListener mListener) {
        add(RxCountDown.countdown(time)
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
}
