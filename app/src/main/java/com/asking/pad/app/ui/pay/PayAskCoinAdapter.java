package com.asking.pad.app.ui.pay;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.AskMoneyEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * 阿思币充值adapter
 * Created by wxwang on 2016/10/27.
 */

public class PayAskCoinAdapter extends RecyclerView.Adapter<PayAskCoinAdapter.CommViewHolder> {

    private List<AskMoneyEntity> mDatas;
    private Context mContext;

    OnCommItemListener mListener;

    public PayAskCoinAdapter(Context context, List<AskMoneyEntity> datas,OnCommItemListener mListener) {
        this.mContext = context;
        this.mDatas = datas;
        this.mListener = mListener;
    }

    @Override
    public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_pay_ask_money_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final CommViewHolder holder, final int position) {
        final AskMoneyEntity askMoneyEntity = mDatas.get(position);

        //金币和rmb都除以10
        holder.mAskMoneyTv.setText(String.valueOf(askMoneyEntity.getPrice() / 10));
        holder.mMoneyTv.setText(String.valueOf(Integer.parseInt(askMoneyEntity.getMoney()) / 10)+"元");
        holder.mMoneyIdTv.setText(askMoneyEntity.getId());

        holder.mCheckedImg.setVisibility(askMoneyEntity.isSelect ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(AskMoneyEntity askMoneyEntity:mDatas){
                    askMoneyEntity.isSelect = false;
                }
                askMoneyEntity.isSelect = true;
                notifyDataSetChanged();
                mListener.OnCommItem(askMoneyEntity);
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

        @BindView(R.id.ask_money_id_tv)
        TextView mMoneyIdTv;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}