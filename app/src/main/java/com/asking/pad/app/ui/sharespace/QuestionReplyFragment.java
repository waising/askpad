package com.asking.pad.app.ui.sharespace;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDAdapter;
import com.alibaba.fastjson.JSON;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.entity.QuestionEntity;
import com.asking.pad.app.entity.sharespace.SpecialComment;
import com.asking.pad.app.mvp.OnCountDownListener;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.camera.ui.CameraActivity;
import com.asking.pad.app.ui.sharespace.special.CommentAdapter;
import com.asking.pad.app.widget.MultiStateView;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;

/**
 * Created by jswang on 2017/7/19.
 */

public class QuestionReplyFragment extends BaseFrameFragment<UserPresenter, UserModel> {
    @BindView(R.id.load_comment)
    MultiStateView load_comment;

    @BindView(R.id.rv_comment)
    RecyclerView rv_comment;

    @BindView(R.id.comment_content_ll)
    LinearLayout commentLL;

    @BindView(R.id.edt_note_content)
    EditText edt_note_content;

    @BindView(R.id.btn_submit)
    Button submitBtn;

    ArrayList<QuestionEntity.AnswerDetail> dataDetailList = new ArrayList<>();
    ArrayList<QuestionEntity.AnwserMoreEntity> dataList = new ArrayList<>();

    QuestionReplyCommentAdapter mAdapter;

    private MaterialDialog mLoadDialog;
    boolean isLoginuUser;
    QuestionEntity.AnwserMoreEntity anwserMoreEntity;
    String questionId;

    public static QuestionReplyFragment newInstance(QuestionEntity.AnwserMoreEntity e, String questionId,boolean isLoginuUser) {
        QuestionReplyFragment fragment = new QuestionReplyFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("anwserMoreEntity",e);
        bundle.putString("questionId", questionId);
        bundle.putBoolean("isLoginuUser", isLoginuUser);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_question_reply);
        ButterKnife.bind(this, getContentView());
        Bundle bundle = getArguments();
        if (bundle != null) {
            anwserMoreEntity = bundle.getParcelable("anwserMoreEntity");
            questionId = bundle.getString("questionId");
            isLoginuUser = bundle.getBoolean("isLoginuUser");
        }
    }

    public void onEventMainThread(AppEventType event) {
        switch (event.type) {
            case AppEventType.NOTE_CAMERA_REQUEST:
                loadImage((String) event.values[0]);
                break;
        }
    }

    @Override
    public void initView() {
        super.initView();
        mLoadDialog = getLoadingDialog().build();

        rv_comment.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new QuestionReplyCommentAdapter(getActivity(),dataDetailList);
//        mAdapter.setLoginuUser(isLoginuUser);
        rv_comment.setAdapter(mAdapter);

        load_comment.setErrorRefBtnTxt2(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initComment();
            }
        });
        load_comment.setViewState(MultiStateView.VIEW_STATE_LOADING);
        //是登录用户的则显示评论框
        if(!TextUtils.isEmpty(anwserMoreEntity.getUserId()) && anwserMoreEntity.getUserId().equals(AppContext.getInstance().getUserId())){
            commentLL.setVisibility(View.VISIBLE);
        }

        if(isLoginuUser)
            submitBtn.setText("追问");
        initComment();
    }

    /**
     * 加载拍照后图片存储路径
     *
     */
    public void loadImage(String filePath) {
        mLoadDialog.show();
        //将本地图片转成bitmap上传
        //File file = new File(filePath);

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap= BitmapFactory.decodeStream(fis);
        MultipartBody.Part body = CommonUtil.getMultipartBodyPart(getActivity(), bitmap, filePath);
        mPresenter.sendSubmitPic(body, new ApiRequestListener<String>(){
            @Override
            public void onResultSuccess(String resStr) {//数据返回成功
                //mLoadDialog.dismiss();
                Log.i(QuestionAnwserActivity.class.getSimpleName(),"上传成功");
                sendContent(resStr);
            }

            @Override
            public void onResultFail(){
                mLoadDialog.dismiss();
            }
        });//提交图片，获取地址，在上传问题
    }

    private void initComment() {
        mPresenter.getQuestionDetail(questionId,new ApiRequestListener<String>(){
            @Override
            public void onResultSuccess(String resStr) {//数据返回成功
                QuestionEntity qe = new Gson().fromJson(resStr, QuestionEntity.class);
                dataList.clear();
                dataDetailList.clear();
                if(qe!=null){
                    if (qe.getList().size() == 0) {
                        load_comment.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                    } else {

                        for (QuestionEntity.AnwserMoreEntity e : qe.getList()){
                            if(e !=null && e.getId() == anwserMoreEntity.getId()){
                                anwserMoreEntity = e;
                                break;
                            }
                        }

                        dataDetailList.addAll(anwserMoreEntity.getList());
                        mAdapter.setAnwserMoreEntity(anwserMoreEntity);
                        mAdapter.setQuestionEntity(qe);
                        mAdapter.notifyDataSetChanged();
                        load_comment.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                    }

                }
            }

            @Override
            public void onResultFail() {
                load_comment.setViewState(MultiStateView.VIEW_STATE_ERROR);
            }
        });
    }

    @OnClick({R.id.iv_photo_view,R.id.btn_submit,R.id.tv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.iv_photo_view:
                CameraActivity.openActivity(getActivity(), AppEventType.NOTE_CAMERA_REQUEST, CameraActivity.FROM_NOTE);
                break;
            case R.id.btn_submit:
                String content = edt_note_content.getText().toString();
                if(TextUtils.isEmpty(content)){
                    Toast.makeText(getActivity(),"回复不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                edt_note_content.setText("");
                mLoadDialog.show();
                sendContent(content);
                break;
        }
    }

    private void sendContent(String content){

        //追问
        if(isLoginuUser){
            mPresenter.sendQaAgainAsk(questionId,anwserMoreEntity.getId(),content, new ApiRequestListener<String>(){
                @Override
                public void onResultSuccess(String resStr) {
                    mLoadDialog.dismiss();
                    initComment();
                    Log.i(QuestionAnwserActivity.class.getSimpleName(),"追问成功");
                }

                @Override
                public void onResultFail(){
                    mLoadDialog.dismiss();
                }
            });
        }else{
            mPresenter.sendQaAgainAnswer(questionId,anwserMoreEntity.getId(),content, new ApiRequestListener<String>(){
                @Override
                public void onResultSuccess(String resStr) {
                    mLoadDialog.dismiss();
                    initComment();
                    Log.i(QuestionAnwserActivity.class.getSimpleName(),"追答成功");
                }

                @Override
                public void onResultFail(){
                    mLoadDialog.dismiss();
                }
            });
        }

    }
}