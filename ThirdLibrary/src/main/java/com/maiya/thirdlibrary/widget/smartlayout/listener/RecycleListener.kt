package com.maiya.thirdlibrary.widget.smartlayout.listener

import android.widget.TextView
import com.maiya.thirdlibrary.widget.smartlayout.adapter.SmartViewHolder


/**
 * Created by Quan on 2018/6/13 0013 15:17
 * E-Mail Address：672114236@qq.com
 */
interface RecycleListener {
    fun onRefresh()
    fun AutoAdapter(holder: SmartViewHolder, item: Any, position: Int)
    fun onLoadMore()
    fun onEmpty(empty: TextView)
    fun datas(datas:List<Any>)

}