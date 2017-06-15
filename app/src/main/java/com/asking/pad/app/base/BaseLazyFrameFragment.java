package com.asking.pad.app.base;

import android.os.Bundle;

import com.asking.pad.app.mvp.BaseModel;
import com.asking.pad.app.mvp.BasePresenter;
import com.asking.pad.app.mvp.TUtil;

/**
 * viewpager懒加载
 * Created by quan on 16/9/20.
 */
public abstract  class BaseLazyFrameFragment<P extends BasePresenter, M extends BaseModel> extends BaseFragment{

    public P mPresenter;

    public M mModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        mPresenter.mContext = this.getContext();
        mPresenter.setModel(mModel);
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) mPresenter.onDestroy();
        super.onDestroy();
    }

}
