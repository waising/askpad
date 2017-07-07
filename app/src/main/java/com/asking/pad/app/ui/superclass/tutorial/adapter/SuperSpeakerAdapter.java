package com.asking.pad.app.ui.superclass.tutorial.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asking.pad.app.R;
import com.asking.pad.app.commom.ParamHelper;
import com.asking.pad.app.entity.superclass.SuperClassSpeaker;
import com.asking.pad.app.ui.superclass.tutorial.topic.TopicItemActivity;
import com.asking.pad.app.widget.AskMathView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/4/12.
 */

public class SuperSpeakerAdapter extends RecyclerView.Adapter<SuperSpeakerAdapter.CommViewHolder>{
    private List<SuperClassSpeaker> mDatas;
    private Context mContext;
    boolean isBuy;
    private RecyclerView recyclerView;
    int curExpandIndex = -1;

    public SuperSpeakerAdapter(Context context,boolean isBuy,RecyclerView recyclerView,List<SuperClassSpeaker> datas){
        this.mContext = context;
        this.mDatas = datas;
        this.isBuy = isBuy;
        this.recyclerView = recyclerView;
    }

    @Override
    public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_speaker,parent,false));
    }

    @Override
    public void onBindViewHolder(final CommViewHolder holder, final int position) {
        final SuperClassSpeaker item = mDatas.get(position);

        holder.titleMathView.setTextColor("#ffaa2a");
        holder.titleMathView.setText("题型"+(position+1)+"："+item.subjectKindName);

        holder.mathView.setText(item.getSubjectDescriptionHtml());


        holder.titleMathView.setOnAskMathClickListener(new AskMathView.OnAskMathClickListener() {
            @Override
            public void OnClick() {
                openExpand(position,holder);
            }
        });
        holder.mathView.setOnAskMathClickListener(openTopicItemActivity(item));
        holder.ll_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExpand(position,holder);
            }
        });
    }

    private void openExpand(int position,CommViewHolder holder){
        if (curExpandIndex != position) {
            holder.el_layout.expand();
            holder.iv_expand.setSelected(true);
            if(curExpandIndex != -1){
                CommViewHolder h = (CommViewHolder) recyclerView.findViewHolderForAdapterPosition(curExpandIndex);
                if (h != null) {
                    h.iv_expand.setSelected(false);
                    h.el_layout.collapse();
                }
            }
            curExpandIndex = position;
        } else {
            holder.el_layout.collapse();
            holder.iv_expand.setSelected(false);
            curExpandIndex = -1;
        }
    }

    private AskMathView.OnAskMathClickListener openTopicItemActivity(final SuperClassSpeaker listBean){
        return new AskMathView.OnAskMathClickListener() {
            @Override
            public void OnClick() {
                Intent intent = new Intent(mContext, TopicItemActivity.class);
                intent.putExtra("isBuy", isBuy);
                HashMap<String, Object> mParams = ParamHelper.acquireParamsReceiver(TopicItemActivity.class.getName());
                mParams.put("SuperClassSpeaker", listBean);
                mContext.startActivity(intent);
            }
        };
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class CommViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.titleMathView)
        AskMathView titleMathView;

        @BindView(R.id.mathView)
        AskMathView mathView;

        @BindView(R.id.el_layout)
        ExpandableLayout el_layout;

        @BindView(R.id.ll_expand)
        View ll_expand;

        @BindView(R.id.iv_expand)
        ImageView iv_expand;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
