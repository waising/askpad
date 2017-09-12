package com.asking.pad.app.ui.sharespace;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenFrameFragment;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.entity.QuestionEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.widget.AskSwipeRefreshLayout;
import com.asking.pad.app.widget.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 问答广场
 */

public class MyQuestionsFragment extends BaseEvenFrameFragment<UserPresenter, UserModel> {


    @BindView(R.id.swipe_layout)
    AskSwipeRefreshLayout swipeLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.load_view)
    MultiStateView multiStateView;

    private int start = 0, limit = 6;
    String type = "";

    private QuestionsAdapter mQustionsAdapter;
    private List<QuestionEntity> questionList = new ArrayList<>();

    public static MyQuestionsFragment newInstance(String type) {
        MyQuestionsFragment fragment = new MyQuestionsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_questions_detail);
        ButterKnife.bind(this, getContentView());

        type = getArguments().getString("type");
    }

    @Override
    public void initView() {
        super.initView();
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。

        swipeLayout.setViewPager(recyclerView);
        swipeLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout ptrFrameLayout) {
                start += limit;
                getDataNow();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                swipeLayout.setMode(PtrFrameLayout.Mode.BOTH);
                start = 0;
                questionList.clear();
                getDataNow();
            }
        });

        multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        multiStateView.setErrorRefBtnTxt2(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataNow();
            }
        });
        getDataNow();
    }

    public void onEventMainThread(AppEventType event) {
        switch (event.type) {
            case AppEventType.QUESTION_ASK:
                getDataNow();
                break;
        }
    }

    public void refshAdapt(List<QuestionEntity> entityComment) {
        if (mQustionsAdapter == null) {
            mQustionsAdapter = new QuestionsAdapter(getContext(), entityComment);
            recyclerView.setAdapter(mQustionsAdapter);
            mQustionsAdapter.setMine(type!="13");
        } else {
            if (start > 0) {
                // 加载更多
                mQustionsAdapter.setData(questionList);
            } else {
                // 顶部tab切换
                mQustionsAdapter.setData(entityComment);
            }
        }
    }

    private void getDataNow() {
        //我的提问
        if(TextUtils.isEmpty(type)){
            mPresenter.getMyQuestionAskList(start, limit, apiRequestListener);
        }else if(type == "13"){//我的回答
            mPresenter.getMyQuestionAnswerList(start, limit,apiRequestListener);
        }

    }

    ApiRequestListener<String> apiRequestListener = new ApiRequestListener<String>(){
        @Override
        public void onResultSuccess(String resStr) {//数据返回成功
            swipeLayout.refreshComplete();
            if (!TextUtils.isEmpty(resStr)) {
                JSONObject resobj = JSON.parseObject(resStr);
                String resList = resobj.getString("list");
                List<QuestionEntity> listPaper = JSON.parseArray(resList, QuestionEntity.class);
                if (listPaper != null && listPaper.size() > 0) {
                    questionList.addAll(listPaper);
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                } else {

                    if (start > 0) {
                        swipeLayout.setMode(PtrFrameLayout.Mode.REFRESH);
                        showShortToast(getResources().getString(R.string.no_more_data));
                    } else {
                        multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                    }
                }
                refshAdapt(questionList);
            }

        }

        @Override
        public void onResultFail() {
            super.onResultFail();

            if (swipeLayout != null) {
                swipeLayout.refreshComplete();
            }
            swipeLayout.setMode(PtrFrameLayout.Mode.REFRESH);

            mQustionsAdapter = null;
            recyclerView.setAdapter(null);

            multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    };
}
