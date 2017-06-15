package com.asking.pad.app.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.bugtags.library.Bugtags;
import com.jaeger.library.StatusBarUtil;

import java.util.Map;

/**
 * Created by jswang on 2017/4/6.
 */

public class BaseActivity extends AppCompatActivity implements BaseFuncIml{
    public Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注：回调 1
        Bugtags.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注：回调 2
        Bugtags.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3
        Bugtags.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        setStatusBar();
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.theme_blue_theme),0);
    }

    public void showShortToast(String msg) {
        if(!TextUtils.isEmpty(msg))
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**根据String的Id来弹出提示*/
    protected void showShortToast(int msgId) {
        Toast.makeText(this, getResources().getString(msgId), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initData();
        initView();
        initListener();
        initLoad();
    }

    public Map<String, String> getAuth() {
        ArrayMap<String, String> header = new ArrayMap<>();
        header.put("Authorization", "Bearer " + AppContext.getInstance().getToken());
        return header;
    }

    protected  void setToolbar(Toolbar toolbar, String title) {
        toolbar.setTitle(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initLoad() {

    }

    private MaterialDialog.Builder mLoadingDialog;
    public MaterialDialog.Builder getLoadingDialog() {
        if(mLoadingDialog == null){
            mLoadingDialog = new MaterialDialog.Builder(this);
            mLoadingDialog.title("请稍候");
            mLoadingDialog.progress(true, 0);
        }
        return mLoadingDialog;
    }

    protected void openActivity(Class<? extends BaseActivity> toActivity) {
        openActivity(toActivity, null);
    }

    protected void openActivity(Class<? extends BaseActivity> toActivity, Bundle parameter) {
        Intent intent = new Intent(this, toActivity);
        if (parameter != null) {
            intent.putExtras(parameter);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter_left_in,R.anim.enter_left_out);
    }
    public void openResultActivity(Class<? extends BaseActivity> toActivity, Bundle parameter) {
        Intent intent = new Intent(this, toActivity);
        if (parameter != null) {
            intent.putExtras(parameter);
        }
        startActivityForResult(intent,0);
        overridePendingTransition(R.anim.enter_left_in,R.anim.enter_left_out);
    }
}
