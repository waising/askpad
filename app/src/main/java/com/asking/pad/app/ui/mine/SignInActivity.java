package com.asking.pad.app.ui.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.widget.calendar.CalendarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * Created by jswang on 2017/4/24.
 */

public class SignInActivity extends BaseFrameActivity<UserPresenter, UserModel> {

    @BindView(R.id.calen)
    CalendarView calen;

    @BindView(R.id.tv_siginin)
    TextView tv_siginin;

    @BindView(R.id.tv_siginin_tip)
    TextView tv_siginin_tip;

    private List<Integer> siginDates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        super.initView();

        tv_siginin.setSelected(true);
        mPresenter.checkTodaySign(new ApiRequestListener<String>(){
            @Override
            public void onResultSuccess(String res) {
                String flag = parseObject(res).getString("flag");
                if(TextUtils.equals(flag,"0")){
                    tv_siginin_tip.setText("");
                }else if(TextUtils.equals(flag,"1")){
                    tv_siginin.setSelected(false);
                    tv_siginin_tip.setText("今日可领 0.5 阿思币");
                }else if(TextUtils.equals(flag,"2")){
                    tv_siginin.setSelected(false);
                    tv_siginin_tip.setText("今日可领 3.5 阿思币");
                }
            }
        });

        mPresenter.dailySign(new ApiRequestListener<JSONObject>(){
            @Override
            public void onResultSuccess(JSONObject  resMap) {
                List<Integer> list = JSON.parseArray(resMap.getString("signDates"),Integer.class);
                siginDates.clear();
                siginDates.addAll(list);
                calen.reCalenView(list);
            }
        });
    }

    @OnClick({R.id.tv_siginin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_siginin:
                if(!tv_siginin.isSelected()){
                    mPresenter.sign(new ApiRequestListener<String>(){
                        @Override
                        public void onResultSuccess(String res) {
                            JSONObject jsonRes = JSON.parseObject(res);
                            String flag = jsonRes.getString("flag");
                            if(!TextUtils.isEmpty(flag)){
                                if(TextUtils.equals("1",flag)){
                                    setMoney(0.5);
                                }else if(TextUtils.equals("2",flag)){
                                    setMoney(3.5);
                                }
                            }
                            showShortToast(jsonRes.getString("msg"));
                        }
                    });
                }
                break;
        }
    }

    private void setMoney(double money){
        siginDates.add(calen.todayDay);
        calen.reCalenView(siginDates);

        tv_siginin.setSelected(true);
        tv_siginin_tip.setText("");

        AppContext.getInstance().getUserEntity().setIntegral(money + AppContext.getInstance().getUserEntity().getIntegral());
        AppContext.getInstance().saveUserData(AppContext.getInstance().getUserEntity());

        EventBus.getDefault().post(new AppEventType(AppEventType.RE_USER_INFO_REQUEST));
    }
}
