package com.asking.pad.app.ui.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenAppCompatActivity;
import com.asking.pad.app.entity.classmedia.ClassMedia;
import com.asking.pad.app.entity.classmedia.StudyRecord;
import com.asking.pad.app.entity.mine.MineStudyRecord;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.classmedia.ClassMediaDetailsActivity;
import com.asking.pad.app.ui.classmedia.ClassPdfDetailsActivity;
import com.asking.pad.app.ui.superclass.classify.ClassifyActivty;
import com.asking.pad.app.widget.AskSwipeRefreshLayout;
import com.asking.pad.app.widget.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by jswang on 2017/6/29.
 */

public class MineStudyRecordActivity extends BaseEvenAppCompatActivity<UserPresenter, UserModel> {
    @BindView(R.id.toolBar)
    Toolbar toolBar;

    @BindView(R.id.load_view)
    MultiStateView load_view;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.swipe_layout)
    AskSwipeRefreshLayout swipeLayout;

    private ArrayList<MineStudyRecord> dataList = new ArrayList<>();

    /**
     * Recyview的adapter
     */
    CommAdapter mAdapter;
    /**
     * 开始页数，一页加载几个
     */
    int start = 0, limit = 10;

    MaterialDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_study_record);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        super.initView();
        setToolbar(toolBar, getString(R.string.shopping_record));
        mDialog = getLoadingDialog().build();

        recycler.setLayoutManager(new GridLayoutManager(this,2));
        mAdapter = new CommAdapter(this);
        recycler.setAdapter(mAdapter);

        swipeLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout ptrFrameLayout) {//加载更多
                start += limit;
                loadData();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {//下拉刷新
                start = 0;
                dataList.clear();
                swipeLayout.setMode(PtrFrameLayout.Mode.BOTH);
                loadData();
            }
        });
        load_view.setErrorRefBtnTxt2(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        load_view.setViewState(load_view.VIEW_STATE_LOADING);
        loadData();
    }

    /**
     * 请求数据
     */
    private void loadData() {
        mPresenter.userreact(start + "", limit + "", new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {
                List<MineStudyRecord> list = JSON.parseArray(resStr, MineStudyRecord.class);
                dataList.addAll(list);
                mAdapter.notifyDataSetChanged();
                if (dataList.size() > 0) {
                    load_view.setViewState(load_view.VIEW_STATE_CONTENT);
                } else {
                    load_view.setViewState(load_view.VIEW_STATE_EMPTY);
                }
                swipeLayout.refreshComplete();
            }

            @Override
            public void onResultFail() {
                load_view.setViewState(load_view.VIEW_STATE_EMPTY);
                swipeLayout.refreshComplete();
            }
        });
    }

    class CommViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.tv_title1)
        TextView tv_title1;

        @BindView(R.id.tv_time)
        TextView tv_time;

        @BindView(R.id.tv_study)
        TextView tv_study;

        @BindView(R.id.iv_expand)
        ImageView iv_expand;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CommAdapter extends RecyclerView.Adapter<CommViewHolder> {
        private Context mContext;

        public CommAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_mine_record_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(final CommViewHolder holder, final int position) {
            final MineStudyRecord e = dataList.get(position);
            holder.tv_title.setText(e.commodityName);
            holder.tv_title1.setText(e.commodityTypeName);
            holder.tv_time.setText(e.createTime);

            holder.tv_study.setText("");
            holder.iv_expand.setVisibility(View.GONE);
            if (e.childCommodityList.size() > 0) {
                holder.iv_expand.setVisibility(View.VISIBLE);
            } else {
                switch (e.getDataType()) {
                    case 0:
                        if (TextUtils.isEmpty(e.scheduleTitle)) {
                            holder.tv_study.setText("马上去学习");
                        } else {
                            holder.tv_study.setText("上次学到了：" + e.scheduleTitle);
                        }
                        break;
                    case 1:
                        if (e.schedulePercent == 0) {
                            holder.tv_study.setText("马上去学习");
                        } else {
                            holder.tv_study.setText("上次学到了：" + e.schedulePercent + "%");
                        }
                        break;
                }
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (e.childCommodityList.size() == 0 && e.getDataType() == 1) {
                        onClickItem(e);
                    }
                }
            });

            holder.iv_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MineStudyRecordDialog mDialog = MineStudyRecordDialog.newInstance(e,new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MineStudyRecord mRecord = (MineStudyRecord)v.getTag();
                            if (mRecord.childCommodityList.size() == 0) {
                                switch (mRecord.getDataType()) {
                                    case 0:
                                        ClassifyActivty.openActivity(mRecord.getClassId(), mRecord.getVersionId(), mRecord.geGradeId());
                                        break;
                                    case 1:
                                        onClickItem(mRecord);
                                        break;
                                }
                            }
                        }
                    });
                    mDialog.show(getSupportFragmentManager(),"MineStudyRecordDialog");
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

    private void onClickItem(final MineStudyRecord e) {
        mDialog.show();
        mPresenter.findByCommodityId(e.commodityId, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {
                JSONObject jsonRes = JSON.parseObject(resStr);
                ClassMedia mClassVideo = JSON.parseObject(resStr, ClassMedia.class);
                mClassVideo.setCourseDataId(jsonRes.getString("commodityId"));
                mClassVideo.setPlayPercentage((int) e.schedulePercent);
                if (mClassVideo.getIsPresent() == 0) {
                    ClassMediaDetailsActivity.openActivity(mClassVideo);
                } else {
                    ClassPdfDetailsActivity.openActivity(mClassVideo);
                }
                mDialog.dismiss();
            }
        });
    }

    public void onEventMainThread(StudyRecord event) {
        for (MineStudyRecord e : dataList) {
            if (TextUtils.equals(event.getCourseDataId(), e.commodityId) && e.getDataType() == 1) {
                e.schedulePercent = event.getPlayPercentage();
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }



}
