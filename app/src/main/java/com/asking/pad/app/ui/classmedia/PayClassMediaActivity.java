package com.asking.pad.app.ui.classmedia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.entity.classmedia.ClassMedia;
import com.asking.pad.app.entity.pay.AskCoinPay;
import com.asking.pad.app.entity.pay.GradePay;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.pingplusplus.android.Pingpp;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.asking.pad.app.R.id.toolBar;

/**
 * Created by jswang on 2017/6/7.
 */

public class PayClassMediaActivity extends BaseFrameActivity<UserPresenter, UserModel> {
    @BindView(toolBar)
    Toolbar mToolbar;

    @BindView(R.id.iv_alipay)
    ImageView iv_alipay;

    @BindView(R.id.iv_wechatpay)
    ImageView iv_wechatpay;

    @BindView(R.id.iv_unionpay)
    ImageView iv_unionpay;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_title1)
    TextView tv_title1;

    @BindView(R.id.tv_content)
    TextView tv_content;

    @BindView(R.id.tv_total_price)
    TextView tv_total_price;

    @BindView(R.id.iv_img)
    ImageView iv_img;

    @BindView(R.id.iv_present)
    ImageView iv_present;

    String payType = "alipay";
    /**
     * KC-单个初升高购买  TC-整套初升高购买  TB-同步课堂购买  ASB-阿思币购买
     */
    String orderType = "";
    String commodityId;
    String finalPrice;

    String imgUrl;
    String title;
    String title1;
    String content;

    /**
     * TC01 - 初升高衔接课-数学
     * TC02- 初升高衔接课-物理
     */
    String packageId;

    MaterialDialog mLoading;

    /**
     * 单个初升高衔接课购买
     *
     * @param activity
     * @param mClassVideo
     */
    public static void openActivity(Activity activity, ClassMedia mClassVideo) {
        Intent intent = new Intent(activity, PayClassMediaActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("orderType", "KC");
        mBundle.putString("courseDataId", mClassVideo.getCourseDataId());
        mBundle.putString("courseName", mClassVideo.getCourseName());
        mBundle.putString("courseTypeName", mClassVideo.getCourseTypeName());
        mBundle.putString("description", mClassVideo.getDescription());
        mBundle.putString("coursePrice", mClassVideo.getCoursePrice() + "");
        mBundle.putString("courseTypeFullName", mClassVideo.getCourseTypeFullName());
        intent.putExtras(mBundle);
        activity.startActivity(intent);
    }

    /**
     * 整套初升高衔接课购买
     *
     * @param activity
     * @param packageId -- TC01 -初升高衔接课-数学   TC02-初升高衔接课-物理
     */
    public static void openActivity(Activity activity, String packageId) {
        Intent intent = new Intent(activity, PayClassMediaActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("orderType", "TC");
        mBundle.putString("packageId", packageId);
        intent.putExtras(mBundle);
        activity.startActivity(intent);
    }

    /**
     * 阿思币购买
     *
     * @param activity
     * @param mAskCoinPay
     */
    public static void openActivity(Activity activity, AskCoinPay mAskCoinPay) {
        Intent intent = new Intent(activity, PayClassMediaActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("orderType", "ASB");
        mBundle.putString("commodityId", mAskCoinPay.commodityId);
        mBundle.putString("value", mAskCoinPay.value);
        mBundle.putString("price", mAskCoinPay.price);
        intent.putExtras(mBundle);
        activity.startActivity(intent);
    }

    /**
     * 同步课堂购买
     *
     * @param activity
     * @param mGradePay
     */
    public static void openActivity(Activity activity, GradePay mGradePay) {
        Intent intent = new Intent(activity, PayClassMediaActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("orderType", "TB");
        mBundle.putString("commodityId", mGradePay.commodityId);
        mBundle.putString("packageName", mGradePay.packageName);
        mBundle.putString("packagePrice", mGradePay.packagePrice);
        mBundle.putString("timeLimit", mGradePay.timeLimit);
        mBundle.putString("imgUrl", mGradePay.imgUrl);
        intent.putExtras(mBundle);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_class_video);
        ButterKnife.bind(this);

        orderType = this.getIntent().getStringExtra("orderType");
    }

    @Override
    public void initView() {
        super.initView();
        setToolbar(mToolbar, getTitle().toString());

        mLoading = getLoadingDialog().build();

        iv_alipay.setSelected(true);
        iv_wechatpay.setSelected(false);
        iv_unionpay.setSelected(false);

        iv_present.setVisibility(View.GONE);

        if (TextUtils.equals(orderType, "KC")) {
            commodityId = getIntent().getStringExtra("courseDataId");
            finalPrice = getIntent().getStringExtra("coursePrice");
            title = getIntent().getStringExtra("courseName");
            title1 = getIntent().getStringExtra("courseTypeFullName");
            content = getIntent().getStringExtra("description");

            setDataView();
        } else if (TextUtils.equals(orderType, "TC")) {
            packageId = getIntent().getStringExtra("packageId");
            getAllClassVideoOrder();
        } else if (TextUtils.equals(orderType, "TB")) {
            orderType = "TC";
            commodityId = getIntent().getStringExtra("commodityId");
            title = getIntent().getStringExtra("packageName");
            finalPrice = getIntent().getStringExtra("packagePrice");
            imgUrl = getIntent().getStringExtra("imgUrl");

            content = String.format("学习时限：%s个月（购买之日算起）", getIntent().getStringExtra("timeLimit"));

            setDataView();
        } else if (TextUtils.equals(orderType, "ASB")) {
            commodityId = getIntent().getStringExtra("commodityId");
            finalPrice = getIntent().getStringExtra("price");
            title = getIntent().getStringExtra("value") + "个币";
            content = "【阿思币】";

            setDataView();
        }
    }

    private void setDataView() {
        tv_title.setText(title);
        tv_title1.setText(title1);
        tv_content.setText(content);

        iv_img.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(imgUrl)) {
            iv_img.setVisibility(View.VISIBLE);
            BitmapUtil.displayImage(imgUrl, iv_img, true);
        }

        BigDecimal p = new BigDecimal(finalPrice);
        double price = p.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        tv_total_price.setText(String.format("%s元", price));
    }

    private void getAllClassVideoOrder() {
        mLoading.show();
        mPresenter.packagefind(packageId, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {
                mLoading.dismiss();
                JSONObject jsonRes = JSON.parseObject(resStr);
                commodityId = jsonRes.getString("commodityId");
                title = jsonRes.getString("packageName");
                content = jsonRes.getString("description");
                finalPrice = jsonRes.getDouble("packagePrice") + "";

                StringBuilder ss = new StringBuilder();
                if(!TextUtils.isEmpty(content)){
                    ss.append(content);
                }
                List<String> array = JSON.parseArray(jsonRes.getString("presentName"),String.class);
                for(String e :array){
                    ss.append("\n\n").append(e);
                }
                content = ss.toString();

                iv_present.setVisibility(View.VISIBLE);

                setDataView();
            }

            @Override
            public void onResultFail() {
                mLoading.dismiss();
            }
        });
    }

    private void onPay() {
        mLoading.show();
        mPresenter.paymentcharge(orderType, payType, commodityId, finalPrice, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {
                mLoading.dismiss();
                JSONObject jsonRes = JSON.parseObject(resStr);
                String charge = jsonRes.getString("charge");
                Pingpp.createPayment(PayClassMediaActivity.this, charge);
            }

            @Override
            public void onResultFail() {
                mLoading.dismiss();
            }
        });
    }

    @OnClick({R.id.tv_pay, R.id.iv_alipay, R.id.iv_wechatpay, R.id.iv_unionpay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_pay:
                onPay();
                break;
            case R.id.iv_alipay:
                payType = "alipay";

                iv_alipay.setSelected(true);
                iv_wechatpay.setSelected(false);
                iv_unionpay.setSelected(false);
                break;
            case R.id.iv_wechatpay:
                payType = "wx";

                iv_alipay.setSelected(false);
                iv_wechatpay.setSelected(true);
                iv_unionpay.setSelected(false);
                break;
            case R.id.iv_unionpay:
                payType = "";

                iv_wechatpay.setSelected(false);
                iv_alipay.setSelected(false);
                iv_unionpay.setSelected(true);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            String result = data.getExtras().getString("pay_result");
            if (TextUtils.equals("success", result)) {//支付成功
                EventBus.getDefault().post(new AppEventType(AppEventType.PAY_SUCCESSS_REQUEST));
                PaySuccessFragment.newInstance().show(getSupportFragmentManager(), "PaySuccessFragment");
            }
        }
    }
}
