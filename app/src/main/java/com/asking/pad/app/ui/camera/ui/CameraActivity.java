package com.asking.pad.app.ui.camera.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.asking.pad.app.R;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;

public class CameraActivity extends FragmentActivity {


    private int evenType;


    private int fromWhere;
    /**
     * 从一对一答疑跳转过来
     */
    public final static int FROM_OTHER = 0;
    /**
     * 从笔记跳转过来
     */
    public final static int FROM_NOTE = 1;

    private CameraMultiFragment cameraMultiFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_main);
        evenType = this.getIntent().getIntExtra("evenType", 0);
        fromWhere = this.getIntent().getIntExtra("fromWhere", 0);

        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        cameraMultiFragment = CameraMultiFragment.newInstance(evenType,fromWhere);

        tr.add(R.id.main, cameraMultiFragment).commitAllowingStateLoss();
    }

    public void goToPage(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction trx = fragmentManager.beginTransaction();
        trx.setTransition(FragmentTransaction.TRANSIT_NONE);
        Fragment fragmentById = fragmentManager.findFragmentById(R.id.main);
        if (fragmentById != null) {
            trx.remove(fragmentById);
        }
        trx.add(R.id.main, fragment).commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1001 && data != null) {
            String imgPath = data.getStringExtra("imgPath");
            int orient = BitmapUtil.getInfoOrientation(imgPath);
            Bundle bundle = new Bundle();
            bundle.putString("pic_url", imgPath);
            bundle.putInt("pic_orient", 2);
            bundle.putInt("pic_info_orient", orient);
            bundle.putInt("pic_index", 1);
            bundle.putInt("pic_res", 1);
            bundle.putInt("evenType", evenType);
            bundle.putInt("fromWhere", fromWhere);
            Fragment f = CameraPublishFragment.newInstance(bundle);
            goToPage(f);
        }
    }

    public static void openActivity(Activity activity, int evenType, int fromWhere) {
        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra("evenType", evenType);
        intent.putExtra("fromWhere", fromWhere);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }

}