package com.asking.pad.app.ui.superclass.tutorial.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.SuperSuperClassSpeakerEntity;
import com.asking.pad.app.ui.superclass.tutorial.topic.TopicItemActivity;
import com.asking.pad.app.widget.AskMathView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/4/12.
 */

public class SuperSpeakerAdapter extends RecyclerView.Adapter<SuperSpeakerAdapter.CommViewHolder>{
    private List<SuperSuperClassSpeakerEntity.ListBean> mDatas;
    private Context mContext;
    String classType;
    boolean isBuy;

    public SuperSpeakerAdapter(Context context,boolean isBuy,String classType, List<SuperSuperClassSpeakerEntity.ListBean> datas){
        this.mContext = context;
        this.mDatas = datas;
        this.classType = classType;
        this.isBuy = isBuy;
    }

    @Override
    public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_speaker,parent,false));
    }

    @Override
    public void onBindViewHolder(final CommViewHolder holder, final int position) {
        SuperSuperClassSpeakerEntity.ListBean item = mDatas.get(position);

        holder.titleMathView.setTextColor("#ffaa2a");
        holder.titleMathView.setText("题型"+(position+1)+"："+item.getKindName());

        try{
            String strTmp = "";
            if(item.getSubject().getSubjectTypeBean().getType_id().equals("1")){//选择题
                for(SuperSuperClassSpeakerEntity.ListBean.SubjectBean.OptionsBean o: item.getSubject().getOptions()){
                    strTmp = strTmp+ o.getOptionName() + ". " + o.getOptionContentHtml().substring(3, o.getOptionContentHtml().length()-4) + "<br/>";
                }
                holder.mathView.setText(item.getSubject().getSubjectDescriptionHtml()+ strTmp);
            }else{
                holder.mathView.setText(item.getSubject().getSubjectDescriptionHtml());
            }
        }catch (Exception e){}

        holder.mathView.setOnAskMathClickListener(openTopicItemActivity(item));
        holder.titleMathView.setOnAskMathClickListener(openTopicItemActivity(item));
    }

    private AskMathView.OnAskMathClickListener openTopicItemActivity(final SuperSuperClassSpeakerEntity.ListBean listBean){
        return new AskMathView.OnAskMathClickListener() {
            @Override
            public void OnClick() {
                Intent intent = new Intent(mContext, TopicItemActivity.class);
                intent.putExtra("classType", classType);
                intent.putExtra("isBuy", isBuy);
                intent.putExtra("SuperSuperClassSpeakerEntity.ListBean", listBean);
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

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
