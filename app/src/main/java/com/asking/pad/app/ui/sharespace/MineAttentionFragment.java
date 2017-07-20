package com.asking.pad.app.ui.sharespace;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.commom.ToastUtil;
import com.asking.pad.app.entity.sharespace.MyAttention;
import com.asking.pad.app.presenter.ShareModel;
import com.asking.pad.app.presenter.SharePresenter;
import com.asking.pad.app.widget.AskSwipeRefreshLayout;
import com.asking.pad.app.widget.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 我的关注
 * Created by jswang on 2017/4/20.
 */

public class MineAttentionFragment extends BaseFrameFragment<SharePresenter, ShareModel> implements MineAttentionGridAdapter.OnClickListener {
    @BindView(R.id.load_view)
    MultiStateView load_view;
    MineAttentionGridAdapter mineAttentionGridAdapter;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    /**
     * 下拉刷新控件
     */
    @BindView(R.id.swipe_layout)
    AskSwipeRefreshLayout swipeLayout;


    /**
     * 开始页数，一页加载几个
     */
    int start = 0, limit = 8;
    /**
     * 数据
     */
    private List<MyAttention> dataList = new ArrayList<>();

    private MaterialDialog mDialog;

    public static MineAttentionFragment newInstance() {
        MineAttentionFragment fragment = new MineAttentionFragment();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_attention);
        ButterKnife.bind(this, getContentView());
    }

    @Override
    public void initView() {
        super.initView();
        mDialog = getLoadingDialog().build();
        GridLayoutManager mgr = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mgr);
    }

    @Override
    public void initLoad() {
        super.initLoad();
        load_view.setErrorRefBtnTxt2(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestList();
            }
        });
        load_view.setViewState(load_view.VIEW_STATE_LOADING);
        requestList();
    }


    @Override
    public void initListener() {
        super.initListener();
        swipeLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout ptrFrameLayout) {//加载更多
                start += limit;
                requestList();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {//下拉刷新
                start = 0;
                dataList.clear();
                swipeLayout.setMode(PtrFrameLayout.Mode.BOTH);
                requestList();
            }
        });
    }

    private void requestList() {
        mPresenter.presenterAttentionList(start, limit, AppContext.getInstance().getUserName(), new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {//数据返回成功
                swipeLayout.refreshComplete(); // 关闭刷新控件动画
                if (!TextUtils.isEmpty(resStr)) {
                    JSONObject resobj = JSON.parseObject(resStr);
                    String resList = resobj.getString("content");
                    List<MyAttention> entity = JSON.parseArray(resList, MyAttention.class);
                    if (entity != null && entity.size() > 0) {
                        load_view.setViewState(load_view.VIEW_STATE_CONTENT);
                        dataList.addAll(entity);
                        refshAdapt();
                    } else {
                        if (start > 0) {//上拉加载更多的情况下，没数据的情况下，加载到最后一页情况
                            swipeLayout.setMode(PtrFrameLayout.Mode.REFRESH);
                            showShortToast(getActivity().getResources().getString(R.string.no_more_data));
                        } else {
                            load_view.setViewState(load_view.VIEW_STATE_EMPTY);
                        }

                    }

                }

            }

            @Override
            public void onResultFail() {
                super.onResultFail();
                load_view.setViewState(load_view.VIEW_STATE_ERROR);
                if (swipeLayout != null) {
                    swipeLayout.refreshComplete();
                }
                start = 0;
                swipeLayout.setMode(PtrFrameLayout.Mode.REFRESH);

            }
        });

    }

    /**
     * 填充页面数据
     */
    public void refshAdapt() {
        if (mineAttentionGridAdapter == null) {
            mineAttentionGridAdapter = new MineAttentionGridAdapter(getActivity(), dataList);
            mineAttentionGridAdapter.setOnClickListner(MineAttentionFragment.this);
            recyclerView.setAdapter(mineAttentionGridAdapter);
        } else {
            mineAttentionGridAdapter.setData(dataList);
            mineAttentionGridAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(int position, View view) {
        MyAttention mySpace = (MyAttention) mineAttentionGridAdapter.getItem(position);
        if (mySpace != null) {
            if (view.isSelected())//取消关注
            {
                requestCancelAttention(mySpace.getId());
            } else {
                requestAttention(mySpace.getId());
            }

        }

    }

    /**
     * 关注教师
     *
     * @param userId
     */
    private void requestAttention(String userId) {
        mDialog.show();
        mPresenter.presenterAttention(AppContext.getInstance().getUserName(), userId, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {//数据返回成功
                mDialog.dismiss();
                ToastUtil.showMessage("关注成功");
            }

            @Override
            public void onResultFail() {
                super.onResultFail();
                mDialog.dismiss();
                ToastUtil.showMessage("关注失败");
            }
        });


    }

    /**
     * 取消关注教师
     *
     * @param userId
     */
    private void requestCancelAttention(String userId) {
        mDialog.show();
        mPresenter.presenterCancelAttention(AppContext.getInstance().getUserName(), userId, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {//数据返回成功
                mDialog.dismiss();
                ToastUtil.showMessage("取消关注成功");
            }

            @Override
            public void onResultFail() {
                super.onResultFail();
                mDialog.dismiss();
                ToastUtil.showMessage("取消关注失败");
            }
        });

    }


}
