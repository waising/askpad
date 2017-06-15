package com.asking.pad.app.ui.personalcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.pay.PayActivity;
import com.asking.pad.app.widget.indicator.TabPageIndicatorFontSize;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 购买记录页面
 * create by linbin
 */

public class BuyRecordActivity extends BaseFrameActivity<UserPresenter, UserModel> {


    @BindView(R.id.indicator)
    TabPageIndicatorFontSize indicator;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    ArrayList<String> tabList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_record);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        super.initView();
        tabList.add(getString(R.string.myLessons)); // 我的课程
        tabList.add(getString(R.string.myRecharge)); // 我的充值
        indicator.setLayoutResource(R.layout.layout_indicator_tab_view_collection);//tab页布局
        CommAdapter mAdapter = new CommAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        indicator.setViewPager(viewPager);
        mAdapter.notifyDataSetChanged();
        indicator.notifyDataSetChanged();
    }

    class CommAdapter extends FragmentStatePagerAdapter {

        public CommAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f;
            if (position == 0) {//我的课程
                f = BuyRecordFragment.newInstance(Constants.Collect.knowledge);
            } else {//我的充值
                f = BuyRecordFragment.newInstance(Constants.Collect.title);
            }
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

    // 阿思币充值（我的充值）

    /**
     * 跳转到支付页面
     *
     * @param bundle
     */
    public void openPayActivity(Bundle bundle) {
        Intent intent = new Intent(this, PayActivity.class);
        intent.putExtras(bundle);
        overridePendingTransition(R.anim.enter_left_in, R.anim.enter_left_out);

        // openResultActivity(PayActivity.class, bundle);
    }

}
