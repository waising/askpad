package com.asking.pad.app.ui.login;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.BuildConfig;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseSwipeBackFrameActivity;
import com.asking.pad.app.commom.AppLoginEvent;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.register.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by jswang on 2017/4/6.
 */

public class LoginActivity extends BaseSwipeBackFrameActivity<UserPresenter, UserModel> {
    @BindView(R.id.et_phone)
    EditText et_phone;

    @BindView(R.id.et_password)
    EditText et_password;

    @BindView(R.id.btn_register)
    Button btn_register;

    ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.parseColor("#38c1ff"));

    MaterialDialog loadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


//        loginAccount.setText("gz0001");
//            loginAccount.setText("GZ18888");
//            loginAccount.setText("GZ18889");
//            loginPass.setText("123456");
//            loginAccount.setText("CZ5111");
//            loginAccount.setText("CZ4001");

        String phone = AppContext.getInstance().geLoginAccount();
        String password = AppContext.getInstance().geLoginPassword();
        et_phone.setText(phone);
        et_password.setText(password);

        if(BuildConfig.DEBUG){
            //et_phone.setText("GZ0001");
//            et_phone.setText("13960917657");
//            et_password.setText("Jing47");
            et_phone.setText("18120845259");
            et_password.setText("123456");
        }
    }

    @Override
    public void initView() {
        SpannableStringBuilder builder = new SpannableStringBuilder(btn_register.getText().toString());
        builder.setSpan(blueSpan, 6, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        btn_register.setText(builder);

        loadDialog = getLoadingDialog().build();
    }

    private void login() {
        final String phone = et_phone.getText().toString();
        final String password = et_password.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            CommonUtil.Toast(this, getResources().getString(R.string.register_account_hint));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            CommonUtil.Toast(this, getResources().getString(R.string.login_pass_hint));
            return;
        }
        loadDialog.show();
        mPresenter.ssoLogin(phone, password, new ApiRequestListener<JSONObject>() {
            @Override
            public void onResultSuccess(JSONObject res) {
                loadDialog.dismiss();

                AppContext.getInstance().saveToken(res.getString("ticket"));
                AppContext.getInstance().saveUserData(res.getString("userInfo"));
                CommonUtil.Toast(mActivity, res.getString("msg"));

                AppContext.getInstance().setLoginAccount(phone);
                AppContext.getInstance().setLoginPassword(password);

                EventBus.getDefault().post(new AppLoginEvent(AppLoginEvent.LOGIN_SUCCESS));
                finish();
            }

            @Override
            public void onResultFail() {
                loadDialog.dismiss();
            }
        });
    }

    @OnClick({R.id.et_phone, R.id.et_password, R.id.forget_password, R.id.btn_login, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_phone:

                break;
            case R.id.et_password:

                break;
            case R.id.forget_password:
                Bundle parameter = new Bundle();
                parameter.putInt("isRegister", 1);
                openActivity(RegisterActivity.class, parameter);
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                openActivity(RegisterActivity.class);
                break;
        }
    }

}
