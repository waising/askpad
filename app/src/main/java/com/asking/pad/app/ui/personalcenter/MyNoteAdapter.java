package com.asking.pad.app.ui.personalcenter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.NoteEntity;
import com.asking.pad.app.widget.AskMathView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 笔记adapter
 * Created by zqshen on 2016/11/24.
 */

public class MyNoteAdapter extends RecyclerView.Adapter<MyNoteAdapter.MessageViewHolder> {

    private Context mContext;
    List<NoteEntity.ListBean> list;

    private NoteAddEditDialog.NoteListner noteListner;

    public MyNoteAdapter(Context context, List<NoteEntity.ListBean> list, NoteAddEditDialog.NoteListner noteListner) {
        this.mContext = context;
        this.list = list;
        this.noteListner = noteListner;
    }

    public void setData(List<NoteEntity.ListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_mynote, parent, false));
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder holder,final int position) {
        if (list != null && list.size() > 0) {
            final NoteEntity.ListBean e = list.get(position);

            holder.mathView.setText(e.getContent());

            final String createTimeFmt = e.getCreateTime_fmt(); // 笔记创建时间
            if (!TextUtils.isEmpty(createTimeFmt)) {
                try {
                    holder.mTvDate.setText(createTimeFmt.substring(5,createTimeFmt.length()));
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            } else {
                holder.mTvDate.setText("");
            }
            holder.tv_title.setText(e.getTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {//跳转修改笔记页面
                @Override
                public void onClick(View v) {
                    openDialog(e,createTimeFmt);
                }
            });
            holder.mathView.setOnAskMathClickListener(new AskMathView.OnAskMathClickListener(){
                @Override
                public void OnClick() {
                    openDialog(e,createTimeFmt);
                }
            });
            holder.imgDel.setOnClickListener(new View.OnClickListener() {//跳转修改笔记页面
                @Override
                public void onClick(View v) {
                    if (CallDelNote != null) {
                        CallDelNote.delNote(position,e.getId());
                    }
                }
            });
        }
    }

    private void openDialog(NoteEntity.ListBean e ,String createTimeFmt){
        NoteAddEditDialog noteAddEditDialog = new NoteAddEditDialog();
        noteAddEditDialog.setFromWhere(NoteAddEditDialog.EDIT_NOTE);
        noteAddEditDialog.setId(e.getId());
        noteAddEditDialog.setTitle(e.getTitle());
        noteAddEditDialog.setContent(e.getContent());
        noteAddEditDialog.setNoteListner(noteListner);
        noteAddEditDialog.setTime(createTimeFmt);
        noteAddEditDialog.show(((FragmentActivity) mContext).getSupportFragmentManager(), "");
    }

    CallDelNote CallDelNote;

    public void startDelNote(CallDelNote CallDelNote) {
        this.CallDelNote = CallDelNote;
    }

    public interface CallDelNote {
        void delNote(int currentPosition, String id);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date)
        TextView mTvDate;

        @BindView(R.id.mathView)
        AskMathView mathView;

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.img_del)
        ImageView imgDel;
        @BindView(R.id.layout)
        LinearLayout layout;

        @BindView(R.id.cardView)
        CardView cardView;


        public MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }
}
