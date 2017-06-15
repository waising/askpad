package com.asking.pad.app.ui.personalcenter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
            // 笔记内容
            final String content = list.get(position).getContent();
            final String createTimeFmt = list.get(position).getCreateTime_fmt(); // 笔记创建时间

            // 笔记ID
            final String id = list.get(position).getId();

            if (content != null) {
                holder.mathView.setText(content);
            } else {
                holder.mathView.setText("");
            }
            if (createTimeFmt != null) {
                holder.mTvDate.setText(createTimeFmt.substring(5,createTimeFmt.length()));
            } else {
                holder.mTvDate.setText("");
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {//跳转修改笔记页面
                @Override
                public void onClick(View v) {
                    NoteAddEditDialog noteAddEditDialog = new NoteAddEditDialog();
                    noteAddEditDialog.setFromWhere(NoteAddEditDialog.EDIT_NOTE);
                    noteAddEditDialog.setId(id);
                    noteAddEditDialog.setContent(content);
                    noteAddEditDialog.setNoteListner(noteListner);
                    noteAddEditDialog.setTime(createTimeFmt);
                    noteAddEditDialog.show(((FragmentActivity) mContext).getSupportFragmentManager(), "");

                }
            });


            holder.imgDel.setOnClickListener(new View.OnClickListener() {//跳转修改笔记页面
                @Override
                public void onClick(View v) {
                    if (CallDelNote != null) {
                        CallDelNote.delNote(position,id);
                    }
                }
            });

        }

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
