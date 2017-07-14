package com.asking.pad.app.ui.main;

import android.os.Bundle;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/4/10.
 */

public class OtoMainFragment extends BaseFragment {

    public static OtoMainFragment newInstance() {
        OtoMainFragment fragment = new OtoMainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_oto_main);
        ButterKnife.bind(this, getContentView());
    }

    @Override
    public void initView() {
        super.initView();

    }
}
