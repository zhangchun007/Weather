package com.zhangsheng.shunxin.weather.fragment

import android.graphics.Color
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.nN
import com.zhangsheng.shunxin.weather.activity.SettingsActivity
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.ClickReport
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.ext.showReport
import com.zhangsheng.shunxin.weather.ext.skipActivity
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean


/**
 * 说明：心晴天气首页天气tab
 * 作者：刘鹏
 * 添加时间：5/14/21 5:42 PM
 * 修改人：liupe
 * 修改时间：5/14/21 5:42 PM
 */
class WeatherFragment : BaseWeatherWithInfoFragment() {
    private var tabColor = "#379BFF"
    private var lastWtid: String = ""

    override fun onResume() {
        super.onResume()
        showReport(EnumType.上报埋点.天气底部tab展示)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            showReport(EnumType.上报埋点.天气底部tab展示)
        }
    }

    override fun showInfoBar(show: Boolean) {
        super.showInfoBar(show)
        if (show) {
            showReport(EnumType.上报埋点.首页信息流频道展示)
        }
    }

    override fun initWeatherBg(data: WeatherBean) {
        binding.weatherBgCurrent.setWeatherStatus(data.wtid)
    }

    override fun weatherBgCutAnim() {
        val wtid = getAppModel().currentWeather.value.nN().weather.nN().wtid
        if (lastWtid != wtid) {
            binding.weatherBgCurrent.setWeatherStatus(wtid)
            lastWtid = wtid
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