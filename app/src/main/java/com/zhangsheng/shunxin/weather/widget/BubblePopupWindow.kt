package com.zhangsheng.shunxin.weather.widget

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.utils.DisplayUtil

/**
 * 说明：自定义popupwindow
 * 作者：刘鹏
 * 添加时间：5/8/21 4:40 PM
 * 修改人：liupe
 * 修改时间：5/8/21 4:40 PM
 */
class BubblePopupWindow : PopupWindow {

    protected var mActivity: Activity? = null

    private var bubbleView: View? = null

    constructor(activity: Activity?) {
        mActivity = activity
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        isFocusable = false
//        isOutsideTouchable = true
        isClippingEnabled = false
        val dw = ColorDrawable(0)
        setBackgroundDrawable(dw)
    }

    fun setBubbleView(view: View?) {
        bubbleView = view
        contentView = bubbleView
    }

    fun setParam(width: Int, height: Int) {
        setWidth(width)
        setHeight(height)
    }

    fun show(parent: View?) {
        show(parent, Gravity.TOP, getMeasuredWidth() / 2.toFloat())
    }

    fun show(parent: View?, gravity: Int) {
        show(parent, gravity, getMeasuredWidth() / 2.toFloat())
    }

    /**
     * 显示弹窗
     *
     * @param parent
     * @param gravity
     * @param angleScle 气泡尖角位置占整个气泡的比例
     */
    fun show(parent: View?, gravity: Int, angleScle: Float) {
        var angle = angleScle
        if (parent == null) {
            return
        }
        //angleScle为气泡尖角位置处于整个气泡的比例，为0的haul，就默认在1/4位置
        if (angle < 0) {
            angle = 0f
        }
        if (!this.isShowing) {
            val location = IntArray(2)
            parent.getLocationOnScreen(location)
            if (mActivity == null || mActivity.nN().isDestroyed || mActivity.nN().isFinishing) {
                return
            }
            when (gravity) {
                Gravity.BOTTOM -> showAsDropDown(parent)
                Gravity.TOP -> {
                    //目前只针对top模式做处理
                    //获取到目标view的宽度
                    val parentWidth = parent.measuredWidth
                    //x为气泡的x坐标，计算方式为  目标view的x坐标 + （目标view的二分之一宽度 - 气泡x坐标到尖角位置的宽度），计算出来的位置，尖角正对目标view的中间
                    val x =
                        (location[0] + (parentWidth / 2 - getMeasuredWidth() * angle)).toInt()
                    showAtLocation(
                        parent,
                        Gravity.NO_GRAVITY,
                        x,
                        location[1] - getMeasureHeight() + DisplayUtil.dip2px( 2f)
                    )
                }
                Gravity.RIGHT -> showAtLocation(
                    parent,
                    Gravity.NO_GRAVITY,
                    location[0] + parent.width,
                    location[1] - parent.height / 2
                )
                Gravity.LEFT -> showAtLocation(
                    parent,
                    Gravity.NO_GRAVITY,
                    location[0] - getMeasuredWidth(),
                    location[1] - parent.height / 2
                )
                else -> {
                }
            }
        } else {
            this.dismiss()
        }
    }

    /**
     * 测量高度
     *
     * @return
     */
    private fun getMeasureHeight(): Int {
        contentView.measure(
            View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED
        )
        return contentView.measuredHeight
    }

    /**
     * 测量宽度
     *
     * @return
     */
    private fun getMeasuredWidth(): Int {
        contentView.measure(
            View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED
        )
        return contentView.measuredWidth
    }

    /**
     * 释放Activity引用
     */
    fun onDestroy() {
        if (null != mActivity) {
            mActivity = null
        }
    }
}