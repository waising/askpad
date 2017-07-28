package com.asking.pad.app.ui.personalcenter;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.commom.AppLoginEvent;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.SystemUtil;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.login.LoginActivity;
import com.asking.pad.app.ui.update.CheckUpdateManager;
import com.asking.pad.app.ui.update.UpdateEvent;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 设置界面
 */
public class SettingActivity extends BaseFrameActivity<UserPresenter, UserModel> implements ModifyPasswordDialog.ChangePassWordListner {


    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.tv_log_out)
    TextView tvLogOut;
    @BindView(R.id.tv_log_in)
    TextView tvLogIn;
    @BindView(R.id.tv_edit_password)
    TextView tvEditPassword;
    @BindView(R.id.tv_about_us)
    TextView tvAboutUs;

    @BindView(R.id.ll_check_update)
    LinearLayout llCheckUpdate;

    @BindView(R.id.tv_version_update)
    TextView tvVersionUpdate;


    MaterialDialog loadDialog;
    private ModifyPasswordDialog resetPwdSuccessFrag;//修改密码弹窗
    private CheckUpdateManager checkUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        super.initView();
        setToolbar(toolBar, "设置");
        loginState(AppContext.getInstance().isLogin());
        loadDialog = getLoadingDialog().build();
        checkUpdateManager = new CheckUpdateManager(this, true);
        tvVersionUpdate.setText("当前版本：v" + SystemUtil.getVersionName(this));
    }

    @OnClick({R.id.tv_edit_password, R.id.tv_about_us, R.id.tv_log_out, R.id.tv_log_in, R.id.ll_check_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_edit_password://修改密码弹窗
                resetPwdSuccessFrag = new ModifyPasswordDialog();
                resetPwdSuccessFrag.setChangePasswordListner(this);
                resetPwdSuccessFrag.show(this.getSupportFragmentManager(), "");
                break;
            case R.id.tv_log_out://退出登录
                //TODO  退出方法
                loadDialog.show();
                mPresenter.logout(new ApiRequestListener<String>() {
                    @Override
                    public void onResultSuccess(String res) {
                        logOut();
                    }

                    @Override
                    public void onResultFail() {
                        logOut();
                    }
                });
                break;
            case R.id.tv_log_in:
                openActivity(LoginActivity.class);
                break;
            case R.id.tv_about_us://关于我们
                CommonUtil.openActivity(AboutUsActivity.class);
                break;
            case R.id.ll_check_update://系统更新
                requestAlertWindow();
                break;

        }
    }

    private void logOut() {
        EventBus.getDefault().post(new AppLoginEvent(AppLoginEvent.LOGIN_OUT));
        loadDialog.dismiss();
        AppContext.getInstance().clearLoginInfo();
        loginState(false);
        NIMClient.getService(AuthService.class).logout();
    }

    public void onEventMainThread(AppLoginEvent event) {
        if (event.type == AppLoginEvent.LOGIN_SUCCESS) {
            loginState(true);
        }
    }

    public void requestAlertWindow() {//自动更新弹窗
        checkUpdateManager.checkUpdate();
    }

    private void loginState(boolean isLogin) {
        if (isLogin) {
            tvLogIn.setVisibility(View.GONE);
            tvLogOut.setVisibility(View.VISIBLE);
        } else {
            tvLogIn.setVisibility(View.VISIBLE);
            tvLogOut.setVisibility(View.GONE);
        }
    }

    /**
     * 代码或关闭快门声音
     *
     * @param isClose
     */
    private void openOrCloseShutter(boolean isClose) {
        AppContext.getInstance().setPreferencesValue("CameraShutter", isClose);
    }

    /**
     * 修改密码请求
     *
     * @param originalPassword
     * @param newPassWord
     * @param confirmPassword
     */
    @Override
    public void changePassword(String originalPassword, String newPassWord, String confirmPassword) {
        if (vetifyOriginalPassword(originalPassword)) {
            return;
        } else if (vetifyNewPassWord(newPassWord)) {
            return;
        } else if (vetifyConfirmPassword(confirmPassword)) {
            return;
        } else if (!TextUtils.equals(newPassWord, confirmPassword)) {
            showShortToast(getString(R.string.password_no_equal));
            return;
        }
        mPresenter.presenterChangePassword(originalPassword, newPassWord, confirmPassword, new ApiRequestListener<JSONObject>() {//题目的id和类型请求
            @Override
            public void onResultSuccess(JSONObject object) {//成功修改密码
                showShortToast(getString(R.string.change_password_success));
                resetPwdSuccessFrag.dismiss();
            }
        });
    }

    /**
     * @param originalPassword
     * @return
     */

    private boolean vetifyOriginalPassword(String originalPassword) {
        if (TextUtils.isEmpty(originalPassword)) {
            showShortToast(getString(R.string.please_enter_old_pass));
            return true;
        } else if (originalPassword.length() < 6 || originalPassword.length() > 20) {
            showShortToast(getString(R.string.vertify_old_pass));
            return true;
        }
        return false;
    }

    private boolean vetifyNewPassWord(String newPassWord) {
        if (TextUtils.isEmpty(newPassWord)) {
            showShortToast(getString(R.string.please_enter_new_pass));
            return true;
        } else if (newPassWord.length() < 6 || newPassWord.length() > 20) {
            showShortToast(getString(R.string.vertify_new_pass));
            return true;
        }
        return false;
    }

    private boolean vetifyConfirmPassword(String confirmPassword) {
        if (TextUtils.isEmpty(confirmPassword)) {
            showShortToast(getString(R.string.please_enter_pass1));
            return true;
        } else if (confirmPassword.length() < 6 || confirmPassword.length() > 20) {
            showShortToast(getString(R.string.please_enter_pass1));
            return true;
        }
        return false;
    }

    public void onEventMainThread(UpdateEvent event) {
        switch (event.state) {
            case 0:
                checkUpdateManager.showProgressDialog();
                break;
            case 1:
                checkUpdateManager.setProgressDialog(event.titProgress, event.progress);
                break;
            case 2:
                checkUpdateManager.dismissProgressDialog();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
