package com.asking.pad.app.ui.classmedia.download;

import com.asking.pad.app.entity.classmedia.ClassMediaTable;
import com.asking.pad.app.ui.downbook.db.DbHelper;
import com.asking.pad.app.ui.downbook.download.DownState;

import de.greenrobot.event.EventBus;
import rx.Subscriber;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by WZG on 2016/7/16.
 */
public class ProgressDownSubscriber<T> extends Subscriber<T> {

    /*下载数据*/
    private ClassMediaTable downInfo;


    public ProgressDownSubscriber(ClassMediaTable downInfo) {
        this.downInfo=downInfo;
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        downInfo.setDownState(DownState.DOWN);
        DbHelper.getInstance().insertOrReplaceClassMedia(downInfo);
        EventBus.getDefault().post(downInfo);
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        if (downInfo.getReadLength() > 0 && downInfo.getReadLength() == downInfo.getCountLength()) {
            downInfo.setDownState(DownState.FINISH);
        }else{
            ClassDownloadManager.getInstance().pause(downInfo);
        }
        DbHelper.getInstance().updateClassMedia(downInfo);
        EventBus.getDefault().post(downInfo);
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        ClassDownloadManager.getInstance().deleteDown(downInfo);
        downInfo.setDownState(DownState.PAUSE);
        DbHelper.getInstance().updateClassMedia(downInfo);
        EventBus.getDefault().post(downInfo);
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
    }

}
