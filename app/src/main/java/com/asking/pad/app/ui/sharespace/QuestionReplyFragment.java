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
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenFrameFragment;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.entity.QuestionEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.camera.ui.CameraActivity;
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

public class QuestionReplyFragment extends BaseEvenFrameFragment<UserPresenter, UserModel> {
    @BindView(R.id.load_comment)
    MultiStateView load_comment;

    @BindView(R.id.rv_comment)
    RecyclerView rv_comment;

    @BindView(R.id.edt_note_content)
    EditText edt_note_content;

    @BindView(R.id.btn_submit)
    Button submitBtn;

    @BindView(R.id.ll_input_comment)
    View ll_input_comment;

    ArrayList<QuestionEntity.AnswerDetail> dataDetailList = new ArrayList<>();
    ArrayList<QuestionEntity.AnwserMoreEntity> dataList = new ArrayList<>();

    QuestionReplyCommentAdapter mAdapter;

    private MaterialDialog mLoadDialog;
    boolean isLoginuUser;
    QuestionEntity.AnwserMoreEntity anwserMoreEntity;
    String questionId;

    /**
     * 2-已采纳
     */
    int dataType;

    public static QuestionReplyFragment newInstance(int dataType, QuestionEntity.AnwserMoreEntity e
            , String questionId, boolean isLoginuUser) {
        QuestionReplyFragment fragment = new QuestionReplyFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("anwserMoreEntity", e);
        bundle.putString("questionId", questionId);
        bundle.putBoolean("isLoginuUser", isLoginuUser);
        bundle.putInt("dataType", dataType);
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
            dataType = bundle.getInt("dataType");
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
        mAdapter = new QuestionReplyCommentAdapter(getActivity(), dataDetailList);
        rv_comment.setAdapter(mAdapter);

        load_comment.setErrorRefBtnTxt2(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initComment();
            }
        });
        load_comment.setViewState(MultiStateView.VIEW_STATE_LOADING);

        if (isLoginuUser)
            submitBtn.setText("追问");

        if (dataType == 2 || TextUtils.equals(anwserMoreEntity.getUserId(), AppContext.getInstance().getUserId())
                || anwserMoreEntity.isAdopt()) {
            ll_input_comment.setVisibility(View.GONE);
        }

        initComment();
    }

    /**
     * 加载拍照后图片存储路径
     */
    public void loadImage(String filePath) {
        mLoadDialog.show();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(fis);
        MultipartBody.Part body = CommonUtil.getMultipartBodyPart(getActivity(), bitmap, filePath);
        mPresenter.sendSubmitPic(body, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {//数据返回成功
                //mLoadDialog.dismiss();
                Log.i(QuestionAnwserActivity.class.getSimpleName(), "上传成功");
                sendContent(resStr);
            }

            @Override
            public void onResultFail() {
                mLoadDialog.dismiss();
            }
        });//提交图片，获取地址，在上传问题
    }

    private void initComment() {
        mPresenter.getQuestionDetail(questionId, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {//数据返回成功
                QuestionEntity qe = new Gson().fromJson(resStr, QuestionEntity.class);
                dataList.clear();
                dataDetailList.clear();
                QuestionEntity.AnswerDetail f = new QuestionEntity.AnswerDetail();
                f.setAnswerTime(anwserMoreEntity.getCreateDate());
                f.setAnswer(anwserMoreEntity.getContent());
                dataDetailList.add(f);
                if (qe != null) {
                    if (qe.getList().size() == 0) {
                        load_comment.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                    } else {
                        for (QuestionEntity.AnwserMoreEntity e : qe.getList()) {
                            if (e != null && e.getId() == anwserMoreEntity.getId()) {
                                anwserMoreEntity = e;
                                break;
                            }
                        }
                        if (anwserMoreEntity.getList() != null && anwserMoreEntity.getList().size() > 0) {
                            dataDetailList.addAll(anwserMoreEntity.getList());
                        }
                    }
                }
                mAdapter.setAnwserMoreEntity(anwserMoreEntity);
                mAdapter.setQuestionEntity(qe);
                mAdapter.notifyDataSetChanged();
                load_comment.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            }

            @Override
            public void onResultFail() {
                load_comment.setViewState(MultiStateView.VIEW_STATE_ERROR);
            }
        });
    }

    @OnClick({R.id.iv_photo_view, R.id.btn_submit, R.id.tv_back})
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
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(getActivity(), "回复不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                edt_note_content.setText("");
                mLoadDialog.show();
                sendContent(content);
                break;
        }
    }

    private void sendContent(String content) {

        //追问
        if (isLoginuUser) {
            mPresenter.sendQaAgainAsk(questionId, anwserMoreEntity.getId(), content, new ApiRequestListener<String>() {
                @Override
                public void onResultSuccess(String resStr) {
                    mLoadDialog.dismiss();
                    initComment();
                    Log.i(QuestionAnwserActivity.class.getSimpleName(), "追问成功");
                }

                @Override
                public void onResultFail() {
                    mLoadDialog.dismiss();
                }
            });
        } else {
            mPresenter.sendQaAgainAnswer(questionId, anwserMoreEntity.getId(), content, new ApiRequestListener<String>() {
                @Override
                public void onResultSuccess(String resStr) {
                    mLoadDialog.dismiss();
                    initComment();
                    Log.i(QuestionAnwserActivity.class.getSimpleName(), "追答成功");
                }

                @Override
                public void onResultFail() {
                    mLoadDialog.dismiss();
                }
            });
        }

    }
}
