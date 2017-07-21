package com.asking.pad.app.ui.sharespace.special;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.commom.DateUtil;
import com.asking.pad.app.entity.sharespace.ShareSpecial;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.asking.pad.app.ui.sharespace.TeacherSpaceActivity;
import com.asking.pad.app.widget.AskSwipeRefreshLayout;
import com.asking.pad.app.widget.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 *
 */

public class SpecialItemFragment extends BaseFrameFragment<UserPresenter, UserModel> {
    @BindView(R.id.load_view)
    MultiStateView load_view;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.swipe_layout)
    AskSwipeRefreshLayout swipeLayout;

    ArrayList<ShareSpecial> dataList = new ArrayList<>();
    CommAdapter mAdapter;

    String mine = "";
    String teacherId = "";
    String gradeId = "";
    String subjectId = "";
    int start = 0;
    int limit = 10;

    public static SpecialItemFragment newInstance(String mine, String teacherId, String gradeId, String subjectId) {
        SpecialItemFragment fragment = new SpecialItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mine", mine);
        bundle.putString("teacherId", teacherId);
        bundle.putString("gradeId", gradeId);
        bundle.putString("subjectId", subjectId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_share_special_item);
        ButterKnife.bind(this, getContentView());
        Bundle bundle = getArguments();
        if (bundle != null) {
            mine = bundle.getString("mine");
            teacherId = bundle.getString("teacherId");
            gradeId = bundle.getString("gradeId");
            subjectId = bundle.getString("subjectId");
        }
    }

    @Override
    public void initView() {
        super.initView();

        GridLayoutManager mgr = new GridLayoutManager(getActivity(), 3);
        recycler.setLayoutManager(mgr);
        mAdapter = new CommAdapter();
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

    public void reLoadData(String teacherId, String gradeId, String subjectId) {
        this.teacherId = teacherId;
        this.gradeId = gradeId;
        this.subjectId = subjectId;
        load_view.setViewState(load_view.VIEW_STATE_LOADING);
        start = 0;
        dataList.clear();
        loadData();
    }

    public void loadData() {
        mPresenter.communionapi(mine, teacherId, gradeId, subjectId, start, limit, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                JSONObject jsonRes = JSONObject.parseObject(res);
                List<ShareSpecial> list = JSON.parseArray(jsonRes.getString("content"), ShareSpecial.class);
                if (list != null && list.size() > 0) {
                    dataList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                }
                if (dataList.size() > 0) {
                    load_view.setViewState(load_view.VIEW_STATE_CONTENT);
                } else {
                    load_view.setViewState(load_view.VIEW_STATE_EMPTY);
                }
                swipeLayout.refreshComplete();
            }

            @Override
            public void onResultFail() {
                load_view.setViewState(load_view.VIEW_STATE_ERROR);
                swipeLayout.refreshComplete();
            }
        });
    }

    class CommViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView iv_avatar;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_state)
        TextView tv_state;

        @BindView(R.id.iv_bg)
        ImageView iv_bg;

        @BindView(R.id.tv_gread)
        TextView tv_gread;

        @BindView(R.id.tv_visitnum)
        TextView tv_visitnum;

        @BindView(R.id.tv_commnum)
        TextView tv_commnum;

        @BindView(R.id.tv_likenum)
        TextView tv_likenum;

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.tv_time)
        TextView tv_time;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CommAdapter extends RecyclerView.Adapter<CommViewHolder> {

        @Override
        public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_share_special_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(final CommViewHolder holder, final int position) {
            final ShareSpecial e = dataList.get(position);

            BitmapUtil.displayImage(e.getPalteImgUrl(), holder.iv_bg);
            BitmapUtil.displayCirImage(e.getTeaAvatarUrl(), holder.iv_avatar);
            holder.tv_name.setText(e.getTeaNickName() + "老师");
            holder.tv_gread.setText(e.getGradeName() + " - " + e.getSubjectName());

            holder.tv_visitnum.setText("浏览" + e.seenCount);
            holder.tv_commnum.setText(e.interactionCount);
            holder.tv_likenum.setText(e.followCount);

            holder.tv_title.setText(e.name);
            holder.tv_time.setText(String.format("%s———%s", DateUtil.getYYMMDDHHMM(e.startTime)
                    , DateUtil.getHHMM(e.endTime)));

            switch (e.getTimeState()) {
                case 0:
                    holder.tv_state.setTextColor(Color.parseColor("#fd3a0d"));
                    holder.tv_state.setText("未开始");
                    break;
                case 1:
                    holder.tv_state.setTextColor(Color.parseColor("#38c1ff"));
                    holder.tv_state.setText("进行中");
                    break;
                case -1:
                    holder.tv_state.setTextColor(Color.parseColor("#aeaeae"));
                    holder.tv_state.setText("已结束");
                    break;
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpecialDetailActivity.openActivity(e);
                }
            });
            holder.iv_avatar.setOnClickListener(new View.OnClickListener() {//跳转到老师空间
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("teacherName", e.getTeaNickName());
                    bundle.putString("className", e.getTeaSubject());
                    bundle.putString("teacherAvatar", e.getTeaAvatarUrl());
                    bundle.putInt("fansNum", e.getTeaFansNum());
                    bundle.putString("teacherId", e.getTeaId());
                    bundle.putBoolean("isSelected", !e.getTeaFavored());
                    openActivity(TeacherSpaceActivity.class, bundle);

                }
            });


        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }
}
