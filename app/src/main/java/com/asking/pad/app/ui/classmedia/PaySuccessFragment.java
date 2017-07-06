package com.asking.pad.app.ui.classmedia;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.asking.pad.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaySuccessFragment extends DialogFragment {
    @BindView(R.id.toolBar)
    Toolbar toolBar;

    public static PaySuccessFragment newInstance() {
        PaySuccessFragment fragment = new PaySuccessFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题栏
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 设置背景透明
        View view = inflater.inflate(R.layout.fragment_pay_succeed, null);
        ButterKnife.bind(this, view);
        setCancelable(false);

        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
    }

    @OnClick({R.id.tv_check_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_check_order://确认
                getActivity().finish();
                break;
        }
    }
}
