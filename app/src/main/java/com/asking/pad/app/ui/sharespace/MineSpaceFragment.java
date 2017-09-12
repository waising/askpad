package com.asking.pad.app.ui.sharespace;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFragment;
import com.asking.pad.app.ui.sharespace.special.SpecialItemFragment;
import com.asking.pad.app.widget.indicator.TabPageIndicator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的空间
 */

public class MineSpaceFragment extends BaseFragment {

    @BindView(R.id.indicator)
    TabPageIndicator indicator;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    ArrayList<String> tabList = new ArrayList<>();


    public static MineSpaceFragment newInstance() {
        MineSpaceFragment fragment = new MineSpaceFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mine_space);
        ButterKnife.bind(this, getContentView());
    }


    @Override
    public void initView() {
        super.initView();
        tabList.add(getString(R.string.my_question));
        tabList.add(getString(R.string.my_answer));
        tabList.add(getString(R.string.my_attention));
        tabList.add(getString(R.string.my_subject));
        indicator.setLayoutResource(R.layout.layout_indicator_tab_view_mine_space);
        CommAdapter mAdapter = new CommAdapter(getChildFragmentManager());
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

            switch(position){
                case 0:
                case 1:
                    f = MyQuestionsFragment2.newInstance(position);
                    break;
                case 3:
                    f =  SpecialItemFragment.newInstance("1","","","");
                    break;
                default:
                    f = MineAttentionFragment.newInstance();
                    break;
            }

//            if (position == 0 || position == 1) {
//                f = MyQuestionsFragment.newInstance(position == 1 ?"13":"");
//            } else  if (position == 3) {
//                f =  SpecialItemFragment.newInstance("1","","","");
//            }else {
//                f = MineAttentionFragment.newInstance();
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
}
