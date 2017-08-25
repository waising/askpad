package com.asking.pad.app.ui.mine;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.mine.MineStudyRecord;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jswang on 2017/4/18.
 */

public class MineStudyRecordDialog extends DialogFragment {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.child_recycler)
    RecyclerView child_recycler;

    MineStudyRecord mRecord;

    View.OnClickListener mListener;

    public static MineStudyRecordDialog newInstance(MineStudyRecord mRecord,View.OnClickListener mListener) {
        MineStudyRecordDialog fragment = new MineStudyRecordDialog();
        Bundle bunle = new Bundle();
        bunle.putSerializable("MineStudyRecord", mRecord);
        fragment.setArguments(bunle);
        fragment.mListener = mListener;
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mRecord = (MineStudyRecord)bundle.getSerializable("MineStudyRecord");
        }
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.dialog_minestudy_view, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView(){
        tv_title.setText(mRecord.commodityName);
        tv_time.setText(mRecord.createTime);

        child_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        child_recycler.setAdapter(new ChildAdapter(mRecord.childCommodityList));
    }

    @OnClick({R.id.iv_close})
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_close:
                dismiss();
                break;
        }
    }

    class ChildHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.tv_title1)
        TextView tv_title1;

        @BindView(R.id.tv_time)
        TextView tv_time;

        @BindView(R.id.tv_study)
        TextView tv_study;

        public ChildHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    ForegroundColorSpan greySpan = new ForegroundColorSpan(Color.parseColor("#333333"));


    class ChildAdapter extends RecyclerView.Adapter<ChildHolder> {

        private List<MineStudyRecord> list = new ArrayList<>();

        public ChildAdapter(List<MineStudyRecord> list) {
            this.list = list;
        }

        @Override
        public ChildHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ChildHolder(LayoutInflater.from(getActivity())
                    .inflate(R.layout.item_mine_record_c_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(final ChildHolder holder, final int position) {
            MineStudyRecord e = list.get(position);
            holder.tv_title.setText(e.commodityName);
            holder.tv_title1.setText(e.commodityTypeName);
            holder.tv_time.setText(e.createTime);

            switch (e.getDataType()) {
                case 0:
                    if (TextUtils.isEmpty(e.scheduleTitle)) {
                        holder.tv_study.setText("马上去学习");
                    } else {
                        holder.tv_study.setText("上次学到了：" + e.scheduleTitle);
                    }
                    break;
                case 1:
                    if (e.schedulePercent == 0) {
                        holder.tv_study.setText("马上去学习");
                    } else {
                        String ss = "上次学到：";
                        SpannableStringBuilder builder = new SpannableStringBuilder(ss + e.schedulePercent + "%");
                        builder.setSpan(greySpan, 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        holder.tv_study.setText(builder.toString());
                    }
                    break;
            }
            holder.itemView.setTag(e);
            holder.itemView.setOnClickListener(mListener);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

}
