package com.asking.pad.app.ui.sharespace;

import android.content.Context;
import android.nfc.cardemulation.HostApduService;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.commom.DateUtil;
import com.asking.pad.app.entity.QuestionEntity;
import com.asking.pad.app.widget.AskMathView;
import com.asking.pad.app.widget.AskSimpleDraweeView;
import com.asking.pad.app.widget.MultiStateView;
import com.asking.pad.app.widget.StarView;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 题目的adpater
 * create by linbin
 */

public class QuestionsAdapter extends SwipeMenuAdapter<QuestionsAdapter.ViewHolder> {


    private Context mContext;

    List<QuestionEntity> questionEntities;

    public QuestionsAdapter(Context context, List<QuestionEntity> questionEntities) {
        this.mContext = context;
        this.questionEntities = questionEntities;
    }

    public void setData(List<QuestionEntity> commentEntity) {
        this.questionEntities = commentEntity;
        notifyDataSetChanged();
    }

    public void removeCommentEntityItem(int index) {
        questionEntities.remove(index);
        notifyItemRemoved(index);
    }

    public String getCommentEntityItemId(int index) {
        return questionEntities.get(index).getId();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
    }

    @Override
    public ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new ViewHolder(realContentView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        QuestionEntity questionEntity = questionEntities.get(position);
        if(questionEntity!=null){
            holder.userNameTv.setText(questionEntity.getUserName());
            holder.questionTitleTv.setText(questionEntity.getTitle());
            //holder.questionImgIv.setImageURI();
            holder.mathView.setText(questionEntity.getDescription());
            holder.kmTv.setText(questionEntity.getKm());

            int m = DateUtil.getMinutes(questionEntity.getCreateDate_Fmt(),DateUtil.currentDatetime());
            String time = questionEntity.getCreateDate_Fmt();
            if(0< m &&m <10){
                time = "刚刚";
            }else if(10< m && m <120){
                time = "10分钟前";
            }
            else if(120< m && m <150){
                time = "2小时前";
            }else if(20*60<m && m<24*60)
                time = "20小时前";

            holder.timeTv.setText(time);
            holder.askQuestionCountTv.setText(questionEntity.getAnswer_size()+" 人回答");

            if(questionEntity.getCaifu()>0) {

                holder.askMoneyTv.setVisibility(View.VISIBLE);
                holder.askIc.setVisibility(View.VISIBLE);
                holder.askMoneyTv.setText(String.valueOf(questionEntity.getCaifu()));
            }

//            holder.mathView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
        }

    }

    @Override
    public int getItemCount() {
        return questionEntities.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ask_money_tv)
        TextView askMoneyTv;

        @BindView(R.id.user_name)
        TextView userNameTv;

        @BindView(R.id.question_title_tv)
        TextView questionTitleTv;

        @BindView(R.id.km_tv)
        TextView kmTv;

        @BindView(R.id.time_tv)
        TextView timeTv;

        @BindView(R.id.ask_question_count)
        TextView askQuestionCountTv;

        @BindView(R.id.ask_ic)
        ImageView askIc;
//        @BindView(R.id.question_img_iv)
//        ImageView questionImgIv;

        @BindView(R.id.user_img_iv)
        AskSimpleDraweeView userImgIv;

//        @BindView(R.id.multiStateView)
//        MultiStateView multiStateView;

        @BindView(R.id.mathView)
        AskMathView mathView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mathView.showWebImage(mContext).formatMath();
        }

        @OnClick({R.id.question_title_tv,R.id.question_ll})
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.question_title_tv:
                case R.id.question_ll:

                    //跳转
                    break;

            }
        }

    }
}
