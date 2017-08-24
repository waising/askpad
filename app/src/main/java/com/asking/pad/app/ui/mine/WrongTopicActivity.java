package com.asking.pad.app.ui.mine;

import android.os.Bundle;
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

    @BindView(R.id.rv_WrongTopic)
    RecyclerView rv_WrongTopic;

    WtCommAdapter classAdapter;
    ErrorCollectionAdapter wrongTopicAdapter;

    ArrayList<LabelEntity> classList = new ArrayList<>();
    ArrayList<MyWrongTopicEntity> wrongTopicList = new ArrayList<>();

    private int start = 0;
    private int limit = 100;

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

        classList.add(new LabelEntity(Constants.subjectKeys[0], Constants.subjectNames[0], true));
        classList.add(new LabelEntity(Constants.subjectKeys[1], Constants.subjectNames[1]));
        classList.add(new LabelEntity(Constants.subjectKeys[2], Constants.subjectNames[2]));
        classList.add(new LabelEntity(Constants.subjectKeys[3], Constants.subjectNames[3]));
        classAdapter = new WtCommAdapter(this, classList, new WtCommAdapter.OnCommItemListener() {
            @Override
            public void OnCommItem(LabelEntity e) {
                start = 0;
                iniWrongTopic(e.getId());
            }
        });
        rv_class.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv_class.setAdapter(classAdapter);

        wrongTopicAdapter = new ErrorCollectionAdapter(this, wrongTopicList);
        wrongTopicAdapter.addHeaderView(header);
        rv_WrongTopic.setLayoutManager(new LinearLayoutManager(this));
        rv_WrongTopic.setAdapter(wrongTopicAdapter);

        iniWrongTopic(Constants.subjectKeys[0]);
    }

    private void iniWrongTopic(String textbookId) {
        mDialog.show();
        mPresenter.errorsubject(textbookId,start,limit, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                mDialog.dismiss();

                JSONObject jsonRes = JSON.parseObject(res);
                List<MyWrongTopicEntity> list = CommonUtil.parseDataToList(jsonRes.getString("content"), new TypeToken<List<MyWrongTopicEntity>>() {
                });
                if(start == 0){
                    wrongTopicList.clear();
                }
                wrongTopicList.addAll(list);
                wrongTopicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onResultFail() {
                mDialog.dismiss();
            }
        });
    }

}
