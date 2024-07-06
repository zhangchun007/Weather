package com.zhangsheng.shunxin.weather.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.maiya.thirdlibrary.ext.*
import com.zhangsheng.shunxin.ad.AdUtils
import com.zhangsheng.shunxin.databinding.LayoutWeatherTopBinding
import com.zhangsheng.shunxin.weather.activity.AirActivity
import com.zhangsheng.shunxin.weather.activity.HighAlertActivity
import com.zhangsheng.shunxin.weather.activity.TyphoonActivity
import com.zhangsheng.shunxin.weather.activity.WeatherActivity
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.*
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.utils.WeatherUtils

class WeatherTopLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    val binding=LayoutWeatherTopBinding.inflate(LayoutInflater.from(context),this,true)


    init {

        initListener()
    }


    fun loadBannerAdv(){
        if (!AdUtils.checkLoadTime()) return
        binding.advBanner.loadAd()
    }

    private fun initListener() {
        binding.warns.ClickReport(EnumType.上报埋点.天气预警点击) {
            skipActivity(HighAlertActivity::class.java) {
                this.putExtra("index", binding.warns.getCurrentIndex())
            }
        }

        binding.tvAir.ClickReport(EnumType.上报埋点.空气质量模块点击) {
            skipActivity(AirActivity::class.java) {
                putExtra("code", getAppModel().currentWeather.value.nN().weather?.regioncode)
                putExtra(
                    "name", LocationEllipsis(
                        getAppModel().currentWeather.value.nN().weather.nN().regionname,
                        getAppModel().currentWeather.value.nN().weather.nN().isLocation
                    )
                )
                putExtra("location", getAppModel().currentWeather.value.nN().weather?.isLocation)
            }
        }

        binding.rlTemp.ClickReport(EnumType.上报埋点.首页实况天气) {
            skipActivity(WeatherActivity::class.java)
        }

        binding.llCloud.ClickReport(EnumType.上报埋点.首页实况天气) {
            skipActivity(WeatherActivity::class.java)
        }

        binding.typhoon.ClickReport(EnumType.上报埋点.首页台风点击){
            skipActivity(TyphoonActivity::class.java)
        }

    }

    fun setWeatherDate(weatherBean: WeatherBean){

        setWarnsAlert(weatherBean)

        if (weatherBean.aqi.isNotEmpty() && weatherBean.aqi != "0") {
            showReport(EnumType.上报埋点.空气质量模块展示)
        }

        if (weatherBean.typhoon=="1"){
            showReport(EnumType.上报埋点.首页台风展示)
            binding.typhoon.isVisible(true)
        }else{
            binding.typhoon.isVisible(false)
        }

        binding.tvAir.setImageBackGround(WeatherUtils.leafIcon(weatherBean.nN().aqiLevel))
        binding.tvAir.text = "  ${weatherBean.nN().aqi}  ${weatherBean.nN().aqiLevel}"
        binding.tvAir.isVisible(weatherBean.nN().aqi.isNotEmpty() && weatherBean.nN().aqi != "0")

        binding.temp.text = weatherBean.nN().tc


        binding.temp.text = weatherBean.nN().tc
        binding.weather.text = weatherBean.nN().wt
        binding.cloud.text = weatherBean.nN().wdir
        binding.sun.text = weatherBean.nN().uvlv
        binding.tvWeight.text = "湿度"
        binding.tvSun.text = "紫外线"
        binding.cloudLevel.text = weatherBean.nN().ws
        binding.weight.text = "${weatherBean.nN().rh}%"
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)

        if (visibility== View.VISIBLE){
            binding.warns.start()
        }else{
            binding.warns.stop()
        }
    }

    private fun setWarnsAlert(data: WeatherBean) {
        Try {
            binding.warns.let {
                var warnsData = data.nN().warns.nN()
                if (warnsData.size > 4) {
                    warnsData = warnsData.subList(0, 4)
                }
                binding.warns.isVisible(warnsData.isNotEmpty())

                if (warnsData.isNotEmpty()){
                    showReport(EnumType.上报埋点.天气预警展示)
                }
                if (warnsData.isNotEmpty()) {
                    binding.warns.initData(warnsData)
                }
            }
        }
    }
}