package com.maiya.thirdlibrary.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by WQ on 16.7.29
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {

    private View itemView;

    public View getItemView() {
        return itemView;
    }

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public View findViewById(int id) {
        return itemView.findViewById(id);
    }


    public <T> T findView(int id) {
        return (T) itemView.findViewById(id);
    }

    public BaseViewHolder setTextViewText(int id, String text) {
        ((TextView) findViewById(id)).setText(text);
        return this;
    }

    public BaseViewHolder setTextViewText(int id, CharSequence text, TextView.BufferType type) {
        ((TextView) findViewById(id)).setText(text, type);
        return this;
    }

    public BaseViewHolder setVisibility(int id, int visibility) {
        findViewById(id).setVisibility(visibility);
        return this;
    }

    public BaseViewHolder setEnable(int id, boolean enable) {
        findViewById(id).setEnabled(enable);
        return this;
    }

    public BaseViewHolder setOnClickListener(int id, View.OnClickListener clickListener) {
        findViewById(id).setOnClickListener(clickListener);
        return this;
    }

}