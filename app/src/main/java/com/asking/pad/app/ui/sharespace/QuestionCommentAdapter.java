package com.asking.pad.app.ui.sharespace;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.QuestionEntity;
import com.asking.pad.app.widget.AskMathView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/7/18.
 */

public class QuestionCommentAdapter extends RecyclerView.Adapter<QuestionCommentAdapter.CommViewHolder> {
    ArrayList<QuestionEntity.AnwserMoreEntity> dataList = new ArrayList<>();

    Context mContext;

    public QuestionCommentAdapter(Activity mActivity, ArrayList<QuestionEntity.AnwserMoreEntity> dataList){
        this.mContext = mActivity;
        this.dataList = dataList;
    }

    @Override
    public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_question_comment_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final CommViewHolder holder, final int position) {
        final QuestionEntity.AnwserMoreEntity e = dataList.get(position);

        try{
            if(e!=null){
                holder.tv_name.setText(e.getUserName());
                holder.tv_time.setText(e.getCreateDate_fmt());
                if(e.getList()!=null && e.getList().size()>0)
                    holder.content_mathview.setText(e.getList().get(0).getAnswer());
                else
                    holder.content_mathview.setText(e.getContent());
            }

        }catch (Exception ex){

        }

        //BitmapUtil.displayCirImage(e.getTeacherImgUrl(), holder.tea_avatar);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
            content_mathview.showWebImage(mContext).formatMath();
        }
    }
}