package com.asking.pad.app.ui.personalcenter.oto;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.asking.pad.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 答疑记录删除的弹窗
 * create by linbin
 */
public class AnswerRecordDelDialog extends DialogFragment {
    @BindView(R.id.tv_cancle)
    TextView tvCancle;

    @BindView(R.id.tv_save)
    TextView vSave;

    @BindView(R.id.tv_msg)
    TextView tv_msg;

    Unbinder unbinder;

    private DelListner mListner;

    private int mPosition;

    private String id;

    String msg;

    public static AnswerRecordDelDialog newInstance(String msg) {
        AnswerRecordDelDialog fragment = new AnswerRecordDelDialog();
        Bundle bundle = new Bundle();
        bundle.putString("msg",msg);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            msg = bundle.getString("msg");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题栏
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 设置背景透明
        View view = inflater.inflate(R.layout.layout_dialog_answer_del, null);
        unbinder = ButterKnife.bind(this, view);

        if(!TextUtils.isEmpty(msg)){
            tv_msg.setText(msg);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_cancle, R.id.tv_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancle://取消
                dismiss();
                break;
            case R.id.tv_save://保存
                if (mListner != null) {
                    mListner.del(mPosition, id,this);
                }
                break;

        }
    }

    /**
     * 修改密码
     */
    public interface DelListner {
        void del(int position, String id,AnswerRecordDelDialog delDialog);
    }

    public void setDelListner(DelListner delListner) {
        mListner = delListner;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public void setId(String id) {
        this.id = id;
    }

}
