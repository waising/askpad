package com.asking.pad.app.ui.start;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.asking.pad.app.mvp.OnCountDownListener;
import com.asking.pad.app.mvp.RxManager;
import com.asking.pad.app.ui.main.MainActivity;
import com.bugtags.library.Bugtags;

/**
 * Created by jswang on 2017/4/21.
 */

public class SplashActivity extends Activity {
    public RxManager mRxManager = new RxManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRxManager.countDownPresenter(5, new OnCountDownListener() {
            @Override
            public void onComplete() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        mRxManager.clear();
        super.onDestroy();
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
}
