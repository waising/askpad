package com.asking.pad.app.ui.personalcenter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenFrameFragment;
import com.asking.pad.app.commom.ShopCartEvent;
import com.asking.pad.app.entity.BookInfo;
import com.asking.pad.app.entity.ShopCartEntity;
import com.asking.pad.app.presenter.ShopCartModel;
import com.asking.pad.app.presenter.ShopCartPresenter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.asking.pad.app.R.id.tv_check;

/**
 * 购物车列表Fragment
 */

public class ShopCartListFragmentCopy extends BaseEvenFrameFragment<ShopCartPresenter, ShopCartModel> implements ShopCartAdapter.RecordDelListner, OnItemClickListener, DeleteDialog.DeleteListner {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;


    ImageView ivChooseAll;


    ShopCartAdapter myShopCartAdapter;


    @BindView(R.id.tv_choose_num)
    TextView tvChooseNum;

    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;

    /**
     * 删除弹窗
     */
    private DeleteDialog deleteDialog;
    private List<ShopCartEntity.ListBean> mList = new ArrayList<>(); // 阿思币（我的充值）


    public static ShopCartListFragmentCopy newInstance() {
        ShopCartListFragmentCopy fragment = new ShopCartListFragmentCopy();
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_shop_cart_list_copy);
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
        getDataNow(); // 请求数据得放在 initLoad 这个方法里
    }

    @Override
    public void initListener() {
        super.initListener();
    }


    /**
     * 删除请求
     */


    /**
     * 购物车列表的请求
     */
    private void getDataNow() {
        mPresenter.presenterShopCartList(new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {//数据返回成功
                if (!TextUtils.isEmpty(resStr)) {
                    JSONObject resobj = JSON.parseObject(resStr);
                    String resList = resobj.getString("list");
                    List<ShopCartEntity.ListBean> entity = JSON.parseArray(resList, ShopCartEntity.ListBean.class);
                    if (entity != null && entity.size() > 0) {
                        llNoData.setVisibility(View.GONE);
                        mList.addAll(entity);
                        if (myShopCartAdapter == null) {
                            myShopCartAdapter = new ShopCartAdapter();
                            recyclerView.setAdapter(myShopCartAdapter);
                        }
                        refsh(); // 刷新数据
                        flushFooterView();
                    } else {
                        llNoData.setVisibility(View.VISIBLE);

                    }

                }

            }

        });

    }

    /**
     * 刷新数据
     */
    public void refsh() {
        myShopCartAdapter.setData(mList);
        myShopCartAdapter.notifyDataSetChanged();
        myShopCartAdapter.setDelClickListener(this);
        myShopCartAdapter.setOnItemClickListener(this);
        setHeader(recyclerView);

        if (isAllChecked()) {
            ivChooseAll.setSelected(true);
        } else {
            ivChooseAll.setSelected(false);
        }
    }


    /**
     * 添加header
     *
     * @param view
     */
    private void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.layout_shopcart_header, view, false);
        ivChooseAll = (ImageView) header.findViewById(R.id.iv_choose_all);
        ivChooseAll.setOnClickListener(new View.OnClickListener() {//全选按钮


            @Override
            public void onClick(View v) {
                chooseAll();
            }
        });
        myShopCartAdapter.setHeaderView(header);
    }


    @Override
    public void del(final int position, String id) {
        showDelDialog(position, id);
    }


    @OnClick({tv_check})
    public void onClick(View view) {
        switch (view.getId()) {
            case tv_check://支付
                if (chooseList().size() == 0) {
                    showShortToast("请选择商品");
                    return;
                }
                EventBus.getDefault().post(new ShopCartEvent(ShopCartEvent.JUMP_TO_NEXT));
                EventBus.getDefault().post(new ShopCartEvent(ShopCartEvent.JUMP_TO_CONFIRM,chooseList(), choosedNum() + "",String.valueOf(countTotalPrice()), getIds()));//传递数据给确认订单页面

//                //使用Bundle传递参数
//                Intent intentConfirmOrder = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("list", (Serializable) chooseList());
//                bundle.putString("sum", choosedNum() + "");
//                bundle.putString("totalPrice", String.valueOf(countTotalPrice()));
//                bundle.putStringArray("StrIds", getIds());
//                intentConfirmOrder.putExtras(bundle);
//                intentConfirmOrder.setClass(ShopCartListFragmentCopy.this, ConfirmOrderActivity.class);
//                startActivity(intentConfirmOrder);

                break;

        }
    }

    /**
     * 全选
     */
    private void chooseAll() {
        //全选中情况下
        if (isAllChecked()) {
            int size = myShopCartAdapter.getItemCount() - 1;
            for (int i = 0; i < size; i++) {
                mList.get(i).setChoose(false);
            }
            ivChooseAll.setSelected(false);
        } else {
            //全不选中情况下
            int size = myShopCartAdapter.getItemCount() - 1;
            for (int i = 0; i < size; i++) {
                mList.get(i).setChoose(true);
                // myShopCartAdapter.mCheckArr[i] = PARAM_CHECKED;
            }
            ivChooseAll.setSelected(true);
        }
        myShopCartAdapter.notifyDataSetChanged();
        flushFooterView();
    }


    private NextPageListener nextPageListener;

    public void setNextPageListener(NextPageListener nextPageListener) {
        this.nextPageListener = nextPageListener;
    }

    /**
     * Item是否全选中判断
     *
     * @return
     */
    private boolean isAllChecked() {
        int size = myShopCartAdapter.getItemCount() - 1;
        for (int i = 0; i < size; i++) {
            if (!mList.get(i).isChoose()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 每一项被点击
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        //点击的时候前面的勾选按钮选择态的设置

        if (mList.get(position).isChoose()) {
            mList.get(position).setChoose(false);
        } else {
            mList.get(position).setChoose(true);

        }
        //根据单个勾选改变全选按钮
        ivChooseAll.setSelected(isAllChecked());
        myShopCartAdapter.notifyDataSetChanged();
        flushFooterView();
    }


    //刷新底部布局
    private void flushFooterView() {
        tvChooseNum.setText(getResources().getString(R.string.shop_cart_num, choosedNum() + ""));
        tvTotalPrice.setText(getResources().getString(R.string.shop_cart_total_price, String.valueOf(countTotalPrice())));
    }

    /**
     * 被选中的数量
     * ,
     *
     * @return
     */
    private int choosedNum() {
        int size = myShopCartAdapter.getItemCount() - 1;
        int sum = 0;
        for (int i = 0; i < size; i++) {
            if (mList.get(i).isChoose()) {
                sum += 1;
            }
        }
        return sum;
    }

    /**
     * 选中的list
     */
    private List<ShopCartEntity.ListBean> chooseList() {
        List<ShopCartEntity.ListBean> list = new ArrayList<>(); // 选中的list
        int size = myShopCartAdapter.getItemCount() - 1;
        for (int i = 0; i < size; i++) {
            ShopCartEntity.ListBean info = mList.get(i);
            if (info != null) {
                if (info.isChoose()) {
                    list.add(info);
                }
            }
        }
        return list;
    }

    /**
     * 计算总价
     *
     * @return
     */
    private double countTotalPrice() {
        long sum = 0;
        int size = myShopCartAdapter.getItemCount() - 1;
        for (int i = 0; i < size; i++) {
            ShopCartEntity.ListBean info = mList.get(i);
            if (info != null) {
                if (info.isChoose()) {
                    String totalFee = null;
                    if (info.getRecharge() != null) {//不为空为阿思币充值页
                        totalFee = info.getRecharge().getPrice(); // 支付金额
                    } else {
                        totalFee = info.getCommodityPrice(); // 支付金额
                    }
                    // 订单总金额（单位是分，要除以100）
                    if (!TextUtils.isEmpty(totalFee)) {
                        long d = Long.parseLong(totalFee);
                        sum += d;
//                        try {
//                            double d = (new Integer(totalFee)).doubleValue();
//                            double price = d / 100;
//                        } catch (NumberFormatException e) {
//                        }
                    }
                }
            }

        }
        BigDecimal integral = new BigDecimal(String.valueOf(sum));
        double totalMoney = integral.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).doubleValue();//除以100，四舍五入保留两位小数
        return totalMoney;

    }


    private String[] getIds() {
        String[] ids = null;
        int size = myShopCartAdapter.getItemCount() - 1;
        ids = new String[size];
        for (int i = 0; i < size; i++) {
            ShopCartEntity.ListBean info = mList.get(i);
            if (info != null) {
                if (info.isChoose()) {
                    ids[i] = info.getId();
                }
            }

        }
        return ids;
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
            mPresenter.presenterDelShopCart(id, new ApiRequestListener<JSONObject>() {//题目的id和类型请求
                @Override
                public void onResultSuccess(JSONObject object) {
                    showShortToast(getResources().getString(R.string.success_del));
                    mList.remove(position - 1);
                    myShopCartAdapter.notifyItemRemoved(position);
                    if (mList.size() == 0) {//购物车数量为空
                        llNoData.setVisibility(View.VISIBLE);
                    } else {
                        llNoData.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
    public void onEventMainThread(BookInfo event) {

    }


}