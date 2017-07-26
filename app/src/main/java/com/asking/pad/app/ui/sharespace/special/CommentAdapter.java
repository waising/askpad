package com.asking.pad.app.ui.sharespace.special;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.commom.DateUtil;
import com.asking.pad.app.entity.sharespace.SpecialComment;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.asking.pad.app.widget.AskMathView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/7/18.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommViewHolder> {
    ArrayList<SpecialComment> dataList = new ArrayList<>();

    Activity mActivity;

    public OnItemCommentListener mListener;

    public interface OnItemCommentListener {
        void OnItemComment(SpecialComment e);
    }

    public CommentAdapter(Activity mActivity, ArrayList<SpecialComment> dataList, OnItemCommentListener mListener) {
        this.mActivity = mActivity;
        this.dataList = dataList;
        this.mListener = mListener;
    }

    @Override
    public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_share_special_comment_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final CommViewHolder holder, final int position) {
        final SpecialComment e = dataList.get(position);

        BitmapUtil.displayUserImage(mActivity,e.getAvatarUrl(), holder.iv_avatar);
        holder.tv_time.setText(DateUtil.friendly_time(DateUtil.getDateToString(e.createDate)));
        holder.tv_name.setText(e.getNickName());
        holder.content_mathview.setText(e.message);

        holder.tv_reply.setText(e.answerCount > 0 ? String.format("%s  回复", e.answerCount) : "");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.OnItemComment(e);
                }
            }
        });
        holder.content_mathview.setOnAskMathClickListener(new AskMathView.OnAskMathClickListener() {
            @Override
            public void OnClick() {
                if (mListener != null) {
                    mListener.OnItemComment(e);
                }
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

        @BindView(R.id.tv_reply)
        TextView tv_reply;

        @BindView(R.id.content_mathview)
        AskMathView content_mathview;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}