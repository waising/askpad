package com.asking.pad.app.ui.superclass.examreview.classex;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFragment;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.entity.LabelEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jswang on 2017/3/2.
 */

public class ClassExamDialogFragment extends BaseFragment {
    @BindView(R.id.rv_difficulty)
    RecyclerView rv_difficulty;

    @BindView(R.id.et_choice_count)
    EditText et_choice_count;

    @BindView(R.id.et_filling_count)
    EditText et_filling_count;

    @BindView(R.id.et_solving_count)
    EditText et_solving_count;

    String difficultyId;

    /**
     * 0-一轮复习  1-二轮复习
     */
    int typeFrom;

    String classId;

    ArrayList<LabelEntity> gradeList = new ArrayList<>();

    public static ClassExamDialogFragment newInstance(int typeFrom,String classId) {
        ClassExamDialogFragment fragment = new ClassExamDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("classId", classId);
        bundle.putInt("typeFrom",typeFrom);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            classId = bundle.getString("classId");
            typeFrom = bundle.getInt("typeFrom",0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setContentView(R.layout.fragment_exam_class);
        ButterKnife.bind(this, getContentView());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        super.initView();

        difficultyId = "1";

        gradeList.add(new LabelEntity("1", getString(R.string.onlinetest_no_level), true));
        gradeList.add(new LabelEntity("2", getString(R.string.onlinetest_basic_level)));
        gradeList.add(new LabelEntity("3", getString(R.string.onlinetest_mid_level)));
        gradeList.add(new LabelEntity("4", getString(R.string.onlinetest_high_level)));

        CommAdapter versionAdapter = new CommAdapter(getActivity());
        rv_difficulty.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        rv_difficulty.setAdapter(versionAdapter);
    }

    @OnClick({ R.id.btn_submit})
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_submit:
                String choice_count = CommonUtil.isEmpty(et_choice_count.getText().toString(),"0");
                String filling_count = CommonUtil.isEmpty(et_filling_count.getText().toString(),"0");
                String solving_count = CommonUtil.isEmpty(et_solving_count.getText().toString(),"0");

                Bundle bundle = new Bundle();
                bundle.putString("classId",classId);
                bundle.putString("difficultyId",difficultyId);
                bundle.putString("choice_count",choice_count);
                bundle.putString("filling_count",filling_count);
                bundle.putString("solving_count",solving_count);
                bundle.putInt("typeFrom",typeFrom);

                CommonUtil.openActivity(ClassExamActivity.class,bundle);
                break;
        }
    }

    public void initLoadData(String classId) {
        this.classId = classId;
    }

    public class CommAdapter extends RecyclerView.Adapter<CommAdapter.CommViewHolder> {
        private Context mContext;

        public CommAdapter(Context context){
            this.mContext = context;
        }

        @Override
        public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_course_view,parent,false));
        }

        @Override
        public void onBindViewHolder(CommViewHolder holder, int position) {
            final LabelEntity e = gradeList.get(position);
            holder.item_name.setSelected(e.getSelect());
            holder.item_name.setText(e.getName());
            holder.item_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (LabelEntity ii : gradeList) {
                        ii.setSelect(false);
                    }
                    e.setSelect(true);
                    notifyDataSetChanged();
                    difficultyId = e.getId();
                }
            });
        }

        @Override
        public int getItemCount() {
            return gradeList.size();
        }

        class CommViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.item_name)
            TextView item_name;

            public CommViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }

    }
}
