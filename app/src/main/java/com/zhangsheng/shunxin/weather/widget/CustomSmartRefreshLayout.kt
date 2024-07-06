package com.zhangsheng.shunxin.weather.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class CustomSmartRefreshLayout : SmartRefreshLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun drawChild(canvas: Canvas?, child: View?, drawingTime: Long): Boolean {
        try {
            return super.drawChild(canvas, child, drawingTime)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    override fun dispatchTouchEvent(e: MotionEvent?): Boolean {
        try {
            return super.dispatchTouchEvent(e)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}