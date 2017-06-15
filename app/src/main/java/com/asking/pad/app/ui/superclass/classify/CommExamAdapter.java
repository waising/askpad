package com.asking.pad.app.ui.superclass.classify;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.commom.CBaseAdapter;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.entity.ExamReviewTree;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.widget.AskMathView;
import com.asking.pad.app.widget.MtGridLayout;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/4/11.
 */

public class CommExamAdapter extends RecyclerView.Adapter<CommExamAdapter.CommViewHolder>{
    private List<ExamReviewTree> mDatas;
    private Context mContext;
    public boolean isHavChild = false;
    UserPresenter mPresenter;
    int curExpandIndex = -1;

    RecyclerView recycler;

    OnCommItemListener mListener;
    public interface OnCommItemListener{
       void OnClickItem(ExamReviewTree e);
    }

    public CommExamAdapter(Context context, UserPresenter mPresenter, List<ExamReviewTree> datas,RecyclerView recycler,OnCommItemListener mListener) {
        this.mContext = context;
        this.mDatas = datas;
        this.mPresenter = mPresenter;
        this.recycler = recycler;
        this.mListener = mListener;
    }

    @Override
    public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_exam_review_require, parent, false));
    }

    @Override
    public void onBindViewHolder(final CommViewHolder holder, final int position) {
        holder.position = position;
        final ExamReviewTree item = mDatas.get(position);

        if(isHavChild){
            if (item.isExpand) {
                holder.el_layout.expand(false);
            } else {
                holder.el_layout.collapse(false);
            }
            holder.iv_expand.setSelected(item.isExpand);
            holder.iv_expand.setVisibility(View.VISIBLE);
        }else{

            holder.iv_expand.setVisibility(View.GONE);
        }

        String tit;
        if(!TextUtils.isEmpty(item.getText())){
            tit = item.getText();
        }else{
            tit = item.name;
        }

        holder.tv_title.setVisibility(View.GONE);
        holder.titleMathView.setVisibility(View.GONE);
        if(CommonUtil.isEnglish(tit)){
            holder.titleMathView.setVisibility(View.VISIBLE);
            holder.titleMathView.setText(Constants.SuperTutorialHtmlCss + tit+ Constants.MD_HTML_SUFFIX);
        }else{
            holder.tv_title.setVisibility(View.VISIBLE);
            holder.tv_title.setText(tit);
        }

        if (item.list != null && item.list.size() > 0) {
            holder.ll_list.removeAllViews();
            holder.ll_list.setAdapter(new GridAdapter(item));
        }

        holder.rl_tit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(isHavChild){
                    if (item.list != null && item.list.size() > 0) {
                        collapseExpandable(holder,item,position);
                    } else {
                        mPresenter.firstreviewkesjd(item._id, new ApiRequestListener<String>() {
                            @Override
                            public void onResultSuccess(String res) {
                                List<ExamReviewTree> list = JSON.parseArray(res, ExamReviewTree.class);
                                if (list != null && list.size() > 0) {
                                    item.list.addAll(list);
                                    notifyDataSetChanged();
                                    collapseExpandable(holder,item,position);
                                }
                            }
                        });
                    }
                }else{
                    mListener.OnClickItem(item);
                }
            }
        });
    }

    private void collapseExpandable(CommViewHolder holder,ExamReviewTree item,int position){
        item.isExpand = !item.isExpand;
        holder.iv_expand.setSelected(item.isExpand);
        if (item.isExpand) {
            holder.el_layout.expand();
            if(curExpandIndex != -1){
                CommViewHolder h = (CommViewHolder) recycler.findViewHolderForAdapterPosition(curExpandIndex);
                if (h != null) {
                    item.isExpand = false;
                    h.iv_expand.setSelected(item.isExpand);
                    h.el_layout.collapse();
                }
            }
            curExpandIndex = position;
        } else {
            holder.el_layout.collapse();
            curExpandIndex = -1;
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class CommViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.el_layout)
        ExpandableLayout el_layout;

        @BindView(R.id.titleMathView)
        AskMathView titleMathView;

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.iv_expand)
        ImageView iv_expand;

        @BindView(R.id.rl_tit)
        RelativeLayout rl_tit;

        @BindView(R.id.ll_list)
        MtGridLayout ll_list;

        int position;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            titleMathView.formatMath();

            ll_list.setOrientation(LinearLayout.VERTICAL);
            ll_list.setColumnCount(1);
        }
    }

    public class GridAdapter extends CBaseAdapter<ExamReviewTree> {
        ExamReviewTree mEntity;

        class ViewHolder {
            AskMathView mathView;
            TextView tv_title;
        }

        public GridAdapter(ExamReviewTree mEntity) {
            super((Activity)mContext, mEntity.list);
            this.mEntity = mEntity;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            GridAdapter.ViewHolder holder = null;
            if (convertView == null) {
                holder = new GridAdapter.ViewHolder();
                convertView = listContainer.inflate(R.layout.item_exam_review_require_c, null);
                holder.mathView = (AskMathView) convertView.findViewById(R.id.mathView);
                holder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            } else {
                holder = (GridAdapter.ViewHolder) convertView.getTag();
            }
            final ExamReviewTree item = listItems.get(position);

            if(CommonUtil.isEnglish(item.name)){
                holder.mathView.setVisibility(View.VISIBLE);
                holder.mathView.setText(Constants.SuperTutorialHtmlCss + item.getText() + Constants.MD_HTML_SUFFIX);
            }else{
                holder.tv_title.setVisibility(View.VISIBLE);
                holder.tv_title.setText(item.getText());
            }
            convertView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    mListener.OnClickItem(item);
                }
            });
            return convertView;
        }
    }
}
