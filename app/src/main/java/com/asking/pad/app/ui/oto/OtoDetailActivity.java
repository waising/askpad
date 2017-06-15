package com.asking.pad.app.ui.oto;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.RadioButton;
import com.asking.pad.app.R;
import com.asking.pad.app.base.SwipeBackActivity;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.widget.AskSimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/4/18.
 */

public class OtoDetailActivity extends SwipeBackActivity {
    @BindView(R.id.toolBar)
    Toolbar toolBar;

    @BindView(R.id.pic_take)
    AskSimpleDraweeView pic_take;

    @BindView(R.id.btn_class)
    RadioButton btn_class;

    @BindView(R.id.btn_gread)
    RadioButton btn_gread;

    @BindView(R.id.btn_askcoin)
    RadioButton btn_askcoin;


    private String subject = "M2";
    private String grade = "07";
    private String qiNiuUrl;
    private int askMoney = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oto_detail);

        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initView() {
        super.initView();
        setToolbar(toolBar,"查看详情");

        subject = getIntent().getStringExtra("subjectId");
        askMoney = getIntent().getIntExtra("askMoney", 0);
        grade = getIntent().getStringExtra("gradeId");
        qiNiuUrl = getIntent().getStringExtra("qiNiuUrl");

        pic_take.setImageUrl(qiNiuUrl);

        btn_askcoin.setText(askMoney+"");
        if(subject.indexOf("M")!=-1){
            btn_class.setText("数学");
        }else if(subject.indexOf("P")!=-1){
            btn_class.setText("物理");
        }
        for(int i = 0; i< Constants.versionTvValues.length; i++){
            if(grade.equals(Constants.versionTvValues[i])){
                btn_gread.setText(Constants.versionTv[i]);
            }
        }
    }
}