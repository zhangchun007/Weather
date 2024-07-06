package com.zhangsheng.shunxin.calendar.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @Description:
 * @Author:         lhy
 * @CreateDate:     2020/4/20 13:29
 */
abstract class BaseHolder<T> : RecyclerView.ViewHolder, View.OnClickListener {

    protected var mOnViewClickListener: OnViewClickListener? = null

    constructor(itemView : View) : super( itemView ) {
        itemView.setOnClickListener(this)
    }

    /**
     * 设置数据
     *
     * @param data
     * @param position
     */
    abstract fun setData(data: T, position: Int)


    /**
     * 在 Activity 的 onDestroy 中使用 [DefaultAdapter.releaseAllHolder] 方法 (super.onDestroy() 之前)
     * [BaseHolder.onRelease] 才会被调用, 可以在此方法中释放一些资源
     */
    open fun onRelease() {}


    override fun onClick(view: View?) {
        if (mOnViewClickListener != null) {
            mOnViewClickListener!!.onViewClick(view, this.position)
        }
    }

    interface OnViewClickListener {
        fun onViewClick(view: View?, position: Int)
    }

    open fun setOnItemClickListener(listener: OnViewClickListener?) {
        mOnViewClickListener = listener
    }
}