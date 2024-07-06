@file:Suppress("DEPRECATION")

package com.zhangsheng.shunxin.weather.fragment

import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.isVisible
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.ext.runOnUi
import com.maiya.thirdlibrary.widget.shapview.ShapeView
import com.maiya.thirdlibrary.widget.smartlayout.adapter.SmartViewHolder
import com.maiya.thirdlibrary.widget.smartlayout.listener.SmartRecycleListener
import com.necer.utils.DensityUtil
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.activity.SettingsActivity
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.ClickReport
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.ext.showReport
import com.zhangsheng.shunxin.weather.ext.skipActivity
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.utils.WeatherUtils


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
        binding.weatherBg.setBackgroundResource(WeatherUtils.setHomeWeatherBg(data.wtid))
        binding.weatherBg1.setBackgroundColor(WeatherUtils.setHomeWeatherBgColor(data.wtid))
        binding.mainView.post {
            runOnUi {
                Try {
                    WeatherUtils.appHomeHigh =
                        binding.weatherBg.height - binding.topBar.height - DensityUtil.dp2px(16)
                }
            }
        }
    }

    override fun weatherBgCutAnim() {
        val wtid = getAppModel().currentWeather.value.nN().weather.nN().wtid
        if (lastWtid != wtid) {
            lastWtid = wtid
            binding.weatherBg1.setBackgroundColor(WeatherUtils.setHomeWeatherBgColor(wtid))
            binding.weatherBg.setBackgroundResource(WeatherUtils.setHomeWeatherBg(wtid))
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

    override fun initTabView() {
        binding.tabView.setSmartListener(object : SmartRecycleListener() {
            override fun AutoAdapter(
                holder: SmartViewHolder,
                item: Any,
                tabPosition: Int
            ) {
                super.AutoAdapter(holder, item, tabPosition)
                val tab = holder.findView<ShapeView>(R.id.tab)
                val tab_select = holder.findView<ShapeView>(R.id.tab_select)
                tab_select.isVisible(index > 1)
                if (index == tabPosition) {
                    tab.isVisible(false)
                    tab_select.isVisible(true)
                } else {
                    tab.isVisible(true)
                    tab_select.isVisible(false)
                }
            }
        })
    }

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
}