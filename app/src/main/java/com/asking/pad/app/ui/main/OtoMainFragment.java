package com.asking.pad.app.ui.main;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFragment;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.NetworkUtils;
import com.asking.pad.app.ui.camera.ui.CameraActivity;
import com.asking.pad.app.ui.oto.NetUnbleDialog;
import com.asking.pad.app.ui.personalcenter.oto.RecordAnswerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jswang on 2017/4/10.
 */

public class OtoMainFragment extends BaseFragment {
    @BindView(R.id.toolBar)
    Toolbar mToolbar;

    @BindView(R.id.iv_oto_explain)
    View iv_oto_explain;

    private PopupWindow popupwindow;

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

        mToolbar.setNavigationIcon(R.mipmap.ic_oto_main_tea);
        mToolbar.setTitle("1对1答疑");
        mToolbar.inflateMenu(R.menu.menu_oto_main);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                CommonUtil.openActivity(RecordAnswerActivity.class);
                return false;
            }
        });
    }

    public void showPopupWindowView() {
        View customView = getActivity().getLayoutInflater().inflate(R.layout.layout_oto_explain_popview, null, false);

        popupwindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupwindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        popupwindow.setOutsideTouchable(true);

        popupwindow.showAsDropDown(iv_oto_explain, 0, 5);
    }

    @OnClick({R.id.ll_oto, R.id.iv_oto_explain})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_oto://一对一答疑
                if (NetworkUtils.isNetworkAvailable()) {
                    CameraActivity.openActivity(getActivity(), AppEventType.HOME_CAMERA_REQUEST, CameraActivity.FROM_OTHER);
                } else {
                    NetUnbleDialog mDialog = new NetUnbleDialog(getActivity());
                    mDialog.show();
                }
                break;
            case R.id.iv_oto_explain:
                showPopupWindowView();
                break;
        }
    }
}
