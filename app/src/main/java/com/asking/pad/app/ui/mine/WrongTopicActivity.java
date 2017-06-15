package com.asking.pad.app.ui.mine;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.entity.ClassEntity;
import com.asking.pad.app.entity.CourseEntity;
import com.asking.pad.app.entity.LabelEntity;
import com.asking.pad.app.entity.MyWrongTopicEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/4/26.
 */

public class WrongTopicActivity extends BaseFrameActivity<UserPresenter, UserModel> {
    @BindView(R.id.toolBar)
    Toolbar mToolbar;

    RecyclerView rv_class;

    RecyclerView rv_version;

    RecyclerView rv_textbook;

    @BindView(R.id.rv_WrongTopic)
    RecyclerView rv_WrongTopic;

    WtCommAdapter classAdapter;
    WtCommAdapter versionAdapter;
    WtCommAdapter textbookAdapter;
    ErrorCollectionAdapter wrongTopicAdapter;

    ArrayList<LabelEntity> classList = new ArrayList<>();
    ArrayList<LabelEntity> versionList = new ArrayList<>();
    ArrayList<LabelEntity> textbookList = new ArrayList<>();
    ArrayList<MyWrongTopicEntity> wrongTopicList = new ArrayList<>();

    String classId;
    String versionId;
    String textbookId;

    private int start = 0;
    private int limit = 10;

    MaterialDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_topic);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        super.initView();

        setToolbar(mToolbar, getString(R.string.err_note));

        mDialog = getLoadingDialog().build();

        View header = LayoutInflater.from(this).inflate(R.layout.activity_wrong_topic_head, rv_class, false);
        rv_class = (RecyclerView)header.findViewById(R.id.rv_class);
        rv_version = (RecyclerView)header.findViewById(R.id.rv_version);
        rv_textbook = (RecyclerView)header.findViewById(R.id.rv_textbook);

        classList.add(new LabelEntity(Constants.subjectKeys[0], Constants.subjectNames[0], true));
        classList.add(new LabelEntity(Constants.subjectKeys[1], Constants.subjectNames[1]));
        classList.add(new LabelEntity(Constants.subjectKeys[2], Constants.subjectNames[2]));
        classList.add(new LabelEntity(Constants.subjectKeys[3], Constants.subjectNames[3]));
        classAdapter = new WtCommAdapter(this, classList, new WtCommAdapter.OnCommItemListener() {
            @Override
            public void OnCommItem(LabelEntity e) {
                initVersion(e.getId());
            }
        });
        rv_class.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv_class.setAdapter(classAdapter);

        versionAdapter = new WtCommAdapter(this, versionList, new WtCommAdapter.OnCommItemListener() {
            @Override
            public void OnCommItem(LabelEntity e) {
                initTextbook(e.getId());
            }
        });
        rv_version.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv_version.setAdapter(versionAdapter);

        textbookAdapter = new WtCommAdapter(this, textbookList, new WtCommAdapter.OnCommItemListener() {
            @Override
            public void OnCommItem(LabelEntity e) {
                iniWrongTopic(e.getId());
            }
        });
        rv_textbook.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv_textbook.setAdapter(textbookAdapter);

        wrongTopicAdapter = new ErrorCollectionAdapter(this,mPresenter, wrongTopicList);
        wrongTopicAdapter.addHeaderView(header);
        rv_WrongTopic.setLayoutManager(new LinearLayoutManager(this));
        rv_WrongTopic.setAdapter(wrongTopicAdapter);

        initVersion(Constants.subjectKeys[0]);
    }

    private void initVersion(String classId) {
        this.classId = classId;
        wrongTopicAdapter.setSubjectCatalogCode(classId);
        mPresenter.modelMyWrongVersions(classId, "", new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                versionList.clear();
                List<CourseEntity> list = CommonUtil.parseDataToList(res, new TypeToken<List<CourseEntity>>() {
                });
                for (int i = 0; i < list.size(); i++) {
                    CourseEntity e = list.get(i);
                    if(i == 0){
                        versionId = String.valueOf(e.getVersionId());
                        versionList.add(new LabelEntity(versionId,e.getVersionName(), true));
                    }else{
                        versionList.add(new LabelEntity(String.valueOf(e.getVersionId()),e.getVersionName()));
                    }
                }
                versionAdapter.notifyDataSetChanged();

                initTextbook(versionId);
            }
        });
    }

    private void initTextbook(String versionId) {
        this.versionId = versionId;
        mPresenter.modelMyWrongGrade(versionId,"1", new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                textbookList.clear();
                List<ClassEntity> list = CommonUtil.parseDataToList(res, new TypeToken<List<ClassEntity>>() {
                });
                for (int i = 0; i < list.size(); i++) {
                    ClassEntity e = list.get(i);
                    if(i == 0){
                        textbookId = String.valueOf(e.getVersionLevelId());
                        textbookList.add(new LabelEntity(textbookId,e.getLevelName(), true));
                    }else{
                        textbookList.add(new LabelEntity(String.valueOf(e.getVersionLevelId()),e.getTextbook()));
                    }
                }
                textbookAdapter.notifyDataSetChanged();

                iniWrongTopic(textbookId);
            }
        });
    }

    private void iniWrongTopic(String textbookId) {
        this.textbookId = textbookId;

        ArrayMap<Object, Object> map = new ArrayMap<>();
        map.put("start", String.valueOf(start));
        map.put("limit", String.valueOf(limit));
        map.put("tag", ""); // tag 错误类型，传""表示请求全部错题
        map.put("version_level_id", textbookId);
        map.put("tips",  new ArrayList());
        String postStr = JSON.toJSONString(map);
        mDialog.show();
        mPresenter.errorCollection(classId,postStr, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                JSONObject jsonRes = JSON.parseObject(res);
                List<MyWrongTopicEntity> list = CommonUtil.parseDataToList(jsonRes.getString("list"), new TypeToken<List<MyWrongTopicEntity>>() {
                });
                if(start == 0){
                    wrongTopicList.clear();
                }
                wrongTopicList.addAll(list);
                wrongTopicAdapter.notifyDataSetChanged();

                mDialog.dismiss();
            }
        });
    }

}
