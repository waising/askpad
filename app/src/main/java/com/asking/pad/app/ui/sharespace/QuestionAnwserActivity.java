package com.asking.pad.app.ui.sharespace;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseActivity;
import com.asking.pad.app.entity.QuestionEntity;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.asking.pad.app.widget.AskMathView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/7/17.
 */

public class QuestionAnwserActivity extends BaseActivity {
    @BindView(R.id.toolBar)
    Toolbar toolBar;

    @BindView(R.id.tv_username)
    TextView tv_username;

    @BindView(R.id.km_tv)
    TextView kmTv;

    @BindView(R.id.time_tv)
    TextView timeTv;

    @BindView(R.id.ask_ic)
    ImageView askIc;

    @BindView(R.id.ask_money_tv)
    TextView askMoneyTv;

    @BindView(R.id.user_img_iv)
    ImageView userImgIv;

    @BindView(R.id.mathView)
    AskMathView mathView;

    @BindView(R.id.question_title_tv)
    TextView questionTitleTv;

    QuestionEntity questionEntity;

    String id = "",km = "";

    /**
     *  2-已采纳
     */
    int dataType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer);
        ButterKnife.bind(this);

        setToolbar(toolBar, "");

        questionEntity = getIntent().getParcelableExtra("questionEntity");
        id = questionEntity.getId();
        km = getIntent().getStringExtra("km");
        dataType = getIntent().getIntExtra("dataType",0);

        mathView.showWebImage(this).formatMath();

        tv_username.setText(questionEntity.getUserName());
        kmTv.setText(km);
        timeTv.setText(questionEntity.getCreateDate_Fmt());

        if(questionEntity.getCaifu()>0) {
            askMoneyTv.setVisibility(View.VISIBLE);
            askIc.setVisibility(View.VISIBLE);
            askMoneyTv.setText(String.valueOf(questionEntity.getCaifu()));
        }
        questionTitleTv.setText(questionEntity.getTitle());
        mathView.setText(questionEntity.getDescription());

        BitmapUtil.displayCirImage(questionEntity.getUserAvatar(),R.dimen.space_60, userImgIv);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, QuestionCommentFragment.newInstance(dataType,id,questionEntity.getUserId()
                        ,questionEntity.getUserAvatar())).commit();
    }
}
