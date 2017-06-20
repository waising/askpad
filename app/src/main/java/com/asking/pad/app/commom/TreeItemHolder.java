package com.asking.pad.app.commom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.unnamed.b.atv.model.TreeNode;


/**
 * Created 2/12/15.
 */
public class TreeItemHolder extends TreeNode.BaseNodeViewHolder<TreeItemHolder.IconTreeItem> {
    private TextView tvValue;
    private ImageView arrowView;

    public TreeItemHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, IconTreeItem value) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_tree_item, null);
        tvValue = (TextView) view.findViewById(R.id.node_value);
        tvValue.setText(value.text);

        arrowView = (ImageView) view.findViewById(R.id.icon);
        if(!((TreeItemHolder.IconTreeItem)node.getValue()).isLeaf()){
            arrowView.setVisibility(View.VISIBLE);
            arrowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tView.toggleNode(node);
                }
            });

        }else{
            arrowView.setVisibility(View.GONE);
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

    public static class IconTreeItem {
        private int icon;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        private String id;
        private String text;
        private int position;

        public boolean isLeaf() {
            return isLeaf;
        }

        public void setLeaf(boolean leaf) {
            isLeaf = leaf;
        }

        private boolean isLeaf;

        public IconTreeItem(){

        }

        public IconTreeItem(int icon, String id,String text,boolean isLeaf, int position) {
            this.icon = icon;
            this.id = id;
            this.text = text;
            this.position = position;
            this.isLeaf = isLeaf;
        }
    }
}
