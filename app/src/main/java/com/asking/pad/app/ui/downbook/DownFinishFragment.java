package com.asking.pad.app.ui.downbook;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseEvenFrameFragment;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.entity.BookInfo;
import com.asking.pad.app.ui.downbook.db.DbHelper;
import com.asking.pad.app.ui.downbook.download.DownState;
import com.asking.pad.app.ui.downbook.download.OkHttpDownManager;
import com.asking.pad.app.ui.downbook.presenter.DownModel;
import com.asking.pad.app.ui.downbook.presenter.DownPresenter;
import com.asking.pad.app.ui.personalcenter.DeleteDialog;
import com.asking.pad.app.widget.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by jswang on 2017/5/2.
 */

public class DownFinishFragment extends BaseEvenFrameFragment<DownPresenter, DownModel> {
    @BindView(R.id.load_view)
    MultiStateView load_view;

    @BindView(R.id.rec_view)
    RecyclerView rec_view;

    CommAdapter mAdapter;

    private ArrayList<BookInfo> dataList = new ArrayList<>();

    public static DownFinishFragment newInstance() {
        DownFinishFragment fragment = new DownFinishFragment();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_down_able);
        ButterKnife.bind(this, getContentView());
    }

    @Override
    public void initView() {
        super.initView();

        GridLayoutManager mgr = new GridLayoutManager(this.getActivity(), 2);
        rec_view.setLayoutManager(mgr);
        mAdapter = new CommAdapter();
        rec_view.setAdapter(mAdapter);

        initBookData();
    }

    private void initBookData(){
        dataList.clear();
        List<BookInfo> dbList = DbHelper.getInstance().getAllBookInfo();
        for(int j= 0;j<dbList.size();j++){
            BookInfo dbE = dbList.get(j);
            if(dbE.getDownState()==DownState.FINISH){
                dataList.add(dbE);
            }
        }
        mAdapter.notifyDataSetChanged();

        if(dataList.size()>0){
            load_view.setViewState(load_view.VIEW_STATE_CONTENT);
        }else{
            load_view.setViewState(load_view.VIEW_STATE_EMPTY);
        }
    }

    public void onEventMainThread(BookInfo event) {
        try {
            if(event.getDownState()==DownState.FINISH){
                initBookData();
            }
        } catch (Exception e) {
        }
    }

    class CommViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_name)
        TextView item_name;

        @BindView(R.id.img_del)
        ImageView img_del;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    DeleteDialog deleteDialog;
    private void showDelDialog(final BookInfo e) {
        if(deleteDialog == null){
            deleteDialog = new DeleteDialog();
        }
        deleteDialog.setDeleteListner(new DeleteDialog.DeleteListner(){
            @Override
            public void ok(int position, String id, DeleteDialog deleteDialog) {
                OkHttpDownManager.getInstance().deleteDown(e);
                initBookData();
                e.setDownState(DownState.DELETE);
                e.deleteFile();
                EventBus.getDefault().post(e);
                deleteDialog.dismiss();
            }
        });
        deleteDialog.setPosition(0);
        deleteDialog.setId("");
        deleteDialog.show(getChildFragmentManager(), "");

    }

    /**
     * recyclew中的adapter
     */
    class CommAdapter extends RecyclerView.Adapter<CommViewHolder> {

        @Override
        public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.listitem_down_finish, parent, false));
        }

        @Override
        public void onBindViewHolder(final CommViewHolder holder, final int position) {
            final BookInfo e = dataList.get(position);
            if(TextUtils.isEmpty(e.getLevelName())){
                holder.item_name.setText(String.format("%s-%s", e.getVersionName(), e.getSubjectCatalogName()));
            }else{
                holder.item_name.setText(String.format("%s-%s-%s", e.getVersionName(), e.getSubjectCatalogName(),e.getLevelName()));
            }
            holder.img_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDelDialog(e);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new AppEventType(AppEventType.BOOK_OPEN_REQUEST,e.getVersionId(),e.getLevelId()));
                    getActivity().finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

}