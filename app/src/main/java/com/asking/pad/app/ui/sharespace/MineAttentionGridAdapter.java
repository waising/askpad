package com.asking.pad.app.ui.sharespace;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.sharespace.MyAttention;
import com.asking.pad.app.widget.AskSimpleDraweeView;

import java.util.List;


/**
 * 我的关注
 */

public class MineAttentionGridAdapter extends RecyclerView.Adapter<MineAttentionGridAdapter.ViewHolder> {


    private List<MyAttention> mList;


    private Context mContext;
    //颜色
    private ForegroundColorSpan colorSpan;
    //连接字符串
    private SpannableStringBuilder mSpannable = new SpannableStringBuilder();

    public MineAttentionGridAdapter(Context context, List<MyAttention> list) {
        this.mContext = context;
        this.mList = list;
        colorSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.light_red));
    }

    public void setData(List<MyAttention> list) {
        this.mList = list;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * 课程名
         */
        TextView tvClassName;

        /**
         * 头像
         */
        AskSimpleDraweeView draweeViewRankAvator;
        /**
         *
         */
        TextView tvTeacherName;
        /**
         *
         */
        TextView tvSubjectNum;


        ImageView ivAttention;

        public ViewHolder(View itemView) {
            super(itemView);
            tvClassName = (TextView) itemView.findViewById(R.id.tv_class);
            draweeViewRankAvator = (AskSimpleDraweeView) itemView.findViewById(R.id.rank_avatar);
            tvTeacherName = (TextView) itemView.findViewById(R.id.tv_teacher_name);
            tvSubjectNum = (TextView) itemView.findViewById(R.id.tv_subject_num);
            ivAttention = (ImageView) itemView.findViewById(R.id.iv_attention);
        }
    }


    @Override
    public void onBindViewHolder(MineAttentionGridAdapter.ViewHolder holder, final int position) {
        MyAttention mySpace = mList.get(position);
        if (mySpace != null) {
            MyAttention.AskInfoBean askInfoBean = mySpace.getAskInfo();
            if (askInfoBean != null) {
                holder.tvClassName.setText(askInfoBean.getSubject());
                holder.tvTeacherName.setText(askInfoBean.getNickName());
                holder.draweeViewRankAvator.setImageUrl(askInfoBean.getAvatar());
                holder.tvSubjectNum.setText(toSubjectNumString(R.string.subject_num, mySpace.getTopicCount() + ""));
                holder.ivAttention.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            v.setSelected(!v.isSelected());
                            mListener.onClick(position, v);
                        }
                    }
                });

            }


        }

    }

    @Override
    public MineAttentionGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MineAttentionGridAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_my_attention_view, parent, false));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public Object getItem(int position) {
        return mList.get(position);
    }


    private OnClickListener mListener;


    public void setOnClickListner(OnClickListener onClickListner) {
        this.mListener = onClickListner;
    }

    interface OnClickListener {
        void onClick(int position, View view);
    }

    private SpannableStringBuilder toSubjectNumString(int resColor, String colorStr) {
        mSpannable.clear();
        String asColor = mContext.getString(resColor, colorStr);
        mSpannable.append(asColor);
        mSpannable.setSpan(colorSpan, 0, asColor.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return mSpannable;
    }

}