package com.asking.pad.app.ui.sharespace;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.commom.ToastUtil;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.sharespace.special.SpecialItemFragment;
import com.asking.pad.app.widget.AskSimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 老师空间
 */
public class TeacherSpaceActivity extends BaseFrameActivity<UserPresenter, UserModel> {


    /**
     * toolbar
     */
    @BindView(R.id.toolBar)
    Toolbar toolBar;

    /**
     * 头像
     */
    @BindView(R.id.ad_avatar)
    AskSimpleDraweeView adAvatar;

    /**
     * 学科
     */
    @BindView(R.id.tv_class)
    TextView tvClass;

    /**
     * 教师名称
     */
    @BindView(R.id.tv_teacher_name)
    TextView tvTeacherName;

    /**
     * 粉丝数
     */
    @BindView(R.id.tv_fans_num)
    TextView tvFansNum;


    private String teacherId;
    /**
     * 是否选中
     */
    private boolean isSelected;

    private int fansNum;


    @BindView(R.id.iv_attention)
    ImageView ivAttention;
    private MaterialDialog mDialog;
    @BindView(R.id.user_name_tv)
    TextView titleName;
    SpecialItemFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_space);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        super.initView();
        mDialog = getLoadingDialog().build();
        toolBar.setTitle("");
        toolBar.setNavigationIcon(R.mipmap.ic_left_back);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fansNum = bundle.getInt("fansNum");
            adAvatar.setImageUrl(bundle.getString("teacherAvatar"));
            tvClass.setText("【" + bundle.getString("className") + "】");
            tvTeacherName.setText(bundle.getString("teacherName") + "老师");
            tvFansNum.setText(getString(R.string.fans_num, fansNum + ""));
            teacherId = bundle.getString("teacherId");
            isSelected = bundle.getBoolean("isSelected");
            ivAttention.setSelected(isSelected);
        }
        fragment = SpecialItemFragment.newInstance("", teacherId, "", "");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment).commit();

    }

    @Override
    public void initLoad() {
        super.initLoad();

    }


    @OnClick({R.id.iv_attention})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_attention://
                view.setSelected(!view.isSelected());
                if (view.isSelected())//取消关注
                {
                    requestCancelAttention(teacherId);
                } else {//关注
                    requestAttention(teacherId);
                }
                break;
        }
    }

    /**
     * 关注教师
     *
     * @param userId
     */
    private void requestAttention(String userId) {
        mDialog.show();
        mPresenter.presenterAttention(AppContext.getInstance().getUserName(), userId, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {//数据返回成功
                mDialog.dismiss();
                ToastUtil.showMessage("关注成功");

                fansNum = fansNum + 1;
                tvFansNum.setText(getString(R.string.fans_num, fansNum+ ""));
            }

            @Override
            public void onResultFail() {
                super.onResultFail();
                mDialog.dismiss();
                ToastUtil.showMessage("关注失败");
            }
        });


    }

    /**
     * 取消关注教师
     *
     * @param userId
     */
    private void requestCancelAttention(String userId) {
        mDialog.show();
        mPresenter.presenterCancelAttention(AppContext.getInstance().getUserName(), userId, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {//数据返回成功
                mDialog.dismiss();
                ToastUtil.showMessage("取消关注成功");

                fansNum = fansNum - 1;
                tvFansNum.setText(getString(R.string.fans_num, fansNum+ ""));
            }

            @Override
            public void onResultFail() {
                super.onResultFail();
                mDialog.dismiss();
                ToastUtil.showMessage("取消关注失败");
            }
        });

    }


}
