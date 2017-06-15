package com.asking.pad.app.base;

import android.os.Bundle;

import com.asking.pad.app.commom.AppManager;
import com.asking.pad.app.mvp.BaseModel;
import com.asking.pad.app.mvp.BasePresenter;
import com.asking.pad.app.mvp.TUtil;

import de.greenrobot.event.EventBus;

/**
 * Created by jswang on 2017/2/22.
 */

public abstract class BaseEvenActivity<P extends BasePresenter, M extends BaseModel> extends SwipeBackActivity {
    public P mPresenter;
    public M mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        mPresenter.mContext = this;
        mPresenter.setModel(mModel);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.onDestroy();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }
}
