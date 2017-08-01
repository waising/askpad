package com.asking.pad.app.ui.sharespace.special;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.DateUtil;
import com.asking.pad.app.entity.sharespace.SpecialComment;
import com.asking.pad.app.mvp.OnCountDownListener;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.camera.ui.CameraActivity;
import com.asking.pad.app.widget.MultiStateView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jswang on 2017/7/19.
 */

public class SpeciaReplyFragment extends BaseFrameFragment<UserPresenter, UserModel> {
    @BindView(R.id.load_comment)
    MultiStateView load_comment;

    @BindView(R.id.rv_comment)
    RecyclerView rv_comment;

    @BindView(R.id.edt_note_content)
    EditText edt_note_content;

    @BindView(R.id.ll_input_comment)
    View ll_input_comment;

    private MaterialDialog mLoadDialog;

    ArrayList<SpecialComment> commentList = new ArrayList<>();
    CommentAdapter mAdapter;

    String communionTopicId;
    String userId;
    int state;

    public static SpeciaReplyFragment newInstance(int state, String communionTopicId, String userId) {
        SpeciaReplyFragment fragment = new SpeciaReplyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("communionTopicId", communionTopicId);
        bundle.putString("userId", userId);
        bundle.putInt("state", state);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_special_reply);
        ButterKnife.bind(this, getContentView());
        Bundle bundle = getArguments();
        if (bundle != null) {
            communionTopicId = bundle.getString("communionTopicId");
            userId = bundle.getString("userId");
            state = bundle.getInt("state");
        }
    }

    @Override
    public void initView() {
        super.initView();

        mLoadDialog = getLoadingDialog().build();

        rv_comment.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CommentAdapter(getActivity(), commentList, null);
        rv_comment.setAdapter(mAdapter);

        load_comment.setErrorRefBtnTxt2(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initComment();
            }
        });
        load_comment.setViewState(MultiStateView.VIEW_STATE_LOADING);
        initComment();

        if (state == 2 || !TextUtils.equals(userId, AppContext.getInstance().getUserId())) {
            ll_input_comment.setVisibility(View.GONE);
        } else {
            ll_input_comment.setVisibility(View.VISIBLE);
        }
    }

    private void initComment() {
        mPresenter.replytopicmsginit("", communionTopicId, userId, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                commentList.clear();
                commentList.addAll(JSON.parseArray(res, SpecialComment.class));
                if (commentList.size() == 0) {
                    load_comment.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                } else {
                    mAdapter.notifyDataSetChanged();
                    load_comment.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                }

                //倒计时10秒显示等待提示
                mPresenter.countDownPresenter(10, new OnCountDownListener() {
                    @Override
                    public void onComplete() {
                        initComment();
                    }
                });
            }

            @Override
            public void onResultFail() {
                load_comment.setViewState(MultiStateView.VIEW_STATE_ERROR);
            }
        });
    }

    public void onEventMainThread(AppEventType event) {
        switch (event.type) {
            case AppEventType.NOTE_CAMERA_REQUEST:
                loadImage((String) event.values[0]);
                break;
        }
    }

    private void submit(String content, String imgUrl) {
        mPresenter.topicmsg(communionTopicId, content, imgUrl, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                mLoadDialog.dismiss();
                initComment();

                edt_note_content.setText("");
            }

            @Override
            public void onResultFail() {
                mLoadDialog.dismiss();
            }
        });
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
                submit("", qiNiuUrl);
            }

            @Override
            public void onResultFail() {
                mLoadDialog.dismiss();
            }
        });
    }

    @OnClick({R.id.iv_photo_view, R.id.tv_back,R.id.btn_submit})
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
                    Toast.makeText(getActivity(), "评价不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                mLoadDialog.show();
                submit(content, "");
                break;
        }
    }
}
