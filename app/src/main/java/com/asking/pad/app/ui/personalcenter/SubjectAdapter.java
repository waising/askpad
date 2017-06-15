package com.asking.pad.app.ui.personalcenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.MySubjectEntity;
import com.asking.pad.app.widget.AskMathView;
import com.asking.pad.app.widget.MultiStateView;
import com.asking.pad.app.widget.StarView;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 题目的adpater
 * create by linbin
 */

public class SubjectAdapter extends SwipeMenuAdapter<SubjectAdapter.ViewHolder> {


    private Context mContext;

    List<MySubjectEntity.ListBean> commentEntity;

    private DelListener listener;


    public SubjectAdapter(Context context, List<MySubjectEntity.ListBean> commentEntity, DelListener listener) {
        this.mContext = context;
        this.commentEntity = commentEntity;
        this.listener = listener;
    }

    public void setData(List<MySubjectEntity.ListBean> commentEntity) {
        this.commentEntity = commentEntity;
        notifyDataSetChanged();
    }

    public void removeCommentEntityItem(int index) {
        commentEntity.remove(index);
        notifyItemRemoved(index);
    }

    public String getCommentEntityItemId(int index) {
        return commentEntity.get(index).getId();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject_list, parent, false);
    }

    @Override
    public ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new ViewHolder(realContentView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (commentEntity != null && commentEntity.size() > 0) {
            MySubjectEntity.ListBean subject = commentEntity.get(position);
            if (subject != null) {
                String subjectDescriptionHtml = subject.getSubject_description_html(); // 题目
                List<MySubjectEntity.ListBean.OptionsBean> answerOptions = subject.getOptions(); // 题目选项list
                int difficulty = subject.getDifficulty();
                String strOptionContent = "";
                if (answerOptions != null && answerOptions.size() > 0) {
                    for (int i = 0; i < answerOptions.size(); i++) {
                        String optionName = answerOptions.get(i).getOption_name();//A,B,C,D这种
                        String optionContentHtml = answerOptions.get(i).getOption_content_html();//选项内容
                        if (optionContentHtml != null && optionContentHtml.contains("</p>")) {//有</p>"符号的
                            strOptionContent += (optionName + "、" + optionContentHtml.substring(3, optionContentHtml.length() - 4) + "<br/>");//拼接选项，去掉<p>符号
                        } else {//没有</p>"符号的
                            strOptionContent += (optionName + "、" + optionContentHtml + "<br/>");
                        }
                    }
                }
                if (subjectDescriptionHtml != null) {//题目+选项
                    holder.mMathViewTitle.setText(subjectDescriptionHtml + strOptionContent);
                } else {//没有题目的，只显示选项
                    holder.mMathViewTitle.setText(strOptionContent);
                }
                // 设置难度值
                if (difficulty > 0) {
                    holder.ratingBar.setmStarNum(difficulty);
                } else {
                    holder.ratingBar.setmStarNum(0);
                }
                holder.subjectNum.setText(String.valueOf(position + 1));//题目题号
                //答案解析
                holder.mathView.setText(subject.getDetails_answer_html());


                holder.tvDel.setOnClickListener(new View.OnClickListener() {//跳转修改笔记页面
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.del(position,getCommentEntityItemId(position));
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return commentEntity.size();  //　收藏的知识点和资讯
    }


    class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.mathView_title)//题目webview
                AskMathView mMathViewTitle;
        @BindView(R.id.ratingBar)//难度星星
                StarView ratingBar;
        @BindView(R.id.subject_num)//题目号
                TextView subjectNum;
        @BindView(R.id.tv_check_detail)//查看详情
                TextView tvCheckDetail;
        @BindView(R.id.tv_del)//删除
                TextView tvDel;
        @BindView(R.id.multiStateView)
        MultiStateView multiStateView;
        @BindView(R.id.expandable_layout)
        ExpandableLayout expandableLayout;//展开控件
        @BindView(R.id.expand_rl)
        LinearLayout expandRl;
        @BindView(R.id.mathView)
        AskMathView mathView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mathView.showWebImage(mContext,multiStateView).formatMath();
        }

        @OnClick({R.id.tv_check_detail})
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.linearLayout://教育咨询和知识点界面
//                    // ToastUtil.showMessage("我我我", Toast.LENGTH_SHORT);
//                    String objId = commentEntity.get(getAdapterPosition()).getObjId();
//                    String tille = commentEntity.get(getAdapterPosition()).getTille();
//                    // 知识点
//                    if (!TextUtils.isEmpty(objId) && subjectCatalog == Constants.Collect.knowledge) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("title", "知识详情");
//                        bundle.putString("knowledge", tille);
//                        bundle.putString("id", objId);
//                        bundle.putString("me", "searchDetail");
//                        // CommonUtil.openAuthActivity(mContext, KnowledgeDetailActivity.class, bundle);//跳转到知识点详情页
//                    }
//                    // 教育资讯
//                    if (!TextUtils.isEmpty(objId) && subjectCatalog == Constants.Collect.refer) {//跳转到教育咨询详情页
//                        Bundle bundle = new Bundle();
//                        bundle.putString("referId", objId);
//                        //     CommonUtil.openActivity(mContext, ReferDetailActivity.class, bundle);
//                    }
//                    break;


                case R.id.tv_check_detail://查看解析
                    v.setSelected(!v.isSelected());
                    ((TextView) v).setText(v.isSelected() ? "收起解析" : "查看解析");
                    if (v.isSelected()) {
                        expandableLayout.expand();
                    } else {
                        expandableLayout.collapse();
                    }
                    break;

            }
        }
    }
}
