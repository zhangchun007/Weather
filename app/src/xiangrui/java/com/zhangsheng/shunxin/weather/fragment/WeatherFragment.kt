@file:Suppress("DEPRECATION")

package com.zhangsheng.shunxin.weather.fragment

import com.maiya.thirdlibrary.ext.nN
import com.zhangsheng.shunxin.weather.activity.SettingsActivity
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.ClickReport
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.ext.showReport
import com.zhangsheng.shunxin.weather.ext.skipActivity
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean


/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/7/15 14:43
 */
class WeatherFragment : BaseWeatherFragment() {
    private var lastWtid: String = ""

    override fun updateInfoBarData(data: WeatherBean) {
    }

    override fun initWeatherBg(data: WeatherBean) {
        binding.weatherBg.setWeatherStatus(data.wtid)
    }

    override fun weatherBgCutAnim() {
        val wtid = getAppModel().currentWeather.value.nN().weather.nN().wtid
        if (lastWtid != wtid) {
            lastWtid = wtid
            binding.weatherBg.setWeatherStatus(wtid)
        }
    }

    override fun changeBarColor(scrollY: Int) {
        binding.refreshLayout.setEnableRefresh(scrollY <= 0)
    }

    override fun initListener() {
        super.initListener()
        binding.setting.ClickReport(EnumType.上报埋点.首页设置点击) {
            skipActivity(SettingsActivity::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        showReport(EnumType.上报埋点.天气底部tab展示)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden){
            showReport(EnumType.上报埋点.天气底部tab展示)
        }
    }
}