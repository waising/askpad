package com.asking.pad.app.ui.sharespace.special;

import android.os.Bundle;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;

import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/7/17.
 */

public class SpecialDetailActivity extends BaseFrameActivity<UserPresenter, UserModel> {

    public static void openActivity(String courseTypeId){
        Bundle bundle = new Bundle();
        bundle.putString("courseTypeId", courseTypeId);
        CommonUtil.openActivity(SpecialDetailActivity.class,bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specical_detail);
        ButterKnife.bind(this);
    }

}
