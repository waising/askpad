package com.asking.pad.app.ui.downbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseActivity;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.widget.indicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.asking.pad.app.R.id.toolBar;

/**
 * Created by jswang on 2017/5/2.
 */

public class DownBookActivity extends BaseActivity {
    @BindView(toolBar)
    Toolbar mToolbar;

    @BindView(R.id.indicator)
    TabPageIndicator indicator;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    ArrayList<String> tabList = new ArrayList<>();
    List<Fragment> listFragments = new ArrayList<>();
    String courseTypeId;

    public static void openActivity(String courseTypeId){
        Bundle bundle = new Bundle();
        bundle.putString("courseTypeId", courseTypeId);
        CommonUtil.openActivity(DownBookActivity.class,bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_book);
        ButterKnife.bind(this);

        courseTypeId = this.getIntent().getStringExtra("courseTypeId");
    }

    @Override
    public void initView() {
        super.initView();
        setToolbar(mToolbar, "离线下载");

        tabList.add("可下载课程");
        tabList.add("已下载课程");

        listFragments.add(DownAbleFragment.newInstance(courseTypeId));
        listFragments.add(DownFinishFragment.newInstance(courseTypeId));

        indicator.setLayoutResource(R.layout.layout_indicator_tab_view3);
        CommAdapter mAdapter = new CommAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        indicator.setViewPager(viewPager);

        mAdapter.notifyDataSetChanged();
        indicator.notifyDataSetChanged();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if(position== 0){
                    ((DownAbleFragment)listFragments.get(0)).netBookData();
                }else{
                    ((DownFinishFragment)listFragments.get(1)).initBookData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    class CommAdapter extends FragmentStatePagerAdapter {

        public CommAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return listFragments.get(position);
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

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
