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
import com.asking.pad.app.base.BaseEvenFrameFragment;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.ParamHelper;
import com.asking.pad.app.entity.superclass.StudyClassGrade;
import com.asking.pad.app.entity.superclass.SuperLessonTree;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.downbook.DownBookActivity;
import com.asking.pad.app.ui.pay.PayAskActivity;
import com.asking.pad.app.ui.superclass.SuperClassActiity;
import com.asking.pad.app.ui.superclass.classify.adapter.GradeAdapter;
import com.asking.pad.app.widget.MultiStateView;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by jswang on 2017/4/14.
 */

public class ClassifySuperFragment extends BaseEvenFrameFragment<UserPresenter, UserModel> implements GradeAdapter.OnCommItemListener {

    @BindView(R.id.rv_grade)
    RecyclerView rv_grade;

    @BindView(R.id.knowledge_tree)
    ViewGroup knowledge_tree;

    @BindView(R.id.load_view)
    MultiStateView load_view;

    @BindView(R.id.know_load_view)
    MultiStateView know_load_view;

    AndroidTreeView tView;

    GradeAdapter gradeAdapter;


    boolean isBuy;
    String commodityId;
    String courseId;
    String classType;
    String className;
    boolean isSelectNode;

    ArrayList<StudyClassGrade> gradeList = new ArrayList<>();

    /**
     * 递归遍历树数据
     *
     * @param node
     * @param list
     */
    List<SuperLessonTree> treeLessonList = new ArrayList<>();
    StudyClassGrade mGrade;

    public static ClassifySuperFragment newInstance(Bundle bundle) {
        ClassifySuperFragment fragment = new ClassifySuperFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            classType = bundle.getString("classType");
            className = bundle.getString("className");
            isSelectNode = bundle.getBoolean("isSelectNode");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setContentView(R.layout.fragment_classify_supper);
        ButterKnife.bind(this, getContentView());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        super.initView();
        gradeAdapter = new GradeAdapter(getActivity(), gradeList, this);
        rv_grade.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_grade.setAdapter(gradeAdapter);

        know_load_view.setErrorRefBtnTxt("点击下载相关课程", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGrade != null) {
                    DownBookActivity.openActivity(mGrade.courseTypeId);
                }
            }
        });
    }

    public void onEventMainThread(AppEventType event) {
        try {
            if (event.type == AppEventType.BOOK_DWON_FINISH_REQUEST || event.type == AppEventType.BOOK_OPEN_REQUEST) {
                for (int i = 0; i < gradeList.size(); i++) {
                    StudyClassGrade e = gradeList.get(i);
                    e.isSelect = false;
                    if (TextUtils.equals(e.commodityId, (String) event.values[0])) {
                        e.isSelect = true;
                        this.commodityId = e.commodityId;
                        this.isBuy = e.getIsBuy();
                    }
                }
                gradeAdapter.notifyDataSetChanged();
                classSection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnCommItem(StudyClassGrade e) {
        e.isSelect = true;
        this.mGrade = e;
        this.commodityId = e.commodityId;
        this.courseId = e.courseId;
        this.isBuy = e.getIsBuy();
        classSection();
    }

    public void initGradeData(String gradeId,List<StudyClassGrade> list) {
        if (list.size() > 0) {
            gradeList.clear();
            gradeList.addAll(list);
            for (int i = 0; i < gradeList.size(); i++) {
                StudyClassGrade e = gradeList.get(i);
                e.isSelect = false;
                if(TextUtils.isEmpty(gradeId)){
                    if (i == 0){
                        OnCommItem(e);
                    }
                }else if(TextUtils.equals(gradeId,e.courseId)){
                    OnCommItem(e);
                }
            }
            gradeAdapter.notifyDataSetChanged();
        } else {
            load_view.setViewState(load_view.VIEW_STATE_EMPTY);
        }
    }

    private void classSection() {
        load_view.setViewState(load_view.VIEW_STATE_CONTENT);
        know_load_view.setViewState(load_view.VIEW_STATE_LOADING);
        mPresenter.synclesson(isBuy, commodityId, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                List<SuperLessonTree> list = JSON.parseArray(res, SuperLessonTree.class);
                initSectionData(list);
            }

            @Override
            public void onResultFail() {
                know_load_view.setViewState(load_view.VIEW_STATE_ERROR);
            }
        });
    }

    private void initSectionData(List<SuperLessonTree> list) {
        know_load_view.setViewState(load_view.VIEW_STATE_CONTENT);

        treeLessonList.clear();

        TreeNode root = TreeNode.root();
        tView = new AndroidTreeView(getActivity(), root);
        setTreeData(root, list);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom2, true);

        tView.setDefaultNodeClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                onNodeClickListener(node, (SuperLessonTree) value);
            }
        });

        tView.setDefaultViewHolder(LessonTreeItemHolder.class);
        knowledge_tree.removeAllViews();
        knowledge_tree.addView(tView.getView());
        //禁止出现下来阴影
        tView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        tView.setUseAutoToggle(true);
        tView.expandAll();
    }

    private void onNodeClickListener(TreeNode node, SuperLessonTree value) {
        if (!node.isLeaf()) {
            return;
        }

        if (isSelectNode) {
            EventBus.getDefault().post(new AppEventType(AppEventType.CLASSIFY_REQUEST
                    , isBuy, commodityId, value.id, value.name, value.knowledgeIndex, value.free, treeLessonList));
            getActivity().finish();
        } else {
            if (isBuy) {
                openSuperClassActiity(value);
            } else {
                if (value.free != 0 || value.purchased != 0) {
                    openSuperClassActiity(value);
                } else {
                    CommonUtil.openAuthActivity(PayAskActivity.class);
                }
            }
        }
    }

    private void openSuperClassActiity(SuperLessonTree value) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isBuy", isBuy);
        bundle.putString("gradeId", commodityId);
        bundle.putString("knowledgeId", value.id);
        bundle.putString("knowledgeName", value.name);
        bundle.putInt("knowledgeIndex", value.knowledgeIndex);
        bundle.putInt("free", value.free);

        bundle.putString("classType", classType);
        bundle.putString("className", className);

        HashMap<String, Object> mParams = ParamHelper.acquireParamsReceiver(ClassifyActivty.class.getName());
        mParams.put("treeLessonList", treeLessonList);
        CommonUtil.openActivity(SuperClassActiity.class, bundle);
    }

    private void setTreeData(TreeNode node, List<SuperLessonTree> list) {
        for (int i = 0; i < list.size(); i++) {
            SuperLessonTree e = list.get(i);

            if (e.getIsLeaf()) {
                e.knowledgeIndex = i;
                treeLessonList.add(e);
            }

            TreeNode tempNode = new TreeNode(e).setViewHolder(new LessonTreeItemHolder(getActivity()));
            node.addChild(tempNode);

            if (e.children != null)
                setTreeData(tempNode, e.children);
        }
    }

}
