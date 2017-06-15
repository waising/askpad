package com.asking.pad.app.ui.personalcenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.BuyRecordsEntity;
import com.asking.pad.app.ui.pay.PayActivity;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.asking.pad.app.R.id.tv_expiration_date;
import static com.asking.pad.app.commom.CommonUtil.openActivity;


/**
 * 购买记录adapter
 * create by libin
 */

public class MyBuyRecordsAdapter extends RecyclerView.Adapter<MyBuyRecordsAdapter.ZQViewHolder> {

    private List<BuyRecordsEntity.ListBean> list;
    private int subjectCatalog;
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

    public void setData(List<BuyRecordsEntity.ListBean> list, int subjectCatalog) {
        this.list = list;
        this.subjectCatalog = subjectCatalog;
    }

//    @Override
//    public View onCreateContentView(ViewGroup parent, int viewType) {
//        if (mContext == null)
//            mContext = parent.getContext();
//        return LayoutInflater.from(mContext).inflate(R.layout.item_mybuyrecords, parent, false);
//    }
//


//    @Override
//    public ZQViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
//        return new ZQViewHolder(realContentView);
//    }


    @Override
    public ZQViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null)
            mContext = parent.getContext();
        if (mHeaderView != null && viewType == TYPE_HEADER) {//如果是headerView布局，则返回头部布局
            return new ZQViewHolder(mHeaderView);
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mybuyrecords, null);//默认返回item布局
        return new ZQViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ZQViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {//如果是头部布局
            return;
        }
        final int realPosition = getRealPositon(holder);
        if (list != null && list.size() > 0) {
            final BuyRecordsEntity.ListBean buyRecordsEntity = list.get(realPosition);
            holder.setOnItemClickListener(mOnItemClickListener);
            final String payType = buyRecordsEntity.getPay_type(); // 支付类型

            // 我的课程&& 阿思币充值 我的服务
            String orderName = buyRecordsEntity.getOrder_name();
            List<BuyRecordsEntity.ListBean.ProductsBean> products = buyRecordsEntity.getProducts();
            String strDetail = "";
            if (products != null && products.size() > 0) {//如果product不为空
                for (int i = 0; i < products.size(); i++) {
//                    if (products.size() > 1)//购物车支付
//                    {
//
//
//                    } else {//立即支付的情况
                    if (products.get(i).getRecharge() == null) {// recharge 为空是课程、服务购买
                        String versionName = products.get(i).getVersionName(); // 教材版本
                        String months = products.get(i).getMonths(); // 月份
                        String courseContent = products.get(i).getCourseContent(); // 年级
                        String commodityType = products.get(i).getCommodityType();
                        if (!TextUtils.isEmpty(commodityType)) {
                            // commodityType 非空是课程、服务购买
                            if (!TextUtils.isEmpty(versionName) && !TextUtils.isEmpty(months) && !TextUtils.isEmpty(courseContent)) {
                                if (commodityType.equals("2")) {
                                    strDetail = strDetail + "课程购买 -" + "【" + versionName + " - " + months + "个月" + " - " + courseContent + "】" + "\n";
                                }
                                if (commodityType.equals("1")) {
                                    strDetail = strDetail + "服务购买 -" + "【" + versionName + " - " + months + "个月" + " - " + courseContent + "】" + "\n";
                                }
                            }
                        } else {
                            // commodityType 为空是知识点购买
                            if (!TextUtils.isEmpty(versionName) && !TextUtils.isEmpty(months) && !TextUtils.isEmpty(courseContent)) {
                                strDetail = strDetail + "知识点购买 -" + "【" + versionName + " - " + months + "个月" + " - " + courseContent + "】" + "\n";
                            }
                        }
                        holder.mTvExpirationDate.setVisibility(View.GONE);//到期时间可见,现在先隐藏掉，服务端说先隐藏掉

                    } else {
                        // recharge 非空是阿思币充值
                        if (products.size() == 1) {
                            orderName = mContext.getString(R.string.pay_ask_money); // 设置标题为阿思币充值
                        }
                        if (products.get(i).getRecharge() != null) {
                            int value = products.get(i).getRecharge().getValue();
                            strDetail = strDetail + "充值了 " + value + " 个阿思币" + "\n";
                        }

                        holder.mTvExpirationDate.setVisibility(View.GONE);//到期时间不可见
                    }
//
//                    }

                }
                if (!TextUtils.isEmpty(strDetail) && strDetail.length() > 2) {
                    strDetail = strDetail.substring(0, strDetail.length() - 1);
                }
                if (orderName != null) {
                    holder.mTvOrderName.setText(orderName); // 设置标题.初中数学，七年级这种
                } else {
                    holder.mTvOrderName.setText("");
                }
                holder.tvOrderContent.setText(strDetail);//充值了10个阿思币这种
            }
            String totalFee = buyRecordsEntity.getTotal_fee(); // 支付金额
            String state = buyRecordsEntity.getState(); // 支付状态
            // String modifyDateStr = buyRecordsEntity.getModifyDateStr(); // 生成订单的时间
            String createDateStr = buyRecordsEntity.getCreateDate_str(); // 下单的时间
            final String id = buyRecordsEntity.getId();
            // 订单总金额（单位是分，要除以100）
            if (!TextUtils.isEmpty(totalFee)) {
                try {
                    double d = (new Double(totalFee)).doubleValue();
                    double price = d / 100;
                    holder.mTvMoney.setText("¥ " + String.valueOf(price));
                } catch (NumberFormatException e) {
                }
            } else {
                holder.mTvMoney.setText("");
            }
/*            // 下单时间（先判断 modifyDate_str 的值是否为空，为空的话那么使用createDate_str ）
            if (modifyDateStr != null && !TextUtils.isEmpty(modifyDateStr)) {
                holder.mTvDate.setText(modifyDateStr);
            } else*/
            if (createDateStr != null) {
                holder.mTvDate.setText(createDateStr); // 下单时间
            } else {
                holder.mTvDate.setText("");
            }


            //订单状态: 0-待支付   1-待支付 , 2-已支付 , 3-订单失效(取消)  4-订单删除
            if (state != null) {
                if (state.equals("0") || state.equals("1")) {
                    holder.mTvOrderState.setText(R.string.to_be_pay);
                    holder.mTvUser.setText(R.string.pay_now);
                    holder.tvPay.setVisibility(View.VISIBLE);
                    holder.tvPay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("orderId", id);
                            bundle.putString("payType", payType);
//                            intent.putExtras(bundle);
                            //   AppContext.getInstance().startActivity(intent);
                            openActivity(PayActivity.class, bundle);
                            //    ((BuyRecordActivity) mContext).openPayActivity(bundle);
                        }
                    });
                }
                if (state.equals("2")) {//已支付
                    // 已支付
                    holder.mTvOrderState.setText(R.string.be_paid);
                    // 0-支付宝  1- 银联  2-微信
                    if (!TextUtils.isEmpty(payType)) {
                        switch (payType) {//支付方式
                            // 支付宝
                            case "0":
                                holder.mTvUser.setText(R.string.alipay_pay);
                                break;
                            // 银联
                            case "1":
                                holder.mTvUser.setText(R.string.unionpay_pay);
                                break;
                            // 微信
                            case "2":
                                holder.mTvUser.setText(R.string.wechatpay_pay);
                                break;
                        }
                    }
                    holder.tvPay.setVisibility(View.GONE);
                }
                if (state.equals("3")) {//订单失效(取消)

                    holder.mTvOrderState.setText(R.string.order_failed);
                    holder.mTvUser.setText("");
                    holder.tvPay.setVisibility(View.GONE);
                }
                if (state.equals("4")) { // 订单删除
                    holder.mTvOrderState.setText(R.string.order_del);
                    holder.mTvUser.setText("");
                    holder.tvPay.setVisibility(View.GONE);
                }
            } else {
                holder.mTvOrderState.setText("");
                holder.mTvUser.setText("");
                holder.tvPay.setVisibility(View.GONE);
            }


            holder.tvDel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (delListener != null) {
                        delListener.del(realPosition + 1, list.get(realPosition).getId(), subjectCatalog);//一个adapter中位置，一个list中的位置
                    }
                }
            });
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
     * 获取真实的position位置
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
        void del(int position, String id, int subjectCatalog);
    }

//    /**
//     * 到期时间
//     *
//     * @param createTime
//     * @param month
//     */
//    private String formatExpireDate(long createTime, int month) {
//        Date date = null;
//        if (month == 12) {
//            date = new Date(createTime + 365 * 24 * 60 * 60 * 1000);
//        } else if (month == 6) {
//            date = new Date(createTime + ((365 + 1) / 2) * 24 * 60 * 60 * 1000);
//        }
//        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
//        String sDateTime = sdf.format(date);//得到精确到秒的表示：08/31/2006 21:08:00
//        return sDateTime;
//    }


    class ZQViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_date)
        TextView mTvDate; // 日期
        @BindView(R.id.tv_order_name)
        TextView mTvOrderName; // 订单标题
        @BindView(R.id.tv_order_content)
        TextView tvOrderContent; // 订单内容
        @BindView(R.id.tv_order_state)
        TextView mTvOrderState; // 订单状态
        @BindView(R.id.tv_money)
        TextView mTvMoney; // 支付金额
        @BindView(R.id.tv_user)
        TextView mTvUser; // 支付方式
        @BindView(tv_expiration_date)
        TextView mTvExpirationDate; // 到期时间
        @BindView(R.id.tv_del)
        TextView tvDel;//删除按钮
        @BindView(R.id.tv_pay)
        TextView tvPay;//付款按钮

        @BindColor(R.color.colorAccent)
        int colorAccent;


        public ZQViewHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) return;
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }

        OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

    }
}
