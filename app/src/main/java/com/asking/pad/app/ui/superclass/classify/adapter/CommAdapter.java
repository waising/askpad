package com.asking.pad.app.ui.superclass.classify.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.LabelEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/4/11.
 */


public class CommAdapter extends RecyclerView.Adapter<CommAdapter.CommViewHolder> {

    private List<LabelEntity> mDatas;
    private Context mContext;

    private int orientation;

    public CommAdapter(Context context,int orientation, List<LabelEntity> datas,OnCommItemListener mListener){
        this.mContext = context;
        this.orientation = orientation;
        this.mDatas = datas;
        this.mListener = mListener;
    }

    @Override
    public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_supertutorial_classify,parent,false),orientation);
    }

    @Override
    public void onBindViewHolder(CommViewHolder holder, int position) {
        final LabelEntity e = mDatas.get(position);
        holder.item_name.setSelected(e.getSelect());
        holder.item_name.setText(e.getName());
        holder.item_name.setOnClickListener(new View.OnClickListener() {
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

        @BindView(R.id.ll_root)
        LinearLayout ll_root;

        public CommViewHolder(View itemView,int orientation) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            if(LinearLayoutManager.HORIZONTAL == orientation){
                ll_root.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
        }
    }

    public interface OnCommItemListener{
        void OnCommItem(LabelEntity e);
    }

    OnCommItemListener mListener;

}