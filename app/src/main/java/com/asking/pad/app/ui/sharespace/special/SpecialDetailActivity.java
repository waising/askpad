package com.asking.pad.app.ui.sharespace.special;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.DateUtil;
import com.asking.pad.app.commom.ParamHelper;
import com.asking.pad.app.entity.sharespace.ShareSpecial;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.asking.pad.app.ui.sharespace.TeacherSpaceActivity;
import com.asking.pad.app.widget.AskMathView;
import com.asking.pad.app.widget.MultiStateView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jswang on 2017/7/17.
 */

public class SpecialDetailActivity extends BaseFrameActivity<UserPresenter, UserModel> {
    @BindView(R.id.tv_favor_num)
    TextView tv_favor_num;

    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;

    @BindView(R.id.tv_teaname)
    TextView tv_teaname;

    @BindView(R.id.tv_subtime)
    TextView tv_subtime;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_statetime)
    TextView tv_statetime;

    @BindView(R.id.load_View)
    MultiStateView load_View;

    @BindView(R.id.content_mathview)
    AskMathView content_mathview;

    @BindView(R.id.tv_visitnum)
    TextView tv_visitnum;

    ShareSpecial mShareSpecial;

    public static void openActivity(ShareSpecial e) {
        HashMap<String, Object> mParams = ParamHelper.acquireParamsReceiver(SpecialDetailActivity.class.getName());
        mParams.put("ShareSpecial", e);
        CommonUtil.openActivity(SpecialDetailActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specical_detail);
        ButterKnife.bind(this);

        HashMap<String, Object> mParams = ParamHelper.acceptParams(SpecialDetailActivity.class.getName());
        mShareSpecial = (ShareSpecial) mParams.get("ShareSpecial");
    }

    @Override
    public void initView() {
        super.initView();
        content_mathview.formatMath().showWebImage(load_View);
        content_mathview.setText(mShareSpecial.contentHtml);

        tv_teaname.setText(mShareSpecial.getTeaNickName() + "老师");
        tv_subtime.setText(DateUtil.getYYMMDDHHMM(mShareSpecial.getCreateDate()));
        tv_title.setText(mShareSpecial.name);
        tv_statetime.setText(String.format("%s———%s", DateUtil.getYYMMDDHHMM(mShareSpecial.startTime)
                , DateUtil.getHHMM(mShareSpecial.endTime)));
        tv_visitnum.setText("浏览" + mShareSpecial.seenCount);
        BitmapUtil.displayCirImage(mShareSpecial.getTeaAvatarUrl(), R.dimen.space_80, iv_avatar);

        tv_favor_num.setSelected(mShareSpecial.followed);
        tv_favor_num.setText(mShareSpecial.followCount + "");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, SpecialCommentFragment.newInstance(mShareSpecial.id)).commit();
    }

    @OnClick({R.id.tv_favor_num,R.id.iv_avatar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_favor_num:
                mPresenter.topicfollow(!tv_favor_num.isSelected(), mShareSpecial.id, new ApiRequestListener<String>() {
                    @Override
                    public void onResultSuccess(String res) {
                        tv_favor_num.setSelected(!tv_favor_num.isSelected());
                        try {
                            if (tv_favor_num.isSelected()) {
                                mShareSpecial.followCount = (Integer.valueOf(mShareSpecial.followCount) + 1) + "";
                                tv_favor_num.setText(mShareSpecial.followCount);
                            } else {
                                mShareSpecial.followCount = (Integer.valueOf(mShareSpecial.followCount) - 1) + "";
                                tv_favor_num.setText(mShareSpecial.followCount);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case R.id.iv_avatar://头像
                Bundle bundle = new Bundle();
                bundle.putString("teacherName", mShareSpecial.getTeaNickName());
                bundle.putString("className", mShareSpecial.getTeaSubject());
                bundle.putString("teacherAvatar", mShareSpecial.getTeaAvatarUrl());
                bundle.putInt("fansNum", mShareSpecial.getTeaFansNum());
                bundle.putString("teacherId", mShareSpecial.getTeaId());
                bundle.putBoolean("isSelected", !mShareSpecial.getTeaFavored());
                openActivity(TeacherSpaceActivity.class, bundle);
                break;

        }
    }

}
