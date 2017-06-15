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
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.entity.CollectionEntity;
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
 * 教育咨询fragement
 */

public class EducationConsultFragment extends BaseFrameFragment<CollectionPresenter, CollectionModel> implements DelListener, DeleteDialog.DeleteListner {


    @BindView(R.id.swipe_layout)
    AskSwipeRefreshLayout swipeLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.iv_no_subject)
    ImageView ivNoSubject;

    private int start = 0, limit = 6, subjectCatalog = Constants.Collect.refer;//下拉刷新标记和tab页标记

    private String token;
    private EducationConsultAdapter mEducationConsultAdapter;
    private List<CollectionEntity.ListBean> commentEntity = new ArrayList<>();
    /**
     * 删除弹窗
     */
    private DeleteDialog deleteDialog;

    public static EducationConsultFragment newInstance() {
        EducationConsultFragment fragment = new EducationConsultFragment();
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
    public void initListener() {
        super.initListener();
        swipeLayout.setViewPager(recyclerView);
        swipeLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout ptrFrameLayout) {//上拉加载更多
                start += limit;
                getDataNow();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {//下拉刷新
                swipeLayout.setMode(PtrFrameLayout.Mode.BOTH);
                start = 0;
                //知识点或教育资讯
                commentEntity.clear();
                getDataNow();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        getDataNow();
    }

    private void getDataNow() {
        token = AppContext.getInstance().getToken();
        mPresenter.presenterMyFavor(111, token, subjectCatalog, start, limit, new ApiRequestListener<String>() {//知识点请求
            @Override
            public void onResultSuccess(String resStr) {//数据返回成功
                swipeLayout.refreshComplete();

                if (!TextUtils.isEmpty(resStr)) {
                    JSONObject resobj = JSON.parseObject(resStr);
                    String resList = resobj.getString("list");
                    List<CollectionEntity.ListBean> listPaper = JSON.parseArray(resList, CollectionEntity.ListBean.class);

                    if (listPaper != null && listPaper.size() > 0) {
                        commentEntity.addAll(listPaper);
                        ivNoSubject.setVisibility(View.GONE);
                    } else {
                        if (start > 0) { // 后台返回的 total 值是 double 类型，不能用 int 整型去接，暂时先用这个方法判断
                            swipeLayout.setMode(PtrFrameLayout.Mode.REFRESH); // 刷到底部没有数据了的话给个提示，并且不让再加载更多。
                            showShortToast(getResources().getString(R.string.no_more_data));
/*                            if (map.get("total") != null) {
                                Integer total = Integer.valueOf((String) map.get("total"));
                                if (total <= start) {
                                    showShortToast("没有更多数据了");
                                }*/
                        } else {
                            ivNoSubject.setVisibility(View.VISIBLE);

                        }
                    }
                    refshAdapt(listPaper);
                }

            }

            @Override
            public void onResultFail() {
                super.onResultFail();
                swipeLayout.setMode(PtrFrameLayout.Mode.BOTH);
                start = 0;
                //知识点或教育资讯
                commentEntity.clear();
                getDataNow();
            }
        });
    }

    public void refshAdapt(List<CollectionEntity.ListBean> entityComment) {
        if (mEducationConsultAdapter == null) {
            mEducationConsultAdapter = new EducationConsultAdapter(getActivity(), entityComment, this);
            recyclerView.setAdapter(mEducationConsultAdapter);
        } else {
            if (start > 0) {
                // 加载更多
                mEducationConsultAdapter.setData(commentEntity);
            } else {
                // 顶部tab切换
                mEducationConsultAdapter.setData(entityComment);
            }
        }
    }


    /**
     * 删除教育请求
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
                mEducationConsultAdapter.removeCommentEntityItem(position);
                mEducationConsultAdapter.notifyItemRangeChanged(position, mEducationConsultAdapter.getItemCount()- position);
                if (mEducationConsultAdapter.getItemCount() == 0) {
                    ivNoSubject.setVisibility(View.VISIBLE);
                } else {
                    ivNoSubject.setVisibility(View.GONE);
                }
            }
        });
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

}
