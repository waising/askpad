package com.asking.pad.app.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenAppCompatActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.AppLoginEvent;
import com.asking.pad.app.entity.UserEntity;
import com.asking.pad.app.entity.classmedia.StudyRecord;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.downbook.db.DbHelper;
import com.asking.pad.app.ui.mine.MineAcivity;
import com.asking.pad.app.ui.update.CheckUpdateManager;
import com.asking.pad.app.widget.AskSimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by jswang on 2017/4/10.
 */

public class MainActivity extends BaseEvenAppCompatActivity<UserPresenter, UserModel> {

    @BindView(R.id.ad_avatar)
    AskSimpleDraweeView ad_avatar;

    @BindView(R.id.tv_user_name)
    TextView tv_user_name;

    @BindView(R.id.radio_group)
    RadioGroup radio_group;

    private int mCurTabIndex = 0;
    List<Fragment> fragments = new ArrayList<>();

    public static void openActivity(Activity activity){
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

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
        fragments.add(ClassMainFragment.newInstance());
        fragments.add(OtoMainFragment.newInstance());
        fragments.add(ShareMainFragment.newInstance());

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment,fragments.get(0))
                .add(R.id.fragment,fragments.get(1))
                .add(R.id.fragment,fragments.get(2))
                .hide(fragments.get(1))
                .hide(fragments.get(2))
                .show(fragments.get(0)).commit();

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_tab1:
                        setFragmentPage(0);
                        break;
                    case R.id.rb_tab2:
                        setFragmentPage(1);
                        break;
                    case R.id.rb_tab3:
                        setFragmentPage(2);
                        break;
                }
            }
        });

        initUser();
        findTreeListWithAllCourse();
    }

    private void setFragmentPage(int index) {
        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
        trx.hide(fragments.get(mCurTabIndex));
        if (!fragments.get(index).isAdded()) {
            trx.add(R.id.fragment, fragments.get(index));
        }
        trx.show(fragments.get(index)).commit();

        mCurTabIndex = index;
    }

    @OnClick({R.id.ll_user_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_user_info:
                MineAcivity.openActivity(this);
                break;
        }
    }

    private void initUser() {
        UserEntity mUser = AppContext.getInstance().getUserEntity();
        if (mUser != null) {
            ad_avatar.setImageUrl(mUser.getAvatar());
            tv_user_name.setText(mUser.getNickName());
        }
    }

    private void findTreeListWithAllCourse(){
        mPresenter.findTreeListWithAllCourse("KC03",new ApiRequestListener<String>(){
            @Override
            public void onResultSuccess(String res) {
                EventBus.getDefault().post(new AppEventType(AppEventType.RE_CLASSIFY_REQUEST));
            }
        });
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
            }
        });
    }



    private void clearUser() {
        tv_user_name.setText("");
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
                refreshUser();
                break;
            case AppEventType.PAY_SUCCESSS_REQUEST:
                refreshUser();
                findTreeListWithAllCourse();
                break;
        }
    }

    public void onEventMainThread(StudyRecord event) {
        DbHelper.getInstance().insertOrReplaceStudyRecord(event);
        mPresenter.updateWithSchedule(event.getCourseDataId(),event.getPlayPercentage()+""
                ,TextUtils.isEmpty(event.getScheduleTitle())?"":event.getScheduleTitle()
                , "","",new ApiRequestListener<String>(){
            @Override
            public void onResultSuccess(String res) {}
        });
    }

}
