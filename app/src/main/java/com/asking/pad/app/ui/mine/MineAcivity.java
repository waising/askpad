package com.asking.pad.app.ui.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.AppLoginEvent;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.entity.UserEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.classmedia.cache.ClassMediaCacheActivity;
import com.asking.pad.app.ui.pay.PayAskActivity;
import com.asking.pad.app.ui.personalcenter.AboutUsActivity;
import com.asking.pad.app.ui.personalcenter.AskCoinRecordActivity;
import com.asking.pad.app.ui.personalcenter.NoteActivity;
import com.asking.pad.app.ui.personalcenter.SettingActivity;
import com.asking.pad.app.widget.AskSimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jswang on 2017/4/10.
 */

public class MineAcivity extends BaseEvenActivity<UserPresenter, UserModel> {
    @BindView(R.id.toolBar)
    Toolbar mToolbar;

    @BindView(R.id.ad_avatar)
    AskSimpleDraweeView ad_avatar;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_shcool)
    TextView tv_shcool;

    @BindView(R.id.tv_grade)
    TextView tv_grade;

    @BindView(R.id.tv_monny)
    TextView tv_monny;

    public static void openActivity(Activity activity){
        Intent intent = new Intent(activity, MineAcivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_second_main);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        super.initView();

        mToolbar.setTitle("个人中心");
        mToolbar.inflateMenu(R.menu.menu_mine_set);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                CommonUtil.openAuthActivity(SettingActivity.class);
                return false;
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initUser();
    }

    @OnClick({R.id.ll_user_info,R.id.ll_pay_book, R.id.ll_cache, R.id.ll_note, R.id.ll_mine_askcoin, R.id.ll_app_down, R.id.ll_about_me})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_user_info:
                CommonUtil.openAuthActivity(UserInfoActivity.class);
                break;
            case R.id.ll_pay_book:
                PayAskActivity.openActivity(0);
                break;
            case R.id.ll_cache:
                CommonUtil.openActivity(ClassMediaCacheActivity.class);
                break;
            case R.id.ll_note:
                CommonUtil.openActivity(NoteActivity.class);
                break;
            case R.id.ll_mine_askcoin:
                CommonUtil.openAuthActivity(AskCoinRecordActivity.class);
                break;
            case R.id.ll_app_down:
                AppDownQRCodeDialog mDialog = new AppDownQRCodeDialog(this);
                mDialog.show();
                break;
            case R.id.ll_about_me://关于我们
                CommonUtil.openActivity(AboutUsActivity.class);
                break;
        }
    }

    private void clearUser() {
        tv_name.setText("");
        tv_shcool.setText("");
        tv_monny.setText("");
        tv_grade.setText("");
    }

    public void onEventMainThread(AppLoginEvent event) {
        switch (event.type) {
            case AppLoginEvent.LOGIN_SUCCESS:
                if (!AppContext.getInstance().isUserDataPerfect()) {
                    mPresenter.checkUserInfo();
                }
                initUser();
                break;
            case AppLoginEvent.LOGIN_OUT:
                clearUser();
                break;
        }
    }

    private void refreshUser() {
        mPresenter.studentinfo(new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                JSONObject resObject = JSON.parseObject(res);
                double integral = resObject.getDouble("integral");

                if (integral != AppContext.getInstance().getUserEntity().getIntegral()) {
                    AppContext.getInstance().getUserEntity().setIntegral(integral);
                    AppContext.getInstance().saveUserData(AppContext.getInstance().getUserEntity());
                }
                tv_monny.setText(String.valueOf(integral));
            }
        });
    }

    public void onEventMainThread(AppEventType event) {
        switch (event.type) {
            case AppEventType.PAY_SUCCESSS_REQUEST:
                refreshUser();
                break;
        }
    }

    private void initUser() {
        UserEntity mUser = AppContext.getInstance().getUserEntity();
        if (mUser != null) {
            ad_avatar.setImageUrl(mUser.getAvatar());
            tv_name.setText(mUser.getNickName());
            tv_shcool.setText(mUser.getSchoolName());
            tv_monny.setText(String.valueOf(mUser.getIntegral()));

            tv_grade.setText(Constants.getUserGradeName()); // 年级
        }
    }
}
