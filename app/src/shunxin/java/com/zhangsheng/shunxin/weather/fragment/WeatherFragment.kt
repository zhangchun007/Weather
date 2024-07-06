package com.zhangsheng.shunxin.weather.fragment

import android.graphics.Color
import androidx.core.view.isVisible
import com.maiya.thirdlibrary.ext.*
import com.zhangsheng.shunxin.weather.activity.SettingsActivity
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.utils.AnimationUtil
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import com.xm.xmlog.XMLogAgent
import com.zhangsheng.shunxin.weather.ext.*
import com.zhangsheng.shunxin.weather.widget.transformer.ZoomOutPageTransformer

/**
 * 说明：顺心天气首页
 * 作者：刘鹏
 * 添加时间：5/14/21 4:52 PM
 * 修改人：liupe
 * 修改时间：5/14/21 4:52 PM
 */
class WeatherFragment : BaseWeatherWithInfoFragment() {
    private var tabColor = "#00000000"
    private var weatherBg: Int = -1
    override fun initView() {
        super.initView()
        binding.vp.setPageTransformer(true, ZoomOutPageTransformer())
    }

    override fun showInfoBar(show: Boolean) {
        super.showInfoBar(show)
        if (show) {
            binding.weatherBgCurrent.let {
                if (it.isAnimating) {
                    binding.weatherBgCurrent.pauseAnimation()
                }
            }
        } else {
            binding.weatherBgCurrent.let {
                if (!it.isAnimating) {
                    binding.weatherBgCurrent.playAnimation()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (binding.infoBar.rlInfoTopBar.isVisible) {
            XMLogAgent.onPageStart(EnumType.上报埋点.信息流主页)
        }else{
            showReport(EnumType.上报埋点.天气底部tab展示)
        }
    }

    override fun onPause() {
        super.onPause()
        stopSpeak()
        if (binding.infoBar.rlInfoTopBar.isVisible) {
            XMLogAgent.onPageEnd(EnumType.上报埋点.信息流主页)
        }
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
//            getCurFragment(index)?.onReLoad()
//            refreshMainData()
        } else {
            stopSpeak()
//            getCurFragment(index)?.onHidden()
        }


    }

    override fun initWeatherBg(data: WeatherBean) {
        binding.weatherBg.setBackgroundResource(WeatherUtils.getWeatherBg(data.wtid))
    }

    override fun weatherBgCutAnim() {
        binding.weatherBg.removeCallbacks(changeBg)
        binding.weatherBg.postDelayed(changeBg, 300)
    }

    private var changeBg = Runnable {
        Try {
            var wtid = getAppModel().currentWeather.value.nN().weather.nN().wtid
            if (wtid == "-1") WeatherUtils.getDatas().listIndex(index)
            if (weatherBg != WeatherUtils.getWeatherBg(wtid)) {
                weatherBg = WeatherUtils.getWeatherBg(wtid)
                AnimationUtil.animChange(
                    binding.weatherBgCurrent,
                    binding.weatherBg,
                    WeatherUtils.getWeatherAnimBg(wtid)
                )
                tabColor = WeatherUtils.getTopBarColor(wtid)
            }

            getCurFragment(index)?.UpdateCardColor(WeatherUtils.weatherCardColor(wtid), true)
            if (index > 0) {
                getCurFragment(index - 1)?.UpdateCardColor(WeatherUtils.weatherCardColor(wtid))
            }
            if (index < WeatherUtils.getDatas().size - 1) {
                getCurFragment(index + 1)?.UpdateCardColor(WeatherUtils.weatherCardColor(wtid))
            }
        }
    }

    override fun changeBarColor(scrollY: Int) {
        Try {
            binding.topBar.setBackgroundColor(Color.parseColor(tabColor))
            binding.infoBar.rlInfoTopBar.setBackgroundColor(Color.parseColor(tabColor))
            binding.refreshLayout.setEnableRefresh(scrollY <= 0)
            when {
                scrollY <= bgHalfHeight -> {
                    alpha = scrollY.toFloat() / bgHalfHeight
                    binding.topBar.background.mutate().alpha = 0
                }
                scrollY.toFloat() <= bgAlphaHeight -> {
                    alpha = (scrollY.toFloat() - bgHalfHeight) / (bgAlphaHeight - bgHalfHeight)
                    binding.topBar.background.mutate().alpha = (alpha * 255).toInt()
                }
                else -> {
                    binding.topBar.background.mutate().alpha = 255
                }
            }
        }
    }

    override fun initListener() {
        super.initListener()
        binding.setting.ClickReport(EnumType.上报埋点.首页设置点击) {
            skipActivity(SettingsActivity::class.java)
        }

    }

}