package com.asking.pad.app.ui.classmedia;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenFrameFragment;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.entity.classmedia.ClassMedia;
import com.asking.pad.app.entity.classmedia.StudyRecord;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.asking.pad.app.ui.downbook.db.DbHelper;
import com.asking.pad.app.widget.AskSwipeRefreshLayout;
import com.asking.pad.app.widget.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by jswang on 2017/6/1.
 */

public class ClassMediaFragment extends BaseEvenFrameFragment<UserPresenter, UserModel> {
    @BindView(R.id.load_view)
    MultiStateView load_view;

    @BindView(R.id.swipe_layout)
    AskSwipeRefreshLayout swipeLayout;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    int start = 0;
    int limit = 10;
    /**
     * KM01 - 数学
     * KM02 - 物理
     */
    String courseTypeId;
    /**
     * true - 已录完
     * <p>
     * false - 未录完
     */
    boolean completeFlag;

    List<ClassMedia> dataList = new ArrayList<>();
    CommAdapter mAdapter;

    public static ClassMediaFragment newInstance(String courseTypeId, OnClassMediaCallBack mCallBack) {
        ClassMediaFragment fragment = new ClassMediaFragment();
        Bundle bundle = new Bundle();
        bundle.putString("courseTypeId", courseTypeId);
        fragment.mCallBack = mCallBack;
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            courseTypeId = bundle.getString("courseTypeId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setContentView(R.layout.fragment_class_video);
        ButterKnife.bind(this, getContentView());
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    public void onEventMainThread(StudyRecord event) {
        for (ClassMedia e : dataList) {
            if (TextUtils.equals(event.getCourseDataId(), e.getCourseDataId())) {
                e.setPlayPercentage(event.getPlayPercentage());
                e.setPlayMax(event.getPlayMax());
                e.setPlayProgress(event.getPlayProgress());
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void initView() {
        super.initView();

        GridLayoutManager mgr = new GridLayoutManager(getActivity(), 4);
        recycler.setLayoutManager(mgr);
        mAdapter = new CommAdapter();
        recycler.setAdapter(mAdapter);

        swipeLayout.setPtrHandler(new PtrDefaultHandler2() {
            public void onLoadMoreBegin(PtrFrameLayout ptrFrameLayout) {//上拉，加载更多
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
                requstData();
            }
        });
        requstData();
    }

    public void requstData() {
        load_view.setViewState(load_view.VIEW_STATE_LOADING);
        start = 0;
        dataList.clear();
        loadData();
    }

    private void loadData() {
        mPresenter.findListByPage(courseTypeId, start, limit, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String resStr) {
                JSONObject jsonRes = JSON.parseObject(resStr);
                completeFlag = jsonRes.getBooleanValue("completeFlag");
                String orders = jsonRes.getString("list");
                List<ClassMedia> list = JSON.parseArray(orders, ClassMedia.class);

                for (StudyRecord e : DbHelper.getInstance().getStudyRecordList()) {
                    for (ClassMedia e1 : list) {
                        if (TextUtils.equals(e.getCourseDataId(), e1.getCourseDataId())) {
                            e1.setPlayPercentage(e.getPlayPercentage());
                            e1.setPlayMax(e.getPlayMax());
                            e1.setPlayProgress(e.getPlayProgress());
                        }
                    }
                }

                dataList.addAll(list);
                mAdapter.notifyDataSetChanged();

                swipeLayout.refreshComplete();
                if (dataList.size() == 0) {
                    load_view.setViewState(load_view.VIEW_STATE_EMPTY);
                } else {
                    load_view.setViewState(load_view.VIEW_STATE_CONTENT);
                }

                mCallBack.OnCallBack(completeFlag);
            }

            @Override
            public void onResultFail() {
                swipeLayout.refreshComplete();
                if (dataList.size() == 0) {
                    load_view.setViewState(load_view.VIEW_STATE_ERROR);
                }
            }
        });
    }

    class CommViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_temp)
        ImageView iv_temp;

        @BindView(R.id.iv_flag)
        ImageView iv_flag;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_price)
        TextView tv_price;

        @BindView(R.id.tv_price_count)
        TextView tv_price_count;

        @BindView(R.id.tv_tea_name)
        TextView tv_tea_name;

        @BindView(R.id.tv_progress)
        TextView tv_progress;

        @BindView(R.id.tea_avatar)
        ImageView tea_avatar;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CommentSecondHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tv_name;

        public CommentSecondHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CommentUnfinishHolder extends RecyclerView.ViewHolder {

        public CommentUnfinishHolder(View itemView) {
            super(itemView);
        }
    }

    class CommAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if (viewType == 0) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_media_layout, parent, false);
                return new CommViewHolder(view);
            } else if (viewType == 1) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_pdf_layout, parent, false);
                return new CommentSecondHolder(view);
            } else if (viewType == 2) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_unfinished_layout, parent, false);
                return new CommentUnfinishHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder mHolder, final int position) {
            if (!completeFlag && position == dataList.size()) {
                return;
            }
            final ClassMedia e = dataList.get(position);
            if (mHolder instanceof CommViewHolder) {
                CommViewHolder holder = (CommViewHolder) mHolder;
                holder.tv_name.setText(e.getCourseName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClassMediaDetailsActivity. openActivity(e);
                    }
                });
                BitmapUtil.displayImage(e.getVideoImgUrl(), holder.iv_temp, true);
                if (TextUtils.equals(Constants.CLASS_MEDIA_TYPE_ID[0], e.getCourseTypeId())) {
                    holder.iv_flag.setImageResource(R.mipmap.ic_class_media_math);
                } else {
                    holder.iv_flag.setImageResource(R.mipmap.ic_class_media_physics);
                }
                holder.tv_progress.setText("");
                if (!TextUtils.equals(e.getPurchaseState(), "0")) {
                    int percentage = e.getPlayPercentage();
                    if (percentage > 0) {
                        holder.tv_progress.setText("已学习"+percentage + "%");
                    }else{
                        holder.tv_progress.setText("马上学习");
                    }
                }
                holder.tv_tea_name.setText(e.getTeacherNickName()+"老师");
                BitmapUtil.displayCirImage(e.getTeacherImgUrl(), holder.tea_avatar);
                holder.tv_price.setText("￥" + e.getPrice());
                holder.tv_price_count.setText(String.format("已有%s人购买", e.getPurchasedNum()));
            } else if (mHolder instanceof CommentSecondHolder) {
                CommentSecondHolder holder = (CommentSecondHolder) mHolder;
                holder.tv_name.setText(e.getCourseName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("ClassMedia", e);
                        CommonUtil.openActivity(ClassPdfDetailsActivity.class, bundle);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            if (!completeFlag) {
                return dataList.size() + 1;
            }
            return dataList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (!completeFlag && position == dataList.size()) {
                return 2;
            }
            return dataList.get(position).getIsPresent();
        }

    }

    public OnClassMediaCallBack mCallBack;

    public interface OnClassMediaCallBack {
        void OnCallBack(boolean completeFlag);
    }
}
