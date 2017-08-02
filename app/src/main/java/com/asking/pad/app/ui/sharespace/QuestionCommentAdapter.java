package com.asking.pad.app.ui.sharespace;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.QuestionEntity;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
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

    int mPosition = 0;
    View mItemView;

    public boolean isShowAdopt = true;
    public boolean isLoginUser;

    QuestionCommentFragment.AdoptCallBack adoptCallBack;

    public void setAdoptCallBack(QuestionCommentFragment.AdoptCallBack adoptCallBack) {
        this.adoptCallBack = adoptCallBack;
    }

    OnItemCommentListener mListener;

    public interface OnItemCommentListener {
        void OnItemComment(QuestionEntity.AnwserMoreEntity e);
    }

    public void setLoginUser(boolean loginUser) {
        isLoginUser = loginUser;
    }

    public QuestionCommentAdapter(Activity mActivity, ArrayList<QuestionEntity.AnwserMoreEntity> dataList, QuestionCommentAdapter.OnItemCommentListener mListener) {
        this.mContext = mActivity;
        this.dataList = dataList;
        this.mListener = mListener;
    }

    public void setVisBtn() {
        mItemView.findViewById(R.id.adopt_btn).setVisibility(View.GONE);
        mItemView.findViewById(R.id.question_sure).setVisibility(View.VISIBLE);
    }

    @Override
    public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_question_comment_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final CommViewHolder holder, final int position) {
        final QuestionEntity.AnwserMoreEntity e = dataList.get(position);

        try {
            if (e != null) {
                BitmapUtil.displayCirImage(e.getUserAvatar(),R.dimen.space_50, holder.iv_avatar);
                holder.tv_name.setText(e.getUserName());
                holder.tv_time.setText(e.getCreateDate_fmt());
                if (e.getList() != null && e.getList().size() > 0) {
                    holder.reAnswersizeTv.setText(String.valueOf(e.getList().size()) + "人回复");
                    holder.reAnswersizeTv.setVisibility(View.VISIBLE);
                } else {
                    holder.reAnswersizeTv.setVisibility(View.GONE);
                }

                holder.adoptBtn.setVisibility(View.GONE);
                if (isLoginUser && !e.isAdopt()) {
                    holder.adoptBtn.setVisibility(View.VISIBLE);
                    holder.adoptBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mItemView = holder.itemView;
                            mPosition = position;
                            adoptCallBack.adopt(e.getId());
                        }
                    });
                }

                if (!isShowAdopt) {
                    holder.adoptBtn.setVisibility(View.GONE);
                }

                holder.content_mathview.setText(e.getContent());
                if (e.isAdopt()) {
                    holder.sureIv.setVisibility(View.VISIBLE);
                } else {
                    holder.sureIv.setVisibility(View.GONE);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

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
        @BindView(R.id.re_answer_size_tv)
        TextView reAnswersizeTv;

        @BindView(R.id.question_sure)
        ImageView sureIv;

        @BindView(R.id.adopt_btn)
        Button adoptBtn;

        @BindView(R.id.content_mathview)
        AskMathView content_mathview;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            content_mathview.showWebImage(mContext).formatMath();
        }
    }
}