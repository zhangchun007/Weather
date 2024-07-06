package com.zhangsheng.shunxin.weather.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.maiya.thirdlibrary.ext.*
import com.zhangsheng.shunxin.databinding.LayoutWeatherMiddleBinding
import com.zhangsheng.shunxin.weather.activity.FifWeatherActivity
import com.zhangsheng.shunxin.weather.activity.WeatherMapActivity
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.ClickReport
import com.zhangsheng.shunxin.weather.ext.skipActivity
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.utils.DataUtil
import com.zhangsheng.shunxin.weather.utils.WeatherUtils

class WeatherMiddleLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    val binding = LayoutWeatherMiddleBinding.inflate(LayoutInflater.from(context), this, true)


    init {
        initListener()
    }

    private fun initListener() {
        binding.llWeather.ClickReport(EnumType.上报埋点.首页分钟级降雨点击) {
            skipActivity(WeatherMapActivity::class.java)
        }
        binding.layoutToday.ClickReport(EnumType.上报埋点.首页今天明天模块今天) {
            skipActivity(FifWeatherActivity::class.java) {
                putExtra("position", 1)
                putExtra("source", "hometoday-15xiangqin")
            }
        }
        binding.layoutTomorrow.ClickReport(EnumType.上报埋点.首页今天明天模块明天) {
            skipActivity(FifWeatherActivity::class.java) {
                putExtra("position", 2)
                putExtra("source", "hometoday-15xiangqin")
            }
        }

    }


    fun setWeatherDate(it: WeatherBean) {

        RainState(it)

        binding.rainTv.isSelected = true
        if (it.nN().falls != null && it.nN().falls.nN().desc.isNotEmpty() && it.nN().isLocation) {
            binding.rainTv.text = it.nN().falls.nN().desc
        } else {
            binding.rainTv.text = "查看未来2小时降雨预报"
        }

        if (it.nN().wtablesNew.nN().size == 2) {
            var day = it.nN().wtablesNew.nN()
                .sortedBy { wt -> DataUtil.Data2TimeStamp(wt.fct, "yyyy-MM-dd") }

            binding.todayTemp.text = day.listIndex(0).tcr

            binding.todayWeather.text = day.listIndex(0).wt

            binding.tomorrowTemp.text = day.listIndex(1).tcr
            binding.tomorrowWeather.text = day.listIndex(1).wt

            binding.todayWeatherIcon.apply {
                progress = 0f
                setAnimation(WeatherUtils.getWeatherAnimBg1(day.listIndex(0).wtid))
                imageAssetsFolder = "images"
                playAnimation()
            }


            binding.tomorrowWeatherIcon.apply {
                progress = 0f
                setAnimation(WeatherUtils.getWeatherAnimBg1(day.listIndex(1).wtid))
                imageAssetsFolder = "images"
                playAnimation()
            }

        }
    }


    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)

        if (visibility == View.VISIBLE) {

            if (!binding.animRainLottie.isAnimating && isRainNeedAnim) {
                binding.animRainLottie.playAnimation()
            }
        } else {
            if (binding.animRainLottie.isAnimating) {
                binding.animRainLottie.cancelAnimation()
            }
        }
    }


    private var isRainNeedAnim = false
    private fun RainState(it: WeatherBean?) {
        var rains = it.nN().falls.nN().rss.nN().filter { rain ->
            rain > 0.08f
        }

        if (rains.isNotEmpty()) {
            binding.rainChart.setData(it.nN().falls.nN().rss.nN())
            binding.rainLayout.isVisible(true)
            binding.animRainLottie.imageAssetsFolder = "images"
            binding.animRainLottie.setAnimation(
                WeatherUtils?.getAnimRainLottie(
                    it.nN().falls.nN().rssPre,
                    rains.listIndex(0)
                )
            )
            binding.animRainLottie.playAnimation()
            isRainNeedAnim = true
            binding.rainChart.animShow()
        } else {
            isRainNeedAnim = false
            binding.rainLayout.isVisible(false)
            binding.animRainLottie.cancelAnimation()
        }
    }


}