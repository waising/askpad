package com.asking.pad.app.ui.personalcenter;

import android.os.Bundle;
import android.support.v7.widget.CardView;
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

public class PaySuccessFragmentCopy extends BaseActivity {


    @BindView(R.id.tv_check_order)
    TextView tvCheckOrder;
    @BindView(R.id.cardView)
    CardView cardView;

    public static PaySuccessFragmentCopy newInstance() {
        PaySuccessFragmentCopy fragment = new PaySuccessFragmentCopy();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pay_succeed_copy);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        super.initView();
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
