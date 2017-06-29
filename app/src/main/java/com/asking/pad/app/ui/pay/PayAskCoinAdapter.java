package com.asking.pad.app.ui.pay;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.pay.AskCoinPay;
import com.asking.pad.app.ui.classmedia.PayClassMediaActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * 阿思币充值adapter
 * Created by wxwang on 2016/10/27.
 */

public class PayAskCoinAdapter extends RecyclerView.Adapter<PayAskCoinAdapter.CommViewHolder> {

    private List<AskCoinPay> mDatas;
    private Activity mActivity;

    public PayAskCoinAdapter(Activity mActivity, List<AskCoinPay> datas) {
        this.mActivity = mActivity;
        this.mDatas = datas;
    }

    @Override
    public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_pay_ask_money_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final CommViewHolder holder, final int position) {
        final AskCoinPay e = mDatas.get(position);

        holder.mAskMoneyTv.setText(String.valueOf(e.value));
        holder.mMoneyTv.setText(String.valueOf(e.getAskCoinPrice()+"元"));
        holder.mCheckedImg.setVisibility(e.isSelect ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayClassMediaActivity.openActivity(mActivity,e);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    class CommViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.checked_img)
        ImageView mCheckedImg;

        @BindView(R.id.ask_money_tv)
        TextView mAskMoneyTv;

        @BindView(R.id.money_tv)
        TextView mMoneyTv;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}