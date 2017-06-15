package com.asking.pad.app.ui.pay;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.entity.CourseEntity;
import com.asking.pad.app.entity.LabelEntity;
import com.asking.pad.app.entity.PayClassEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.google.gson.reflect.TypeToken;

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

    @BindView(R.id.rv_textbook)
    RecyclerView rv_textbook;

    @BindView(R.id.rv_timelimit)
    RecyclerView rv_timelimit;

    @BindView(R.id.rv_grade)
    RecyclerView rv_grade;

    @BindView(R.id.tv_pay_price)
    TextView tv_pay_price;

    ArrayList<LabelEntity> subjectList = new ArrayList<>();
    ArrayList<LabelEntity> timelimitList = new ArrayList<>();
    ArrayList<CourseEntity> textbookList = new ArrayList<>();
    ArrayList<LabelEntity> gradeList = new ArrayList<>();

    PayTextBookAdapter textbookAdapter;
    CommAdapter gradeAdapter;

    String subjectId = Constants.subjectKeys[0];
    int textbookId;
    String gradeId;
    int month = 12;

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

        subjectList.add(new LabelEntity(Constants.subjectKeys[0], Constants.getClassName(getActivity(), Constants.subjectKeys[0]), true));
        subjectList.add(new LabelEntity(Constants.subjectKeys[1], Constants.getClassName(getActivity(), Constants.subjectKeys[1])));
        subjectList.add(new LabelEntity(Constants.subjectKeys[2], Constants.getClassName(getActivity(), Constants.subjectKeys[2])));
        subjectList.add(new LabelEntity(Constants.subjectKeys[3], Constants.getClassName(getActivity(), Constants.subjectKeys[3])));
        rv_subject.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        rv_subject.setAdapter(new CommAdapter(this.getActivity(), subjectList, new OnCommItemListener() {
            @Override
            public void OnCommItem(LabelEntity e) {
                subjectId = e.getId();
                initPayData();
            }
        }));

        timelimitList.add(new LabelEntity(12, "12个月", true));
        timelimitList.add(new LabelEntity(6, "6个月"));
        rv_timelimit.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rv_timelimit.setAdapter(new CommAdapter(this.getActivity(), timelimitList, new OnCommItemListener() {
            @Override
            public void OnCommItem(LabelEntity e) {
                month = e.getIcon();
                mPresenter.getCommodityList(subjectId, month, 2, gradeListener);
            }
        }));

        rv_textbook.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        textbookAdapter = new PayTextBookAdapter(this.getActivity(), textbookList, new OnCommItemListener() {
            @Override
            public void OnCommItem(CourseEntity e) {
                textbookId = e.getVersionId();
            }
        });
        rv_textbook.setAdapter(textbookAdapter);

        rv_grade.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        gradeAdapter = new CommAdapter(this.getActivity(), gradeList, new OnCommItemListener() {
            @Override
            public void OnCommItem(LabelEntity e) {
                setPayData(e.getId(), e.values[0]);
            }
        });
        rv_grade.setAdapter(gradeAdapter);

        initPayData();
    }

    ApiRequestListener gradeListener = new ApiRequestListener<String>() {
        @Override
        public void onResultSuccess(String res) {
            List<PayClassEntity> list = CommonUtil.parseDataToList(res, new TypeToken<List<PayClassEntity>>() {
            });
            gradeList.clear();
            for (int i = 0; i < list.size(); i++) {
                PayClassEntity e = list.get(i);
                if (i == 0) {
                    gradeList.add(new LabelEntity(e.getId(), e.getCourseContent(), true, e.getCommodityPrice()));
                    setPayData(e.getId(), e.getCommodityPrice());
                } else {
                    gradeList.add(new LabelEntity(e.getId(), e.getCourseContent(), false, e.getCommodityPrice()));
                }
            }
            gradeAdapter.notifyDataSetChanged();
        }
    };

    public void setPayData(String id, String price) {
        gradeId = id;
        BigDecimal integral = new BigDecimal(price);
        double money = integral.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        tv_pay_price.setText(String.format("%s  元", money));
    }

    private void initPayData() {
        mPresenter.versionClassic(subjectId, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                List<CourseEntity> list = CommonUtil.parseDataToList(res, new TypeToken<List<CourseEntity>>() {
                });
                textbookList.clear();
                textbookList.addAll(list);
                if (textbookList.size() > 0) {
                    list.get(0).isSelect = true;
                    textbookId = textbookList.get(0).getVersionId();
                }
                textbookAdapter.notifyDataSetChanged();
            }
        });
        mPresenter.getCommodityList(subjectId, month, 2, gradeListener);
    }

    @OnClick({R.id.btn_pay, R.id.tv_add_shopcart})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pay://支付
                if (TextUtils.isEmpty(gradeId) || textbookId == 0) {
                    showShortToast("数据参数不完整");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("commodityId", gradeId);
                bundle.putInt("versionId", textbookId);
                openActivity(PayActivity.class, bundle);
                break;
            case R.id.tv_add_shopcart://加入购物车
                addShopcart();
                break;
        }

    }

    /**
     * 加入购物车请求
     */
    private void addShopcart() {
        mPresenter.presenterAddShopCartSuper(gradeId, textbookId, new ApiRequestListener<JSONObject>() {//题目的id和类型请求
            @Override
            public void onResultSuccess(JSONObject object) {//数据返回成功
                showShortToast(getResources().getString(R.string.add_shop_cart_succeed));
            }
        });

    }


}
