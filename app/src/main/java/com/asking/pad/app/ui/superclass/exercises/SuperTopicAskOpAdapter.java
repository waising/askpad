package com.asking.pad.app.ui.superclass.exercises;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.ResultEntity;
import com.asking.pad.app.entity.SuperBuyClearanceEntity;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/4/12.
 */

public class SuperTopicAskOpAdapter extends RecyclerView.Adapter<SuperTopicAskOpAdapter.ViewHolder> {
    private List<SuperBuyClearanceEntity.ListBean.OptionsBean> answerOptions;
    private Context context;
    private LayoutInflater layoutInflater;


    private RadioButton view;

    private Map<Integer, RadioButton> lays;
    private OnCommItemListener switchFragmentCall;

    public SuperTopicAskOpAdapter(Context context, List<SuperBuyClearanceEntity.ListBean.OptionsBean> answerOptions,
                                  OnCommItemListener switchFragmentCall) {
        this.context = context;
        this.answerOptions = answerOptions;
        this.switchFragmentCall = switchFragmentCall;
        layoutInflater = LayoutInflater.from(context);
        lays = new ArrayMap<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_common_recycle_options, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SuperBuyClearanceEntity.ListBean.OptionsBean answerOption = answerOptions.get(position);
        holder.rBtn.setText(answerOption.getOptionName());
        holder.rBtn.setChecked(answerOption.isSelect());

        lays.put(position, holder.rBtn);
        if (answerOption.getResultEntity() != null) {
            ResultEntity resultEntity = answerOption.getResultEntity();
            boolean isOption = resultEntity.getAnswerResult().equals(answerOptions.get(position).getOptionName());
            boolean isRight = resultEntity.getAnswerResult().equals(resultEntity.getRightResult());
            ImageView mStateIv = (ImageView) ((RelativeLayout) (lays.get(position).getParent())).findViewById(R.id.state_bg);
            lays.get(position).setEnabled(false);
            if (isOption) {
                if (isRight) {
                    mStateIv.setImageResource(R.mipmap.ask_right);
                } else {
                    mStateIv.setImageResource(R.mipmap.ask_wrong);
                }
                mStateIv.setVisibility(View.VISIBLE);
            } else {
                if (!isRight && answerOptions.get(position).getOptionName().equals(resultEntity.getRightResult())) {
                    mStateIv.setImageResource(R.mipmap.ask_wrong);
                }
            }
            if (answerOptions.get(position).getOptionName().equals(resultEntity.getRightResult())) {
                mStateIv.setImageResource(R.mipmap.ask_right);
            }
        } else {
            holder.rBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (view != null && view != v) {
                        view.setChecked(false);
                    }
                    if (view != v) {
                        view = (RadioButton) v;
                        view.setChecked(true);
                        //跳转
                        switchFragmentCall.sendNextFragmentMessage("", view.getText().toString());
                    }
                }
            });
        }
    }

    /**更新结果*/
    public void updateResult(ResultEntity resultEntity){
        for(int i=0;i<answerOptions.size();i++) {
            String optionName = answerOptions.get(i).getOptionName();
            String  result = resultEntity.getAnswerResult();
            boolean isOption = TextUtils.equals(optionName,result);
            boolean isRight = TextUtils.equals(resultEntity.getAnswerResult(),resultEntity.getRightResult());
            ImageView mStateIv = (ImageView)((RelativeLayout)(lays.get(i).getParent())).findViewById(R.id.state_bg);
            lays.get(i).setEnabled(false);
            if (isOption) {
                if (isRight) {
                    mStateIv.setImageResource(R.mipmap.ask_right);
                } else {
                    mStateIv.setImageResource(R.mipmap.ask_wrong);
                }
                mStateIv.setVisibility(View.VISIBLE);
            } else {
                if (!isRight && answerOptions.get(i).getOptionName().equals(resultEntity.getRightResult())) {
                    mStateIv.setImageResource(R.mipmap.ask_wrong);
                }
            }
            if (answerOptions.get(i).getOptionName().equals(resultEntity.getRightResult())) {
                mStateIv.setImageResource(R.mipmap.ask_right);
            }
        }
    }

    @Override
    public int getItemCount() {
        return answerOptions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rBtn)
        RadioButton rBtn;
        @BindView(R.id.state_bg)
        ImageView stateBg;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnCommItemListener {
        void sendNextFragmentMessage(String id, String answer);
        void sendIndex(int index);
    }
}
