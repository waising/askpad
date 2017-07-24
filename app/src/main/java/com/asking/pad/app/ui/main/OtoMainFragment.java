package com.asking.pad.app.ui.main;

import android.os.Bundle;
import android.view.View;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFragment;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.NetworkUtils;
import com.asking.pad.app.ui.camera.ui.CameraActivity;
import com.asking.pad.app.ui.oto.NetUnbleDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

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



    @OnClick({R.id.iv_oto})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_oto://一对一答疑
                if (NetworkUtils.isNetworkAvailable()) {
                    CameraActivity.openActivity(getActivity(), AppEventType.HOME_CAMERA_REQUEST,CameraActivity.FROM_OTHER);
                }else{
                    NetUnbleDialog mDialog = new NetUnbleDialog(getActivity());
                    mDialog.show();
                }
                break;
        }
    }
}
