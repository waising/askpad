package com.asking.pad.app.ui.sharespace.special;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.entity.LabelEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.sharespace.pop.GradePopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 */

public class SpecialFragment extends BaseFrameFragment<UserPresenter, UserModel> {

    @BindView(R.id.tv_selectgrade)
    TextView tv_selectgrade;

    String gradeId = "";
    String subjectId = "";

    SpecialItemFragment fragment;

    public static SpecialFragment newInstance() {
        SpecialFragment fragment = new SpecialFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_share_special);
        ButterKnife.bind(this, getContentView());
    }

    @Override
    public void initView() {
        super.initView();
        fragment = SpecialItemFragment.newInstance("","","");
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment,fragment).commit();
    }

    @OnClick({R.id.tv_selectgrade})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_selectgrade:
                new GradePopupWindow().showPopupWindowView(getActivity(),view,new GradePopupWindow.OnGradePopupListener(){
                    @Override
                    public void OnGradePopup(LabelEntity gradeEntity, LabelEntity subjectEntity) {
                        gradeId = gradeEntity.getId();
                        subjectId = subjectEntity.getId();
                        tv_selectgrade.setText(String.format("%s - %s",gradeEntity.getName(),subjectEntity.getName()));
                        fragment.reLoadData("",gradeId,subjectId);
                    }
                });
                break;
        }
    }
}
