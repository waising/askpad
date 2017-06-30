package com.asking.pad.app.ui.downbook;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenFrameFragment;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.entity.BookInfo;
import com.asking.pad.app.ui.downbook.db.DbHelper;
import com.asking.pad.app.ui.downbook.download.DownState;
import com.asking.pad.app.ui.downbook.download.OkHttpDownManager;
import com.asking.pad.app.ui.downbook.presenter.DownModel;
import com.asking.pad.app.ui.downbook.presenter.DownPresenter;
import com.asking.pad.app.widget.MultiStateView;
import com.asking.pad.app.widget.RoundProgressBar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by jswang on 2017/5/2.
 */

public class DownAbleFragment extends BaseEvenFrameFragment<DownPresenter, DownModel> {
    @BindView(R.id.load_view)
    MultiStateView load_view;

    @BindView(R.id.rec_view)
    RecyclerView rec_view;

    CommAdapter mAdapter;

    private ArrayList<BookInfo> netDataList = new ArrayList<>();
    private ArrayList<BookInfo> dataList = new ArrayList<>();

    String courseTypeId;

    public static DownAbleFragment newInstance(String courseTypeId) {
        DownAbleFragment fragment = new DownAbleFragment();
        Bundle bundle = new Bundle();
        bundle.putString("courseTypeId", courseTypeId);
        fragment.setArguments(bundle);
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

        load_view.setErrorRefBtnTxt2(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netBookData();
            }
        });
        netBookData();
    }

    private void netBookData(){
        load_view.setViewState(load_view.VIEW_STATE_LOADING);
        mPresenter.finListdWithDownloadUrl(courseTypeId,0,1000,new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                netDataList.clear();
                netDataList.addAll(JSON.parseArray(res, BookInfo.class));
                if(netDataList.size()>0){
                    initBookData();
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

    private void initBookData(){
        Observable<Object> mObservable = Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(final Subscriber<? super Object> subscriber) {
                dataList.clear();
                List<BookInfo> dbList = DbHelper.getInstance().getAllBookInfo();
                for(int i= 0;i<netDataList.size();i++){
                    BookInfo e = netDataList.get(i);
                    e.setDownState(DownState.START);
                    if(!dbList.contains(e)){
                        dataList.add(e);
                    }else{
                        for(int j= 0;j<dbList.size();j++){
                            BookInfo dbE = dbList.get(j);
                            if(TextUtils.equals(e.getBookId(),dbE.getBookId())){
                                if(dbE.getDownState()<DownState.START){
                                    dbE.setDownState(DownState.PAUSE);
                                }
                                if(TextUtils.equals(e.getUrl(),dbE.getUrl())){
                                    if(dbE.getDownState()!=DownState.FINISH){
                                        dataList.add(dbE);
                                    }
                                }else{
                                    dataList.add(e);
                                }
                            }
                        }
                    }
                }
                subscriber.onCompleted();
            }
        });
        mPresenter.newThread(mObservable,new ApiRequestListener<String>(){
            @Override
            public void onResultSuccess(String res) {
                if(dataList.size()>0){
                    load_view.setViewState(load_view.VIEW_STATE_CONTENT);
                    mAdapter.notifyDataSetChanged();
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

    public void onEventMainThread(BookInfo event) {
        try {
            if(event.getDownState() == DownState.FINISH){
                if (null != dataList && dataList.size() > 0) {
                    Iterator<BookInfo> it = dataList.iterator();
                    while(it.hasNext()){
                        BookInfo stu = it.next();
                        if (TextUtils.equals(event.getBookId(), stu.getBookId())) {
                            it.remove();
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
                EventBus.getDefault().post(new AppEventType(AppEventType.BOOK_DWON_FINISH_REQUEST,event.getVersionId(),event.getLevelId()));
            }else if(event.getDownState() == DownState.DELETE){
                initBookData();
            }else {
                for (int i = 0; i < dataList.size(); i++) {
                    if (TextUtils.equals(event.getBookId(), dataList.get(i).getBookId())) {
                        CommViewHolder mHolder = (CommViewHolder) rec_view.findViewHolderForAdapterPosition(i);

                        mHolder.item_unzip.setVisibility(event.getDownState() == DownState.UN7ZIP?View.VISIBLE:View.GONE);
                        mHolder.progress.setVisibility(event.getDownState() == DownState.UN7ZIP?View.GONE:View.VISIBLE);
                        mHolder.progress.setDownPause(event.getDownState());
                        mHolder.progress.setMax((int) event.getCountLength());
                        mHolder.progress.setProgress((int) event.getReadLength());
                        break;
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    class CommViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_name)
        TextView item_name;

        @BindView(R.id.item_unzip)
        TextView item_unzip;

        @BindView(R.id.progress)
        RoundProgressBar progress;

        public CommViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CommAdapter extends RecyclerView.Adapter<CommViewHolder> {
        @Override
        public CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.listitem_down_able, parent, false));
        }

        @Override
        public void onBindViewHolder(final CommViewHolder holder, final int position) {
            final BookInfo e = dataList.get(position);
            if(TextUtils.isEmpty(e.getLevelName())){
                holder.item_name.setText(String.format("%s-%s", e.getVersionName(), e.getSubjectCatalogName()));
            }else{
                holder.item_name.setText(String.format("%s-%s-%s", e.getVersionName(), e.getSubjectCatalogName(),e.getLevelName()));
            }

            holder.item_unzip.setVisibility(e.getDownState() == DownState.UN7ZIP?View.VISIBLE:View.GONE);
            holder.progress.setVisibility(e.getDownState() == DownState.UN7ZIP?View.GONE:View.VISIBLE);
            holder.progress.setDownPause(e.getDownState());
            holder.progress.setMax((int) e.getCountLength());
            holder.progress.setProgress((int) e.getReadLength());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (e.getDownState()){
                        case DownState.START:
                        case DownState.STOP:
                        case DownState.PAUSE:
                        case DownState.ERROR:
                            OkHttpDownManager.getInstance().startDown(e);
                            break;
                        case DownState.DOWN:
                            OkHttpDownManager.getInstance().pause(e);
                            holder.progress.setDownPause(e.getDownState());
                            break;
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
