package com.asking.pad.app.ui.personalcenter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.entity.BuyRecordsEntity;
import com.asking.pad.app.presenter.BuyRecordModel;
import com.asking.pad.app.presenter.BuyRecordPresenter;
import com.asking.pad.app.widget.AskSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 购买记录里的fragement,我的课程和我的充值
 * create by linbin
 */

public class BuyRecordFragment extends BaseFrameFragment<BuyRecordPresenter, BuyRecordModel> implements MyBuyRecordsAdapter.RecordDelListner, DeleteDialog.DeleteListner {


    @BindView(R.id.swipe_layout)
    AskSwipeRefreshLayout swipeLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    MyBuyRecordsAdapter myBuyRecordsAdapter;
    int start = 0, limit = 20, subjectCatalog = 1;

    private List<BuyRecordsEntity.ListBean> listMyLessons = new ArrayList<>(); //超级辅导课（我的课程）
    private List<BuyRecordsEntity.ListBean> listAsk = new ArrayList<>(); // 阿思币（我的充值）
    /**
     * 删除弹窗
     */
    private DeleteDialog deleteDialog;
    @BindView(R.id.no_data)
    LinearLayout llNodata;


    public static BuyRecordFragment newInstance() {
        BuyRecordFragment fragment = new BuyRecordFragment();
        return fragment;
    }


    public static BuyRecordFragment newInstance(int subjectCatalog) {
        BuyRecordFragment fragment = new BuyRecordFragment();
        Bundle bunle = new Bundle();
        bunle.putInt("subjectCatalog", subjectCatalog);
        fragment.setArguments(bunle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_buy_record);
        ButterKnife.bind(this, getContentView());
    }

    @Override
    public void initView() {
        super.initView();
        initRecycleView();
    }

    /**
     * 初始化recyclewView
     */
    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。

    }

    @Override
    public void initData() {
        super.initData();
        subjectCatalog = getArguments().getInt("subjectCatalog");
        getDataNow(); // 请求数据得放在 initLoad 这个方法里
    }

    @Override
    public void initListener() {
        super.initListener();
        swipeLayout.setViewPager(recyclerView); // 解决上下拉刷新控件 PtrHTFrameLayout 和 viewpager 以及侧滑删除等横向控件冲突
        swipeLayout.setPtrHandler(new PtrDefaultHandler2() {
            public void onLoadMoreBegin(PtrFrameLayout ptrFrameLayout) {//上拉，加载更多
                start += limit;
                getDataNow();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {//下拉刷新
                start = 0;
                listMyLessons.clear();
                listAsk.clear();
                swipeLayout.setMode(PtrFrameLayout.Mode.BOTH);
                getDataNow();
            }
        });
    }


    /**
     * 删除请求
     */


    /**
     * 我的购买记录的请求
     */
    private void getDataNow() {
        String token = AppContext.getInstance().getToken();
        mPresenter.presenterMyBuyRecords(token, start, limit, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {//数据返回成功
                swipeLayout.refreshComplete();
                if (!TextUtils.isEmpty(resStr)) {
                    JSONObject resobj = JSON.parseObject(resStr);
                    String resList = resobj.getString("list");
                    List<BuyRecordsEntity.ListBean> entity = JSON.parseArray(resList, BuyRecordsEntity.ListBean.class);
                    if (entity != null && entity.size() > 0) {

                        for (int i = 0; i < entity.size(); i++) {
                            // 将后台返回给我们的数据进行筛选分类
                            BuyRecordsEntity.ListBean buyRecordsEntity = entity.get(i);
                            // 对于多个产品组合而成的订单（购物车），目前暂时取第一个产品作为分类的依据
                            if (buyRecordsEntity.getProducts() != null && buyRecordsEntity.getProducts().size() > 0) {
                                String commodityType = buyRecordsEntity.getProducts().get(0).getCommodityType();
                                BuyRecordsEntity.ListBean.ProductsBean.RechargeBean recharge = buyRecordsEntity.getProducts().get(0).getRecharge();
                                if (recharge != null) {
                                    // recharge 不为 null 的话是阿思币充值（我的充值），为 null 的话是基础知识包和超级辅导课购买
                                    listAsk.add(buyRecordsEntity);
                                } else {
                                    if (commodityType != null && commodityType.equals(String.valueOf(Constants.Collect.knowledge))) {
                                        // commodityType 的值为 1 的话是基础知识包，(我的服务)
//                                        listBase.add(buyRecordsEntity);
                                    } else {
                                        // commodityType 的值为 2 的话是超级辅导课（我的课程） ,为 null 是知识点购买
                                        listMyLessons.add(buyRecordsEntity);
                                    }

                                }
                            }
                        }
                        if (myBuyRecordsAdapter == null) {
                            myBuyRecordsAdapter = new MyBuyRecordsAdapter();
                            recyclerView.setAdapter(myBuyRecordsAdapter);
                        }
                        refsh(); // 刷新数据
                    } else {
                        if (start > 0) {
                            //刷到底部没有数据了的话给个提示，并且不让再加载更多。
                            // 后台返回的 total 值是 double 类型，不能用 int 整型去接，麻烦，暂时先用这个方法判断
/*                if(map.get("total")!=null){
                    Integer total = Integer.valueOf((String) map.get("total"));
                    if(total<=start){
                        showShortToast("没有更多数据了");
                    }*/
                            swipeLayout.setMode(PtrFrameLayout.Mode.REFRESH);
                            showShortToast(getResources().getString(R.string.no_more_data));
                        }
                    }


                }

            }

            @Override
            public void onResultFail() {
                super.onResultFail();
                requestFailure();
            }
        });
    }

    /**
     * 请求失败
     */
    private void requestFailure() {
        if (swipeLayout != null) {
            swipeLayout.refreshComplete();
        }
        start = 0;
        swipeLayout.setMode(PtrFrameLayout.Mode.REFRESH);
        showShortToast(getResources().getString(R.string.no_more_data));
    }

    /**
     * 刷新数据
     */
    public void refsh() {
        switch (subjectCatalog) {
            case Constants.Collect.knowledge:// 超级辅导课（我的课程）
                if (listMyLessons != null && listMyLessons.size() > 0) {
                    llNodata.setVisibility(View.GONE);
                } else {
                    llNodata.setVisibility(View.VISIBLE);
                }
                myBuyRecordsAdapter.setData(listMyLessons, subjectCatalog);
                break;
            case Constants.Collect.title: // 阿思币充值（我的充值）
                if (listAsk != null && listAsk.size() > 0) {
                    llNodata.setVisibility(View.GONE);
                } else {
                    llNodata.setVisibility(View.VISIBLE);
                }
                myBuyRecordsAdapter.setData(listAsk, subjectCatalog);
                break;

        }
        myBuyRecordsAdapter.notifyDataSetChanged();
        myBuyRecordsAdapter.setDelClickListener(this);
        setHeader(recyclerView);
    }


    /**
     * 添加header
     *
     * @param view
     */
    private void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.layout_buyrecords_header, view, false);
        switch (subjectCatalog) {
            case Constants.Collect.knowledge:
                // 超级辅导课（我的课程）//服务端说先隐藏掉
                header.findViewById(R.id.tv_expired_date).setVisibility(View.GONE);
                break;
            case Constants.Collect.title:
                // 阿思币充值（我的充值）
                header.findViewById(R.id.tv_expired_date).setVisibility(View.GONE);
                break;
        }
        myBuyRecordsAdapter.setHeaderView(header);
    }


    @Override
    public void del(final int position, String id, final int subjectCatalog) {
        showDelDialog(position, id);
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

    @Override
    public void ok(final int position, String id, DeleteDialog deleteDialog) {
        deleteDialog.dismiss();
        if (!TextUtils.isEmpty(id)) {
            mPresenter.presenterDelMyBuyRecords(id, new ApiRequestListener<JSONObject>() {//题目的id和类型请求
                @Override
                public void onResultSuccess(JSONObject object) {//成功保存笔记内容
                    showShortToast(getResources().getString(R.string.success_del));
                    switch (subjectCatalog) {
                        case Constants.Collect.knowledge:
                            // 超级辅导课（我的课程）
                            listMyLessons.remove(position - 1);
                            myBuyRecordsAdapter.notifyItemRemoved(position);
                            myBuyRecordsAdapter.notifyItemRangeChanged(position, listMyLessons.size() - position);
                            setNodata(listMyLessons);
                            break;
                        case Constants.Collect.title:
                            // 阿思币充值（我的充值）
                            listAsk.remove(position - 1);
                            myBuyRecordsAdapter.notifyItemRemoved(position);
                            myBuyRecordsAdapter.notifyItemRangeChanged(position, listAsk.size() - position);
                            setNodata(listAsk);
                            break;
                    }


                }

            });//删除请求
        }

    }


    private void setNodata(List<BuyRecordsEntity.ListBean> list) {
        if (list.size() == 0) {
            llNodata.setVisibility(View.VISIBLE);
        } else {
            llNodata.setVisibility(View.GONE);
        }

    }
}
