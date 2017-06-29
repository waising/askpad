package com.asking.pad.app.ui.pay;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.entity.LabelEntity;
import com.asking.pad.app.entity.pay.GradePay;
import com.asking.pad.app.entity.pay.SubjectPay;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.classmedia.PayClassMediaActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 超级辅导课购买
 * Created by jswang on 2017/4/20.
 */

public class PaySuperClassFragment extends BaseFrameFragment<UserPresenter, UserModel> {
    @BindView(R.id.rv_subject)
    RecyclerView rv_subject;

    @BindView(R.id.rv_timelimit)
    RecyclerView rv_timelimit;

    @BindView(R.id.rv_grade)
    RecyclerView rv_grade;

    @BindView(R.id.tv_pay_price)
    TextView tv_pay_price;

    ArrayList<LabelEntity> subjectList = new ArrayList<>();
    ArrayList<LabelEntity> timelimitList = new ArrayList<>();
    ArrayList<GradePay> gradeList = new ArrayList<>();

    CommAdapter subjectAdapter;
    PayGradeAdapter gradeAdapter;

    String subjectId;
    GradePay mGradePay;
    int timeLimit = 12;

    public static PaySuperClassFragment newInstance() {
        PaySuperClassFragment fragment = new PaySuperClassFragment();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pay_superclass);
        ButterKnife.bind(this, getContentView());
    }

    @Override
    public void initView() {
        super.initView();

        rv_subject.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        subjectAdapter = new CommAdapter(this.getActivity(), subjectList, new OnCommItemListener() {
            @Override
            public void OnCommItem(LabelEntity e) {
                subjectId = e.getId();
                initGradeData();
            }
        });
        rv_subject.setAdapter(subjectAdapter);

        timelimitList.add(new LabelEntity(12, "12个月", true));
        timelimitList.add(new LabelEntity(6, "6个月"));
        rv_timelimit.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rv_timelimit.setAdapter(new CommAdapter(this.getActivity(), timelimitList, new OnCommItemListener() {
            @Override
            public void OnCommItem(LabelEntity e) {
                timeLimit = e.getIcon();
                initGradeData();
            }
        }));

        rv_grade.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        gradeAdapter = new PayGradeAdapter(this.getActivity(), gradeList, new OnCommItemListener() {
            @Override
            public void OnCommItem(GradePay e) {
                setPayData(e);
            }
        });
        rv_grade.setAdapter(gradeAdapter);

        initSubjectData();
    }

    private void initSubjectData() {
        mPresenter.productType("TC-TBKT", new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                List<SubjectPay> list = JSON.parseArray(res, SubjectPay.class);
                subjectList.clear();
                for (int i = 0; i < list.size(); i++) {
                    SubjectPay e = list.get(i);
                    if (i == 0) {
                        subjectList.add(new LabelEntity(e.packageTypeId, e.packageTypeName, true));
                        subjectId = e.packageTypeId;
                        initGradeData();
                    } else {
                        subjectList.add(new LabelEntity(e.packageTypeId, e.packageTypeName, false));
                    }
                }
                subjectAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initGradeData() {
        mPresenter.getCommodityList(subjectId, timeLimit, 0, 100, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                List<GradePay> list = JSON.parseArray(res, GradePay.class);
                gradeList.clear();
                gradeList.addAll(list);
                if (gradeList.size() > 0) {
                    GradePay e = gradeList.get(0);
                    e.isSelect = true;
                    setPayData(e);
                }
                gradeAdapter.notifyDataSetChanged();
            }
        });
    }

    public void setPayData(GradePay e) {
        mGradePay = e;
        BigDecimal integral = new BigDecimal(mGradePay.packagePrice);
        double money = integral.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        tv_pay_price.setText(String.format("%s  元", money));
    }

    @OnClick({R.id.btn_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pay://支付
                if (mGradePay != null) {
                    PayClassMediaActivity.openActivity(getActivity(), mGradePay);
                }
                break;
        }

    }

}
