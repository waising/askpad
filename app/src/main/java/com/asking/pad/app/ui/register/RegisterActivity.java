package com.asking.pad.app.ui.register;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.AppManager;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jswang on 2017/4/6.
 */

public class RegisterActivity extends BaseEvenActivity<UserPresenter, UserModel> {
    @BindView(R.id.et_phone)
    EditText et_phone;

    @BindView(R.id.et_password)
    EditText et_password;

    @BindView(R.id.et_affirm_password)
    EditText et_affirm_password;

    @BindView(R.id.et_verificode)
    EditText et_verificode;

    @BindView(R.id.tv_checbox_register)
    View tv_checbox_register;

    @BindView(R.id.ll_checbox_register)
    View ll_checbox_register;

    @BindView(R.id.btn_reg)
    Button btn_reg;

    @BindView(R.id.tv_verificode)
    TextView tv_verificode;

    /**
     * 0-注册，1-忘记密码
     */
    int isRegister = 0;

    MaterialDialog loadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        isRegister = this.getIntent().getIntExtra("isRegister", 0);
    }

    @Override
    public void initView() {
        super.initView();
        if (isRegister == 1) {
            btn_reg.setText(getString(R.string.commit));
            ll_checbox_register.setVisibility(View.GONE);
        }

        loadDialog = getLoadingDialog().build();
    }

    public void onEventMainThread(AppEventType event) {
        switch (event.type) {
            case AppEventType.WEB_VIEW_PROTOCOL_REQUEST:
                tv_checbox_register.setSelected(true);
                break;
        }
    }

    private void register() {
        String phone = et_phone.getText().toString();
        String password = et_password.getText().toString();
        String affirm_password = et_affirm_password.getText().toString();
        String verificode = et_verificode.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            CommonUtil.Toast(this, getResources().getString(R.string.register_account_hint));
            return;
        }
        if (et_phone.getText().toString().length() != 11) {
            CommonUtil.Toast(this, "请输入正确手机号");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            CommonUtil.Toast(this, getResources().getString(R.string.login_pass_hint));
            return;
        }
        if (TextUtils.isEmpty(affirm_password)) {
            CommonUtil.Toast(this, "请再次确认密码");
            return;
        }
        if (!TextUtils.equals(affirm_password, password)) {
            CommonUtil.Toast(this, "两次输入密码不一样");
            return;
        }
        if (TextUtils.isEmpty(verificode)) {
            CommonUtil.Toast(this, "请输入手机验证码");
            return;
        }
        if (isRegister == 1) {
            loadDialog.show();
            mPresenter.resetPass(phone, password, verificode, new ApiRequestListener<JSONObject>() {
                @Override
                public void onResultSuccess(JSONObject res) {
                    loadDialog.dismiss();
                    finish();
                }
            });
        } else {
            loadDialog.show();
            if (tv_checbox_register.isSelected()) {
                mPresenter.register(phone, password, verificode, new ApiRequestListener<JSONObject>() {
                    @Override
                    public void onResultSuccess(JSONObject res) {
                        loadDialog.dismiss();

                        AppContext.getInstance().saveToken(res.getString("ticket"));
                        AppContext.getInstance().saveUserData(res.getString("userInfo"));
                        CommonUtil.Toast(mActivity, res.getString("msg"));

                        AppManager.getAppManager().finishActivity(LoginActivity.class);
                        finish();
                    }
                });
            } else {
                CommonUtil.Toast(mActivity, "请先阅读并同意条款内容");
            }
        }
    }

    private void getVerifiCode() {
        String phone = et_phone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            CommonUtil.Toast(this, getResources().getString(R.string.register_account_hint));
            return;
        }
        setVerificodeView();
        if (isRegister == 1) {
            mPresenter.getResetPassYZM(phone,new ApiRequestListener<JSONObject>(){
                @Override
                public void onResultFail() {
                    reVerifiView();
                }
            });
        } else {
            mPresenter.getYZM(phone,new ApiRequestListener<JSONObject>(){
                @Override
                public void onResultFail() {
                    reVerifiView();
                }
            });
        }
    }

    private void reVerifiView(){
        try {
            if(mTime != null){
                mTime.cancel();
                mTime = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        tv_verificode.setText("获取验证码");
        tv_verificode.setEnabled(true);
    }


    @OnClick({R.id.tv_verificode, R.id.tv_checbox_register, R.id.register_chbx_text, R.id.btn_reg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_verificode:
                getVerifiCode();
                break;
            case R.id.tv_checbox_register:
                tv_checbox_register.setSelected(!tv_checbox_register.isSelected());
                break;
            case R.id.forget_password:

                break;
            case R.id.btn_reg:
                register();
                break;
            case R.id.register_chbx_text:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.WEB_URL, Constants.PROTOCOL);
                bundle.putString(Constants.WEB_TITLE, "服务条款");
                openActivity(WebViewProtocolActivity.class, bundle);
                break;
        }
    }

    private VerifiTimeCount mTime;
    private  void setVerificodeView(){
        tv_verificode.setEnabled(false);
        if(mTime == null){
            mTime  = new VerifiTimeCount(60000, 1000);
        }
        mTime.start();
    }

    class VerifiTimeCount extends CountDownTimer {

        public VerifiTimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_verificode.setText(String.format("%s秒",millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            reVerifiView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTime != null){
            mTime.cancel();
            mTime = null;
        }
    }
}
