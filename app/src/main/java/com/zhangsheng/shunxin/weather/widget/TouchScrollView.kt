package com.zhangsheng.shunxin.weather.widget

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.widget.NestedScrollView
import kotlin.math.abs


/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2019/12/17 11:05
 */
class TouchScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {
    private var onScrollistener: OnScrollistener? = null
    private var isNeedScroll = true
    private var maxScrollHeight = 0
    private var scrollState = FOCUS_UP
    private var scrollDownHeight = 0f
    private var lastScrollHeight = 0

    companion object {
        val START_SCROLL = 0
        val STOP_SCROLL = 1
    }

    private val touchEventId = -9983761
    private var isStopScroll = true
    private var lastY = 0

    interface OnScrollistener {
        fun onScroll(scrollY: Int, oldScrollY: Int)
        fun isViewPageVisible(): Boolean
        fun stick(flag: Boolean) {}
        fun onScrollState(int: Int) {}
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onScrollistener = null
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        maxScrollHeight = try {
            scrollDownHeight = height.toFloat()
            getChildAt(0).bottom
        } catch (e: Exception) {
            0
        }
        if (lastScrollHeight != maxScrollHeight && scrollState == FOCUS_DOWN) {
            lastScrollHeight = maxScrollHeight
            fullScroll(FOCUS_DOWN)
        }
    }

    fun upToScroll() {
        if (scrollState == FOCUS_DOWN) {
            fullScroll(FOCUS_UP)
            isNeedScroll = true
            scrollState = FOCUS_UP
            isStopScroll = true
            onScrollistener?.stick(false)
        }
    }

    fun isTop(): Boolean {
        return scrollState == FOCUS_UP
    }


    override fun onScrollChanged(scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        super.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY)
        onScrollistener?.onScroll(scrollY, oldScrollY)

        if (onScrollistener == null || !onScrollistener!!.isViewPageVisible()) return
        if (scrollState == FOCUS_UP && isNeedScroll) {
            if (maxScrollHeight > 0 && maxScrollHeight <= scrollY + scrollDownHeight) {
                scrollState = FOCUS_DOWN
                fullScroll(FOCUS_DOWN)
                isNeedScroll = false
                onScrollistener?.stick(true)
            }
        }

    }

    fun setOnScrollistener(onScrollistener: OnScrollistener?) {
        this.onScrollistener = onScrollistener
    }


    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        if (!isNeedScroll) {
            return false
        }
        return super.onStartNestedScroll(child, target, nestedScrollAxes)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
//        when (ev?.action) {
//            MotionEvent.ACTION_UP -> {
//                handler.sendMessageDelayed(handler.obtainMessage(touchEventId, this), 5);
//            }
//
//            MotionEvent.ACTION_MOVE -> {
//                if (abs(lastY - scrollY) > 50 && isStopScroll && isTop()) {
//                    isStopScroll = false
//                    onScrollistener?.onScrollState(START_SCROLL)
//                }
//            }
//        }

        return isNeedScroll && super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return isNeedScroll && super.onInterceptTouchEvent(ev)
    }


//    private val handler = object : Handler() {
//        override fun handleMessage(msg: Message) {
//            super.handleMessage(msg)
//            if (!isTop()) return
//            val scroller = msg.obj as View
//            if (msg.what === touchEventId) {
//                if (lastY == scroller.scrollY) {
//                    isStopScroll = true
//                    onScrollistener?.onScrollState(STOP_SCROLL)
//                } else {
//                    sendMessageDelayed(
//                        obtainMessage(
//                            touchEventId,
//                            scroller
//                        ), 500
//                    )
//                    lastY = scroller.scrollY
//                }
//            }
//        }
//    }
}