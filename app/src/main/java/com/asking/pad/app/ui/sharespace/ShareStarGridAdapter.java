package com.asking.pad.app.ui.sharespace;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.CandidateEntity;
import com.asking.pad.app.widget.AskSimpleDraweeView;

import java.util.ArrayList;

import static com.asking.pad.app.R.id.iv_rank;


public class ShareStarGridAdapter extends RecyclerView.Adapter<ShareStarGridAdapter.ViewHolder> {


    private ArrayList<CandidateEntity> mList;



    private Context mContext;

    public ShareStarGridAdapter(Context context, ArrayList<CandidateEntity> list) {
        this.mContext = context;
        this.mList = list;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * 图片
         */
        ImageView ivRank;
        /**
         * 排名数
         */
        TextView tvRankNum;


        AskSimpleDraweeView draweeViewRankAvator;

        TextView tvRankName;
        /**
         * 被采纳数
         */
        TextView tvAcceptNum;


        public ViewHolder(View itemView) {
            super(itemView);
            ivRank = (ImageView) itemView.findViewById(iv_rank);
            tvRankNum = (TextView) itemView.findViewById(R.id.tv_rank_num);
            draweeViewRankAvator = (AskSimpleDraweeView) itemView.findViewById(R.id.rank_avatar);
            tvRankName = (TextView) itemView.findViewById(R.id.tv_rank_name);
            tvAcceptNum = (TextView) itemView.findViewById(R.id.tv_accept_num);

        }
    }



    @Override
    public void onBindViewHolder(ShareStarGridAdapter.ViewHolder holder, final int position) {
        CandidateEntity candidateEntity = mList.get(position);
        if (candidateEntity != null) {
            if (position == 0) {//第一名
                holder.ivRank.setImageResource(R.mipmap.ic_share_num_1);
                holder.tvRankNum.setVisibility(View.GONE);
            } else if (position == 1) {//第二名
                holder.ivRank.setImageResource(R.mipmap.ic_share_num_2);
                holder.tvRankNum.setVisibility(View.GONE);
            } else if (position == 2) {//第三名
                holder.ivRank.setImageResource(R.mipmap.ic_share_num_3);
                holder.tvRankNum.setVisibility(View.GONE);
            } else {
                holder.ivRank.setImageResource(R.mipmap.ic_share_nums);
                holder.tvRankNum.setVisibility(View.VISIBLE);
                holder.tvRankNum.setText(position);
            }
            holder.tvAcceptNum.setText(candidateEntity.getAcceptNum());
            holder.tvRankName.setText(candidateEntity.getRankName());

        }


    }

    @Override
    public ShareStarGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShareStarGridAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_share_star_grid_view, parent, false));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public Object getItem(int position) {
        return mList.get(position);
    }
}