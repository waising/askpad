package com.asking.pad.app.ui.downbook;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenFrameFragment;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.entity.book.BookDownInfo;
import com.asking.pad.app.ui.downbook.db.DbBookHelper;
import com.asking.pad.app.ui.downbook.db.DbHelper;
import com.asking.pad.app.ui.downbook.download.DownState;
import com.asking.pad.app.ui.downbook.presenter.DownModel;
import com.asking.pad.app.ui.downbook.presenter.DownPresenter;
import com.asking.pad.app.ui.mine.CommDialog;
import com.asking.pad.app.ui.personalcenter.DeleteDialog;
import com.asking.pad.app.widget.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by jswang on 2017/5/2.
 */

public class DownFinishFragment extends BaseEvenFrameFragment<DownPresenter, DownModel> {
    @BindView(R.id.load_view)
    MultiStateView load_view;

    @BindView(R.id.rec_view)
    RecyclerView rec_view;

    CommAdapter mAdapter;

    String courseTypeId;
    private ArrayList<BookDownInfo> dataList = new ArrayList<>();

    OnDownFinishListener mListener;
    public interface OnDownFinishListener{
        void OnDownFinish(BookDownInfo e);
    }

    public static DownFinishFragment newInstance(String courseTypeId,OnDownFinishListener mListener) {
        DownFinishFragment fragment = new DownFinishFragment();
        Bundle bundle = new Bundle();
        bundle.putString("courseTypeId", courseTypeId);
        fragment.setArguments(bundle);
        fragment.mListener = mListener;
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_down_able);
        ButterKnife.bind(this, getContentView());

        Bundle bundle = getArguments();
        if (bundle != null) {
            courseTypeId = bundle.getString("courseTypeId");
        }
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

    public void initBookData(){
        Observable<Object> mObservable = Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(final Subscriber<? super Object> subscriber) {
                dataList.clear();
                List<BookDownInfo> dbList = DbHelper.getInstance().getAllBookDownInfo(courseTypeId);
                for(int j= 0;j<dbList.size();j++){
                    BookDownInfo dbE = dbList.get(j);
                    if(dbE.getDownState()==DownState.FINISH){
                        dataList.add(dbE);
                    }
                }
                subscriber.onCompleted();
            }
        });
        mPresenter.newThread(mObservable,new ApiRequestListener<String>(){
            @Override
            public void onResultSuccess(String res) {
                if(dataList.size()>0){
                    mAdapter.notifyDataSetChanged();
                    load_view.setViewState(load_view.VIEW_STATE_CONTENT);
                }else{
                    load_view.setViewState(load_view.VIEW_STATE_EMPTY);
                }
            }

            @Override
            public void onResultFail() {
                load_view.setViewState(load_view.VIEW_STATE_ERROR);
            }
        });
    }

    public void onEventMainThread(BookDownInfo event) {
        try {
            if(event.getDownState()==DownState.FINISH){
                initBookData();
            }
        } catch (Exception e) {
        }
    }

    class CommViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_root)
        View ll_root;

        @BindView(R.id.item_name)
        TextView item_name;

        @BindView(R.id.img_del)
        ImageView img_del;

        @BindView(R.id.tv_update)
        TextView tv_update;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    DeleteDialog deleteDialog;
    private void showDelDialog(final BookDownInfo e) {
        if(deleteDialog == null){
            deleteDialog = new DeleteDialog();
        }
        deleteDialog.setDeleteListner(new DeleteDialog.DeleteListner(){
            @Override
            public void ok(int position, String id, DeleteDialog deleteDialog) {
                DbBookHelper.deleteBookDownInfoThread(getActivity(),e,new ApiRequestListener(){
                    @Override
                    public void onResultSuccess(Object res) {
                        initBookData();
                    }
                });
                deleteDialog.dismiss();
            }
        });
        deleteDialog.setPosition(0);
        deleteDialog.setId("");
        deleteDialog.show(getChildFragmentManager(), "");

    }

    CommDialog cDialog;
    private void showUpDialog(final BookDownInfo e) {
        if(cDialog == null){
            cDialog = new CommDialog();
        }
        cDialog.setDeleteListner(new CommDialog.DeleteListner(){
            @Override
            public void ok(int position, String id, CommDialog deleteDialog) {
                DbBookHelper.deleteBookDownInfoThread(getActivity(),e,new ApiRequestListener(){
                    @Override
                    public void onResultSuccess(Object res) {
                        initBookData();
                        if(mListener!=null){
                            e.setUpdate(0);
                            e.setDownState(DownState.START);
                            mListener.OnDownFinish(e);
                        }
                    }
                });
                deleteDialog.dismiss();
            }
        });
        cDialog.setPosition(0);
        cDialog.setId("");
        cDialog.show(getChildFragmentManager(), "");
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
            final BookDownInfo e = dataList.get(position);
            holder.item_name.setText(String.format("%s-%s", e.getCommodityName(), e.getCourseName()));
            holder.img_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDelDialog(e);
                }
            });
            holder.tv_update.setVisibility(View.GONE);
            if(e.getUpdate() == 1){
                holder.tv_update.setVisibility(View.VISIBLE);
            }
            holder.ll_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(e.getUpdate() == 1){
                        showUpDialog(e);
                    }else{
                        EventBus.getDefault().post(new AppEventType(AppEventType.BOOK_OPEN_REQUEST,e.getCommodityId()));
                        getActivity().finish();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

}
