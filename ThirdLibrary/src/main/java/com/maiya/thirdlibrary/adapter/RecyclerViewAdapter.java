package com.maiya.thirdlibrary.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    private List<T> data;
    private int layout;
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    public RecyclerViewAdapter(List<T> data, int layout, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
        if (data != null) this.data = data;
        this.layout = layout;
        if (clickListener != null) this.clickListener = clickListener;
        if (longClickListener != null) this.longClickListener = longClickListener;
    }

    public RecyclerViewAdapter(List<T> data, int layout) {
        if (data != null) this.data = data;
        this.layout = layout;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        bindDataToItemView(holder, data.get(position), position);
        bindItemViewClickListener(holder);
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        else return data.size();
    }

    private void bindItemViewClickListener(final BaseViewHolder holder) {
        if (holder.getItemView() != null) {
            if (clickListener != null) {
                holder.getItemView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.onClick(v, holder.getLayoutPosition());
                    }
                });
            }
            if (longClickListener != null) {
                holder.getItemView().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        longClickListener.onLongClick(v, holder.getLayoutPosition());
                        return true;
                    }
                });
            }
        }
    }

    public RecyclerViewAdapter setItemClickListener(OnItemClickListener onItemClickListener) {
        this.clickListener = onItemClickListener;
        return this;
    }

    public RecyclerViewAdapter setItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.longClickListener = onItemLongClickListener;
        return this;
    }

    public RecyclerViewAdapter addItem(T t, int pos) {
        if (data != null && pos <= data.size()) {
            data.add(t);
            notifyItemInserted(pos);
        }
        return this;
    }

    public RecyclerViewAdapter removeItem(int pos) {
        if (data != null && pos < data.size()) {
            data.remove(pos);
            notifyItemRemoved(pos);
        }
        return this;
    }

    public RecyclerViewAdapter setData(List<T> newData) {
        if (null == this.data) data = new ArrayList<>();
        if (null != newData) {
            this.data.clear();
            this.data.addAll(newData);
        }
        return this;
    }

    protected abstract void bindDataToItemView(BaseViewHolder holder, T item, int position);
}
