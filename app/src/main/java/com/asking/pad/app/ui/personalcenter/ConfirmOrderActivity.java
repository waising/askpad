package com.asking.pad.app.ui.personalcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.SystemUtil;
import com.asking.pad.app.entity.PayEntity;
import com.asking.pad.app.entity.ShopCartEntity;
import com.asking.pad.app.entity.ShopCartPayEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.pingplusplus.android.Pingpp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.asking.pad.app.R.id.tv_pay;

;


/**
 * 确认订单activity
 * Created by wxwang on 2016/11/30.
 */
public class ConfirmOrderActivity extends BaseFrameActivity<UserPresenter, UserModel> {

    private static final String CLIENTIP = "127.0.0.1";

    Map<Integer, String> payMap;
    //String rechargeId = "", commodityId = "", orderId = "", payType = "";
    int versionId = 0;
    long mExitTime;

    PayEntity payEntity;
    @BindView(tv_pay)
    TextView tvPay;
    @BindView(R.id.tv_check)
    TextView tvCheck;


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;


    ShopCartConfrimAdapter myShopCartAdapter;


    @BindView(R.id.tv_choose_num)
    TextView tvChooseNum;

    private List<ShopCartEntity.ListBean> mList;

    private String chooseNum;

    private String totalPrice;

    @BindView(R.id.toolBar)
    Toolbar mToolbar;

    private String[] ids = null;


    @BindView(R.id.alipay_ly)

    ImageView ivAlipay;

    @BindView(R.id.unionpay_ly)

    ImageView ivYinLian;

    @BindView(R.id.wechatpay_ly)

    ImageView ivWeChat;
    private int mSelected = -1;//记录选中哪一个

    private final int[] ivRedioMedicineCycles = {R.id.alipay_ly, R.id.wechatpay_ly, R.id.unionpay_ly,};


    private String myOrderId;

    private ShopCartPayEntity shopCartPayEntity;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_confirm_order);
        ButterKnife.bind(this);
    }

    //
    @Override
    public void initView() {
        super.initView();
        mToolbar.setNavigationIcon(null);
        setToolbar(mToolbar, getString(R.string.shop_cart));
        tvPay.setVisibility(View.VISIBLE);
        tvCheck.setVisibility(View.GONE);
        initRecycleView();
    }

    @Override
    public void initData() {
        super.initData();
        payEntity = new PayEntity();

        shopCartPayEntity = new ShopCartPayEntity();
        tvPay.setOnClickListener(onClickListner);
        ivAlipay.setOnClickListener(onClickListner);
        ivYinLian.setOnClickListener(onClickListner);
        ivWeChat.setOnClickListener(onClickListner);

//        rechargeId = getIntent().getStringExtra("rechargeId");
//        commodityId = getIntent().getStringExtra("commodityId");
//        versionId = getIntent().getIntExtra("versionId", 0);
        setMap();

    }

    // int[] viewTypeId = new int[]{R.id.alipay_ly, R.id.unionpay_ly, R.id.wechatpay_ly};

    //  boolean isRePay = false;

    private List<ImageView> cycleRadioViews;//

    private int pay_style = -1;


    @Override
    public void initLoad() {
        super.initLoad();
        if (myShopCartAdapter == null) {
            myShopCartAdapter = new ShopCartConfrimAdapter();
            recyclerView.setAdapter(myShopCartAdapter);
        }
        refsh(); // 刷新数据
        cycleRadioViews = new ArrayList<>();

//        if (!TextUtils.isEmpty(orderId) && !TextUtils.isEmpty(payType)) {
//            isRePay = true;
//            reCharge(viewTypeId[Integer.valueOf(payType)]);
//        }

        initReadioMedicineCycles();
    }

    private void initReadioMedicineCycles() {
        for (int res : ivRedioMedicineCycles) {
            View view = findViewById(res);
            cycleRadioViews.add((ImageView) view);
            view.setOnClickListener(onClickListner);
        }

    }

    private View.OnClickListener onClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.alipay_ly:
                    pay_style = R.id.alipay_ly;
                    updateRadioCycle(0);
                    break;
                case R.id.wechatpay_ly:
                    pay_style = R.id.wechatpay_ly;
                    updateRadioCycle(1);
                    break;
                case R.id.unionpay_ly:
                    pay_style = R.id.unionpay_ly;
                    updateRadioCycle(2);
                    break;
                case R.id.tv_pay://支付
                    if (pay_style == -1) {
                        showShortToast("请选择支付方式");
                        return;
                    }
                    pay(pay_style);
                    break;
            }

        }
    };

    /**
     * 刷新用药周期单选勾选态
     */
    private void updateRadioCycle(int position) {
        if (mSelected != position) {//避免重复点击
            mSelected = position;//记录选中哪一个
            for (int i = 0; i < cycleRadioViews.size(); i++) {
                if (i == position) {//选中状态下
                    cycleRadioViews.get(i).setBackgroundResource(R.mipmap.pay_bg);
                } else {//未选中状态下
                    cycleRadioViews.get(i).setBackgroundResource(R.mipmap.ic_bg_blank);
                }
            }
        }


    }


    /**
     * 初始化recyclewView
     */
    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
    }


    private void pay(final int viewId) {
        if (pay_style == R.id.wechatpay_ly && !SystemUtil.isWeixinAvilible(this)) {
            showShortToast("没有检测到微信客户端");
            return;
        }
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            mExitTime = System.currentTimeMillis();

            mPresenter.getShopCartPayAdd(ids, new ApiRequestListener<String>() {//题目的id和类型请求
                @Override
                public void onResultSuccess(String resStr) {//数据返回成功
                    JSONObject resobj = JSON.parseObject(resStr);
                    myOrderId = resobj.getString("orderId");
                    shopCartPayEntity.setOrderId(myOrderId);
                    String[] payinfo = payMap.get(viewId).split("_");
                    shopCartPayEntity.setType(payinfo[0]);
                    shopCartPayEntity.setPayType(payinfo[1]);
                    mPresenter.ShopCartPay(shopCartPayEntity, mPayListener);
                }
            });

        }
    }

    /**
     * 支付监听
     */
    ApiRequestListener mPayListener = new ApiRequestListener<String>() {
        @Override
        public void onResultSuccess(String res) {
            try {
                AppContext.getInstance().setPreferencesValue("charge", res);
                Pingpp.createPayment(ConfirmOrderActivity.this, res);
            } catch (Exception e) {
                e.printStackTrace();
                showShortToast("支付失败！");
            }
        }
    };
//
//    private void reCharge(int payTypeId) {
//        payEntity.setOrderId(orderId);
//        String[] payinfo = payMap.get(payTypeId).split("_");
//        payEntity.setType(payinfo[0]);
//        payEntity.setPayType(payinfo[1]);
//
//        mPresenter.getAppReCharge(payEntity, mPayListener);
//    }
//
//

    /**
     * 设置支付map
     */
    private void setMap() {
        payMap = new ArrayMap<>();
        payMap.put(R.id.alipay_ly, "0_alipay");
        payMap.put(R.id.unionpay_ly, "1_upacp");
        payMap.put(R.id.wechatpay_ly, "2_wx");
    }


    /**
     * 刷新数据
     */
    public void refsh() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            mList = (ArrayList<ShopCartEntity.ListBean>) bundle.getSerializable("list");
            ids = bundle.getStringArray("StrIds");
            chooseNum = bundle.getString("sum");
            totalPrice = bundle.getString("totalPrice");
            tvChooseNum.setText(getResources().getString(R.string.shop_cart_num, chooseNum));
            tvTotalPrice.setText(getResources().getString(R.string.shop_cart_total_price, totalPrice));
            myShopCartAdapter.setData(mList);
            myShopCartAdapter.notifyDataSetChanged();
            setHeader(recyclerView);
        }

    }

    /**
     * 添加header
     *
     * @param view
     */
    private void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(this).inflate(R.layout.layout_shopcart_header_pay, view, false);
        myShopCartAdapter.setHeaderView(header);
    }

    /**
     * 支付页面返回处理
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                try {
                    String result = data.getExtras().getString("pay_result");
                    //成功
                    if (TextUtils.equals("success",result)) {//支付成功
                        //获取订单
                        //刷新用户信息
                        showShortToast("支付成功");
                        finish();
                        CommonUtil.openActivity(PaySuccessActivity.class);//跳转到支付成功页面
                    }else {
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
