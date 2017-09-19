package com.asking.pad.app.ui.commom;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.widget.AskMathWebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jswang on 2017/4/18.
 */

public class WebDialog extends DialogFragment {
    @BindView(R.id.math_web)
    AskMathWebView math_web;

    int type;
    String gid;

    public UserPresenter mPresenter;
    public UserModel mModel;

    public static WebDialog newInstance(int type, String gid) {
        WebDialog fragment = new WebDialog();
        Bundle bunle = new Bundle();
        bunle.putInt("type", type);
        bunle.putString("gid", gid);
        fragment.setArguments(bunle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            gid = bundle.getString("gid");
            type = bundle.getInt("type");
        }

        mPresenter = new UserPresenter();
        mModel = new UserModel();
        mPresenter.mContext = this.getContext();
        mPresenter.setModel(mModel);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.dialog_web_view, null);
        ButterKnife.bind(this, view);
        if (type == 0) {
            mPresenter.tipdatalink(gid, new ApiRequestListener<String>() {
                @Override
                public void onResultSuccess(String resStr) {
                    JSONObject jsonRes = JSON.parseObject(resStr);
                    math_web.setText(jsonRes.getString("tip_link_content_html"));
                }
            });
        } else {
            mPresenter.getQuestionSubjectContent(gid, new ApiRequestListener<String>() {
                @Override
                public void onResultSuccess(String resStr) {
                    JSONObject jsonRes = JSON.parseObject(resStr);
                    math_web.setText(jsonRes.getString("tip_link_content_html"));
                }
            });
        }
        return view;
    }

    @OnClick({R.id.iv_close})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) mPresenter.onDestroy();
        super.onDestroy();
    }

}
