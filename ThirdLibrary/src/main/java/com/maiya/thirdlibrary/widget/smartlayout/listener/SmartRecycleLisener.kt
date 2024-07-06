package com.maiya.thirdlibrary.widget.smartlayout.listener

import android.widget.TextView
import com.maiya.thirdlibrary.widget.smartlayout.adapter.SmartViewHolder


/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/4/15 10:50
 */
open class  SmartRecycleListener : RecycleListener {
    override fun onRefresh() {

    }
    override fun AutoAdapter(holder: SmartViewHolder, item: Any, position: Int) {
    }
    override fun onLoadMore() {
    }
    override fun onEmpty(empty: TextView) {
    }

    override fun datas(datas: List<Any>) {

    }

}