package com.asking.pad.app.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenAppCompatActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.AppLoginEvent;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.entity.UserEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.mine.SignInActivity;
import com.asking.pad.app.ui.mine.UserInfoActivity;
import com.asking.pad.app.ui.mine.WrongTopicActivity;
import com.asking.pad.app.ui.personalcenter.AskCoinRecordActivity;
import com.asking.pad.app.ui.update.CheckUpdateManager;
import com.asking.pad.app.widget.AskSimpleDraweeView;
import com.asking.pad.app.widget.indicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.asking.pad.app.R.id.tv_error_note;

/**
 * Created by jswang on 2017/4/10.
 */

public class MainActivity extends BaseEvenAppCompatActivity<UserPresenter, UserModel> {

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

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.indicator)
    TabPageIndicator indicator;

    List<Fragment> listFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        CheckUpdateManager checkUpdateManager = new CheckUpdateManager(this, false);
        checkUpdateManager.checkUpdate();
    }

    @Override
    public void initView() {
        listFragments.add(new FirstMainFragment());
        listFragments.add(new SecondMainFragment());

        CommAdapter mAdapter = new CommAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(2);
        indicator.setLayoutResource(R.layout.layout_indicator_tab_view6);
        indicator.setViewPager(viewPager);

        initUser();
        findTreeListWithAllCourse();
    }

    @OnClick({R.id.ll_user_info, R.id.tv_sign, tv_error_note, R.id.tv_msg, R.id.tv_monny})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sign:
                CommonUtil.openAuthActivity(SignInActivity.class);
                break;
            case tv_error_note:
                CommonUtil.openAuthActivity(WrongTopicActivity.class);
                break;
            case R.id.tv_msg:

                break;
            case R.id.ll_user_info:
                CommonUtil.openAuthActivity(UserInfoActivity.class);
                break;
            case R.id.tv_monny:
                CommonUtil.openAuthActivity(AskCoinRecordActivity.class);
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

            tv_name.setSelected(mUser.getSex() == 0 ? false : true);

            tv_grade.setText(Constants.getUserGradeName()); // 年级
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

    private void findTreeListWithAllCourse(){
        mPresenter.findTreeListWithAllCourse("KC03",new ApiRequestListener<String>(){
            @Override
            public void onResultSuccess(String res) {
                EventBus.getDefault().post(new AppEventType(AppEventType.RE_CLASSIFY_REQUEST));
            }
        });
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
                findTreeListWithAllCourse();
                break;
            case AppLoginEvent.LOGIN_OUT:
                clearUser();
                break;
        }
    }

    public void onEventMainThread(AppEventType event) {
        switch (event.type) {
            case AppEventType.RE_USER_INFO_REQUEST:
                initUser();
                break;
            case AppEventType.PAY_SUCCESSS_REQUEST:
                refreshUser();
                findTreeListWithAllCourse();
                break;
        }
    }

    class CommAdapter extends FragmentStatePagerAdapter {

        public CommAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return listFragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public int getCount() {
            return listFragments.size();
        }
    }
}
