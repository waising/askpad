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
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.ParamHelper;
import com.asking.pad.app.commom.TreeItemHolder;
import com.asking.pad.app.entity.KnowledgeDetailEntity;
import com.asking.pad.app.entity.KnowledgeEntity;
import com.asking.pad.app.entity.superclass.StudyClassGrade;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.downbook.DownBookActivity;
import com.asking.pad.app.ui.superclass.SuperClassActiity;
import com.asking.pad.app.ui.superclass.classify.adapter.GradeAdapter;
import com.asking.pad.app.widget.MultiStateView;
import com.google.gson.reflect.TypeToken;
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

    boolean isSelectNode;
    boolean isBuy;
    String gradeId;
    String levelId;
    String versionId;
    String classType;
    ArrayList<StudyClassGrade> gradeList = new ArrayList<>();

    /**
     * 递归遍历树数据
     *
     * @param node
     * @param list
     */
    List<TreeNode> treeNodeList = new ArrayList<>();

    StudyClassGrade mGrade;

    public static ClassifySuperFragment newInstance(Bundle bundle ) {
        ClassifySuperFragment fragment = new ClassifySuperFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            isBuy = bundle.getBoolean("isBuy");
            classType = bundle.getString("classType");
            versionId = bundle.getString("versionId");
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

        know_load_view.setErrorRefBtnTxt("点击下载相关课程",new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.openActivity(DownBookActivity.class);
            }
        });
    }

    public void onEventMainThread(AppEventType event) {
        try {
            if(event.type== AppEventType.BOOK_DWON_FINISH_REQUEST){
                if(mGrade!=null && TextUtils.equals(mGrade.getLevelId(),(String)event.values[1])){
                    OnCommItem(mGrade);
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void OnCommItem(StudyClassGrade e) {
        this.mGrade = e;
        gradeId = e.getVersionLevelId();
        levelId = e.getLevelId();
        classSection();
    }

    public void classGrade2(String versionId,List<StudyClassGrade> list) {
        load_view.setViewState(load_view.VIEW_STATE_LOADING);
        if(getActivity() != null ){
            this.versionId = versionId;
            initGradeData(list);
        }
    }


    public void classGrade(String versionId,String gradeId, List<StudyClassGrade> list) {
        load_view.setViewState(load_view.VIEW_STATE_LOADING);
        if(getActivity() != null ){
            this.versionId = versionId;

            gradeList.clear();
            gradeList.addAll(list);
            for(StudyClassGrade ii:gradeList){
                ii.isSelect = false;
                if(TextUtils.equals(gradeId,ii.getLevelId())){
                    ii.isSelect = true;
                    levelId = ii.getLevelId();
                    this.gradeId = ii.getVersionLevelId() + "";
                    gradeAdapter.notifyDataSetChanged();
                    classSection();
                    break;
                }
            }
        }
    }

    public void classGrade(String versionId) {
        load_view.setViewState(load_view.VIEW_STATE_LOADING);
        if(getActivity() != null ){
            this.versionId = versionId;
            if (!isBuy) {
                mPresenter.classFreeGrade(versionId, new ApiRequestListener<String>() {
                    @Override
                    public void onResultSuccess(String res) {
                        List<StudyClassGrade> list = JSON.parseArray(res, StudyClassGrade.class);
                        initGradeData(list);
                    }

                    @Override
                    public void onResultFail() {
                        load_view.setViewState(load_view.VIEW_STATE_EMPTY);
                    }
                });
            }
        }
    }

    private void initGradeData(List<StudyClassGrade> list) {
        gradeList.clear();
        gradeList.addAll(list);
        if(gradeList.size()>0){
            StudyClassGrade ii = list.get(0);
            ii.isSelect = true;
            levelId = ii.getLevelId();
            gradeId = ii.getVersionLevelId() + "";
        }
        gradeAdapter.notifyDataSetChanged();
        classSection();
    }

    private void classSection() {
        Constants.setBookdir(classType,versionId,levelId+"");

        load_view.setViewState(load_view.VIEW_STATE_CONTENT);
        know_load_view.setViewState(load_view.VIEW_STATE_LOADING);
        if (isBuy) {
            mPresenter.classSection(gradeId , new ApiRequestListener<String>() {
                @Override
                public void onResultSuccess(String res) {
                    if(!TextUtils.isEmpty(res)){
                        List<KnowledgeEntity> list = CommonUtil.parseDataToList(res, new TypeToken<List<KnowledgeEntity>>() {
                        });
                        initSectionData(list);
                    }else{
                        know_load_view.setViewState(load_view.VIEW_STATE_ERROR);
                    }
                }

                @Override
                public void onResultFail() {
                    know_load_view.setViewState(load_view.VIEW_STATE_ERROR);
                }
            });
        } else {
            mPresenter.classFreeSection(gradeId, new ApiRequestListener<String>() {
                @Override
                public void onResultSuccess(String res) {
                    List<KnowledgeEntity> list = CommonUtil.parseDataToList(res, new TypeToken<List<KnowledgeEntity>>() {
                    });
                    initSectionData(list);
                }

                @Override
                public void onResultFail() {
                    know_load_view.setViewState(load_view.VIEW_STATE_EMPTY);
                }
            });
        }
    }

    private void initSectionData(List<KnowledgeEntity> list) {
        know_load_view.setViewState(load_view.VIEW_STATE_CONTENT);

        treeNodeList.clear();

        TreeNode root = TreeNode.root();
        tView = new AndroidTreeView(getActivity(), root);
        setTreeData(root, list);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom2, true);

        tView.setDefaultNodeClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                onNodeClickListener(node,value);
            }
        });

        tView.setDefaultViewHolder(TreeItemHolder.class);
        knowledge_tree.removeAllViews();
        knowledge_tree.addView(tView.getView());
        //禁止出现下来阴影
        tView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        tView.setUseAutoToggle(true);
        tView.expandAll();
    }

    private void onNodeClickListener(TreeNode node, Object value){
        if (!node.isLeaf()) {
            return;
        }
        String knowledgeName = ((TreeItemHolder.IconTreeItem) value).getText();
        String knowledgeId = ((TreeItemHolder.IconTreeItem) value).getId();
        int knowledgeIndex = 0;
        for (int i = 0; i < treeNodeList.size(); i++) {
            TreeNode e = treeNodeList.get(i);
            String id = ((TreeItemHolder.IconTreeItem) e.getValue()).getId();
            if(TextUtils.equals(knowledgeId,id)){
                knowledgeIndex = i;
            }
        }
        if (isSelectNode) {
            EventBus.getDefault().post(new AppEventType(AppEventType.CLASSIFY_REQUEST, isBuy, classType
                    , gradeId, knowledgeName, knowledgeId, knowledgeIndex));
        } else {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isBuy", isBuy);
            bundle.putString("classType", classType);
            bundle.putString("gradeId", gradeId);
            bundle.putString("knowledgeName", knowledgeName);
            bundle.putString("knowledgeId", knowledgeId);
            bundle.putInt("knowledgeIndex", knowledgeIndex);
            CommonUtil.openActivity(SuperClassActiity.class, bundle);
        }
        HashMap<String, Object> mParams = ParamHelper.acquireParamsReceiver(ClassifyActivty.class.getName());
        mParams.put("treeNodeList", treeNodeList);
        getActivity().finish();
    }

    private void setTreeData(TreeNode node, List<KnowledgeEntity> list) {
        int index = 0;
        for (KnowledgeEntity knowledgeEntity : list) {
            boolean isLeaf = isLeaf(knowledgeEntity);
            TreeNode tempNode = getNode(knowledgeEntity.getId(), knowledgeEntity.getText(), isLeaf, index, knowledgeEntity.getKnowledgeDetailEntity());
            node.addChild(tempNode);

            if (isLeaf) {
                treeNodeList.add(tempNode);
            }

            if (knowledgeEntity.getKnowledgeList() != null)
                setTreeData(tempNode, knowledgeEntity.getKnowledgeList());
            index++;
        }
    }

    private boolean isLeaf(KnowledgeEntity knowledgeEntity) {
        return knowledgeEntity.getKnowledgeList() == null;
    }

    private TreeNode getNode(String id, String name, boolean isLeaf, int index, KnowledgeDetailEntity KnowledgeDetail) {
        TreeItemHolder.IconTreeItem ico = new TreeItemHolder.IconTreeItem(R.mipmap.attr_down, id, name, isLeaf, index, KnowledgeDetail);
        return new TreeNode(ico).setViewHolder(new TreeItemHolder(getActivity()));
    }

}
