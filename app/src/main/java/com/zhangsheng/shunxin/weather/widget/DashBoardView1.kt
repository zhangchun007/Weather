package com.zhangsheng.shunxin.weather.widget

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.RelativeLayout
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import kotlinx.android.synthetic.main.layout_dash_board_view.view.*

class DashBoardView1 : RelativeLayout {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.layout_dash_board_view, this)
    }

    fun setProgressData(level: String, progressNum: Int) {
        tv_mun.setTextColor(WeatherUtils.airColors(level)[1])
        tv_level.text = level
        im_water.setImageResource(WeatherUtils.headerIcom(level))
        setTextColors(WeatherUtils.airColors(level))
        mdbv.setProgressNum(progressNum.toFloat())
        setProgressNum(progressNum)
    }

    private fun setProgressNum(progressNum1: Int) {
        var progressNum = progressNum1
        if (progressNum > 500) progressNum = 500
        val time: Long = 2000
        post {
            val animator = ValueAnimator.ofInt(0, progressNum).setDuration(time)
            animator.addUpdateListener { animation ->
                var animatedValue = animation.animatedValue as Int
                tv_mun.text = animatedValue.toString()
                mdbv.starAnimator(animatedValue)
            }

            val animation: Animation = RotateAnimation(
                0.0f,
                calculateRelativeAngleWithValue(progressNum),
                rel.width.toFloat(), 0.0f
            )
            animation.duration = time
            animation.fillAfter = true

            animator.start()
            rel.startAnimation(animation)
        }
    }

    private fun setTextColors(list: IntArray) {
        mdbv.setTextColors(list)
    }

//    private fun setAirQuality(text: String) {
//        mdbv.setAirQuality(text)
//    }

    private fun calculateRelativeAngleWithValue(value: Int): Float {
        return 270 * value * 1f / 500
    }
}