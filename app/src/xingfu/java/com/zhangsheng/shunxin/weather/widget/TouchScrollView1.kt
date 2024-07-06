package com.zhangsheng.shunxin.weather.widget

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView


/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2019/12/17 11:05
 */
class TouchScrollView1 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {
    private var onScrollistener: OnScrollistener? = null


    interface OnScrollistener {
        fun onScroll(scrollY: Int, oldScrollY: Int)
    }

    override fun onScrollChanged(scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        super.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY)
        onScrollistener?.onScroll(scrollY, oldScrollY)
    }
    fun setOnScrollistener(onScrollistener: OnScrollistener?) {
        this.onScrollistener = onScrollistener
    }

}