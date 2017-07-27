package com.asking.pad.app.ui.mine;

import android.content.Context;
import android.view.View;

import com.asking.pad.app.R;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.dialog.widget.base.BaseDialog;

import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/4/18.
 */

public class AppDownQRCodeDialog extends BaseDialog<AppDownQRCodeDialog> {

    public AppDownQRCodeDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        widthScale(0.4f);
        showAnim(new BounceTopEnter());
        View inflate = View.inflate(mContext, R.layout.dialog_app_down_qrcode, null);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void setUiBeforShow() {

    }

}
