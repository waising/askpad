package com.asking.pad.app.ui.personalcenter.oto;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.commom.CourseViewHelper;
import com.asking.pad.app.commom.DateUtil;
import com.asking.pad.app.commom.OnItemLabelEntityListener;
import com.asking.pad.app.entity.LabelEntity;
import com.asking.pad.app.entity.RecordAnswer;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.asking.pad.app.ui.oto.FeedBackDialog;
import com.asking.pad.app.widget.StarView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jswang on 2017/6/13.
 */

public class OtoCommentActivity extends BaseFrameActivity<UserPresenter, UserModel> {

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

    MaterialDialog loadDialog;

    ArrayList<LabelEntity> askcoinList = new ArrayList<>();
    RecordAnswer mRecord;

    int askCoin = 0;
    int askStar = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oto_end);
        ButterKnife.bind(this);

        mRecord = (RecordAnswer)this.getIntent().getSerializableExtra("OtoRecord");
    }

    @Override
    public void initView() {
        super.initView();

        setToolbar(toolBar,"评价");
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
        loadDialog = getLoadingDialog().content("加载中...").build();

        iv_firstorder_flag.setVisibility(View.GONE);

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


        if (mRecord.bill != null) {
            tv_take_coin.setText(mRecord.bill.price);
        }

        if (mRecord.time != null) {
            tv_time.setText(DateUtil.getTimeExpend(mRecord.time.startTime, mRecord.time.endTime));
        }

        if (mRecord.student != null) {
            tv_user_coin.setText(mRecord.student.integral+"");
        }

        if(mRecord.teacher != null){
            tv_tea_name.setText(mRecord.teacher.name);
            tv_tea_code.setText(mRecord.teacher.code);
            if (!TextUtils.isEmpty(mRecord.teacher.avatar)) {
                BitmapUtil.displayUserImage(this, mRecord.teacher.avatar, user_img_iv);
            }
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
                mPresenter.orderevaluate(mRecord.orderId, askCoin + "", askStar + "", et_content.getText().toString(), new ApiRequestListener<String>() {
                    @Override
                    public void onResultSuccess(String res) {
                        Toast.makeText(OtoCommentActivity.this,"已评价",Toast.LENGTH_SHORT).show();
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
        if(mRecord.teacher != null){
            String userName = AppContext.getInstance().getUserName();
            mPresenter.studentfavor(userName, mRecord.teacher.account, new ApiRequestListener<String>() {
                @Override
                public void onResultSuccess(String res) {
                    cb_favor.setChecked(true);
                }
            });
        }
    }

    FeedBackDialog mDialog;
    private void showFeedBack(){
        mDialog = new FeedBackDialog(mActivity,new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String cause = mDialog.tv_cause.getText().toString();
                String content = mDialog.et_content.getText().toString();
                mPresenter.studentcomplain(cause,content,mRecord.orderId,new ApiRequestListener<String>(){
                    @Override
                    public void onResultSuccess(String res) {
                        Toast.makeText(OtoCommentActivity.this,"已投诉",Toast.LENGTH_SHORT).show();
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
}
