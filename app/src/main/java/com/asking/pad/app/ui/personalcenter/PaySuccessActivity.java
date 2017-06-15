package com.asking.pad.app.ui.personalcenter;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseActivity;
import com.asking.pad.app.commom.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 支付成功fragement
 */

public class PaySuccessActivity extends BaseActivity {


    @BindView(R.id.tv_check_order)
    TextView tvCheckOrder;
    @BindView(R.id.cardView)
    CardView cardView;

    @BindView(R.id.toolBar)
    Toolbar mToolbar;

    public static PaySuccessActivity newInstance() {
        PaySuccessActivity fragment = new PaySuccessActivity();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pay_succeed);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        super.initView();
        mToolbar.setNavigationIcon(null);
        setToolbar(mToolbar, getString(R.string.shop_cart));
    }


    @Override
    public void initListener() {
        super.initListener();

    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick(R.id.tv_check_order)
    public void onClick() {
        CommonUtil.openAuthActivity(BuyRecordActivity.class);
        finish();
    }
}
