package com.zhangsheng.shunxin.weather.widget.weather

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.maiya.thirdlibrary.ext.*
import com.zhangsheng.shunxin.databinding.ViewWeatherCardBinding
import com.zhangsheng.shunxin.weather.activity.AirActivity
import com.zhangsheng.shunxin.weather.activity.WeatherActivity
import com.zhangsheng.shunxin.weather.activity.WeatherMapActivity
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.*
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.utils.AnimationUtil
import com.zhangsheng.shunxin.weather.utils.WeatherUtils

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2021/4/14 18:07
 */
class WeatherCardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private val binding = ViewWeatherCardBinding.inflate(LayoutInflater.from(context), this, true)
    private var curCardColor: IntArray? = null
    private var weatherBean: WeatherBean? = null
    private var isRainNeedAnim = false
    private var isRainState = false

    init {
        initListener()
    }

    private fun initListener() {
        binding.llRain.ClickReport(EnumType.上报埋点.首页分钟级降雨点击) {
            skipActivity(WeatherMapActivity::class.java)
        }

        binding.rainLayout.ClickReport(EnumType.上报埋点.首页分钟级降雨点击) {
            skipActivity(WeatherMapActivity::class.java)
        }

        binding.rlWeatherCard.ClickReport(EnumType.上报埋点.首页实况天气) {
            skipActivity(WeatherActivity::class.java)
        }

        binding.llCloud.ClickReport(EnumType.上报埋点.首页实况天气) {
            skipActivity(WeatherActivity::class.java)
        }

        binding.tvAir.ClickReport(EnumType.上报埋点.空气质量模块点击) {
            skipActivity(AirActivity::class.java) {
                putExtra("code", weatherBean.nN().regioncode)
                putExtra(
                    "name", LocationEllipsis(
                        getAppModel().currentWeather.value.nN().weather.nN().regionname,
                        getAppModel().currentWeather.value.nN().weather.nN().isLocation
                    )
                )
                putExtra("location", weatherBean.nN().isLocation)
            }
        }
    }


    fun upDateWeather(it: WeatherBean) {
        this.weatherBean = it
        RainState(it)
        if (it.aqi.isNotEmpty() && it.aqi != "0") {
            showReport(EnumType.上报埋点.空气质量模块展示)
        }
        binding.rainTv.isSelected = true
        if (it.nN().falls != null && it.nN().falls.nN().desc.isNotEmpty() && it.nN().isLocation) {
            binding.rainTv.text = it.nN().falls.nN().desc
        } else {
            binding.rainTv.text = "查看未来2小时降雨预报"
        }
        binding.temp.text = it.nN().tc
        binding.weather.text = it.nN().wt
        binding.cloud.text = it.nN().wdir
        binding.tvWeight.isVisible(true)
        binding.cloudLevel.text = it.nN().ws
        binding.weight.text = "${it.nN().rh}%"

        binding.iconAir.setBackgroundResource(WeatherUtils.leafIcon(it.nN().aqiLevel))
        binding.tvAir.text = "  ${it.nN().aqi}  ${it.nN().aqiLevel}"
        binding.tvAir.isVisible(it.nN().aqi.isNotEmpty() && it.nN().aqi != "0")
    }

    fun UpdateCardColor(colors: IntArray, isAnim: Boolean = false) {
        Try {
            if (curCardColor == null) {
                binding.rlWeatherCard.exeConfig(binding.rlWeatherCard.getConfig().apply {
                    this.startColor = colors[0]
                    this.endColor = colors[1]
                })
                curCardColor = colors
            } else if (curCardColor != colors) {
                if (isAnim) {
                    AnimationUtil.ChangeCardColor(
                        binding.rlWeatherCard,
                        curCardColor.nN(),
                        colors,
                        500
                    )
                } else {
                    binding.rlWeatherCard.exeConfig(binding.rlWeatherCard.getConfig().apply {
                        this.startColor = colors[0]
                        this.endColor = colors[1]
                    })
                }
                curCardColor = colors
            }
        }
    }

    private fun RainState(it: WeatherBean?) {
        var rains = it.nN().falls.nN().rss.nN().filter { rain ->
            rain > 0.08f
        }
        isRainNeedAnim = if (rains.isNotEmpty()) {
            changeRainState(it.nN().falls.nN(), rains)
            true
        } else {
            closeRainState()
            false
        }
    }

    private fun changeRainState(falls: WeatherBean.Fall, rains: List<Float>) {
        Try {
            if (!isRainState) {
                binding.rainChart.setData(falls.nN().rss.nN())
                isRainState = true
                binding.weather.runOnTime(2000) {
                    var anim = ValueAnimator.ofFloat(0f, 1f)
                    anim.duration = 200

                    anim.addUpdateListener {

                        var rate = it.animatedValue as Float

                        binding.temp.setTextSize(
                            TypedValue.COMPLEX_UNIT_DIP,
                            72f - (72f - 65f) * rate
                        )
                        binding.weather.setTextSize(
                            TypedValue.COMPLEX_UNIT_DIP,
                            24f - (24f - 20f) * rate
                        )

                        binding.rlWeatherCard.layoutParams =
                            binding.rlWeatherCard.layoutParams.apply {
                                this?.height = (130f.dp2Px() + (150f - 130f).dp2Px() * rate).toInt()
                            }

                        binding.llCloud.layoutParams =
                            (binding.llCloud.layoutParams as RelativeLayout.LayoutParams).apply {
                                addRule(RIGHT_OF, binding.rlCardDate.id)
                                this.topMargin = (55f.dp2Px() + (80f - 55f).dp2Px() * rate).toInt()
                                this.leftMargin =
                                    (118f.dp2Px() - (118f - 10f).dp2Px() * rate).toInt()
                            }

                        binding.weather.layoutParams =
                            (binding.weather.layoutParams as RelativeLayout.LayoutParams).apply {
                                this.topMargin = (7f.dp2Px() + (39f - 7f).dp2Px() * rate).toInt()
                                this.leftMargin = (29f.dp2Px() - 29f.dp2Px() * rate).toInt()
                            }
                        binding.air.layoutParams =
                            (binding.air.layoutParams as FrameLayout.LayoutParams).apply {
                                this.gravity = Gravity.RIGHT xor Gravity.BOTTOM
                            }

                    }

                    binding.rainLayout.isVisible(true)
                    binding.animRainLottie.imageAssetsFolder = "images"
                    binding.animRainLottie.setAnimation(
                        WeatherUtils.getAnimRainLottie(
                            falls.rssPre,
                            rains.listIndex(0)
                        )
                    )
                    binding.animRainLottie.playAnimation()
                    binding.rainChart.animShow()
                    anim?.start()
                }
            } else {
                binding.rainChart.refreshData(falls.nN().rss.nN())
                binding.animRainLottie.setAnimation(
                    WeatherUtils.getAnimRainLottie(
                        falls.rssPre,
                        rains.listIndex(0)
                    )
                )
                binding.animRainLottie.playAnimation()
            }
        }
    }

    private fun closeRainState() {
        Try {
            if (isRainState) {
                isRainState = false
                binding.weather.runOnTime(2000) {
                    var anim = ValueAnimator.ofFloat(0f, 1f)
                    anim.duration = 200

                    anim?.addUpdateListener {

                        val rate = it.animatedValue as Float

                        binding.temp.setTextSize(
                            TypedValue.COMPLEX_UNIT_DIP,
                            65f + (72f - 65f) * rate
                        )
                        binding.weather.setTextSize(
                            TypedValue.COMPLEX_UNIT_DIP,
                            20f + (24f - 20f) * rate
                        )

                        binding.rlWeatherCard.layoutParams =
                            binding.rlWeatherCard.layoutParams.apply {
                                this.height = (150f.dp2Px() - (150f - 130f).dp2Px() * rate).toInt()
                            }
                        binding.llCloud.layoutParams =
                            (binding.llCloud.layoutParams as RelativeLayout.LayoutParams).apply {
                                addRule(RIGHT_OF, binding.temp.id)
                                this.topMargin = (80f.dp2Px() - (80f - 55f).dp2Px() * rate).toInt()
                                this.leftMargin =
                                    (10f.dp2Px() + (29f - 10f).dp2Px() * rate).toInt()
                            }

                        binding.weather.layoutParams =
                            (binding.weather.layoutParams as RelativeLayout.LayoutParams).apply {
                                this.topMargin = (39f.dp2Px() - (39f - 7f).dp2Px() * rate).toInt()
                                this.leftMargin = (29f.dp2Px() * rate).toInt()
                            }
                        binding.air.layoutParams =
                            (binding.air.layoutParams as FrameLayout.LayoutParams).apply {
                                this.gravity = Gravity.RIGHT xor Gravity.TOP
                            }

                    }

                    binding.rainLayout.isVisible(false)
                    binding.animRainLottie.cancelAnimation()
                    binding.rainChart.animDissMiss()
                    anim?.start()
                }
            }
        }
    }


    fun stopRainAnim() {
        binding.animRainLottie.let {
            if (it.isAnimating) {
                it.cancelAnimation()
            }
        }
    }

    fun startRainAnim() {
        if (isRainNeedAnim) {
            binding.animRainLottie.let {
                if (!it.isAnimating) {
                    it.playAnimation()
                }
            }
        }
    }


}