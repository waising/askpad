package com.asking.pad.app.ui.main;

import android.content.Context;
import android.widget.ImageView;

import com.asking.pad.app.entity.BannerInfo;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.asking.pad.app.widget.banner.AutoPagerAdapter;

/**
 * Created by jswang on 2017/7/24.
 */

public class BannerPagerAdapter extends AutoPagerAdapter {
    public BannerPagerAdapter(Context context) {
        super(context);
    }

    @Override
    public void getView(ImageView target, BannerInfo aVar) {
        target.setScaleType(ImageView.ScaleType.FIT_XY);
        BitmapUtil.displayImage(aVar.imageUrl, target);
    }
}
