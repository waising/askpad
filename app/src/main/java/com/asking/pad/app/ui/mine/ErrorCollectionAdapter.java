package com.asking.pad.app.ui.mine;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.MyWrongTopicEntity;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.widget.AskMathView;
import com.asking.pad.app.widget.StarView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/4/27.
 */

public class ErrorCollectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MyWrongTopicEntity> mDatas;
    private Context mContext;

    private final int TYPE_HEADER = 1000;
    private final int TYPE_NORMAL = 1001;

    private View mHeaderView;

    private UserPresenter mPresenter;

    private String subjectCatalogCode;

    ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            return TYPE_HEADER;
        }  else {
            return TYPE_NORMAL;
        }
    }

    private boolean haveHeaderView() {
        return mHeaderView != null;
    }

    private boolean isHeaderView(int position) {
        return haveHeaderView() && position == 0;
    }

    public void addHeaderView(View headerView) {
        if (haveHeaderView()) {
            throw new IllegalStateException("hearview has already exists!");
        } else {
            //避免出现宽度自适应
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            headerView.setLayoutParams(params);
            mHeaderView = headerView;
            notifyItemInserted(0);
        }
    }

    public ErrorCollectionAdapter(Context context, UserPresenter mPresenter,List<MyWrongTopicEntity> datas){
        this.mContext = context;
        this.mDatas = datas;
        this.mPresenter = mPresenter;
    }

    public void setSubjectCatalogCode(String subjectCatalogCode){
        this.subjectCatalogCode = subjectCatalogCode;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder mViewHolder = null;
        if (viewType == TYPE_HEADER) {
            mViewHolder = new HeaderViewHolder(mHeaderView);
        }else if (viewType == TYPE_NORMAL){
            mViewHolder = new CommViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_error_collection,parent,false));
        }
        return mViewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommViewHolder) {
            if (haveHeaderView()) position--;
            final CommViewHolder mViewHolder = (CommViewHolder)holder;
            final MyWrongTopicEntity e = mDatas.get(position);

            mViewHolder.tv_index.setText(""+(position+1));
            mViewHolder.topic_mathview.setText(e.getSubjectDescriptionHtml());

            String error_count = String.format("错误次数：%s次",e.getErrorCount());
            SpannableStringBuilder builder = new SpannableStringBuilder(error_count);
            builder.setSpan(redSpan, 5, 5+(e.getErrorCount()+"").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mViewHolder.tv_error_count.setText(builder);

            mViewHolder.answer_mathView.setText(e.analysisTopic);
            mViewHolder.answer_mathView.setVisibility(e.isSelect?View.VISIBLE:View.GONE);
            mViewHolder.tv_parsing.setChecked(e.isSelect);

            mViewHolder.tv_parsing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    e.isSelect = !e.isSelect;
                    mViewHolder.answer_mathView.setVisibility(e.isSelect?View.VISIBLE:View.GONE);
                    mViewHolder.tv_parsing.setChecked(e.isSelect);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int count = (mDatas == null ? 0 : mDatas.size());
        if (mHeaderView != null) {
            count++;
        }
        return count;
    }

    //头部 ViewHolder
    class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    class CommViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_index)
        TextView tv_index;

        @BindView(R.id.topic_mathview)
        AskMathView topic_mathview;

        @BindView(R.id.tv_error_count)
        TextView tv_error_count;

        @BindView(R.id.start_view)
        StarView start_view;

        @BindView(R.id.answer_mathView)
        AskMathView answer_mathView;

        @BindView(R.id.tv_parsing)
        CheckBox tv_parsing;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
