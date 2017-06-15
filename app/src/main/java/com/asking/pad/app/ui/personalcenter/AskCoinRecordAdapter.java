package com.asking.pad.app.ui.personalcenter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.commom.DateUtil;
import com.asking.pad.app.entity.IntegralLog;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AskCoinRecordAdapter extends RecyclerView.Adapter<AskCoinRecordAdapter.CommViewHolder> {

    private List<IntegralLog> dataList;

    public AskCoinRecordAdapter(List<IntegralLog> dataList){
        this.dataList = dataList;
    }

    @Override
    public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_askcoin_record, null);//默认返回item布局
        return new CommViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CommViewHolder holder, int position) {
        IntegralLog e = dataList.get(position);
        try{
            holder.tv_date.setText(DateUtil.formatDatetime(new Date(e.getCreateTime())));
        }catch (Exception ex){}
        holder.tv_state.setText("");
        holder.tv_integral.setText(e.getIntegral());
        holder.tv_remark.setText(e.getRemark());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class CommViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date)
        TextView tv_date;

        @BindView(R.id.tv_state)
        TextView tv_state;

        @BindView(R.id.tv_integral)
        TextView tv_integral;

        @BindView(R.id.tv_remark)
        TextView tv_remark;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
