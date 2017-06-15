package com.asking.pad.app.ui.superclass.examreview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.entity.ExerAskEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jswang on 2017/3/2.
 */

public class ExamReviewExerFragment extends BaseFrameFragment<UserPresenter, UserModel> {
    String classId;
    ViewPager view_pager;
    ExamAdapter mAdapter;

    TextView tv_index;
    TextView tv_size;

    private List<ExerAskEntity> dataList = new ArrayList<>();

    public static ExamReviewExerFragment newInstance(String classId) {
        ExamReviewExerFragment fragment = new ExamReviewExerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("classId", classId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            classId = bundle.getString("classId");
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View rootView = layoutInflater.inflate(R.layout.fragment_exam_review_tree, null);
        view_pager = (ViewPager)rootView.findViewById(R.id.view_pager);
        mAdapter = new ExamAdapter(getChildFragmentManager());
        view_pager.setAdapter(mAdapter);

        tv_index  = (TextView)rootView.findViewById(R.id.tv_index);
        tv_size  = (TextView)rootView.findViewById(R.id.tv_size);

        if(!TextUtils.isEmpty(classId)){
            initLoadData(classId);
        }

        view_pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tv_index.setText((position+1)+"");
            }
        });

        return rootView;
    }

    public void initLoadData(String classId) {
        this.classId = classId;
        if(getActivity()!=null && view_pager!=null){
            mPresenter.firstreviewshizyl(classId,new ApiRequestListener<String>(){
                @Override
                public void onResultSuccess(String res) {
                    if(dataList == null){
                        dataList = new ArrayList<>();
                    }
                    dataList.clear();
                    dataList.addAll(JSON.parseArray(res,ExerAskEntity.class));
                    mAdapter.notifyDataSetChanged();

                    tv_index.setText(1+"");
                    tv_size.setText("/"+dataList.size());
                    view_pager.post(new Runnable() {
                        @Override
                        public void run() {
                            view_pager.setCurrentItem(0);
                        }
                    });
                }
            });
        }
    }

    class ExamAdapter extends FragmentStatePagerAdapter {

        public ExamAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = ExamReviewExerAskFragment.newInstance(position+1,dataList.get(position),getOnExerFragmentListener());
            return f;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    private ExamReviewExerAskFragment.OnExerFragmentListener getOnExerFragmentListener(){
        return  new ExamReviewExerAskFragment.OnExerFragmentListener(){
            @Override
            public void sendNextFragmentMessage(String id, String answer) {

            }

            @Override
            public void sendIndex(int index) {

            }
        };
    }
}
