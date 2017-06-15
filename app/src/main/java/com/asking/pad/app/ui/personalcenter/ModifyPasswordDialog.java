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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.asking.pad.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 修改密码的弹窗
 * create by linbin
 */

public class ModifyPasswordDialog extends DialogFragment {

    @BindView(R.id.edt_original_password)
    EditText edtOriginalPassword;
    @BindView(R.id.edt_new_password)
    EditText edtNewPassword;
    @BindView(R.id.edt_confirm_password)
    EditText edtConfirmPassword;
    @BindView(R.id.tv_cancle)
    TextView tvCancle;
    @BindView(R.id.tv_save)
    TextView vSave;

    @BindView(R.id.iv_close)
    ImageView tvClose;

    Unbinder unbinder;


    private ChangePassWordListner mListner;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题栏
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 设置背景透明
        View view = inflater.inflate(R.layout.layout_dialog_modify_password, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_cancle, R.id.tv_save, R.id.iv_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancle://取消
                dismiss();
                break;
            case R.id.tv_save://保存
                String originalPassword = edtOriginalPassword.getText().toString();
                String newPassword = edtNewPassword.getText().toString();
                String confirmPassword = edtConfirmPassword.getText().toString();
                if (mListner != null) {
                    mListner.changePassword(originalPassword, newPassword, confirmPassword);
                }
                break;
            case R.id.iv_close://关闭
                dismiss();
                break;
        }
    }

    /**
     * 修改密码
     */
    interface ChangePassWordListner {
        void changePassword(String originalPassword, String newPassWord, String confirmPassword);
    }

    public void setChangePasswordListner(ChangePassWordListner changePassWordListner) {
        mListner = changePassWordListner;
    }
}
