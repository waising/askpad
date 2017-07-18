package com.asking.pad.app.ui.sharespace.pop;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.LabelEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/7/17.
 */

public class GradePopupWindow {

    LabelEntity gradeEntity;
    LabelEntity subjectEntity;

    RecyclerView rv_grade;
    ArrayList<LabelEntity> gradeList = new ArrayList<>();

    RecyclerView rv_subject;
    ArrayList<LabelEntity> subjectList = new ArrayList<>();

    private PopupWindow popupwindow;

    public void showPopupWindowView(Activity mActivity, View anchor, final OnGradePopupListener mListener) {
        View customView = mActivity.getLayoutInflater().inflate(R.layout.sharespace_grade_popview_item, null, false);
        rv_grade = (RecyclerView) customView.findViewById(R.id.rv_grade);
        rv_subject = (RecyclerView) customView.findViewById(R.id.rv_subject);

        gradeList.add(new LabelEntity("", "全部年级", true));
        gradeList.add(new LabelEntity("7", "七年级", false));
        gradeList.add(new LabelEntity("8", "八年级", false));
        gradeList.add(new LabelEntity("9", "九年级", false));
        gradeList.add(new LabelEntity("10", "高一", false));
        gradeList.add(new LabelEntity("11", "高二", false));
        gradeList.add(new LabelEntity("12", "高三", false));

        subjectList.add(new LabelEntity("", "全部科目", true));
        subjectList.add(new LabelEntity("M", "数学", false));
        subjectList.add(new LabelEntity("P", "物理", false));

        gradeEntity = gradeList.get(0);
        subjectEntity = subjectList.get(0);

        rv_grade.setLayoutManager(new GridLayoutManager(mActivity, 3));
        rv_grade.setAdapter(new CommAdapter(mActivity, gradeList, new OnItemListener() {
            @Override
            public void OnItem(LabelEntity e) {
                gradeEntity = e;
                mListener.OnGradePopup(gradeEntity, subjectEntity);
            }
        }));

        rv_subject.setLayoutManager(new GridLayoutManager(mActivity, 3));
        rv_subject.setAdapter(new CommAdapter(mActivity, subjectList, new OnItemListener() {
            @Override
            public void OnItem(LabelEntity e) {
                subjectEntity = e;
                mListener.OnGradePopup(gradeEntity, subjectEntity);
            }
        }));

        popupwindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupwindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        popupwindow.setOutsideTouchable(true);

        popupwindow.showAsDropDown(anchor, 0, 5);
    }

    class CommAdapter extends RecyclerView.Adapter<CommAdapter.CommViewHolder> {
        ArrayList<LabelEntity> dataList = new ArrayList<>();
        Activity mActivity;
        OnItemListener mListener;

        public CommAdapter(Activity mActivity, ArrayList<LabelEntity> dataList, OnItemListener mListener) {
            this.mActivity = mActivity;
            this.dataList = dataList;
            this.mListener = mListener;
        }

        @Override
        public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_grade_popup, parent, false));
        }

        @Override
        public void onBindViewHolder(CommViewHolder holder, final int position) {
            final LabelEntity e = dataList.get(position);
            holder.item_name.setSelected(e.getSelect());
            holder.item_name.setText(e.getName());
            holder.item_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (LabelEntity ii : dataList) {
                        ii.setSelect(false);
                    }
                    e.setSelect(true);
                    notifyDataSetChanged();

                    mListener.OnItem(e);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        class CommViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.item_name)
            TextView item_name;

            public CommViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public interface OnGradePopupListener {
        void OnGradePopup(LabelEntity gradeEntity, LabelEntity subjectEntity);
    }

    private interface OnItemListener {
        void OnItem(LabelEntity e);
    }
}
