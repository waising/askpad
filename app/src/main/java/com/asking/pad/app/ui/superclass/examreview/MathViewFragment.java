package com.asking.pad.app.ui.superclass.examreview;

import android.os.Bundle;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFragment;
import com.asking.pad.app.widget.AskMathView;
import com.asking.pad.app.widget.MultiStateView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/4/20.
 */

public class MathViewFragment extends BaseFragment {
    String math;

    @BindView(R.id.load_view)
    MultiStateView load_view;

    @BindView(R.id.math_view)
    AskMathView math_view;

    public static MathViewFragment newInstance(String math) {
        MathViewFragment fragment = new MathViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("math", math);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_base_mathview);
        ButterKnife.bind(this, getContentView());
        Bundle bundle = getArguments();
        if (bundle != null) {
            math = bundle.getString("math");
        }

        math_view.formatMath().showWebImage(load_view);
        math_view.setText(math);

    }

}
