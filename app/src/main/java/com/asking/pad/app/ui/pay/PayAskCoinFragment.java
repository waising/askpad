package com.asking.pad.app.ui.pay;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.entity.AskMoneyEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.widget.MultiStateView;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 阿思币充值页面
 * Created by jswang on 2017/4/21.
 */

public class PayAskCoinFragment extends BaseFrameFragment<UserPresenter, UserModel> {
    @BindView(R.id.load_view)
    MultiStateView load_view;

    @BindView(R.id.rec_view)
    RecyclerView rec_view;

    List<AskMoneyEntity> mDatas = new ArrayList<>();
    PayAskCoinAdapter mAdapter;

    String rechargeId;

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
        mAdapter = new PayAskCoinAdapter(getActivity(), mDatas, new OnCommItemListener() {//选中项
            @Override
            void OnCommItem(AskMoneyEntity e) {
                setSelectAskCoin(e);
            }
        });
        rec_view.setAdapter(mAdapter);
        //获取ask币套餐列表
        load_view.setViewState(MultiStateView.VIEW_STATE_LOADING);
        mPresenter.getRechargeList(new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                List<AskMoneyEntity> list = CommonUtil.parseDataToList(res, new TypeToken<List<AskMoneyEntity>>() {
                });
                if (list != null && list.size() > 0) {
                    Collections.sort(list, new Comparator<AskMoneyEntity>() {
                        @Override
                        public int compare(AskMoneyEntity o1, AskMoneyEntity o2) {
                            if (o1.getPrice() < o2.getPrice()) {
                                return -1;
                            }
                            return 1;
                        }
                    });
                    mDatas.clear();
                    mDatas.addAll(list);
                    setSelectAskCoin(mDatas.get(0));
                    mAdapter.notifyDataSetChanged();
                    load_view.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                } else {
                    load_view.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                }
            }
        });
    }

    private void setSelectAskCoin(AskMoneyEntity e) {
        e.isSelect = true;
        rechargeId = e.getId();
    }


    @OnClick({R.id.btn_pay, R.id.tv_add_shopcart})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pay://立即购买
                Bundle bundle = new Bundle();
                bundle.putString("rechargeId", rechargeId);
                openActivity(PayActivity.class, bundle);
                break;
            case R.id.tv_add_shopcart://加入购物车
                addShopcart();
                break;

        }
    }

    /**
     * 加入购物车请求
     */
    private void addShopcart() {
        mPresenter.presenterAddShopCartAskCoin(rechargeId, new ApiRequestListener<JSONObject>() {//题目的id和类型请求
            @Override
            public void onResultSuccess(JSONObject object) {//数据返回成功
                showShortToast(getResources().getString(R.string.add_shop_cart_succeed));
            }
        });

    }

}
