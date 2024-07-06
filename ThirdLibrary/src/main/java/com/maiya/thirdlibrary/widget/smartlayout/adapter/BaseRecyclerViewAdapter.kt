package com.maiya.thirdlibrary.widget.smartlayout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.widget.smartlayout.click.SmartOnItemClickListener
import com.maiya.thirdlibrary.widget.smartlayout.click.SmartOnItemLongClickListener
import java.util.*

abstract class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<SmartViewHolder> {
    private var data: MutableList<T>? = null
    private var layout: Int
    private var clickListener: SmartOnItemClickListener? = null
    private var longClickListener: SmartOnItemLongClickListener? = null


    constructor(
        data: MutableList<T>?,
        layout: Int,
        clickListener: SmartOnItemClickListener?,
        longClickListener: SmartOnItemLongClickListener?
    ) {
        if (data != null) this.data = data
        this.layout = layout
        if (clickListener != null) this.clickListener = clickListener
        if (longClickListener != null) this.longClickListener = longClickListener
    }

    constructor(data: MutableList<T>?, layout: Int) {
        if (data != null) this.data = data
        this.layout = layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):SmartViewHolder {
        return SmartViewHolder(
            LayoutInflater.from(parent.context).inflate(layout, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder:SmartViewHolder,
        position: Int
    ) {
        bindDataToItemView(holder, data!![position], position)
        bindItemViewClickListener(holder)
    }

    override fun getItemCount(): Int {
        return if (data == null) 0 else data.nN().size
    }

    private fun bindItemViewClickListener(holder:SmartViewHolder) {
        if (holder.itemView != null) {
            if (clickListener != null) {
                holder.itemView.setOnClickListener { v ->
                    clickListener.nN().onClick(
                        v,
                        holder.layoutPosition
                    )
                }
            }
            if (longClickListener != null) {
                holder.itemView
                    .setOnLongClickListener { v ->
                        longClickListener.nN().onLongClick(v, holder.layoutPosition)
                        true
                    }
            }
        }
    }

    fun setItemClickListener(onItemClickListener: SmartOnItemClickListener?): BaseRecyclerViewAdapter<*> {
        clickListener = onItemClickListener
        return this
    }

    fun setItemLongClickListener(onItemLongClickListener: SmartOnItemLongClickListener?): BaseRecyclerViewAdapter<*> {
        longClickListener = onItemLongClickListener
        return this
    }

    fun addItem(t: T, pos: Int): BaseRecyclerViewAdapter<*> {
        if (data != null && pos <= data.nN().size) {
            data!!.add(t)
            notifyItemInserted(pos)
        }
        return this
    }

    fun removeItem(pos: Int): BaseRecyclerViewAdapter<*> {
        if (data != null && pos < data.nN().size) {
            data!!.removeAt(pos)
            notifyItemRemoved(pos)
        }
        return this
    }

    fun setData(newData: List<T>?): BaseRecyclerViewAdapter<*> {
        if (null == data) data = ArrayList()
        if (null != newData) {
            data?.clear()
            data?.addAll(newData)
        }
        return this
    }

    protected abstract fun bindDataToItemView(
        holder:SmartViewHolder?,
        item: T,
        position: Int
    )
}