package com.asking.pad.app.ui.sharespace;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import de.greenrobot.event.EventBus;
import okhttp3.MultipartBody;

/**
 * Created by jswang on 2017/7/19.
 */

public class QuestionCommentFragment extends BaseEvenFrameFragment<UserPresenter, UserModel> {
    @BindView(R.id.load_comment)
    MultiStateView load_comment;

    @BindView(R.id.rv_comment)
    RecyclerView recycler;

    @BindView(R.id.edt_note_content)
    EditText edt_note_content;

    @BindView(R.id.answer_size)
    TextView answerSizeTv;

    @BindView(R.id.ll_input_comment)
    View ll_input_comment;

    ArrayList<QuestionEntity.AnwserMoreEntity> dataList = new ArrayList<>();
    QuestionCommentAdapter mAdapter;

    private MaterialDialog mLoadDialog;

    /**
     * 2-已采纳
     */
    int dataType;

    String questionId;
    String askUserId;
    String quesAvatar;

    public static QuestionCommentFragment newInstance(int dataType, String questionId, String askUserId, String quesAvatar) {
        QuestionCommentFragment fragment = new QuestionCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("questionId", questionId);
        bundle.putString("askUserId", askUserId);
        bundle.putInt("dataType", dataType);
        bundle.putString("quesAvatar", quesAvatar);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_question_commnet);
        ButterKnife.bind(this, getContentView());
        Bundle bundle = getArguments();
        if (bundle != null) {
            questionId = bundle.getString("questionId");
            askUserId = bundle.getString("askUserId");
            dataType = bundle.getInt("dataType");
            quesAvatar = bundle.getString("quesAvatar");
        }
    }

    @Override
    public void initView() {
        super.initView();

        mLoadDialog = getLoadingDialog().build();

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new QuestionCommentAdapter(getActivity(), dataList, new QuestionCommentAdapter.OnItemCommentListener() {
            @Override
            public void OnItemComment(QuestionEntity.AnwserMoreEntity e) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment, QuestionReplyFragment.newInstance(dataType, e, questionId,quesAvatar
                                , mAdapter.isLoginUser,mAdapter.isShowAdopt))
                        .commit();
            }
        });
        mAdapter.setAdoptCallBack(new AdoptCallBack() {
            @Override
            public void adopt(String askId) {
                mPresenter.qaAdoptAnswer(questionId, askId, new ApiRequestListener<String>() {
                    @Override
                    public void onResultSuccess(String resStr) {//数据返回成功
                        showShortToast("采纳成功");
                        mAdapter.setVisBtn();
                        initComment();
                        EventBus.getDefault().post(new AppEventType(AppEventType.RE_USER_INFO_REQUEST));
                    }

                    @Override
                    public void onResultFail() {
                        showShortToast("采纳失败");
                    }
                });
            }
        });

        //是否登录用户
        mAdapter.setLoginUser(TextUtils.equals(AppContext.getInstance().getUserId(),askUserId));
        recycler.setAdapter(mAdapter);

        load_comment.setErrorRefBtnTxt2(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initComment();
            }
        });
        load_comment.setEmptyStyle(R.mipmap.ic_no_data1,"还没有人回答呀","","");
        load_comment.setViewState(MultiStateView.VIEW_STATE_LOADING);

        if (dataType == 2 || TextUtils.equals(askUserId, AppContext.getInstance().getUserId())) {
            ll_input_comment.setVisibility(View.GONE);
        }

        initComment();
    }

    public void onEventMainThread(AppEventType event) {
        switch (event.type) {
            case AppEventType.NOTE_CAMERA_REQUEST:
                loadImage((String) event.values[0]);
                break;
        }
    }

    private void initComment() {
        mPresenter.getQuestionDetail(questionId, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {//数据返回成功
                QuestionEntity qe = new Gson().fromJson(resStr, QuestionEntity.class);
                dataList.clear();
                if (qe != null) {
                    if (qe.getList() == null || qe.getList().size() == 0) {
                        load_comment.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                    } else {
                        dataList.addAll(qe.getList());
                        load_comment.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                        answerSizeTv.setText(dataList.size()+"");
                    }
                }

                for(QuestionEntity.AnwserMoreEntity e: dataList){
                    if(e.isAdopt()){
                        mAdapter.isShowAdopt = false;
                        ll_input_comment.setVisibility(View.GONE);
                        break;
                    }
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onResultFail() {
                load_comment.setViewState(MultiStateView.VIEW_STATE_ERROR);
            }
        });
    }


    /**
     * 加载拍照后图片存储路径
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


    @OnClick({R.id.iv_photo_view, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_photo_view:
                CameraActivity.openActivity(getActivity(), AppEventType.NOTE_CAMERA_REQUEST, CameraActivity.FROM_NOTE);
                break;
            case R.id.btn_submit:
                String content = edt_note_content.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(getActivity(), "回复不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                mLoadDialog.show();
                edt_note_content.setText("");
                sendContent(content);
                break;
        }
    }

    private void sendContent(String content) {
        mPresenter.sendQAAnswer(questionId, content, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {
                mLoadDialog.dismiss();
                initComment();
                Log.i(QuestionAnwserActivity.class.getSimpleName(), "回答成功");
            }

            @Override
            public void onResultFail() {
                mLoadDialog.dismiss();
            }
        });
    }

    public interface AdoptCallBack {
        void adopt(String askId);
    }
}
