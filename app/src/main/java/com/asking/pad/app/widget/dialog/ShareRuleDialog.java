package com.asking.pad.app.widget.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.asking.pad.app.R;

/**
 * 分享规则的弹窗
 * create by linbin
 */
public class ShareRuleDialog extends DialogFragment {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题栏
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 设置背景透明
        View view = inflater.inflate(R.layout.layout_dialog_share_rule, null);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
