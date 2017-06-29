package com.asking.pad.app.ui.pay;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.pay.GradePay;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wxwang on 2016/10/27.
 */

public class PayGradeAdapter extends RecyclerView.Adapter<PayGradeAdapter.CommViewHolder> {

    private List<GradePay> mDatas;
    private Context mContext;

    OnCommItemListener mListener;

    public PayGradeAdapter(Context context, List<GradePay> datas, OnCommItemListener mListener){
        this.mContext = context;
        this.mDatas = datas;
        this.mListener =mListener;
    }

    @Override
    public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_comm_ask_view,parent,false));
    }

    @Override
    public void onBindViewHolder(final CommViewHolder holder, final int position) {
        final GradePay mGradePay = mDatas.get(position);
        holder.itemView.setSelected(mGradePay.isSelect);
        holder.item_name.setText(mGradePay.packageName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (GradePay ii : mDatas) {
                    ii.isSelect = false;
                }
                mGradePay.isSelect = true;
                notifyDataSetChanged();
                mListener.OnCommItem(mGradePay);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    class CommViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_name)
        TextView item_name;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

