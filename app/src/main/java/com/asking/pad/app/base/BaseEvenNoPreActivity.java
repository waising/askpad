package com.asking.pad.app.base;

import android.os.Bundle;

import de.greenrobot.event.EventBus;

/**
 * Created by jswang on 2017/2/22.
 */

public abstract class BaseEvenNoPreActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
