package com.asking.pad.app.ui.pay;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 充值页面
 */

public class PayAskActivity extends BaseFrameActivity<UserPresenter, UserModel> {
    @BindView(R.id.toolBar)
    Toolbar mToolbar;

    int dataType;

    public static void openActivity(int dataType) {
        Bundle bundle = new Bundle();
        bundle.putInt("dataType", dataType);
        CommonUtil.openActivity(PayAskActivity.class, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_ask);
        ButterKnife.bind(this);

        dataType = this.getIntent().getIntExtra("dataType",0);
    }

    @Override
    public void initView() {
        super.initView();
        mToolbar.setTitle("");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(dataType == 0){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment,PaySuperClassFragment.newInstance()).commit();
        }else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment,PayAskCoinFragment.newInstance()).commit();
        }
    }
}
