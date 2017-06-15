package com.asking.pad.app.ui.oto;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.CourseViewHelper;
import com.asking.pad.app.commom.DateUtil;
import com.asking.pad.app.commom.OnItemLabelEntityListener;
import com.asking.pad.app.commom.ParamHelper;
import com.asking.pad.app.entity.LabelEntity;
import com.asking.pad.app.entity.OtoRecord;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.asking.pad.app.widget.StarView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by jswang on 2017/4/18.
 */

public class OtoEndActivity extends BaseFrameActivity<UserPresenter, UserModel> {
    @BindView(R.id.toolBar)
    Toolbar toolBar;

    @BindView(R.id.user_img_iv)
    ImageView user_img_iv;

    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.tv_take_coin)
    TextView tv_take_coin;

    @BindView(R.id.tv_user_coin)
    TextView tv_user_coin;

    @BindView(R.id.tv_tea_code)
    TextView tv_tea_code;

    @BindView(R.id.tv_tea_name)
    TextView tv_tea_name;

    @BindView(R.id.iv_firstorder_flag)
    View iv_firstorder_flag;

    @BindView(R.id.start_view)
    StarView start_view;

    @BindView(R.id.et_content)
    EditText et_content;

    @BindView(R.id.cb_favor)
    CheckBox cb_favor;

    @BindView(R.id.ll_askcoin)
    LinearLayout ll_askcoin;

    ArrayList<LabelEntity> askcoinList = new ArrayList<>();

    int askTimes;
    String orderId;
    String teaName;
    String teaAccount;
    String teaAvatar;

    int askCoin = 0;
    int askStar = 0;

    MaterialDialog loadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_oto_end, null);
        setContentView(contentView);
        ButterKnife.bind(this);

        HashMap<String, Object> mParams = ParamHelper.acceptParams(OtoLearningActivity.class.getName());
        orderId = (String) mParams.get("orderId");
        askTimes = (int) mParams.get("askTimes");
        teaName = (String) mParams.get("teaName");
        teaAvatar = (String) mParams.get("teaAvatar");

        cb_favor.setChecked(false);
    }

    @Override
    public void initView() {
        super.initView();
        setToolbar(toolBar, "答疑结束");

        loadDialog = getLoadingDialog().content("加载中...").build();

        //投诉
        toolBar.inflateMenu(R.menu.menu_oto_end);
        toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_follow:
                        showFeedBack();
                        break;
                }
                return true;
            }
        });

        tv_tea_name.setText(teaName);
        if (!TextUtils.isEmpty(teaAvatar)) {
            BitmapUtil.displayUserImage(this, teaAvatar, user_img_iv);
        }

        askcoinList.add(new LabelEntity(0,"","0 枚",true));
        askcoinList.add(new LabelEntity(1,"","1 枚"));
        askcoinList.add(new LabelEntity(2,"", "2 枚"));
        askcoinList.add(new LabelEntity(3,"", "3 枚"));
        askcoinList.add(new LabelEntity(4,"","4 枚"));
        askcoinList.add(new LabelEntity(5,"", "5 枚"));
        CourseViewHelper.getView2(this, ll_askcoin, getString(R.string.oto_t3), CourseViewHelper.getCourseViewAdapter(this, askcoinList
                , new OnItemLabelEntityListener() {
                    @Override
                    public void OnItemLabelEntity(LabelEntity e) {
                        askCoin = e.getIcon();
                    }
                }));

        start_view.setmStarItemClickListener(new StarView.OnStarItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                askStar = pos + 1;
            }
        });
    }

    FeedBackDialog mDialog;
    private void showFeedBack(){
        mDialog = new FeedBackDialog(mActivity,new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String cause = mDialog.tv_cause.getText().toString();
                String content = mDialog.et_content.getText().toString();
                mPresenter.studentcomplain(cause,content,orderId,new ApiRequestListener<String>(){
                    @Override
                    public void onResultSuccess(String res) {
                        Toast.makeText(OtoEndActivity.this,"已投诉",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onResultFail() {
                        finish();
                    }
                });
            }
        });
        mDialog.show();
    }

    @Override
    public void initData() {
        super.initData();
        if (askTimes == 0) {
            mPresenter.orderfirstorder(orderId, new ApiRequestListener<String>() {
                @Override
                public void onResultSuccess(String res) {
                    initUserInfo(res);
                }

                @Override
                public void onResultFail() {
                    tv_user_coin.setText(AppContext.getInstance().getUserEntity().getIntegral()+"");
                }
            });
        } else {
            mPresenter.ordercheckbill(orderId, new ApiRequestListener<String>() {
                @Override
                public void onResultSuccess(String res) {
                    initUserInfo(res);
                }

                @Override
                public void onResultFail() {
                    tv_user_coin.setText(AppContext.getInstance().getUserEntity().getIntegral()+"");
                }
            });
            iv_firstorder_flag.setVisibility(View.GONE);
        }
    }

    private void initUserInfo(String res) {
        try {
            OtoRecord e = JSON.parseObject(res, OtoRecord.class);
            if (e.bill != null) {
                tv_take_coin.setText(e.bill.price);
            }
            if (e.time != null) {
                tv_time.setText(DateUtil.getTimeExpend(e.time.startTime, e.time.endTime));
            }
            if (e.teacher != null) {
                teaAccount = e.teacher.account;
                tv_tea_code.setText(e.teacher.code);
            }
            if (e.student != null) {
                tv_user_coin.setText(e.student.integral+"");

                AppContext.getInstance().getUserEntity().setIntegral(e.student.integral);
                AppContext.getInstance().saveUserData(AppContext.getInstance().getUserEntity());
                EventBus.getDefault().post(new AppEventType(AppEventType.RE_USER_INFO_REQUEST));
            }
        } catch (Exception e) {
        }
    }

    @OnClick({ R.id.cb_favor,R.id.btn_submit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cb_favor:
                if (!cb_favor.isChecked()) {
                    studentfavor();
                }
                break;
            case R.id.btn_submit:
                loadDialog.show();
                mPresenter.orderevaluate(orderId, askCoin + "", askStar + "", et_content.getText().toString(), new ApiRequestListener<String>() {
                    @Override
                    public void onResultSuccess(String res) {
                        Toast.makeText(OtoEndActivity.this,"已评价",Toast.LENGTH_SHORT).show();
                        if(askCoin>0) {
                            AppContext.getInstance().getUserEntity().setIntegral(AppContext.getInstance().getUserEntity().getIntegral() - askCoin);
                            AppContext.getInstance().saveUserData(AppContext.getInstance().getUserEntity());
                        }
                        finish();
                    }

                    @Override
                    public void onResultFail() {
                        loadDialog.dismiss();
                    }
                });
                break;
        }
    }

    private void studentfavor() {
        String userName = AppContext.getInstance().getUserName();
        mPresenter.studentfavor(userName, teaAccount, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                cb_favor.setChecked(true);
            }
        });
    }
}
