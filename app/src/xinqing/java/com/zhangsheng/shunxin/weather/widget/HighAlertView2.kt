package com.zhangsheng.shunxin.weather.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.animation.*
import android.widget.FrameLayout
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.isVisible
import com.maiya.thirdlibrary.ext.listIndex
import com.zhangsheng.shunxin.databinding.LayoutHightAlertBinding
import com.zhangsheng.shunxin.databinding.LayoutHightAlertsBinding
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.utils.WeatherUtils


class HighAlertView2 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var play = false

    private var alerts = ArrayList<WeatherBean.Warns>()

    private var currentIndex = 0

    private val binding = LayoutHightAlertsBinding.inflate(LayoutInflater.from(context), this, true)

    fun getCurrentIndex(): Int {
        return currentIndex
    }

    fun initData(datas: List<WeatherBean.Warns>) {
        if (datas.isEmpty()) return
        alerts.clear()
        alerts.addAll(datas)
        binding.tv2.text = getAlertText(0, true)
        if (datas.size >= 2) {
            binding.tv1.text = getAlertText(1, false)
            play = true
            startAnim()
            binding.llRight.isVisible(true)
        } else {
            binding.llRight.isVisible(false)
            binding.llLeft.setBackgroundResource(WeatherUtils.hightAlertBg(alerts.listIndex(0).level))
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
        val str = alerts.listIndex(position).type
        if (isShowView) {
            binding.llLeft.setBackgroundResource(WeatherUtils.hightAlertBg(alerts.listIndex(position).level))

        } else {
            binding.llRight.setBackgroundResource(
                WeatherUtils.hightAlertBg(
                    alerts.listIndex(
                        position
                    ).level
                )
            )
        }
        return if (str.length > 2) str else "${str}预警"

    }

    var animCount = 0;
    private fun startAnim() {
        Try {
            if (!play || animCount != 0 || alerts.size < 2) return@Try
            animCount += 1
            var animationSet = AnimationSet(true)
            var alphaAnimation = AlphaAnimation(1f, 0f)
            var scaleAnimation = ScaleAnimation(
                1f,
                0.9f,
                1f,
                0.9f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            animationSet.addAnimation(alphaAnimation)
            animationSet.addAnimation(scaleAnimation)
            animationSet.duration = 300
            animationSet.fillAfter = false
            binding.llLeft.startAnimation(animationSet)

            animationSet.setAnimationListener(object : Animation.AnimationListener {

                override fun onAnimationRepeat(p0: Animation?) {

                }

                override fun onAnimationEnd(p0: Animation?) {
                    binding.tv2.text = getNext(true)
                    if (getCurrentIndex() == alerts.size - 1) {
                        binding.tv1.text = getAlertText(0, false)
                    } else {
                        binding.tv1.text = getAlertText(getCurrentIndex() + 1, false)
                    }
                    binding.tv2.postDelayed(Runnable {
                        animCount -= 1
                        startAnim()
                    }, 3000)

                }

                override fun onAnimationStart(p0: Animation?) {

                }
            })

            var animationSet2 = AnimationSet(true)
            var translateAnimation = TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                //X轴移动的结束位置
                Animation.RELATIVE_TO_SELF, 0f,
                //y轴开始位置
                Animation.RELATIVE_TO_SELF, 0f,
                //y轴移动后的结束位置
                Animation.RELATIVE_TO_SELF, -0.1f
            )
            var scaleAnimation2 = ScaleAnimation(
                1.0f,
                1f,
                1f,
                1.1f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            var alphaAnimation2 = AlphaAnimation(0.35f, 1f)
            animationSet2.addAnimation(translateAnimation)
            animationSet2.addAnimation(scaleAnimation2)
            animationSet2.addAnimation(alphaAnimation2)
            animationSet2.duration = 300
            animationSet2.fillAfter = false
            binding.llRight.startAnimation(animationSet2)

            animationSet2.setAnimationListener(
                object : Animation.AnimationListener {
                    override fun onAnimationRepeat(p0: Animation?) {

                    }

                    override fun onAnimationEnd(p0: Animation?) {
                    }

                    override fun onAnimationStart(p0: Animation?) {
//                        binding.tv1.text = getNext()
                    }

                })

        }

    }


    private fun getNext(isShowView: Boolean): String {
        return if (alerts.size >= 2) {

            currentIndex++
            currentIndex = currentIndex % alerts.size

            Log.e("llLeft", "getNext--currentIndex==" + currentIndex + "--isShowView=" + isShowView)
            getAlertText(currentIndex, isShowView)
        } else {
            ""
        }
    }


}