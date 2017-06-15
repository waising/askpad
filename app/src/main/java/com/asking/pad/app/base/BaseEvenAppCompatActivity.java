package com.asking.pad.app.base;

import android.os.Bundle;

import com.asking.pad.app.mvp.BaseModel;
import com.asking.pad.app.mvp.BasePresenter;
import com.asking.pad.app.mvp.TUtil;

import de.greenrobot.event.EventBus;

/**
 * Created by jswang on 2017/4/6.
 */

public class BaseEvenAppCompatActivity<P extends BasePresenter, M extends BaseModel> extends BaseActivity{

    public P mPresenter;

    public M mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);

        mPresenter.mContext = this;
        mPresenter.setModel(mModel);

    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.onDestroy();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
