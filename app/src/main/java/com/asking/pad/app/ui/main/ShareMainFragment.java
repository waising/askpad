package com.asking.pad.app.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFragment;
import com.asking.pad.app.entity.LabelEntity;
import com.asking.pad.app.ui.sharespace.MineSpaceFragment;
import com.asking.pad.app.ui.sharespace.QuestionsFragment;
import com.asking.pad.app.ui.sharespace.ShareStarFragement;
import com.asking.pad.app.ui.sharespace.special.SpecialFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.asking.pad.app.R.id.toolBar;

/**
 * Created by jswang on 2017/4/10.
 */

public class ShareMainFragment extends BaseFragment {
    @BindView(toolBar)
    Toolbar mToolbar;

    @BindView(R.id.rv_version)
    RecyclerView rv_version;

    CommAdapter mAdapter;
    ArrayList<LabelEntity> dataList = new ArrayList<>();

    private int mCurTabIndex = 0;
    List<Fragment> fragments = new ArrayList<>();

    public static ShareMainFragment newInstance() {
        ShareMainFragment fragment = new ShareMainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_share_main);
        ButterKnife.bind(this, getContentView());
    }

    @Override
    public void initView() {
        super.initView();
        setToolbarStyle("问答广场",R.mipmap.ic_share_ques_head);

        dataList.add(new LabelEntity(R.mipmap.ic_share_ques_head,"问答广场",true));
        dataList.add(new LabelEntity(R.mipmap.ic_share_special_head,"共享专题",false));
        dataList.add(new LabelEntity(R.mipmap.ic_share_star_head,"共享之星",false));
        dataList.add(new LabelEntity(R.mipmap.ic_share_ques_head,"我的空间",false));

        mAdapter = new CommAdapter();
        rv_version.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_version.setAdapter(mAdapter);

        fragments.add(QuestionsFragment.newInstance());
        fragments.add(SpecialFragment.newInstance());
        fragments.add(ShareStarFragement.newInstance());
        fragments.add(MineSpaceFragment.newInstance());

        getChildFragmentManager().beginTransaction()
                .add(R.id.fragment,fragments.get(0))
                .add(R.id.fragment,fragments.get(1))
                .add(R.id.fragment,fragments.get(2))
                .add(R.id.fragment,fragments.get(3))
                .hide(fragments.get(1))
                .hide(fragments.get(2))
                .hide(fragments.get(3))
                .show(fragments.get(0)).commit();
    }

    public void setToolbarStyle(String title,int resId){
        mToolbar.setNavigationIcon(resId);
        mToolbar.setTitle(title);
    }

    private void setFragmentPage(int index) {
        FragmentTransaction trx = getChildFragmentManager().beginTransaction();
        trx.hide(fragments.get(mCurTabIndex));
        if (!fragments.get(index).isAdded()) {
            trx.add(R.id.fragment, fragments.get(index));
        }
        trx.show(fragments.get(index)).commit();

        mCurTabIndex = index;
    }

    public void onCommItem(int position){
        setFragmentPage(position);
    }

    class CommAdapter extends RecyclerView.Adapter<CommAdapter.CommViewHolder> {
        @Override
        public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_supertutorial_classify,parent,false));
        }

        @Override
        public void onBindViewHolder(CommViewHolder holder,final int position) {
            final LabelEntity e = dataList.get(position);
            holder.item_name.setSelected(e.getSelect());
            holder.item_name.setText(e.getName());
            holder.item_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (LabelEntity ii : dataList) {
                        ii.setSelect(false);
                    }
                    e.setSelect(true);
                    notifyDataSetChanged();

                    setToolbarStyle(e.getName(),e.getIcon());
                    onCommItem(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
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
