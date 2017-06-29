package com.asking.pad.app.ui.superclass.examreview.classex.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.classex.SubjectOption;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/5/24.
 */

public class TopicItemOpAdapter extends RecyclerView.Adapter<TopicItemOpAdapter.CommViewHolder> {
    private List<SubjectOption> dataList;
    private LayoutInflater layoutInflater;

    public String rightAnswer;
    public String userAnswer;


    public TopicItemOpAdapter(Context context, List<SubjectOption> dataList) {
        this.dataList = dataList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_common_recycle_options, null, false);
        return new CommViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommViewHolder holder, int position) {
        final SubjectOption e = dataList.get(position);

        holder.rBtn.setText(e.getOptionName());
        holder.rBtn.setChecked(e.isSelect);
        holder.rBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(e.isSelect){
                    e.isSelect = false;
                }else{
                    for(SubjectOption e:dataList){
                        e.isSelect = false;
                    }
                    e.isSelect = true;
                }
                notifyDataSetChanged();
            }
        });

        if(!TextUtils.isEmpty(userAnswer)){
            String optionName = e.getOptionName();
            boolean isOption = TextUtils.equals(optionName,userAnswer);
            boolean isRight = TextUtils.equals(userAnswer,rightAnswer);
            boolean isOptionRight = TextUtils.equals(optionName,rightAnswer);

            if (isOption) {
                if (isRight) {
                    holder.state_bg.setImageResource(R.mipmap.ask_right);
                } else {
                    holder.state_bg.setImageResource(R.mipmap.ask_wrong);
                }
                holder.state_bg.setVisibility(View.VISIBLE);
            } else {
                if (!isRight && isOptionRight) {
                    holder.state_bg.setImageResource(R.mipmap.ask_wrong);
                }
            }
            if (isOptionRight) {
                holder.state_bg.setImageResource(R.mipmap.ask_right);
            }
        }

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class CommViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rBtn)
        RadioButton rBtn;

        @BindView(R.id.state_bg)
        ImageView state_bg;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
