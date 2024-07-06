package com.zhangsheng.shunxin.calendar.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maiya.thirdlibrary.ext.isNull
import com.maiya.thirdlibrary.ext.nN

/**
 * @Description:
 * @Author:         lhy
 * @CreateDate:     2020/4/20 13:27
 */
abstract class DefaultAdapter<T> : RecyclerView.Adapter<BaseHolder<T>> {

    protected var mInfos: MutableList<T>? = null
    protected var mOnItemClickListener: OnRecyclerViewItemClickListener<T>? = null


    constructor(infos: MutableList<T>) : super() {

        mInfos = infos
    }

    /**
     * 创建 [BaseHolder]
     *
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<T> {
        val view = LayoutInflater.from(parent.context)
            .inflate(getLayoutId(viewType), parent, false)
        var mHolder = getHolder(view, viewType)
        mHolder?.setOnItemClickListener(object :
            BaseHolder.OnViewClickListener {
            //设置Item点击事件
            override fun onViewClick(view: View?, position: Int) {
                if (mOnItemClickListener != null && mInfos.nN().size.nN(0) > 0) {
                    mInfos?.get(position)?.let {
                        mOnItemClickListener?.onItemClick(view, viewType, it, position)
                    }
                }
            }
        })
        return mHolder
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: BaseHolder<T>, position: Int) {
        holder.setData(mInfos!![position], position)
    }


    /**
     * 返回数据的个数
     *
     * @return
     */
    override fun getItemCount(): Int {
        return mInfos!!.size
    }


    open fun getInfos(): List<T>? {
        return mInfos
    }

    /**
     * 获得某个 `position` 上的 item 的数据
     *
     * @param position
     * @return
     */
    open fun getItem(position: Int): T? {
        return if (mInfos == null) null else mInfos?.get(position)

    }

    /**
     * 让子类实现用以提供 [BaseHolder]
     *
     * @param v
     * @param viewType
     * @return
     */
    abstract fun getHolder(v: View, viewType: Int): BaseHolder<T>

    /**
     * 提供用于 `item` 布局的 `layoutId`
     *
     * @param viewType
     * @return
     */
    abstract fun getLayoutId(viewType: Int): Int


    /**
     * 遍历所有[BaseHolder],释放他们需要释放的资源
     *
     * @param recyclerView
     */
    open fun releaseAllHolder(recyclerView: RecyclerView?) {
        if (recyclerView == null) return
        for (i in recyclerView.childCount - 1 downTo 0) {
            val view = recyclerView.getChildAt(i)
            val viewHolder = recyclerView.getChildViewHolder(view)
            if (viewHolder != null && viewHolder is BaseHolder<*>) {
                viewHolder.onRelease()
            }
        }
    }


    open interface OnRecyclerViewItemClickListener<T> {
        fun onItemClick(
            view: View?,
            viewType: Int,
            data: T,
            position: Int
        )
    }

    open fun setOnItemClickListener(listener: OnRecyclerViewItemClickListener<T>) {
        mOnItemClickListener = listener
    }

    fun setData(data: List<T>) {
        mInfos.isNull {
            mInfos = ArrayList<T>()
        }
        mInfos?.clear()
        mInfos?.addAll(data)
    }

}