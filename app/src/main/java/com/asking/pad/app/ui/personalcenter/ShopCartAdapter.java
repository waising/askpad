package com.asking.pad.app.ui.personalcenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.ShopCartEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 购物车列表adapter
 * create by libin
 */

public class ShopCartAdapter extends RecyclerView.Adapter<ShopCartAdapter.ZQViewHolder> {

    private List<ShopCartEntity.ListBean> list;
    private Context mContext;
    /**
     * headerView
     */
    private View mHeaderView;
    private OnItemClickListener mOnItemClickListener;
    /**
     * 头部布局
     */
    public static final int TYPE_HEADER = 0;

    /**
     * 正常布局
     */
    public static final int TYPE_NORMAL = 1;


    private RecordDelListner delListener;


    public void setDelClickListener(RecordDelListner delListener) {
        this.delListener = delListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setData(List<ShopCartEntity.ListBean> list) {
        this.list = list;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            list.get(i).setChoose(true);
        }
        notifyDataSetChanged();
    }

    @Override
    public ZQViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null)
            mContext = parent.getContext();
        if (mHeaderView != null && viewType == TYPE_HEADER) {//如果是headerView布局，则返回头部布局
            return new ZQViewHolder(mHeaderView);
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopcart, null);//默认返回item布局
        return new ZQViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ZQViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {//如果是头部布局
            return;
        }
        final int realPosition = getRealPositon(holder);
        if (list != null && list.size() > 0) {
            final ShopCartEntity.ListBean buyRecordsEntity = list.get(realPosition);
            if (buyRecordsEntity != null) {
                String totalFee = null;

                if (buyRecordsEntity.getRecharge() != null)//不为空为阿思币充值页
                {
                    holder.tvClassName.setText(mContext.getString(R.string.pay_ask_money));// 设置标题为阿思币充值
                    holder.tvClassVersion.setText("【充值了 " + buyRecordsEntity.getRecharge().getValue() + " 个阿思币】");
                    totalFee = buyRecordsEntity.getRecharge().getPrice(); // 支付金额
                } else {//课程充值页
                    holder.tvClassName.setText(buyRecordsEntity.getCommodityName());
                    holder.tvClassVersion.setText(mContext.getString(R.string.shop_cart_format, buyRecordsEntity.getVersionName(), buyRecordsEntity.getMonths(), buyRecordsEntity.getCourseContent()));
                    totalFee = buyRecordsEntity.getCommodityPrice(); // 支付金额
                }
                // 订单总金额（单位是分，要除以100）
                if (!TextUtils.isEmpty(totalFee)) {
                    try {
                        double d = (new Double(totalFee)).doubleValue();
                        double price = d / 100;
                        holder.tvMoney.setText("¥ " + String.valueOf(price));
                    } catch (NumberFormatException e) {
                    }
                } else {
                    holder.tvMoney.setText("");
                }


                holder.ivChoose.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(realPosition);
                        }
                    }
                });

                holder.ivDel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (delListener != null) {
                            delListener.del(realPosition + 1, buyRecordsEntity.getId());//一个是adapter中的位置，一个是list中的位置
                        }
                    }
                });

            }

            if (buyRecordsEntity.isChoose())//选中
            {
                holder.ivChoose.setSelected(true);

            } else {
                holder.ivChoose.setSelected(false);
            }

        }

    }

    /**
     * 添加headerView
     *
     * @param headView
     */
    public void setHeaderView(View headView) {
        mHeaderView = headView;
        notifyItemInserted(0);//刷新0position的view
    }

    /**
     * 获取headerView
     *
     * @return
     */
    public View getHeaderView() {
        return mHeaderView;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? list.size() : list.size() + 1;
    }

    /**
     * 获取真实的position位置,在list中的位置
     *
     * @return
     */
    public int getRealPositon(RecyclerView.ViewHolder holder) {

        int position = holder.getLayoutPosition();

        return mHeaderView == null ? position : position - 1;//如果header为空，则为pisition,
    }

    /**
     * 获取View类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) {//如果头部布局为空，返回item布局
            return TYPE_NORMAL;
        }
        if (position == 0) {//如果是第一项，返回header布局
            return TYPE_HEADER;
        }

        return TYPE_NORMAL;
    }


    interface RecordDelListner {
        void del(int position, String id);
    }


    class ZQViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.iv_choose)
        ImageView ivChoose;
        @BindView(R.id.tv_class_name)
        TextView tvClassName;
        @BindView(R.id.tv_class_version)
        TextView tvClassVersion;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.iv_del)
        ImageView ivDel;
        @BindView(R.id.item_layout)
        LinearLayout itemLayout;

        public ZQViewHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) return;
            ButterKnife.bind(this, itemView);
        }


    }
}
