package com.asking.pad.app.ui.superclass.classify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.ParamHelper;
import com.asking.pad.app.entity.ExamReviewTree;
import com.asking.pad.app.entity.LabelEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.downbook.DownBookActivity;
import com.asking.pad.app.ui.superclass.classify.adapter.CommAdapter;
import com.asking.pad.app.ui.superclass.examreview.ExamReviewFirstActivity;
import com.asking.pad.app.ui.superclass.examreview.ExamReviewSecondActivity;
import com.asking.pad.app.widget.MultiStateView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by jswang on 2017/4/14.
 */

public class ClassifyExamReviewFragment extends BaseFrameFragment<UserPresenter, UserModel> implements CommAdapter.OnCommItemListener {

    @BindView(R.id.rv_review)
    RecyclerView rv_review;

    @BindView(R.id.rv_exam)
    RecyclerView rv_exam;

    @BindView(R.id.load_view)
    MultiStateView load_view;

    CommAdapter reviewAdapter;
    CommExamAdapter classAdapter;

    boolean isSelectNode;
    boolean isBuy;
    String classType;

    /**
     * M2-初中数学（8）  P2-初中物理（6）  M3-高中数学（9）  P3-高中物理（7）
     */
    private String actionType;
    /**
     * @param type 0-一轮复习 1-二轮复习
     */
    String mExamReviewType = "0";

    ArrayList<LabelEntity> reviewList = new ArrayList<>();
    ArrayList<ExamReviewTree> classList = new ArrayList<>();

    public static ClassifyExamReviewFragment newInstance(Bundle bundle) {
        ClassifyExamReviewFragment fragment = new ClassifyExamReviewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mExamReviewType = bundle.getString("mExamReviewType", "0");
            isBuy = bundle.getBoolean("isBuy");
            classType = bundle.getString("classType");
            isSelectNode = bundle.getBoolean("isSelectNode");
            actionType = Constants.getActionType(classType);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setContentView(R.layout.fragment_classify_exam_review);
        ButterKnife.bind(this, getContentView());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void initView() {
        super.initView();
        reviewList.clear();
        reviewList.add(new LabelEntity(3, "0", getString(R.string.exam_review_first), true));
        reviewList.add(new LabelEntity(3, "1", getString(R.string.exam_review_second), false));

        reviewAdapter = new CommAdapter(getActivity(), LinearLayoutManager.HORIZONTAL, reviewList, this);
        rv_review.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_review.setAdapter(reviewAdapter);
        reviewAdapter.notifyDataSetChanged();

        classAdapter = new CommExamAdapter(getActivity(), mPresenter, classList, rv_exam, new CommExamAdapter.OnCommItemListener() {
            @Override
            public void OnClickItem(ExamReviewTree e) {
                OnExamReviewTreeItem(e);
            }
        });
        rv_exam.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_exam.setAdapter(classAdapter);

        if (isSelectNode) {
            rv_review.setVisibility(View.GONE);
        }

        classExam(mExamReviewType);

        load_view.setErrorRefBtnTxt(getString(R.string.down_book_btn), getString(R.string.down_book_content),
                R.mipmap.ic_down_book, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommonUtil.openActivity(DownBookActivity.class);
                    }
                });
    }

    @Override
    public void OnCommItem(LabelEntity e) {
        switch (e.getType()) {  //0-版本  1-精学  2-年级 3-一二复习
            case 3:
                classExam(e.getId());
                break;
        }
    }

    public void classExam(String type) {
        mExamReviewType = type;

        Constants.setBookdir(classType, String.format("-%s", actionType), "");

        load_view.setViewState(load_view.VIEW_STATE_LOADING);
        if (TextUtils.equals(mExamReviewType, "0")) {
            mPresenter.firstreviewzhangjd(actionType, new ApiRequestListener<String>() {
                @Override
                public void onResultSuccess(String res) {
                    if (!TextUtils.isEmpty(res)) {
                        load_view.setViewState(load_view.VIEW_STATE_CONTENT);
                        classList.clear();
                        classList.addAll(JSON.parseArray(res, ExamReviewTree.class));
                        if (TextUtils.equals("6", actionType)) {
                            classAdapter.isHavChild = false;
                            classAdapter.notifyDataSetChanged();
                        } else {
                            if (classList != null && classList.size() > 0) {
                                final ExamReviewTree p = classList.get(0);
                                mPresenter.firstreviewkesjd(p._id, new ApiRequestListener<String>() {
                                    @Override
                                    public void onResultSuccess(String res) {
                                        List<ExamReviewTree> list = JSON.parseArray(res, ExamReviewTree.class);
                                        p.list.addAll(list);
                                        classAdapter.isHavChild = true;
                                        classAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                    } else {
                        load_view.setViewState(load_view.VIEW_STATE_ERROR);
                    }
                }

                @Override
                public void onResultFail() {
                    load_view.setViewState(load_view.VIEW_STATE_ERROR);
                }
            });
        } else {
            mPresenter.secondreviewtree(actionType, new ApiRequestListener<String>() {

                @Override
                public void onResultSuccess(String res) {
                    if (!TextUtils.isEmpty(res)) {
                        load_view.setViewState(load_view.VIEW_STATE_CONTENT);

                        classList.clear();
                        classList.addAll(JSON.parseArray(res, ExamReviewTree.class));
                        classAdapter.isHavChild = false;
                        classAdapter.notifyDataSetChanged();
                    } else {
                        load_view.setViewState(load_view.VIEW_STATE_ERROR);
                    }
                }

                @Override
                public void onResultFail() {
                    load_view.setViewState(load_view.VIEW_STATE_ERROR);
                }
            });
        }
    }

    private void OnExamReviewTreeItem(ExamReviewTree e) {
        int knowledgeIndex = 0;
        for (int i = 0; i < classList.size(); i++) {
            if (TextUtils.equals(e.id, classList.get(i).id)) {
                knowledgeIndex = i;
            }
        }
        if (isSelectNode) {
            String knowledgeId;
            if (TextUtils.equals(mExamReviewType, "0")) {
                knowledgeId = e._id;
            } else {
                knowledgeId = e.id;
                HashMap<String, Object> mParams = ParamHelper.acquireParamsReceiver(ClassifyActivty.class.getName());
                mParams.put("examList", classList);
            }
            EventBus.getDefault().post(new AppEventType(AppEventType.CLASSIFY_REQUEST, isBuy, classType
                    , knowledgeId, e.getText(), knowledgeIndex, e.name));
        } else {
            Bundle parameter = new Bundle();
            parameter.putBoolean("isBuy", isBuy);
            parameter.putString("classType", classType);

            if (TextUtils.equals(mExamReviewType, "0")) {
                parameter.putString("knowledgeId", e._id);
                parameter.putString("knowledgeName", e.getText());

                openActivity(ExamReviewFirstActivity.class, parameter);
            } else {
                parameter.putInt("knowledgeIndex", knowledgeIndex);
                parameter.putString("knowledgeId", e.id);
                parameter.putString("knowledgeName", e.name);
                HashMap<String, Object> mParams = ParamHelper.acquireParamsReceiver(ClassifyActivty.class.getName());
                mParams.put("examList", classList);

                openActivity(ExamReviewSecondActivity.class, parameter);
            }
        }
        getActivity().finish();
    }

}
