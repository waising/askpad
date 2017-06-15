package com.asking.pad.app.ui.personalcenter;

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

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 删除的弹窗
 * create by linbin
 */

public class DeleteDialog extends DialogFragment {


    Unbinder unbinder;


    private DeleteListner mListner;

    private int position;
    private String id;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题栏
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 设置背景透明
        View view = inflater.inflate(R.layout.dialog_delete_layout, null);
        unbinder = ButterKnife.bind(this, view);
        initData(view);
        return view;
    }

    /**
     * @param view
     */
    private void initData(View view) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_ok, R.id.btn_cancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok://确认
                if (mListner != null) {
                    mListner.ok(position, id, DeleteDialog.this);
                }
                break;
            case R.id.btn_cancle://取消
                dismiss();
                break;
        }
    }


    /**
     * 笔记回调监听
     */
    public interface DeleteListner {
        void ok(int position, String id, DeleteDialog deleteDialog);//确认删除


    }


    public void setPosition(int position) {
        this.position = position;
    }


    public void setId(String id) {
        this.id = id;
    }


    /**
     * 设置删除监听
     *
     * @param deleteListner
     */
    public void setDeleteListner(DeleteListner deleteListner) {
        mListner = deleteListner;
    }


}
