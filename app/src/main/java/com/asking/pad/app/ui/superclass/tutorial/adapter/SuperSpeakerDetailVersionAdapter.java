package com.asking.pad.app.ui.superclass.tutorial.adapter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.AnswerOption;
import com.asking.pad.app.entity.ResultEntity;
import com.asking.pad.app.entity.SubjectEntity;
import com.asking.pad.app.widget.AskMathView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wxwang on 2016/10/27.
 */

public class SuperSpeakerDetailVersionAdapter extends RecyclerView.Adapter<SuperSpeakerDetailVersionAdapter.ViewHolder> {
    private List<SubjectEntity> mDatas;
    private Context mContext;
    private RecyclerView recyclerViewParent;
    private ArrayMap<Integer, CheckBox> submitBtns;


    public SuperSpeakerDetailVersionAdapter(Context context, List<SubjectEntity> datas, RecyclerView recyclerViewParent) {
        this.mContext = context;
        this.mDatas = datas;
        this.recyclerViewParent = recyclerViewParent;
        submitBtns = new ArrayMap<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_supertutorial_speaker_version_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.position = position;
        SubjectEntity item = mDatas.get(position);
        String answer = "";
        if (item.getSubjectType().getTypeId().equals("1")) {//选择题才入
            for (AnswerOption a : item.getAnswerOptions()) {
                answer = answer + a.getOptionName() + ". " + a.getOptionContentHtml().substring(3, a.getOptionContentHtml().length() - 4) + "<br/>";
            }
            holder.mathView.setText("题" + (position + 1) + "：" + item.getSubjectDescriptionHtml() + answer);
        } else {
            holder.mathView.setText("题" + (position + 1) + "：" + item.getSubjectDescriptionHtml());
        }

        submitBtns.put(position, holder.detail);
        holder.detail.setTag(holder.detailAnswer);
        holder.detail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                View vTmp = (View) buttonView.getTag();
                vTmp.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
        holder.detailAnswer.setText(item.getDetailsAnswerHtml());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mathView)
        AskMathView mathView;
        @BindView(R.id.detail)
        CheckBox detail;
        @BindView(R.id.detailAnswer)
        AskMathView detailAnswer;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mathView.showWebImage(mContext).formatMath();
            detailAnswer.formatMath().showWebImage();
        }
    }

    /**
     * 更新提交成功的结果
     */
    public void updateSubmit(int position, ResultEntity resultEntity) {

    }
}
