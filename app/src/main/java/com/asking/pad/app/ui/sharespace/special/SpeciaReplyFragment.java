package com.asking.pad.app.ui.sharespace.special;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.entity.sharespace.SpecialComment;
import com.asking.pad.app.mvp.OnCountDownListener;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
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

    ArrayList<SpecialComment> commentList = new ArrayList<>();
    CommentAdapter mAdapter;

    String communionTopicId;
    String userId;

    public static SpeciaReplyFragment newInstance(String communionTopicId,String userId) {
        SpeciaReplyFragment fragment = new SpeciaReplyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("communionTopicId", communionTopicId);
        bundle.putString("userId", userId);
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
        }
    }

    @Override
    public void initView() {
        super.initView();

        rv_comment.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CommentAdapter(getActivity(),commentList,null);
        rv_comment.setAdapter(mAdapter);

        load_comment.setErrorRefBtnTxt2(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initComment();
            }
        });
        load_comment.setViewState(MultiStateView.VIEW_STATE_LOADING);
        initComment();
    }

    private void initComment(){
        mPresenter.replytopicmsginit("",communionTopicId,userId,new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                commentList.clear();
                commentList.addAll(JSON.parseArray(res,SpecialComment.class));
                if(commentList.size() == 0){
                    load_comment.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                }else{
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

    @OnClick({R.id.tv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }
}
