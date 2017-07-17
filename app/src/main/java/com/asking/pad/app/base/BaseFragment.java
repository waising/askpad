package com.asking.pad.app.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.asking.pad.app.R;

import rx.Subscription;

/**
 * Created by Jun on 2016/5/2.
 */
public class BaseFragment extends Fragment implements BaseFuncIml {
    protected  BaseFragment mthis;

    private View mContentView;

    private ViewGroup container;

    private MaterialDialog.Builder mLoadingDialog;

    private Subscription mSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mthis = this;
        initData();
        initView();
        initListener();
        initLoad();
        this.container = container;
        return mContentView;
    }

    public void setContentView(int viewId) {
        this.mContentView = getActivity().getLayoutInflater().inflate(viewId, container, false);
    }

    public View getContentView() {
        return mContentView;
    }

    protected void showShortToast(String pMsg) {
        Toast.makeText(getActivity(), pMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        initLoadingDialog();
    }

    @Override
    public void initListener() {
    }

    @Override
    public void initLoad() {

    }

    protected void openActivity(Class<? extends BaseActivity> toActivity) {
        openActivity(toActivity, null);
    }

    protected void openActivity(Class<? extends BaseActivity> toActivity, Bundle parameter) {
        Intent intent = new Intent(getActivity(), toActivity);
        if (parameter != null) {
            intent.putExtras(parameter);
        }
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.enter_left_in,R.anim.enter_left_out);
    }

    private void initLoadingDialog() {
        mLoadingDialog = new MaterialDialog.Builder(getActivity());
        mLoadingDialog.title("请稍候");
        mLoadingDialog.progress(true, 0);
    }

    public MaterialDialog.Builder getLoadingDialog() {
        return mLoadingDialog;
    }

    protected void setToolbar(Toolbar toolbar, String title) {
        toolbar.setTitle(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

}