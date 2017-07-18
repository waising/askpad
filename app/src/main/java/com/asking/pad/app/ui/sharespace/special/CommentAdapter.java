package com.asking.pad.app.ui.sharespace.special;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.sharespace.ShareSpecial;
import com.asking.pad.app.widget.AskMathView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/7/18.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommViewHolder> {
    ArrayList<ShareSpecial> dataList = new ArrayList<>();

    Activity mActivity;

    public CommentAdapter(Activity mActivity,ArrayList<ShareSpecial> dataList){
        this.mActivity = mActivity;
        this.dataList = dataList;
    }

    @Override
    public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_share_special_comment_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final CommViewHolder holder, final int position) {
        final ShareSpecial e = dataList.get(position);

        //BitmapUtil.displayCirImage(e.getTeacherImgUrl(), holder.tea_avatar);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpecialDetailActivity.openActivity("");
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class CommViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView iv_avatar;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_time)
        TextView tv_time;

        @BindView(R.id.content_mathview)
        AskMathView content_mathview;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}