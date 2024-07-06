package com.zhangsheng.shunxin.weather.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2019/12/17 15:27
 */
class pTextView : AppCompatTextView {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun isFocused(): Boolean {
        return true
    }
}