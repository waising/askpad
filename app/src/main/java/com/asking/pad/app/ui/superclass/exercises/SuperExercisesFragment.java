package com.asking.pad.app.ui.superclass.exercises;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.entity.superclass.exer.SubjectExerEntity;
import com.asking.pad.app.entity.superclass.exer.SubjectTopicEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.widget.indicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jswang on 2017/4/10.
 */

public class SuperExercisesFragment extends BaseFrameFragment<UserPresenter, UserModel> {

    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    @BindView(R.id.indicator)
    TabPageIndicator indicator;

    @BindView(R.id.rv_subject)
    RecyclerView rv_subject;

    CommAdapter subjectAdapter;

    CommPagerAdapter mPagerAdapter;

    boolean isBuy;
    String gradeId;
    String knowledgeId;

    private String topic_id;
    private int pageIndex = 0;
    private int startIndex = 0;
    private int limit = 5;
    boolean flag;

    private List<SubjectTopicEntity> subjectList = new ArrayList<>();
    private List<SubjectExerEntity> topicList = new ArrayList<>();

    public static SuperExercisesFragment newInstance(Bundle bundle) {
        SuperExercisesFragment fragment = new SuperExercisesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            gradeId = bundle.getString("gradeId");
            knowledgeId = bundle.getString("knowledgeId");
            isBuy = bundle.getBoolean("isBuy");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setContentView(R.layout.fragment_super_exercises);
        ButterKnife.bind(this, getContentView());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        super.initView();

        subjectAdapter = new CommAdapter(getActivity(), subjectList);
        rv_subject.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_subject.setAdapter(subjectAdapter);

        mPagerAdapter = new CommPagerAdapter(getChildFragmentManager());
        mViewpager.setAdapter(mPagerAdapter);
        indicator.setLayoutResource(R.layout.layout_indicator_tab_view2);
        indicator.setViewPager(mViewpager);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        flag = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        flag = true;
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        if (mViewpager.getCurrentItem() == mViewpager.getAdapter()
                                .getCount() - 1 && !flag) {
                            loaMoreData();
                        }
                        flag = true;
                        break;
                }
            }
        });

        refreshData(gradeId, knowledgeId, isBuy);
    }

    private boolean isRunLoaMoreData = false;

    private void loaMoreData() {
        if (!isRunLoaMoreData) {
            isRunLoaMoreData = true;
            pageIndex = pageIndex + 1;
            startIndex = pageIndex * limit;
            loadSubject();
        }
    }

    public void refreshData(String gradeId, String knowledgeId, boolean isBuy) {
        this.pageIndex = 0;
        this.gradeId = gradeId;
        this.knowledgeId = knowledgeId;
        this.isBuy = isBuy;

        mPresenter.getSubjectTopic(isBuy,gradeId, knowledgeId, new ApiRequestListener<String>() {

            @Override
            public void onResultSuccess(String res) {
                subjectList.clear();
                subjectList.addAll(JSON.parseArray(res, SubjectTopicEntity.class));
                if (subjectList.size() > 0) {
                    SubjectTopicEntity entity = subjectList.get(0);
                    topic_id = entity.topicId;
                    entity.isSelect = true;
                }
                subjectAdapter.notifyDataSetChanged();
                loadSubject();
            }
        });
    }

    private void loadSubject() {
        mPresenter.getAllSubjectClassic(isBuy,gradeId, knowledgeId, topic_id, startIndex, limit, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                JSONObject jsonRes = JSON.parseObject(res);
                List<SubjectExerEntity> list = JSON.parseArray(jsonRes.getString("subjects"),SubjectExerEntity.class);
                if(isBuy){
                    int endIndex = startIndex + limit;
                    if(startIndex > list.size()){
                        startIndex = list.size();
                    }
                    if(endIndex > list.size()){
                        endIndex = list.size();
                    }
                    list = list.subList(startIndex,endIndex);
                }
                if (startIndex == 0) {
                    topicList.clear();
                }
                topicList.addAll(list);
                mPagerAdapter.notifyDataSetChanged();
                indicator.notifyDataSetChanged();

                if (startIndex == 0) {
                    mViewpager.post(new Runnable() {
                        @Override
                        public void run() {
                            mViewpager.setCurrentItem(0);
                        }
                    });
                }

                isRunLoaMoreData = false;
            }

            @Override
            public void onResultFail() {
                if(pageIndex>0){
                    pageIndex = pageIndex - 1;
                }
                isRunLoaMoreData = false;
            }
        });
    }

    class CommViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_name)
        TextView item_name;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class CommAdapter extends RecyclerView.Adapter<CommViewHolder> {

        private List<SubjectTopicEntity> mDatas;
        private Context mContext;

        public CommAdapter(Context context, List<SubjectTopicEntity> datas) {
            this.mContext = context;
            this.mDatas = datas;
        }

        @Override
        public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_super_exercises, parent, false));
        }

        @Override
        public void onBindViewHolder(CommViewHolder holder, int position) {
            final SubjectTopicEntity e = mDatas.get(position);
            holder.item_name.setSelected(e.isSelect);
            holder.item_name.setText(e.topicName);
            holder.item_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (SubjectTopicEntity ii : mDatas) {
                        ii.isSelect = false;
                    }
                    e.isSelect = true;
                    notifyDataSetChanged();
                    topic_id = e.topicId;
                    pageIndex = 0;
                    loadSubject();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }
    }

    @OnClick({R.id.iv_pre, R.id.iv_next})
    public void onClick(View view) {
        int index;
        switch (view.getId()) {
            case R.id.iv_pre:
                index = mViewpager.getCurrentItem() - 1;
                if (index >= 0) {
                    mViewpager.setCurrentItem(index);
                }
                break;
            case R.id.iv_next:
                index = mViewpager.getCurrentItem() + 1;
                if (index < mPagerAdapter.getCount()) {
                    mViewpager.setCurrentItem(index);
                } else {
                    loaMoreData();
                }
                break;
        }
    }

    class CommPagerAdapter extends FragmentStatePagerAdapter {


        public CommPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return topicList.size();
        }

        @Override
        public Fragment getItem(int position) {
            SubjectExerEntity e = topicList.get(position);
            return SuperTopicAskFragment.newInstance(e,position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return (position + 1) + "";
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
