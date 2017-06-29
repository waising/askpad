package com.asking.pad.app.ui.superclass.classify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.entity.superclass.SuperLessonTree;
import com.unnamed.b.atv.model.TreeNode;

/**
 * Created by jswang on 2017/6/28.
 */

public class LessonTreeItemHolder extends TreeNode.BaseNodeViewHolder<SuperLessonTree> {
    private TextView tvValue;
    private ImageView arrowView;
    private TextView tv_free;

    public LessonTreeItemHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, SuperLessonTree value) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_tree_item, null);
        tvValue = (TextView) view.findViewById(R.id.node_value);
        tv_free = (TextView) view.findViewById(R.id.tv_free);

        tvValue.setText(value.name);
        tv_free.setText("");

        arrowView = (ImageView) view.findViewById(R.id.icon);
        if (!value.getIsLeaf()) {
            arrowView.setVisibility(View.VISIBLE);
            arrowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tView.toggleNode(node);
                }
            });
        } else {
            arrowView.setVisibility(View.GONE);
        }
        if(value.free != 0){
            tv_free.setText("免费");
        }
        return view;
    }

    @Override
    public void toggleSelectionMode(boolean editModeEnabled) {
    }

    @Override
    public void toggle(boolean active) {
        arrowView.setSelected(active);
    }

}

