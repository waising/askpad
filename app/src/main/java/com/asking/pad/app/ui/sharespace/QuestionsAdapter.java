package com.asking.pad.app.ui.sharespace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.DateUtil;
import com.asking.pad.app.entity.QuestionEntity;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.asking.pad.app.widget.AskMathView;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * 题目的adpater
 * create by linbin
 */

public class QuestionsAdapter extends SwipeMenuAdapter<QuestionsAdapter.ViewHolder> {

    public void setMine(boolean mine) {
        isMine = mine;
    }

    //是否我的
    private boolean isMine = false;

    private Context mContext;

    int dataType;

    List<QuestionEntity> questionEntities;

    public QuestionsAdapter(Context context, List<QuestionEntity> questionEntities) {
        this.mContext = context;
        this.questionEntities = questionEntities;
    }

    public QuestionsAdapter(Context context, int dataType, List<QuestionEntity> questionEntities) {
        this.mContext = context;
        this.dataType = dataType;
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
        if (questionEntity != null) {
            if(TextUtils.isEmpty(questionEntity.getUserName())){
                holder.userNameTv.setText(AppContext.getInstance().getUserName());
            }else{
                holder.userNameTv.setText(questionEntity.getUserName());
            }
            holder.questionTitleTv.setText(questionEntity.getTitle());
            holder.mathView.setText(16,questionEntity.getDescription());
            holder.kmTv.setText(getSubjectName(questionEntity.getKm()) + " - " + getGradleName(questionEntity.getLevelId()));
            BitmapUtil.displayCirImage(questionEntity.getUserAvatar(), R.dimen.space_40, holder.userImgIv);
            int m = DateUtil.getMinutes(questionEntity.getCreateDate_Fmt(), DateUtil.currentDatetime());
            String time = questionEntity.getCreateDate_Fmt();
            if (0 < m && m < 10) {
                time = "刚刚";
            } else if (10 < m && m < 120) {
                time = "10分钟前";
            } else if (120 < m && m < 150) {
                time = "2小时前";
            } else if (20 * 60 < m && m < 24 * 60)
                time = "20小时前";

            holder.timeTv.setText(time);
            holder.askQuestionCountTv.setText(questionEntity.getAnswer_size()+"");
            if (questionEntity.getCaifu() > 0) {
                holder.askMoneyTv.setVisibility(View.VISIBLE);
                holder.askIc.setVisibility(View.VISIBLE);
                holder.askMoneyTv.setText(String.valueOf(questionEntity.getCaifu()));
            } else {
                holder.askMoneyTv.setVisibility(View.INVISIBLE);
                holder.askIc.setVisibility(View.INVISIBLE);
            }
            try {
                holder.questionStatusTv.setVisibility(View.VISIBLE);
                holder.questionsure.setVisibility(View.GONE);
                int color = R.color.black;
                String status = "";
                if (questionEntity.getAnswer_size() <= 0) {
                    status = "待回答";
                    color = R.color.green;
                } else if (!TextUtils.equals(questionEntity.getState(), "2")) {
                    status = "待采纳";
                    color = R.color.theme_blue_theme;
                } else if (TextUtils.equals(questionEntity.getState(), "2")) {
                    status = "已采纳";
                    color = R.color.orange;
                    holder.questionsure.setVisibility(View.VISIBLE);
                }
                if (isMine) {
                    holder.userNameTv.setVisibility(View.GONE);
                    holder.userImgIv.setVisibility(View.GONE);
                    holder.questionStatusTv.setText(status);
                    holder.questionStatusTv.setTextColor(ContextCompat.getColor(mContext, color));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    String getGradleName(String levelId) {
        try {
            if (!TextUtils.isEmpty(levelId)) {
                int integerId = Integer.valueOf(levelId); // 包装类 Integer 不能直接运算(下面的减1)，会报错，得转成基本数据类型 int
                if (integerId > 0) { //要再判断下
                    String gradeVersionValue = Constants.gradeVersionValues[integerId - 1];
                    if (!TextUtils.isEmpty(gradeVersionValue)) {
                        return gradeVersionValue;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return questionEntities.size();

    }

    public String getSubjectName(String subject) {
        return TextUtils.equals(subject, "M") ? "数学" : "物理";
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

        @BindView(R.id.question_status_tv)
        TextView questionStatusTv;

        @BindView(R.id.ask_ic)
        ImageView askIc;

        @BindView(R.id.question_sure)
        ImageView questionsure;

        @BindView(R.id.user_img_iv)
        ImageView userImgIv;

        @BindView(R.id.mathView)
        AskMathView mathView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mathView.showWebImage(mContext).formatMath();
        }

        @OnClick({R.id.question_title_tv, R.id.question_ll})
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.question_title_tv:
                case R.id.question_ll:
                    QuestionEntity q = questionEntities.get(getAdapterPosition());
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("questionEntity", q);
                    bundle.putInt("dataType", dataType);
                    bundle.putString("km", getSubjectName(q.getKm()) + " - " + getGradleName(q.getLevelId()));
                    Intent intent = new Intent(AppContext.getInstance().getApplicationContext(), QuestionAnwserActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtras(bundle);
                    AppContext.getInstance().startActivity(intent);
                    break;

            }
        }

    }
}
