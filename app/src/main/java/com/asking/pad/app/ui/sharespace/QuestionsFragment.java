package com.asking.pad.app.ui.sharespace;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.entity.RecordAnswer;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.widget.AskSwipeRefreshLayout;
import com.asking.pad.app.widget.MultiStateView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 回答广场
 */

public class QuestionsFragment extends BaseFrameFragment<UserPresenter, UserModel> {
    @BindView(R.id.load_view)
    MultiStateView load_view;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    /**
     * 下拉刷新控件
     */
    @BindView(R.id.swipe_layout)
    AskSwipeRefreshLayout swipeLayout;

    ArrayList<RecordAnswer> dataList = new ArrayList<>();
    CommAdapter mAdapter;

    /**
     * 开始页数，一页加载几个
     */
    int start = 0, limit = 10;

    public static QuestionsFragment newInstance() {
        QuestionsFragment fragment = new QuestionsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_share_question);
        ButterKnife.bind(this, getContentView());
    }

    @Override
    public void initView() {
        super.initView();

        GridLayoutManager mgr = new GridLayoutManager(getActivity(), 3);
        recycler.setLayoutManager(mgr);
        mAdapter = new CommAdapter();
        recycler.setAdapter(mAdapter);
    }

    class CommViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)//背景图片
        ImageView iv_avatar;

        @BindView(R.id.tv_name)//标签图标
        ImageView tv_name;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CommAdapter extends RecyclerView.Adapter<CommViewHolder> {

        @Override
        public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_share_question_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(final CommViewHolder holder, final int position) {
            final RecordAnswer e = dataList.get(position);

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }
}
