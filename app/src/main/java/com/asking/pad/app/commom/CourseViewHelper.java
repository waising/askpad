package com.asking.pad.app.commom;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.LabelEntity;
import com.asking.pad.app.widget.NoScrollGridView;

import java.util.List;

/**
 * Created by jswang on 2017/4/11.
 */

public class CourseViewHelper {

    public static void getView2(Activity mContext, LinearLayout pView, String title, BaseAdapter adapter) {
        View mRootView = LayoutInflater.from(mContext).inflate(R.layout.layout_cours_view2, null);
        ((TextView) mRootView.findViewById(R.id.title)).setText(title);
        NoScrollGridView gridView = (NoScrollGridView) mRootView.findViewById(R.id.course_view);
        gridView.setAdapter(adapter);
        pView.removeAllViews();
        pView.addView(mRootView);
    }

    public static CourseViewAdapter getCourseViewAdapter(Activity mContext, List<LabelEntity> data, OnItemLabelEntityListener mListenerr) {
        return new CourseViewAdapter(mContext, data, mListenerr);
    }


}
