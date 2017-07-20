package com.asking.pad.app.ui.sharespace;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenAppCompatActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.DateUtil;
import com.asking.pad.app.entity.QuestionEntity;
import com.asking.pad.app.entity.sharespace.ShareSpecial;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.camera.ui.CameraActivity;
import com.asking.pad.app.ui.sharespace.special.CommentAdapter;
import com.asking.pad.app.widget.AskMathView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jswang on 2017/7/17.
 */

public class QuestionAnwserActivity extends BaseEvenAppCompatActivity<UserPresenter, UserModel> {
    @BindView(R.id.edt_note_content)
    EditText edt_note_content;//新增笔记按钮

    @BindView(R.id.rv_comment)
    RecyclerView recycler;

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

    @BindView(R.id.answer_size)
    TextView answerSizeTv;

    ArrayList<QuestionEntity.AnwserMoreEntity> dataList = new ArrayList<>();
    QuestionCommentAdapter mAdapter;

    private MaterialDialog mLoadDialog;

    QuestionEntity questionEntity;

    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        super.initView();
        mLoadDialog = getLoadingDialog().build();
        mathView.showWebImage(this).formatMath();

        if(questionEntity!=null){
            userNameTv.setText(questionEntity.getUserName());
            kmTv.setText(questionEntity.getKm());
            timeTv.setText(questionEntity.getCreateDate_Fmt());

            if(questionEntity.getCaifu()>0) {
                askMoneyTv.setVisibility(View.VISIBLE);
                askIc.setVisibility(View.VISIBLE);
                askMoneyTv.setText(String.valueOf(questionEntity.getCaifu()));
            }
            questionTitleTv.setText(questionEntity.getTitle());
            mathView.setText(questionEntity.getDescription());
            answerSizeTv.setText(questionEntity.getAnswer_size()+"人回答");
        }

        recycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new QuestionCommentAdapter(this,dataList);
        recycler.setAdapter(mAdapter);
    }

    @Override
    public void initData(){
        super.initData();
        questionEntity = getIntent().getParcelableExtra("questionEntity");
        id = questionEntity.getId();
    }

    @Override
    public void initLoad(){
        super.initLoad();
        mPresenter.getQuestionDetail(id,new ApiRequestListener<String>(){
            @Override
            public void onResultSuccess(String resStr) {//数据返回成功
                QuestionEntity qe = new Gson().fromJson(resStr, QuestionEntity.class);
                dataList.clear();
                if(qe!=null){
                    dataList.addAll(qe.getList());
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void onEventMainThread(AppEventType event) {
        switch (event.type) {
            case AppEventType.NOTE_CAMERA_REQUEST:
                loadImage((String)event.values[0]);
                break;
        }
    }

    /**
     * 加载拍照后图片存储路径
     *
     * @param ivPhotoView
     * @param ivTakePhoto
     */
    String qiNiuImgName;
    public void loadImage(String filePath) {
        mLoadDialog.show();
        qiNiuImgName = DateUtil.currentDateMilltime().replace(":", "-").replace(" ", "_") + "note.jpg";
        mPresenter.qiNiuUploadFile(filePath, qiNiuImgName, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                String qiNiuUrl = Constants.QiNiuHead + qiNiuImgName;
                submit("5965d99d5905eec328694d0d","",qiNiuUrl);
            }

            @Override
            public void onResultFail() {
                mLoadDialog.dismiss();
            }
        });
    }

    private void submit(String communionTopicId,String content,String imgUrl){
        mPresenter.topicmsg(communionTopicId,content,imgUrl,new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                mLoadDialog.dismiss();
            }

            @Override
            public void onResultFail() {
                mLoadDialog.dismiss();
            }
        });
    }

    @OnClick({R.id.iv_photo_view,R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_photo_view:
                CameraActivity.openActivity(QuestionAnwserActivity.this, AppEventType.NOTE_CAMERA_REQUEST, CameraActivity.FROM_NOTE);
                break;
            case R.id.btn_submit:
                String content = edt_note_content.getText().toString();
                if(!TextUtils.isEmpty(content)){
                    Toast.makeText(QuestionAnwserActivity.this,"评价不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                mLoadDialog.show();
                submit("5965d99d5905eec328694d0d",content,"");
                break;
        }
    }

}
