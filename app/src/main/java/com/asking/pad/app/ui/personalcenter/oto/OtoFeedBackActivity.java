package com.asking.pad.app.ui.personalcenter.oto;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseActivity;
import com.asking.pad.app.commom.DateUtil;
import com.asking.pad.app.entity.RecordAnswer;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/6/13.
 */

public class OtoFeedBackActivity extends BaseActivity {

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

    @BindView(R.id.tv_tea_name)
    TextView tv_tea_name;

    @BindView(R.id.tv_tea_code)
    TextView tv_tea_code;

    @BindView(R.id.tv_cause)
    TextView tv_cause;

    @BindView(R.id.tv_feedback)
    TextView tv_feedback;

    RecordAnswer mRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oto_feed_back);
        ButterKnife.bind(this);

        mRecord = (RecordAnswer)this.getIntent().getSerializableExtra("OtoRecord");
    }

    @Override
    public void initView() {
        super.initView();

        setToolbar(toolBar,"投诉详情");

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
}
