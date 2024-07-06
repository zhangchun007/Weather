package com.maiya.thirdlibrary.widget.smartlayout.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class SmartViewHolder extends RecyclerView.ViewHolder {

    private View itemView;

    public View getItemView() {
        return itemView;
    }

    public SmartViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public View findViewById(int id) {
        return itemView.findViewById(id);
    }

    public <T> T findView(int id) {
        return (T) itemView.findViewById(id);
    }

    public SmartViewHolder setTextViewText(int id, String text) {
        ((TextView) findViewById(id)).setText(text);
        return this;
    }
    public SmartViewHolder setImageRes(int id, int res) {
        ((ImageView) findViewById(id)).setImageResource(res);
        return this;
    }

    public SmartViewHolder setImageUrl(Context context,int id, String url) {
        Glide.with(context).load(url).into(  ((ImageView) findViewById(id)));
        return this;
    }

    public SmartViewHolder setTextViewText(int id, int text) {
        ((TextView) findViewById(id)).setText("" + text);
        return this;
    }

    public SmartViewHolder setTextViewText(int id, CharSequence text, TextView.BufferType type) {
        ((TextView) findViewById(id)).setText(text, type);
        return this;
    }



    public SmartViewHolder setVisibility(int id, int visibility) {
        findViewById(id).setVisibility(visibility);
        return this;
    }

    public SmartViewHolder setTextColor(int id, int color) {
        ((TextView) findViewById(id)).setTextColor(color);
        return this;
    }

    public SmartViewHolder setEnable(int id, boolean enable) {
        findViewById(id).setEnabled(enable);
        return this;
    }

    public SmartViewHolder setClickable(int id, boolean enable) {
        findViewById(id).setClickable(enable);
        return this;
    }

    public SmartViewHolder setOnClickListener(int id, View.OnClickListener clickListener) {
        findViewById(id).setOnClickListener(clickListener);
        return this;
    }

    public SmartViewHolder setOnItemClickListener(View.OnClickListener clickListener) {
        itemView.setOnClickListener(clickListener);
        return this;
    }

    public SmartViewHolder setOnLongClickListener(int id, View.OnLongClickListener clickListener) {
        findViewById(id).setOnLongClickListener(clickListener);
        return this;
    }
}