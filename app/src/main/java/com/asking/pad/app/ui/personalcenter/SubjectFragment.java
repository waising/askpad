package com.asking.pad.app.ui.personalcenter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.entity.MySubjectEntity;
import com.asking.pad.app.presenter.CollectionModel;
import com.asking.pad.app.presenter.CollectionPresenter;
import com.asking.pad.app.widget.AskSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * 题目fragement
 * create by linbin
 */

public class SubjectFragment extends BaseFrameFragment<CollectionPresenter, CollectionModel> implements DelListener, DeleteDialog.DeleteListner {


    @BindView(R.id.swipe_layout)
    AskSwipeRefreshLayout swipeLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.iv_no_subject)
    ImageView ivNoSubject;


    private int start = 0, limit = 6;


    private SubjectAdapter mSubjectAdapter;
    private List<MySubjectEntity.ListBean> commentEntity = new ArrayList<>();
    private String token;
    /**
     * 删除弹窗
     */
    private DeleteDialog deleteDialog;


    public static SubjectFragment newInstance() {
        SubjectFragment fragment = new SubjectFragment();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_subject);
        ButterKnife.bind(this, getContentView());
    }

    @Override
    public void initView() {
        super.initView();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
    }

    @Override
    public void initData() {
        super.initData();
        getDataNow();

    }

    private void getDataNow() {
        token = AppContext.getInstance().getToken();
        mPresenter.presenterMySubject(start, limit, new ApiRequestListener<String>() {//题目的id和类型请求
            @Override
            public void onResultSuccess(String resStr) {//数据返回成功
                swipeLayout.refreshComplete();
                if (!TextUtils.isEmpty(resStr)) {
                    JSONObject resobj = JSON.parseObject(resStr);
                    String resList = resobj.getString("list");
                    List<MySubjectEntity.ListBean> listPaper = JSON.parseArray(resList, MySubjectEntity.ListBean.class);
                    if (listPaper != null && listPaper.size() > 0) {
                        commentEntity.addAll(listPaper);
                        ivNoSubject.setVisibility(View.GONE);
                    } else {

                        if (start > 0) {
                            swipeLayout.setMode(PtrFrameLayout.Mode.REFRESH);
                            showShortToast(getResources().getString(R.string.no_more_data));
                        } else {
                            ivNoSubject.setVisibility(View.VISIBLE);
                        }
                    }
                    refshAdapt(commentEntity);
                }


            }

            @Override
            public void onResultFail() {
                super.onResultFail();

                if (swipeLayout != null) {
                    swipeLayout.refreshComplete();
                }
                swipeLayout.setMode(PtrFrameLayout.Mode.REFRESH);
                mSubjectAdapter = null;
                recyclerView.setAdapter(null);
            }
        });
    }

    @Override
    public void initListener() {
        super.initListener();
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
                commentEntity.clear();
                getDataNow();
            }
        });


    }

    public void refshAdapt(List<MySubjectEntity.ListBean> entityComment) {
        if (mSubjectAdapter == null) {
            mSubjectAdapter = new SubjectAdapter(getActivity(), entityComment, this);
            recyclerView.setAdapter(mSubjectAdapter);
        } else {
            if (start > 0) {
                // 加载更多
                mSubjectAdapter.setData(commentEntity);
            } else {
                // 顶部tab切换
                mSubjectAdapter.setData(entityComment);
            }
        }
    }

    /**
     * 显示删除弹窗
     */
    private void showDelDialog(int position, String id) {
        deleteDialog = new DeleteDialog();
        deleteDialog.setDeleteListner(this);
        deleteDialog.setPosition(position);
        deleteDialog.setId(id);
        deleteDialog.show(getActivity().getSupportFragmentManager(), "");

    }

    /**
     * 删除请求
     */

    @Override
    public void del(final int position, String id) {
        showDelDialog(position, id);
    }

    @Override
    public void ok(final int position, String id, DeleteDialog deleteDialog) {
        deleteDialog.dismiss();
        mPresenter.presenterDelMyFavor(222, token, id, new ApiRequestListener<JSONObject>() {//题目的id和类型请求
            @Override
            public void onResultSuccess(JSONObject object) {//
                showShortToast(getResources().getString(R.string.success_del));
                mSubjectAdapter.removeCommentEntityItem(position);
                mSubjectAdapter.notifyItemRangeChanged(position, mSubjectAdapter.getItemCount() - position);
                if (mSubjectAdapter.getItemCount() == 0) {
                    ivNoSubject.setVisibility(View.VISIBLE);
                } else {
                    ivNoSubject.setVisibility(View.GONE);
                }

            }
        });
    }
}
