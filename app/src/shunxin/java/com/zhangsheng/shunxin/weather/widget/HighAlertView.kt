package com.zhangsheng.shunxin.weather.widget

import android.content.Context
import android.content.Entity
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.*
import android.widget.FrameLayout
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.isVisible
import com.maiya.thirdlibrary.ext.listIndex
import com.maiya.thirdlibrary.ext.setSingleClickListener
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.ClickReport
import com.zhangsheng.shunxin.weather.ext.showReport
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import kotlinx.android.synthetic.main.layout_hight_alert.view.*

class HighAlertView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var play = false

    private var alerts = ArrayList<WeatherBean.Warns>()

    private var currentIndex = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_hight_alert, this)
    }

    fun getCurrentIndex():Int{
        return currentIndex
    }

    fun initData(datas: List<WeatherBean.Warns>) {
        if (datas.isEmpty()) return
        alerts.clear()
        alerts.addAll(datas)
        tv2.text = getAlertText(0, true)
        if (datas.size >= 2) {
            tv1.text = getAlertText(1, false)
            play = true
            startAnim()
            showReport(EnumType.上报埋点.天气预警展示)
        } else {
            ll_left.isVisible(false)
        }
    }

    fun stop() {
        play = false
    }
    fun start() {
        if (alerts.size >= 2) {
            play = true
            startAnim()
        }
    }
    private fun getAlertText(position: Int, isShowView: Boolean): String {
        if (position > alerts.size - 1) return ""
        var str = alerts.listIndex(position).type
        if (isShowView) {
            icon.setImageResource(WeatherUtils.hightAlertIcon(alerts.listIndex(position).type))
            ll_right.exeConfig(ll_right.getConfig().apply {
                this.startColor =
                    Color.parseColor(WeatherUtils.hightAlertColors(alerts.listIndex(position).level)[0])
                this.endColor =
                    Color.parseColor(WeatherUtils.hightAlertColors(alerts.listIndex(position).level)[1])
            })
        } else {
            icon_next.setImageResource(WeatherUtils.hightAlertIcon(alerts.listIndex(position).type))
            ll_left.exeConfig(ll_left.getConfig().apply {
                this.startColor =
                    Color.parseColor(WeatherUtils.hightAlertColors(alerts.listIndex(position).level)[0])
                this.endColor =
                    Color.parseColor(WeatherUtils.hightAlertColors(alerts.listIndex(position).level)[1])
            })
        }

        return if (str.length > 2) str else "${str}预警"

    }

    var animCount = 0;
    private fun startAnim() {
        Try {
            if (!play || animCount != 0||alerts.size<2) return@Try
            animCount += 1
            var animationSet = AnimationSet(true)
            var alphaAnimation = AlphaAnimation(1f, 0f)
            var scaleAnimation = ScaleAnimation(
                1f,
                0f,
                1f,
                0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            animationSet.addAnimation(alphaAnimation)
            animationSet.addAnimation(scaleAnimation)
            animationSet.duration = 500
            animationSet.fillAfter = false
            ll_right.startAnimation(animationSet)

            animationSet.setAnimationListener(object : Animation.AnimationListener {

                override fun onAnimationRepeat(p0: Animation?) {

                }

                override fun onAnimationEnd(p0: Animation?) {
                    tv2.text = getAlertText(currentIndex, true)
                    tv2.postDelayed(Runnable {
                        animCount -= 1
                        startAnim()
                    }, 3000)

                }

                override fun onAnimationStart(p0: Animation?) {

                }
            })

            var animationSet2 = AnimationSet(true)
            var translateAnimation = TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                //X轴移动的结束位置
                Animation.RELATIVE_TO_SELF, 1.05f,
                //y轴开始位置
                Animation.RELATIVE_TO_SELF, 0.0f,
                //y轴移动后的结束位置
                Animation.RELATIVE_TO_SELF, 0f
            )
            var scaleAnimation2 = ScaleAnimation(
                0.8f,
                1f,
                0.8f,
                1f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            animationSet2.addAnimation(translateAnimation)
            animationSet2.addAnimation(scaleAnimation2)
            animationSet2.duration = 500
            animationSet2.fillAfter = false
            ll_left.startAnimation(animationSet2)

            animationSet2.setAnimationListener(
                object : Animation.AnimationListener {
                    override fun onAnimationRepeat(p0: Animation?) {

                    }

                    override fun onAnimationEnd(p0: Animation?) {
                    }

                    override fun onAnimationStart(p0: Animation?) {
                        tv1.text = getNext()
                    }

                })
        }
    }

    fun setClickListener(func:()->Unit){
        ll_right.ClickReport(EnumType.上报埋点.天气预警点击) {
            func()
        }
        ll_left.ClickReport(EnumType.上报埋点.天气预警点击) {
            func()
        }
    }


    private fun getNext(): String {
        return if (alerts.size >= 2) {
            if (currentIndex >= alerts.size - 1) {
                currentIndex = 0
            } else {
                currentIndex += 1
            }
            getAlertText(currentIndex, false)
        } else {
            ""
        }
    }


}