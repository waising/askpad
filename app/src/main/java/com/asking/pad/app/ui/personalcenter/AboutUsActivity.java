package com.asking.pad.app.ui.personalcenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseActivity;
import com.asking.pad.app.commom.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 关于我们界面
 */

public class AboutUsActivity extends BaseActivity {
    @BindView(R.id.toolBar)
    Toolbar toolBar;

    @BindView(R.id.vesion_number)
    TextView vesionNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
    }


    @Override
    public void initView() {
        super.initView();
        setToolbar(toolBar, "关于我们");
        vesionNumber.setText(CommonUtil.getVersionName(this));
    }
}
