package com.asking.pad.app.ui.sharespace;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.QuestionEntity;
import com.asking.pad.app.widget.AskMathView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/7/17.
 */

public class QuestionAnwserActivity extends AppCompatActivity {
    @BindView(R.id.user_name_tv)
    TextView userNameTv;

    @BindView(R.id.km_tv)
    TextView kmTv;
    @BindView(R.id.time_tv)
    TextView timeTv;

    @BindView(R.id.ask_ic)
    ImageView askIc;
    @BindView(R.id.ask_money_tv)
    TextView askMoneyTv;

    @BindView(R.id.mathView)
    AskMathView mathView;
    @BindView(R.id.question_title_tv)
    TextView questionTitleTv;

    QuestionEntity questionEntity;

    String id = "",km = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer);
        ButterKnife.bind(this);

        questionEntity = getIntent().getParcelableExtra("questionEntity");
        id = questionEntity.getId();
        km = getIntent().getStringExtra("km");

        mathView.showWebImage(this).formatMath();

        if(questionEntity!=null){
            userNameTv.setText(questionEntity.getUserName());
            kmTv.setText(km);
            timeTv.setText(questionEntity.getCreateDate_Fmt());

            if(questionEntity.getCaifu()>0) {
                askMoneyTv.setVisibility(View.VISIBLE);
                askIc.setVisibility(View.VISIBLE);
                askMoneyTv.setText(String.valueOf(questionEntity.getCaifu()));
            }
            questionTitleTv.setText(questionEntity.getTitle());
            mathView.setText(questionEntity.getDescription());
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, QuestionCommentFragment.newInstance(id,questionEntity.getUserId())).commit();
    }
}
