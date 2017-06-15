package com.asking.pad.app.ui.oto;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;

import com.asking.pad.app.R;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.dialog.widget.base.BaseDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jswang on 2017/4/18.
 */

public class NetUnbleDialog extends BaseDialog<NetUnbleDialog> {
    @BindView(R.id.iv_close)
    public ImageButton iv_close;


    public NetUnbleDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        widthScale(0.4f);
        showAnim(new BounceTopEnter());
        View inflate = View.inflate(mContext, R.layout.dialog_net_unable, null);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void setUiBeforShow() {

    }

    @OnClick({R.id.iv_close})
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_close:
                dismiss();
                break;
        }
    }

}
