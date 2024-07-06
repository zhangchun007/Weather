package com.zhangsheng.shunxin.weather.widget.weather

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.maiya.thirdlibrary.ext.*
import com.my.business.imp.plu.IPluViewListener
import com.zhangsheng.shunxin.databinding.LayoutWeatherRainSnowForecastBinding
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.net
import com.zhangsheng.shunxin.weather.ext.showReport
import com.zhangsheng.shunxin.weather.net.ReCodeUtils
import com.zhangsheng.shunxin.weather.net.bean.WeatherRainSnowForecastBean
import com.zhangsheng.shunxin.weather.utils.DataUtil
import com.zhangsheng.shunxin.weather.utils.LocationUtil
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.json.JSONObject

class WeatherRainSnowForecast @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private val binding =
        LayoutWeatherRainSnowForecastBinding.inflate(LayoutInflater.from(context), this, true)
    private val CHANNEL = "WeatherForecast"
    private var listener: IPluViewListener? = null
    private var iconUrl: String = ""
    private var iconName: String = ""


    fun requestDate(param: JSONObject) {
        iconUrl = param.optString("iconurl", "")
        iconName = param.optString("iconname", "")
        requestDate { data ->
            initView(data)
        }
    }

    fun setIPluViewListener(listener: IPluViewListener) {
        this.listener = listener
    }

    private fun initView(bean: WeatherRainSnowForecastBean) {
        listener?.onPassVerification()
        if (iconUrl.isNotEmpty()) {
            Glide.with(binding.iconView).load(iconUrl).into(binding.iconView)
        }

        if (bean.ftc.isNotEmpty()){
            Try {
               binding.tvTime.text="更新于${DataUtil.date2date(bean.ftc,"yyyy-MM-dd HH:mm:ss","HH:mm")}"
            }
        }else{
            showReport(EnumType.上报埋点.雨雪天气提醒上报,subactid = "RainSnow_ftc_empty")
            listener?.onViewLoadedFailed("RainSnow_ftc_empty")
            return
        }

        if (bean.nN().fall.nN().desc.isNullOrEmpty()){
            showReport(EnumType.上报埋点.雨雪天气提醒上报,subactid = "RainSnow_desc_empty")
            listener?.onViewLoadedFailed("RainSnow_desc_empty")
            return
        }
        if (bean.fall.nN().rss.isNullOrEmpty()){
            showReport(EnumType.上报埋点.雨雪天气提醒上报,subactid = "RainSnow_rss_empty")
            listener?.onViewLoadedFailed("RainSnow_rss_empty")
            return
        }
        if (bean.fall.nN().rssPre!="1"&&bean.fall.nN().rssPre!="0"){
            showReport(EnumType.上报埋点.雨雪天气提醒上报,subactid = "RainSnow_rssPre_error")
            listener?.onViewLoadedFailed("RainSnow_rssPre_error")
            return
        }

        if (iconName.isNotEmpty()) {
            binding.iconTitle.text = iconName
        }
        if (bean.tc.isNullOrEmpty()||bean.wt.isNullOrEmpty()||bean.wtid.isNullOrEmpty()){
            binding.tvTemp.isVisible(false)
            binding.tvWeather.isVisible(false)
            binding.iconWeather.isVisible(false)
        }else{
            binding.tvTemp.text = "${bean.tc}°"
            binding.tvWeather.text = bean.wt
            binding.iconWeather.setImageResource(WeatherUtils.getWeatherForecastIcon(bean.wtid, false))
        }
        binding.tvRainDes.text = bean.nN().fall.nN().desc
        binding.title.text = if (bean.fall.nN().rssPre == "1") "短时降雪预报" else "短时降雨预报"

        binding.animRainLottie.imageAssetsFolder = "images"
        binding.animRainLottie.setAnimation(if (bean.fall.nN().rssPre == "1") "dsyb_snow.json" else "dsyb_rain.json")
        binding.animRainLottie.playAnimation()

        binding.tvLocation.text =
            LocationUtil.getLocation().district.isStr(LocationUtil.getLocation().city)
        binding.tvLocation.isSelected = true


        try {
            binding.chart.setData(bean.fall.nN().rss.nN())
        } catch (e: Exception) {
        }
        listener?.onViewRenderSuccess(this, binding.exLayout, binding.close)
    }

    private fun requestDate(initView: (WeatherRainSnowForecastBean) -> Unit) {

        var location = LocationUtil.getLocation()

        if (location.lng.parseDouble() > 0 && location.lat.parseDouble() > 0) {
            GlobalScope.async(Dispatchers.IO) {
                try {
                    var data = net().城市降雨提醒(
                        location.nN().lng,
                        location.nN().lat,
                        location.nN().province,
                        location.nN().city,
                        location.nN().district,
                        "${location.nN().locationType}"
                    )
                    if (data.nN().get("code").asString == "1") {
                        withContext(Dispatchers.Main){
                            initView(
                                Gson().fromJson(
                                    ReCodeUtils.decode(
                                        data.nN().get("data").asString
                                    ), WeatherRainSnowForecastBean::class.java
                                )
                            )
                        }
                    }else{
                        showReport(EnumType.上报埋点.雨雪天气提醒上报,subactid = "RainSnow_request_fail")
                        listener?.onViewLoadedFailed("RainSnow_request_fail")
                    }
                } catch (e: Exception) {
                    showReport(EnumType.上报埋点.雨雪天气提醒上报,subactid = "RainSnow_Exception")
                    listener?.onViewLoadedFailed("RainSnow_Exception")
                    e.printStackTrace()
                }
            }
        }else{
            showReport(EnumType.上报埋点.雨雪天气提醒上报,subactid = "has_no_location")
            listener?.onViewLoadedFailed("has_no_location")
        }
    }


}