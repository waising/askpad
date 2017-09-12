package com.asking.pad.app.ui.sharespace;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.entity.LabelEntity;
import com.asking.pad.app.entity.QuestionSubjectEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.main.ShareMainFragment;
import com.asking.pad.app.ui.sharespace.pop.GradePopupWindow;
import com.asking.pad.app.ui.sharespace.question.QuestionWebFragment;
import com.asking.pad.app.widget.indicator.TabPageIndicator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 回答广场
 */

public class QuestionsFragment extends BaseFrameFragment<UserPresenter, UserModel> {

    @BindView(R.id.indicator)
    TabPageIndicator indicator;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    ArrayList<String> tabList = new ArrayList<>();
    String gradeId = "";
    String subjectId = "";

    public static QuestionsFragment newInstance() {
        QuestionsFragment fragment = new QuestionsFragment();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_questions);
        ButterKnife.bind(this, getContentView());
    }

    @Override
    public void initView() {
        super.initView();

        tabList.clear();
        tabList.add(getString(R.string.aq_no_quest));
        tabList.add(getString(R.string.aq_askmoney_quest));
        tabList.add(getString(R.string.aq_quested));

        indicator.setLayoutResource(R.layout.layout_indicator_tab_view3);
        CommAdapter mAdapter = new CommAdapter(getFragmentManager());
        viewPager.setAdapter(mAdapter);
        indicator.setViewPager(viewPager);

        mAdapter.notifyDataSetChanged();
        indicator.notifyDataSetChanged();

        ((ShareMainFragment) (getParentFragment())).setToolbarRight(R.menu.menu_question_ask, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                openActivity(QuestionAskActivity.class);
                return false;
            }
        });
    }

    class CommAdapter extends FragmentStatePagerAdapter {

        public CommAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = QuestionWebFragment.newInstance(position);
//            if(position == 0){
//
//            }else{
//                f = QuestionsDetailFragment.newInstance(position);
//            }
            return f;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabList.get(position);
        }

        @Override
        public int getCount() {
            return tabList.size();
        }
    }

    @OnClick({R.id.tv_selectgrade})
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.tv_selectgrade:
                new GradePopupWindow().showPopupWindowView(getActivity(), view, new GradePopupWindow.OnGradePopupListener() {
                    @Override
                    public void OnGradePopup(LabelEntity gradeEntity, LabelEntity subjectEntity) {
                        gradeId = gradeEntity.getId();
                        subjectId = subjectEntity.getId();//km

                        QuestionSubjectEntity qs = new QuestionSubjectEntity();
                        qs.setKm(subjectId);
                        qs.setLevelId(gradeId);

                        ((TextView) view).setText(gradeEntity.getName() + "-" + subjectEntity.getName());

                        EventBus.getDefault().post(new AppEventType(AppEventType.QUESTION_REF, qs));

                    }
                });
                break;
        }
    }
}
