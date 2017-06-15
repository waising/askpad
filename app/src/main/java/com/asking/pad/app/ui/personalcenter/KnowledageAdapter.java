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
 * 知识点adpater
 * create by linbin
 */

public class KnowledageAdapter extends SwipeMenuAdapter<KnowledageAdapter.ViewHolder> {

    private Context mContext;

    List<CollectionEntity.ListBean> commentEntity;//收藏的知识点和资讯

    private DelListener delListener;

    public KnowledageAdapter(Context context, List<CollectionEntity.ListBean> commentEntity, DelListener delListener) {
        this.mContext = context;
        this.commentEntity = commentEntity;
        this.delListener = delListener;
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
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_knowledge_list, parent, false);
    }

    @Override
    public ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new ViewHolder(realContentView);
    }


    @Override
    public void onBindViewHolder(KnowledageAdapter.ViewHolder holder, final int position) {
        if (commentEntity != null && commentEntity.size() > 0) {
            // 知识点
            CollectionEntity.ListBean item = commentEntity.get(position);
            final String tille = item.getTille(); // 标题
            if (tille != null) {
                holder.mtvTitle.setText(tille);
            } else {
                holder.mtvTitle.setText("");
            }


            holder.imgDel.setOnClickListener(new View.OnClickListener() {//跳转修改笔记页面
                @Override
                public void onClick(View v) {
                    if (delListener != null) {
                        delListener.del(position, getCommentEntityItemId(position));
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


        @BindView(R.id.del)
        ImageView imgDel;

        @BindView(R.id.tv_title)//标题
                TextView mtvTitle;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
