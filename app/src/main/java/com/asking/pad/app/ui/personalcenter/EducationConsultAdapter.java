package com.asking.pad.app.ui.personalcenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.CollectionEntity;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 教育咨询adpater
 * create by linbin
 */

public class EducationConsultAdapter extends SwipeMenuAdapter<EducationConsultAdapter.ViewHolder> {

    private Context mContext;

    List<CollectionEntity.ListBean> commentEntity;//收藏的知识点和资讯
    private DelListener mDelListner;

    public EducationConsultAdapter(Context context, List<CollectionEntity.ListBean> commentEntity, DelListener delListner) {
        this.mContext = context;
        this.commentEntity = commentEntity;
        this.mDelListner = delListner;
    }

    public void setData(List<CollectionEntity.ListBean> commentEntity) {
        this.commentEntity = commentEntity;
        notifyDataSetChanged();
    }

    public void removeCommentEntityItem(int index) {
        commentEntity.remove(index);
        notifyItemRemoved(index);
    }

    public String getCommentEntityItemId(int index) {
        return commentEntity.get(index).getId();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_education_consult_list, parent, false);
    }

    @Override
    public ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new ViewHolder(realContentView);
    }


    @Override
    public void onBindViewHolder(EducationConsultAdapter.ViewHolder holder, final int position) {
        if (commentEntity != null && commentEntity.size() > 0) {
            CollectionEntity.ListBean itemRefer = commentEntity.get(position);
            final String tilleRefer = itemRefer.getTille(); // 标题
            if (tilleRefer != null) {//标题
                holder.mtvTitle.setText(tilleRefer);
            } else {
                holder.mtvTitle.setText("");
            }

            holder.imgDel.setOnClickListener(new View.OnClickListener() {//跳转修改笔记页面
                @Override
                public void onClick(View v) {

                    if (mDelListner != null) {
                        mDelListner.del(position, getCommentEntityItemId(position));
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return commentEntity.size();  //　收藏的知识点和资讯
    }


    class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.iv_del)
        ImageView imgDel;

        @BindView(R.id.tv_title)//标题
                TextView mtvTitle;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }


}
