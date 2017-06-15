package com.asking.pad.app.ui.superclass.classify.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.superclass.StudyClassVersion;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/5/25.
 */

public class VersionAdapter extends RecyclerView.Adapter<VersionAdapter.CommViewHolder> {

    private List<StudyClassVersion> mDatas;
    private Context mContext;

    public VersionAdapter(Context context, List<StudyClassVersion> datas, OnCommItemListener mListener){
        this.mContext = context;
        this.mDatas = datas;
        this.mListener = mListener;
    }

    @Override
    public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_supertutorial_classify,parent,false));
    }

    @Override
    public void onBindViewHolder(CommViewHolder holder, int position) {
        final StudyClassVersion e = mDatas.get(position);
        holder.item_name.setSelected(e.isSelect);
        holder.item_name.setText(e.getVersionName());
        holder.item_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (StudyClassVersion ii : mDatas) {
                    ii.isSelect = false;
                }
                e.isSelect = true;
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

    public interface OnCommItemListener{
        void OnCommItem(StudyClassVersion e);
    }

    OnCommItemListener mListener;

}

