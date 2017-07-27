package com.asking.pad.app.ui.personalcenter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.entity.IntegralLog;
import com.asking.pad.app.entity.UserEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.pay.PayAskActivity;
import com.asking.pad.app.widget.AskSimpleDraweeView;
import com.asking.pad.app.widget.AskSwipeRefreshLayout;
import com.asking.pad.app.widget.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by jswang on 2017/5/31.
 */

public class AskCoinRecordActivity extends BaseEvenActivity<UserPresenter, UserModel> {
    @BindView(R.id.toolBar)
    Toolbar mToolbar;

    @BindView(R.id.ad_avatar)
    AskSimpleDraweeView ad_avatar;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_monny)
    TextView tv_monny;

    @BindView(R.id.swipe_layout)
    AskSwipeRefreshLayout swipeLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.load_view)
    MultiStateView load_view;

    AskCoinRecordAdapter mAdapter;
    private List<IntegralLog> dataList = new ArrayList<>();
    int start = 0;
    int limit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_askcoin_record);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        super.initView();

        mToolbar.setTitle("");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAdapter = new AskCoinRecordAdapter(dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        load_view.setViewState(load_view.VIEW_STATE_LOADING);
        netBookData();
        initUser();

        swipeLayout.setPtrHandler(new PtrDefaultHandler2() {
            public void onLoadMoreBegin(PtrFrameLayout ptrFrameLayout) {//上拉，加载更多
                start += limit;
                netBookData();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {//下拉刷新
                start = 0;
                dataList.clear();
                swipeLayout.setMode(PtrFrameLayout.Mode.BOTH);
                netBookData();
            }
        });
    }

    private void initUser(){
        UserEntity mUser = AppContext.getInstance().getUserEntity();
        if(mUser != null){
            ad_avatar.setImageUrl(mUser.getAvatar());
            tv_name.setText(mUser.getNickName());
            tv_monny.setText(String.valueOf(mUser.getIntegral()));

            tv_name.setSelected(mUser.getSex() == 0 ? false : true);
        }
    }

    private void refreshUser() {
        mPresenter.studentinfo(new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                JSONObject resObject = JSON.parseObject(res);
                double integral = resObject.getDouble("integral");

                if (integral != AppContext.getInstance().getUserEntity().getIntegral()) {
                    AppContext.getInstance().getUserEntity().setIntegral(integral);
                    AppContext.getInstance().saveUserData(AppContext.getInstance().getUserEntity());
                }
                tv_monny.setText(String.valueOf(integral));
            }
        });
    }

    public void onEventMainThread(AppEventType event) {
        switch (event.type) {
            case AppEventType.PAY_SUCCESSS_REQUEST:
                refreshUser();
                break;
        }
    }

    private void netBookData(){
        mPresenter.integralLog(start,limit, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                load_view.setViewState(load_view.VIEW_STATE_CONTENT);

                JSONObject jsonRes= JSON.parseObject(res);
                List<IntegralLog> list = JSON.parseArray(jsonRes.getString("integralLogs"),IntegralLog.class);
                dataList.addAll(list);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onResultFail() {
                load_view.setViewState(load_view.VIEW_STATE_ERROR);
            }
        });

    }

    @OnClick({R.id.tv_recharge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_recharge:
                PayAskActivity.openActivity(1);
                break;
        }
    }

}
