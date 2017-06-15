package com.asking.pad.app.ui.pay;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.LabelEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/4/21.
 */

public class CommAdapter extends RecyclerView.Adapter<CommAdapter.CommViewHolder> {
    private List<LabelEntity> mDatas;
    private Context mContext;

    OnCommItemListener mListener;

    public CommAdapter(Context context,List<LabelEntity> datas,OnCommItemListener mListener){
        this.mContext = context;
        this.mDatas = datas;
        this.mListener = mListener;
    }

    @Override
    public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_comm_ask_view,parent,false));
    }

    @Override
    public void onBindViewHolder(CommViewHolder holder,final int position) {
        final LabelEntity e = mDatas.get(position);
        holder.itemView.setSelected(e.getSelect());
        holder.item_name.setText(e.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (LabelEntity ii : mDatas) {
                    ii.setSelect(false);
                }
                e.setSelect(true);
                notifyDataSetChanged();
                mListener.OnCommItem(e);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
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


