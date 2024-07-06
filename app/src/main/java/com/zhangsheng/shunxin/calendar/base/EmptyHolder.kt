package com.zhangsheng.shunxin.calendar.base

import android.view.View

/**
 * @Description:
 * @Author:         lhy
 * @CreateDate:     2020/4/20 13:29
 */
 class EmptyHolder<T>(itemView: View) : BaseHolder<T>(itemView), View.OnClickListener {
    override fun setData(data: T, position: Int) {
    }

}