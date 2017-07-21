package com.asking.pad.app.ui.sharespace;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.commom.DateUtil;
import com.asking.pad.app.entity.QuestionEntity;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.asking.pad.app.widget.AskMathView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/7/18.
 */

public class QuestionReplyCommentAdapter extends RecyclerView.Adapter<QuestionReplyCommentAdapter.CommViewHolder> {
    ArrayList<QuestionEntity.AnswerDetail> dataList = new ArrayList<>();

    public void setAnwserMoreEntity(QuestionEntity.AnwserMoreEntity anwserMoreEntity) {
        this.anwserMoreEntity = anwserMoreEntity;
    }

    public void setQuestionEntity(QuestionEntity questionEntity) {
        this.questionEntity = questionEntity;
    }

    QuestionEntity questionEntity ;
    QuestionEntity.AnwserMoreEntity anwserMoreEntity;

    Context mContext;

    public QuestionReplyCommentAdapter(Activity mActivity, ArrayList<QuestionEntity.AnswerDetail> dataList) {
        this.mContext = mActivity;
        this.dataList = dataList;
    }

    @Override
    public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_question_comment_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final CommViewHolder holder, final int position) {
        final QuestionEntity.AnswerDetail e = dataList.get(position);

        try{
            if(e!=null&&anwserMoreEntity!=null&&questionEntity!=null){
                BitmapUtil.displayCirImage(null, holder.iv_avatar);

                //如果回答者为空则取最上面用户名
                if(TextUtils.isEmpty(e.getAnswer())){
                    holder.tv_name.setText(questionEntity.getUserName());
                    holder.tv_time.setText(DateUtil.getDateToString(e.getAskTime()));

                    holder.content_mathview.setText(e.getAsk());
                }else{
                    holder.tv_name.setText(anwserMoreEntity.getUserName());
                    holder.tv_time.setText(DateUtil.getDateToString(e.getAnswerTime()));
                    holder.content_mathview.setText(e.getAnswer());
                }

                //采纳
//                if(anwserMoreEntity.isAdopt()){
//                    holder.sureIv.setVisibility(View.VISIBLE);
//                }
            }

        }catch (Exception ex){

        }
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
        @BindView(R.id.re_answer_size_tv)
        TextView reAnswersizeTv;

        @BindView(R.id.question_sure)
        ImageView sureIv;

        @BindView(R.id.content_mathview)
        AskMathView content_mathview;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            content_mathview.showWebImage(mContext).formatMath();
        }
    }
}