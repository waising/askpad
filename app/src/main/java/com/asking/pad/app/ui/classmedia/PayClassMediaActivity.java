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
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.pingplusplus.android.Pingpp;

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

    String payType = "alipay";
    /**
     * KC-单个初升高购买  TC-整套初升高购买
     */
    String orderType = "";
    String courseDataId;
    String finalPrice;

    /**
     * TC01 - 初升高衔接课-数学
      TC02- 初升高衔接课-物理
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
        mBundle.putString("coursePrice", mClassVideo.getPrice());
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

        if (TextUtils.equals(orderType, "KC")) {
            courseDataId = getIntent().getStringExtra("courseDataId");
            finalPrice =  getIntent().getStringExtra("coursePrice");

            tv_title.setText(getIntent().getStringExtra("courseName"));
            tv_title1.setText(getIntent().getStringExtra("courseTypeFullName"));
            tv_content.setText(getIntent().getStringExtra("description"));
            tv_total_price.setText(String.format("%s元",finalPrice));
        } else if (TextUtils.equals(orderType, "TC")) {
            packageId =  getIntent().getStringExtra("packageId");
            getAllClassVideoOrder();
        }
    }

    private void getAllClassVideoOrder() {
        mLoading.show();
        mPresenter.packagefind(packageId,new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {
                mLoading.dismiss();
                JSONObject jsonRes = JSON.parseObject(resStr);
                courseDataId = jsonRes.getString("commodityId");
                String packageName = jsonRes.getString("packageName");
                String description = jsonRes.getString("description");
                finalPrice = jsonRes.getDouble("packagePrice")+"";
                double packagePrice = jsonRes.getDouble("packagePrice")/100;

                tv_title.setText(packageName);
                tv_content.setText(description);
                tv_total_price.setText(String.format("%s元", packagePrice));
            }

            @Override
            public void onResultFail() {
                mLoading.dismiss();
            }
        });
    }

    private void onPay() {
        mLoading.show();
        mPresenter.paymentcharge(orderType, payType, courseDataId,finalPrice, new ApiRequestListener<String>() {
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
                finish();
            }
        }
    }
}
