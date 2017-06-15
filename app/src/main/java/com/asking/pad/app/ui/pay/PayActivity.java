package com.asking.pad.app.ui.pay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.SystemUtil;
import com.asking.pad.app.entity.PayEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.pingplusplus.android.Pingpp;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;


/**
 * 支付页面
 * Created by wxwang on 2016/11/30.
 */
public class PayActivity extends BaseFrameActivity<UserPresenter, UserModel> {

    private static final String CLIENTIP = "127.0.0.1";

    @BindView(R.id.toolBar)//标题
            Toolbar mToolbar;

    Map<Integer, String> payMap;
    String rechargeId = "", commodityId = "", orderId = "", payType = "";
    int versionId = 0;
    long mExitTime;

    // String charge;
    PayEntity payEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        super.initView();
        setToolbar(mToolbar, getString(R.string.pay_title));
    }

    @Override
    public void initData() {
        super.initData();
        payEntity = new PayEntity();

        rechargeId = getIntent().getStringExtra("rechargeId");
        commodityId = getIntent().getStringExtra("commodityId");
        versionId = getIntent().getIntExtra("versionId", 0);

        orderId = getIntent().getStringExtra("orderId");
        payType = getIntent().getStringExtra("payType");

        setMap();
    }

    int[] viewTypeId = new int[]{R.id.alipay_ly, R.id.unionpay_ly, R.id.wechatpay_ly};

    boolean isRePay = false;//是否支付过

    @Override
    public void initLoad() {
        super.initLoad();
        if (!TextUtils.isEmpty(orderId) && !TextUtils.isEmpty(payType)) {//购买记录里继续支付跳转过来的
            isRePay = true;
            reCharge(viewTypeId[Integer.valueOf(payType)]);
        }


    }

    @OnClick({R.id.alipay_ly, R.id.wechatpay_ly, R.id.unionpay_ly})//点击三个支付选项
    public void onClick(View view) {

        if (view.getId() == R.id.wechatpay_ly && !SystemUtil.isWeixinAvilible(this)) {//如果没有微信客户端
            showShortToast("没有检测到微信客户端");
            return;
        }
        if ((System.currentTimeMillis() - mExitTime) > 2000) {//避免重复点击
            mExitTime = System.currentTimeMillis();
            if (isRePay) {//调用继续支付接口
                reCharge(view.getId());
            } else {//调用立即支付接口
                payEntity.setClientIP(CLIENTIP);
                payEntity.setCommodityId(commodityId);
                payEntity.setRechargeId(rechargeId);
                payEntity.setNum(1);//1:立即支付
                String[] payinfo = payMap.get(view.getId()).split("_");
                payEntity.setType(payinfo[0]);
                payEntity.setPayType(payinfo[1]);
                payEntity.setVersionId(versionId);
                mPresenter.getAppCharge(payEntity, mPayListener);
            }

        }
    }

    /**
     * 立即支付和继续支付成功后的监听
     */
    ApiRequestListener mPayListener = new ApiRequestListener<String>() {
        @Override
        public void onResultSuccess(final String res) {
            try {
//                charge = res;
                AppContext.getInstance().setPreferencesValue("charge", res);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        Pingpp.createPayment(PayActivity.this, res);
                    }
                }, 500);   //5秒
            } catch (Exception e) {
                e.printStackTrace();
                showShortToast("支付失败！");
            }
        }

        @Override
        public void onResultFail() {
            super.onResultFail();
            Log.e("failure","fddf");
        }
    };

    /**
     * 调用继续支付接口
     *
     * @param payTypeId
     */
    private void reCharge(int payTypeId) {
        payEntity.setOrderId(orderId);
        String[] payinfo = payMap.get(payTypeId).split("_");
        payEntity.setType(payinfo[0]);
        payEntity.setPayType(payinfo[1]);
//        new Handler().postDelayed(new Runnable(){
//            public void run() {
        mPresenter.getAppReCharge(payEntity, mPayListener);
//            }
//        }, 500);   //5秒
    }

    /**
     * 支付页面返回处理
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                try {
                    String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - 支付成功
                 * "fail"    - 支付失败
                 * "cancel"  - 取消支付
                 * "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
                 */
                    String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                    String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
//                showMsg(result, errorMsg, extraMsg);
                    //成功
                    if ("success".equals(result)) {//支付成功
                        String charge = AppContext.getInstance().getPreferencesStr("charge");
                        //获取订单
                        String orderNo = JSON.parseObject(charge).getString("order_no");
                        //刷新用户信息
                        mPresenter.appChargeSuccess(orderNo, new ApiRequestListener<JSONObject>() {
                            @Override
                            public void onResultSuccess(JSONObject res) {
                                EventBus.getDefault().post(new AppEventType(AppEventType.PAY_SUCCESSS_REQUEST));
                                showShortToast(res.getString("msg"));
                                if (!TextUtils.isEmpty(orderId)) {//继续支付过来
                                    setResult(RESULT_OK);
                                }
                                finish();
                            }
                        });

                    }else{

                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 支付map
     */
    private void setMap() {
        payMap = new ArrayMap<>();
        payMap.put(R.id.alipay_ly, "0_alipay");
        payMap.put(R.id.unionpay_ly, "1_upacp");
        payMap.put(R.id.wechatpay_ly, "2_wx");
    }
}
