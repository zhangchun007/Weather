package com.zhangsheng.shunxin.weather.bottom

import androidx.fragment.app.Fragment

abstract class FragmentTab {
    private var mPosition: Int = 0
    private var mTitle: String? = ""

    fun setPosition(position : Int) {
        mPosition = position
    }

    fun getPosition() : Int = mPosition

    fun setTitle(title: String?) {
        mTitle = title
    }

    fun getTitle() : String? = mTitle

    abstract fun createFragment() : Fragment

}