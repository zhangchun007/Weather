package com.zhangsheng.shunxin.weather.fragment

import android.graphics.Color
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.View.AUTOFILL_TYPE_TEXT
import androidx.core.view.isVisible
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.utils.StatusBarUtil
import com.xm.xmlog.XMLogAgent
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.information.utils.DensityUtil
import com.zhangsheng.shunxin.weather.activity.SettingsActivity
import com.zhangsheng.shunxin.weather.bottom.BottomBarItem
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.ClickReport
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.ext.showReport
import com.zhangsheng.shunxin.weather.ext.skipActivity
import com.zhangsheng.shunxin.weather.model.AppViewModel
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.utils.*
import org.libpag.PAGFile
import org.libpag.PAGScaleMode
import org.libpag.PAGView


/**
 * 说明：心晴天气首页天气tab
 * 作者：刘鹏
 * 添加时间：5/14/21 5:42 PM
 * 修改人：liupe
 * 修改时间：5/14/21 5:42 PM
 */
class WeatherFragment : BaseWeatherWithInfoFragment() {
    private var tabColor = "#ffffff"
    private var weatherBg: String = ""

    //当前tab栏滑动的状阀值
    private var fraction = 0f

    //记录当前vp的位置
    private var currentpos = 0
    override fun onResume() {
        super.onResume()
        if (getAppModel().appPageIndex == BottomBarItem.CMD_WEATHER) {
            if (binding.infoBar.rlInfoTopBar.isVisible) {
                XMLogAgent.onPageStart(EnumType.上报埋点.信息流主页)
            } else {
                showReport(EnumType.上报埋点.天气底部tab展示)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (getAppModel().appPageIndex == BottomBarItem.CMD_WEATHER) {
            if (binding.infoBar.rlInfoTopBar.isVisible) {
                XMLogAgent.onPageEnd(EnumType.上报埋点.信息流主页)
            }
        }
    }

    override fun initView() {
        super.initView()
        binding.weatherBgCurrent.setScaleMode(PAGScaleMode.Stretch)
        binding.weatherBgCurrent.setRepeatCount(-1)

        binding.weatherBg.setScaleMode(PAGScaleMode.Stretch)
        binding.weatherBg.setRepeatCount(-1)

        if (activity != null)
            StatusBarUtil.setStatusBarDarkTheme(activity, true)
    }


    override fun showInfoBar(show: Boolean) {
        super.showInfoBar(show)
        if (!show) {
            val wtid = getAppModel().currentWeather.value.nN().weather.nN().wtid
            if (WeatherUtils.isNeedChangeColor(wtid)) {
                Handler().postDelayed({
                    changeImageViewsWhiteOrBlack(true)
                }, 500)
            }
        }
    }

    override fun initListener() {
        super.initListener()
        binding.imgHomeSetting.ClickReport(EnumType.上报埋点.首页设置点击) {
            skipActivity(SettingsActivity::class.java)
        }
    }

    override fun initWeatherBg(data: WeatherBean) {
//        binding.weatherBg.setBackgroundResource(WeatherUtils.getPagWeatherBg(data.wtid))
        binding.weatherBg.file =
            PAGFile.Load(activity.nN().assets, WeatherUtils.getWeatherPagAnimBg(data.wtid))
        binding.weatherBg.stop()
    }

    override fun weatherBgCutAnim() {
        binding.weatherBg.removeCallbacks(changeBg)
        binding.weatherBg.postDelayed(changeBg, 300)
    }


    private var changeBg = Runnable {
        Try {
            val wtid = getAppModel().currentWeather.value.nN().weather.nN().wtid
            if (weatherBg != WeatherUtils.getWeatherPagAnimBg(wtid)) {
                var res = PAGFile.Load(activity.nN().assets, WeatherUtils.getWeatherPagAnimBg(wtid))
                CustomAnimationUtil.pagAnimChange(
                    binding.weatherBgCurrent,
                    binding.weatherBg,
                    res
                )
                weatherBg = WeatherUtils.getWeatherPagAnimBg(wtid)
            }

            //文字需要从黑色更改成白色更改颜色
            if (WeatherUtils.isNeedChangeColor(wtid)) {

                //设置WeatherPageFragment中文字的颜色
                getCurFragment(index)?.UpdateCardColor(true)
                changeImageViewsWhiteOrBlack(true)

            } else {

//                设置WeatherPageFragment中文字的颜色
                getCurFragment(index)?.UpdateCardColor(false)
                changeImageViewsWhiteOrBlack(false)
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
                    fraction = 0f
                    binding.topBar.background.mutate().alpha = 0
                }
                scrollY.toFloat() <= bgAlphaHeight -> {
                    alpha = (scrollY.toFloat() - bgHalfHeight) / (bgAlphaHeight - bgHalfHeight)
                    fraction = alpha
                    binding.topBar.background.mutate().alpha = (alpha * 255).toInt()
                }
                else -> {
                    binding.topBar.background.mutate().alpha = 255
                    fraction = 1f
                }
            }

            Log.e("changeBarColor", "scrollY==" + scrollY + "--fraction=" + fraction)
            binding.viewShadow.visibility =
                if (scrollY > 1990 && !binding.infoBar.rlInfoTopBar.isVisible) View.VISIBLE else View.GONE
            //设置信息流顶部定位logo为黑色
            mIvInfoLocation?.setImageResource(R.mipmap.icon_location_black)

            val wtid = getAppModel().currentWeather.value.nN().weather.nN().wtid
            if (fraction > 0.1 && WeatherUtils.isNeedChangeColor(wtid)) {
                changeTableBarTextColor(fraction)
            }
        }
    }

    /**
     * 设置状态栏颜色
     */
    override fun setStatusBarDarkTheme() {
        if (activity == null || !isAdded) return
        val wtid = getAppModel().currentWeather.value.nN().weather.nN().wtid
        if (WeatherUtils.isNeedChangeColor(wtid)) {
            StatusBarUtil.setStatusBarDarkTheme(activity, false)
        } else {
            StatusBarUtil.setStatusBarDarkTheme(activity, true)
        }
    }

    /**
     * 当天气背景是暗色的时候，顶部导航是文字是白色，随着滑动需要变成黑色
     */
    var lastFraction = -1f //防止过度刷新
    private fun changeTableBarTextColor(fraction: Float) {
        var currentColor = CalculateUtil.colorEvaluate(
            fraction,
            Color.parseColor("#FFFFFF"),
            Color.parseColor("#222222")
        )
        binding.tvLocation.setTextColor(currentColor)
        binding.imgLocalAdd.setColorFilter(currentColor)
        binding.iconLocation.setColorFilter(currentColor)
        binding.imgHomeSetting.setColorFilter(currentColor)

        if (fraction <= 0.25f && lastFraction != fraction) {
            changeTabViewsColorWhiteOrBlack(true)

        } else if (fraction == 1.0f && lastFraction != fraction) {
            changeTabViewsColorWhiteOrBlack(false)
        }

        lastFraction = fraction
    }

    private fun changeTabViewsColorWhiteOrBlack(isWhite: Boolean) {
        if (isWhite) {
            getAppModel().isRefreshBlackModel = true
            binding.tabView.notifyData(WeatherUtils.initData())
            binding.speak.setAnimation("icon_voice_white.json")
            binding.refreshStatusView.setRefeshTextColor(getAppModel().isRefreshBlackModel)
            if (activity != null)
                StatusBarUtil.setStatusBarDarkTheme(activity, false)
        } else {
            getAppModel().isRefreshBlackModel = false
            binding.tabView.notifyData(WeatherUtils.initData())
            binding.speak.setAnimation("icon_voice.json")
            binding.refreshStatusView.setRefeshTextColor(getAppModel().isRefreshBlackModel)
            if (activity != null)
                StatusBarUtil.setStatusBarDarkTheme(activity, true)
        }
    }

    private fun changeImageViewsWhiteOrBlack(isWhite: Boolean) {
        if (isWhite){
            binding.imgLocalAdd.setColorFilter(Color.parseColor("#FFFFFF"))
            binding.iconLocation.setColorFilter(Color.parseColor("#FFFFFF"))
            binding.imgHomeSetting.setColorFilter(Color.parseColor("#FFFFFF"))
            binding.imgLocalAdd.setImageResource(R.mipmap.icon_local_add_white)
            binding.iconLocation.setImageResource(R.mipmap.icon_location_white)
            binding.imgHomeSetting.setImageResource(R.mipmap.icon_home_setting_white)
            binding.tvLocation.setTextColor(Color.parseColor("#FFFFFF"))

            changeTabViewsColorWhiteOrBlack(isWhite)

        }else{
            binding.imgLocalAdd.setColorFilter(Color.parseColor("#222222"))
            binding.iconLocation.setColorFilter(Color.parseColor("#222222"))
            binding.imgHomeSetting.setColorFilter(Color.parseColor("#222222"))
            binding.imgLocalAdd.setImageResource(R.mipmap.icon_local_add_black)
            binding.iconLocation.setImageResource(R.mipmap.icon_location_black)
            binding.imgHomeSetting.setImageResource(R.mipmap.icon_home_setting)
            binding.tvLocation.setTextColor(Color.parseColor("#222222"))

            changeTabViewsColorWhiteOrBlack(isWhite)
        }
    }
}