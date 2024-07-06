package com.zhangsheng.shunxin.weather.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class CustomViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    private var listener: CustomListener? = null
    private var isScroll = true
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        try {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)

            listener?.let {
                listener?.upDateHeight(measuredHeight)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addCustomListener(listener: CustomListener) {
        this.listener = listener
    }


    interface CustomListener {
        fun upDateHeight(height: Int)

    }


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return try {
            if (isScroll) {
                super.onInterceptTouchEvent(ev)
            } else {
                false
            }
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return try {
            if (isScroll) {
                super.onTouchEvent(ev)
            } else {
                true;// 可行,消费,拦截事件
            }
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    fun setScroll(scroll: Boolean) {
        isScroll = scroll
    }
}