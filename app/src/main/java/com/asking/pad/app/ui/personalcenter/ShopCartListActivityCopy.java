package com.asking.pad.app.ui.personalcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseEvenFrameFragment;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.ShopCartEvent;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.widget.NoRightClipViewPager;
import com.pingplusplus.android.Pingpp;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 购物车列表
 */

public class ShopCartListActivityCopy extends BaseFrameActivity<UserPresenter, UserModel> {

    @BindView(R.id.viewpager)
    NoRightClipViewPager viewPager;

    @BindView(R.id.toolBar)
    Toolbar mToolbar;
    /**
     * list
     */
    private ArrayList<BaseEvenFrameFragment> list = new ArrayList<BaseEvenFrameFragment>();

    public static ShopCartListActivityCopy newInstance() {
        ShopCartListActivityCopy fragment = new ShopCartListActivityCopy();
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart_list_copy);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

    }

    @Override
    public void initView() {
        super.initView();
        mToolbar.setNavigationIcon(null);
        setToolbar(mToolbar, getString(R.string.shop_cart));
        CommAdapter commAdapter = new CommAdapter(list);
        viewPager.setAdapter(commAdapter);
     //   viewPager.setOffscreenPageLimit(0);//设置缓存页数
        // viewPager.addOnPageChangeListener(pageChangeListener);
        viewPager.setScrollble(false);
    }

    /**
     * viewPager滑动监听
     */
//    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
//
//        @Override
//        public void onPageSelected(int arg0) {
//            switch (arg0) {
//
//                case QUESTION_ONE:
//                    if (!questionOneFinish) {
//                        viewPager.setScrollble(false);
//                    } else {
//                        viewPager.setScrollble(true);
//                    }
//
//                    break;
//                case QUESTION_TWO:
//                    if (!questionTwoFinish) {
//                        viewPager.setScrollble(false);
//                    } else {
//                        viewPager.setScrollble(true);
//                    }
//
//                    break;
//                case QUESTION_THREE:
//                    if (!questionThreeFinish) {
//                        viewPager.setScrollble(false);
//                    } else {
//                        viewPager.setScrollble(true);
//                    }
//                    break;
//
//            }
//
//        }
//
//        @Override
//        public void onPageScrolled(int position, float offsetPersent,
//                                   int offsetPixel) {
//
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int scrollState) {
//
//        }
//    };
    @Override
    public void initData() {
        super.initData();
        list.add(ShopCartListFragmentCopy.newInstance());//购物车
        list.add(ConfirmOrderFragmentCopy.newInstance());//确认订单页面
        list.add(ConfirmOrderFragmentCopy.newInstance());//支付成功页面

    }

    @Override
    public void initListener() {
        super.initListener();
    }

    public class CommAdapter extends FragmentPagerAdapter {


        private ArrayList<BaseEvenFrameFragment> list;

        public ArrayList<BaseEvenFrameFragment> getList() {
            return list;
        }

        public void setList(ArrayList<BaseEvenFrameFragment> list) {
            this.list = list;
        }


        public CommAdapter(ArrayList<BaseEvenFrameFragment> list) {
            super(ShopCartListActivityCopy.this.getSupportFragmentManager());
            this.list = list;
        }


        @Override
        public BaseEvenFrameFragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(ShopCartEvent event) {
        switch (event.type) {
            case ShopCartEvent.JUMP_TO_NEXT://跳转到下一页
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                break;
        }
    }

    /**
     * 支付页面返回处理
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                try {
                    String result = data.getExtras().getString("pay_result");
                    //成功
                    if (TextUtils.equals("success",result)) {//支付成功
                        showShortToast("支付成功");
                        finish();
                        CommonUtil.openActivity(PaySuccessActivity.class);//跳转到支付成功页面
                    } else {
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}