package com.asking.pad.app.ui.camera.views;

import android.hardware.Camera;

/**
 * Created by jswang on 2017/4/17.
 */

public interface OnCameraActionCallback {
    void onCameraMoving();

    void onCameraOpenFailed();

    void onFocusEnd(boolean z);

    void onFocusStarted();

    void onFocusStarted(int i, int i2);

    void onPreviewChanged(byte[] bArr, Camera camera);

    void onTakePictureStarted();
}
