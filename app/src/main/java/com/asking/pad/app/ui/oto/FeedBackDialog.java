package com.asking.pad.app.ui.oto;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.commom.AlertDialogUtil;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.base.BaseDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jswang on 2017/4/18.
 */

public class FeedBackDialog extends BaseDialog<FeedBackDialog> {
    @BindView(R.id.tv_cause)
    public TextView tv_cause;

    @BindView(R.id.et_content)
    public  EditText et_content;

    @BindView(R.id.btn_submit)
    public  Button btn_submit;

    View.OnClickListener mListener;

    public FeedBackDialog(Context context,View.OnClickListener mListener) {
        super(context);
        this.mListener = mListener;
    }

    @Override
    public View onCreateView() {
        widthScale(0.4f);
        showAnim(new BounceTopEnter());
        View inflate = View.inflate(mContext, R.layout.activity_mine_feed_back, null);
        ButterKnife.bind(this, inflate);
        btn_submit.setOnClickListener(mListener);
        return inflate;
    }

    @Override
    public void setUiBeforShow() {

    }

    @OnClick({R.id.rl_cause,R.id.iv_close})
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.rl_cause:
                showCourseDialog();
                break;
            case R.id.iv_close:
                dismiss();
                break;
        }
    }

    private void showCourseDialog(){
        final String[] courseArr = new String[]{"老师讲的不好","不够详细"};
        AlertDialogUtil.showNormalListDialogStringArr(mContext, courseArr, new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_cause.setText(courseArr[position]);
                AlertDialogUtil.dismiss();
            }
        });

    }

}
