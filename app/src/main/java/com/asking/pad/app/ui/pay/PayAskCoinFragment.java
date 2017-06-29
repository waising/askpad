package com.asking.pad.app.ui.pay;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.entity.pay.AskCoinPay;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.widget.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 阿思币充值页面
 * Created by jswang on 2017/4/21.
 */

public class PayAskCoinFragment extends BaseFrameFragment<UserPresenter, UserModel> {
    @BindView(R.id.load_view)
    MultiStateView load_view;

    @BindView(R.id.rec_view)
    RecyclerView rec_view;

    List<AskCoinPay> mDatas = new ArrayList<>();
    PayAskCoinAdapter mAdapter;

    public static PayAskCoinFragment newInstance() {
        PayAskCoinFragment fragment = new PayAskCoinFragment();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pay_askcoin);
        ButterKnife.bind(this, getContentView());
    }

    @Override
    public void initView() {
        super.initView();

        rec_view.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        mAdapter = new PayAskCoinAdapter(getActivity(), mDatas);
        rec_view.setAdapter(mAdapter);
        //获取ask币套餐列表
        load_view.setViewState(MultiStateView.VIEW_STATE_LOADING);
        mPresenter.getRechargeList(0,100,new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                JSONObject jsonRes = JSON.parseObject(res);
                List<AskCoinPay> list = JSON.parseArray(jsonRes.getString("list"),AskCoinPay.class);
                if (list != null && list.size() > 0) {
                    mDatas.clear();
                    mDatas.addAll(list);
                    mAdapter.notifyDataSetChanged();
                    load_view.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                } else {
                    load_view.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                }
            }
        });
    }
}
