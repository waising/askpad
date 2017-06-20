package com.asking.pad.app.ui.classmedia;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseEvenNoPreActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.widget.indicator.TabPageIndicator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jswang on 2017/6/1.
 */

public class ClassMediaActivity extends BaseEvenNoPreActivity {
    @BindView(R.id.indicator)
    TabPageIndicator indicator;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.tv_all_pay)
    TextView tv_all_pay;

    ArrayList<String> tabList = new ArrayList<>();
    ArrayList<Fragment> fragments = new ArrayList<>();

    boolean mathCompleteFlag = false;
    boolean physicsCompleteFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_media);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        super.initView();

        tabList.add("数学");
        tabList.add("物理");

        fragments.add(ClassMediaFragment.newInstance(Constants.CLASS_MEDIA_TYPE_ID[0],new ClassMediaFragment.OnClassMediaCallBack(){
            @Override
            public void OnCallBack(boolean completeFlag) {
                mathCompleteFlag = completeFlag;
                if(viewPager.getCurrentItem() == 0){
                    if(mathCompleteFlag){
                        tv_all_pay.setVisibility(View.VISIBLE);
                    }else{
                        tv_all_pay.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }));
        fragments.add(ClassMediaFragment.newInstance(Constants.CLASS_MEDIA_TYPE_ID[1],new ClassMediaFragment.OnClassMediaCallBack(){
            @Override
            public void OnCallBack(boolean completeFlag) {
                physicsCompleteFlag = completeFlag;
                if(viewPager.getCurrentItem() == 1){
                    if(physicsCompleteFlag){
                        tv_all_pay.setVisibility(View.VISIBLE);
                    }else{
                        tv_all_pay.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }));

        indicator.setLayoutResource(R.layout.layout_indicator_tab_view5);
        CommAdapter mAdapter = new CommAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        indicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                tv_all_pay.setVisibility(View.INVISIBLE);
                if(position == 1){
                    if(physicsCompleteFlag){
                        tv_all_pay.setVisibility(View.VISIBLE);
                    }
                }else{
                    if(mathCompleteFlag){
                        tv_all_pay.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    public void onEventMainThread(AppEventType event) {
        switch (event.type) {
            case AppEventType.PAY_SUCCESSS_REQUEST:
                ((ClassMediaFragment)fragments.get(0)).requstData();
                ((ClassMediaFragment)fragments.get(1)).requstData();
                break;
        }
    }


    class CommAdapter extends FragmentStatePagerAdapter {

        public CommAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabList.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }


    @OnClick({R.id.tv_all_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_all_pay:
                Bundle mBundle = new Bundle();
                mBundle.putString("orderType", "TC");
                CommonUtil.openActivity(PayClassMediaActivity.class, mBundle);
                break;
        }
    }

}
