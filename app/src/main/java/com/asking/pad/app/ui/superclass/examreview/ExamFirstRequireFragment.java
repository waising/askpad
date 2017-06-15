package com.asking.pad.app.ui.superclass.examreview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.commom.CBaseAdapter;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.entity.ExamRequireEntity;
import com.asking.pad.app.entity.ExerAskEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.widget.AskMathView;
import com.asking.pad.app.widget.MtGridLayout;
import com.asking.pad.app.widget.MultiStateView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by jswang on 2017/3/2.
 */

public class ExamFirstRequireFragment extends BaseFrameFragment<UserPresenter, UserModel> {
    @BindView(R.id.recyclerView)
    RecyclerView recycler;
    @BindView(R.id.multiStateView)
    MultiStateView multiStateView;

    @BindView(R.id.ll_tab1)
    View ll_tab1;
    @BindView(R.id.ll_tab2)
    View ll_tab2;
    @BindView(R.id.ll_tab3)
    View ll_tab3;

    String classId = "";
    private String index;

    private List<ExamRequireEntity> mDataList = new ArrayList<>();
    private CommAdapter mAdapter;

    public static ExamFirstRequireFragment newInstance(String classId) {
        ExamFirstRequireFragment fragment = new ExamFirstRequireFragment();
        Bundle bundle = new Bundle();
        bundle.putString("classId", classId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_exam_first_req);
        ButterKnife.bind(this, getContentView());

        Bundle bundle = getArguments();
        if (bundle != null) {
            classId = bundle.getString("classId");
        }
    }

    @Override
    public void initData() {
        super.initData();
        mAdapter = new CommAdapter(getContext(), mDataList);
    }

    @Override
    public void initView() {
        super.initView();
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(mAdapter);
    }

    @Override
    public void initLoad() {
        super.initLoad();
        if (!TextUtils.isEmpty(classId)) {
            setTabSelect(1);
        }
    }


    @OnClick({R.id.ll_tab1, R.id.ll_tab2, R.id.ll_tab3})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_tab1:
                setTabSelect(1);
                break;
            case R.id.ll_tab2:
                setTabSelect(2);
                break;
            case R.id.ll_tab3:
                setTabSelect(3);
                break;
        }
    }

    private void setTabSelect(int index) {
        this.index = index+"";
        switch (index) {
            case 1:
                if (!ll_tab1.isSelected()) {
                    ll_tab1.setSelected(true);
                    ll_tab2.setSelected(false);
                    ll_tab3.setSelected(false);
                    initLoadData(classId);
                }
                break;
            case 2:
                if (!ll_tab2.isSelected()) {
                    ll_tab1.setSelected(false);
                    ll_tab2.setSelected(true);
                    ll_tab3.setSelected(false);
                    initLoadData(classId);
                }
                break;
            case 3:
                if (!ll_tab3.isSelected()) {
                    ll_tab1.setSelected(false);
                    ll_tab2.setSelected(false);
                    ll_tab3.setSelected(true);
                    initLoadData(classId);
                }
                break;
        }
    }

    public void initLoadData(String classId) {
        this.classId = classId;
        if (multiStateView != null) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
            mPresenter.firstreviewbeigk(classId, index, new ApiRequestListener<String>() {
                @Override
                public void onResultSuccess(String res) {
                    List<ExamRequireEntity> list = JSON.parseArray(res, ExamRequireEntity.class);
                    if (list != null && list.size() > 0) {
                        mDataList.clear();
                        mDataList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);

                        final ExamRequireEntity e = mDataList.get(0);
                        mPresenter.firstreviewdiant(e._id, new ApiRequestListener<JSONObject>() {
                            @Override
                            public void onResultSuccess(JSONObject jsonRes) {
                                String res = jsonRes.getString("diant");
                                List<ExerAskEntity> list = JSON.parseArray(res, ExerAskEntity.class);
                                e.diant.addAll(list);
                                mAdapter.notifyDataSetChanged();
                            }
                        });

                    } else {
                        multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                    }
                }
            });
        }
    }

    class CommAdapter extends RecyclerView.Adapter<CommAdapter.ViewHolder> {

        private static final int UNSELECTED = -1;
        private List<ExamRequireEntity> mDatas;
        private Context mContext;
        private int selectedItem = UNSELECTED;
        int prePosition = -1;
        private ArrayMap<Integer, ExpandableLayout> exLays;

        public CommAdapter(Context context, List<ExamRequireEntity> datas) {
            this.mContext = context;
            this.mDatas = datas;
            exLays = new ArrayMap<>();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_exam_review_require, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.position = position;
            final ExamRequireEntity item = mDatas.get(position);

            holder.tv_title.setVisibility(View.GONE);
            holder.titleMathView.setVisibility(View.GONE);
            if(CommonUtil.isEnglish(item.content)){
                holder.titleMathView.setVisibility(View.VISIBLE);
                holder.titleMathView.setText("考点" + (position + 1) + "：" + Constants.SuperTutorialHtmlCss + item.content + Constants.MD_HTML_SUFFIX);
            }else{
                holder.tv_title.setVisibility(View.VISIBLE);
                holder.tv_title.setText("考点" + (position + 1) + "：" + item.content);
            }

            holder.mImageView.setSelected(false);
            holder.mExpandableLayout.collapse(false);
            exLays.put(position, holder.mExpandableLayout);
            holder.mImageView.setImageResource(R.mipmap.attr_right);

            if (item.diant != null && item.diant.size() > 0) {
                holder.ll_list.removeAllViews();
                holder.ll_list.setAdapter(new GridAdapter(item));
            }
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }


        public void setItemImg(int prePosition) {
            if (prePosition > -1) {
                exLays.get(prePosition).collapse();
                ((ImageView) ((LinearLayout) exLays.get(prePosition).getParent()).findViewById(R.id.iv_expand)).setImageResource(R.mipmap.attr_right);
            }

        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            @BindView(R.id.el_layout)
            ExpandableLayout mExpandableLayout;
            @BindView(R.id.titleMathView)
            AskMathView titleMathView;
            @BindView(R.id.tv_title)
            TextView tv_title;

            @BindView(R.id.iv_expand)
            ImageView mImageView;
            @BindView(R.id.rl_tit)
            View rl_tit;

            @BindView(R.id.ll_list)
            MtGridLayout ll_list;

            int position;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                titleMathView.formatMath();
                rl_tit.setOnClickListener(this);

                ll_list.setOrientation(LinearLayout.VERTICAL);
                ll_list.setColumnCount(1);
            }

            @Override
            public void onClick(View v) {
                final ExamRequireEntity item = mDatas.get(position);
                if (item.diant != null && item.diant.size() > 0) {
                    collapseExpandable();
                } else {
                    mPresenter.firstreviewdiant(item._id, new ApiRequestListener<JSONObject>() {
                        @Override
                        public void onResultSuccess(JSONObject jsonRes) {
                            String res = jsonRes.getString("diant");
                            List<ExerAskEntity> list = JSON.parseArray(res, ExerAskEntity.class);
                            if (list != null && list.size() > 0) {
                                item.diant.addAll(list);
                                mAdapter.notifyDataSetChanged();
                                recycler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        collapseExpandable();
                                    }
                                });
                            }
                        }
                    });
                }

            }

            public void collapseExpandable() {
                mImageView.setSelected(false);
                mExpandableLayout.collapse();
                if (position == selectedItem) {
                    selectedItem = UNSELECTED;
                } else {
                    mImageView.setSelected(true);
                    mExpandableLayout.expand();
                    selectedItem = position;
                }
                mImageView.setImageResource(R.mipmap.attr_down);
                setItemImg(prePosition);
                prePosition = selectedItem;
            }
        }


    }

    public class GridAdapter extends CBaseAdapter<ExerAskEntity> {
        ExamRequireEntity mEntity;

        class ViewHolder {
            MultiStateView multiStateView;
            AskMathView mathView;
            View btn_detail;
        }

        public GridAdapter(ExamRequireEntity mEntity) {
            super((Activity) getContext(), mEntity.diant);
            this.mEntity = mEntity;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            GridAdapter.ViewHolder holder = null;
            if (convertView == null) {
                holder = new GridAdapter.ViewHolder();
                convertView = listContainer.inflate(R.layout.item_exam_review_require_c2, null);
                holder.multiStateView = (MultiStateView)convertView.findViewById(R.id.multiStateView);
                holder.mathView = (AskMathView) convertView.findViewById(R.id.mathView);
                holder.btn_detail = convertView.findViewById(R.id.btn_detail);
                convertView.setTag(holder);
            } else {
                holder = (GridAdapter.ViewHolder) convertView.getTag();
            }
            final ExerAskEntity item = listItems.get(position);

            holder.mathView.formatMath().showWebImage(holder.multiStateView);
            holder.mathView.setText(item.subject_description_html);
            holder.btn_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.xiangxjt = item.getXiangjbj();
                    item.qiaoxqj = item.getQiaoxqj();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ExerAskEntity", item);
                    bundle.putInt("showType", 1);
                    openActivity(ExamTopicDetailsActivity.class, bundle);
                }
            });

            return convertView;
        }
    }
}

