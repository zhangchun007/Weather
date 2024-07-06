package com.zhangsheng.shunxin.weather.utils

import com.amap.api.maps.model.LatLng
import com.maiya.thirdlibrary.ext.listIndex
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.ext.parseDouble
import com.maiya.thirdlibrary.utils.CacheUtil
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.LocationEllipsis
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.net.bean.cityBean

abstract class BaseWeatherUtils {
    var isNight = false
    private var weathers = ArrayList<WeatherBean>()

    open fun getWeatherBean(position: Int): WeatherBean {
        return if (weathers.size - 1 >= position) {
            weathers.listIndex(position)
        } else {
            WeatherBean()
        }
    }

    open fun delete(position: Int) {
        weathers.removeAt(position)
        saveData()

    }

    open fun initData(): ArrayList<WeatherBean> {
        if (weathers.isNotEmpty()) return weathers

        var list = CacheUtil.getList(Constant.SP_WEATHER_DATA, WeatherBean::class.java)
        weathers.clear()
        if (list.isNotEmpty()) {
            weathers.addAll(list)
        }
        return weathers
    }

    open fun addWeather(data: WeatherBean, position: Int, isLocation: Boolean = false) {
        if (isLocation) {
            addLocationWeather(data)
        } else {
            if (position >= weathers.size) {
                weathers.add(data)
            } else {
                weathers[position] = data
            }
        }
        saveData()
    }

    open fun isWeatherDateEmpty(): Boolean {
        return initData().isEmpty() || initData().listIndex(0).tc.isEmpty()
    }

    open fun upDateWeather(date: WeatherBean, position: Int) {
        if (position < weathers.size) {
            weathers[position] = date
            saveData()
        }
    }

    open fun addLocationWeather(data: WeatherBean) {
        data.isLocation = true
        if (weathers.size == 0) {
            weathers.add(data)
        } else {
            when {
                weathers.listIndex(0).isLocation -> weathers[0] = data
                weathers.size >= 9 -> weathers[0] = data
                weathers.size >= 1 && weathers.listIndex(0).tc.isEmpty() && weathers.listIndex(0).regioncode.isEmpty() -> weathers[0] =
                    data
                else -> weathers.add(0, data)
            }
        }
    }

    open fun addWeather(data: WeatherBean) {
        if (weathers.size > 0 && weathers.listIndex(0).tc.isEmpty() && weathers.listIndex(0).regioncode.isEmpty() && !weathers.listIndex(
                0
            ).isLocation
        ) {
            weathers[0] = data
        } else {
            weathers.add(data)
        }
    }


    open fun getDatas(): ArrayList<WeatherBean> {
        return weathers
    }

    open fun saveData() {
        CacheUtil.put(Constant.SP_WEATHER_DATA, weathers)
    }


    protected open fun iconIndex(code: String): Int {
        var index = when (code) {
            EnumType.天气气象.晴天 -> 0
            EnumType.天气气象.多云 -> 1
            EnumType.天气气象.阴天 -> 2
            EnumType.天气气象.雾霾 -> 3
            EnumType.天气气象.中度雾霾 -> 4
            EnumType.天气气象.重度雾霾 -> 5
            EnumType.天气气象.小雨 -> 6
            EnumType.天气气象.中雨 -> 7
            EnumType.天气气象.大雨 -> 8
            EnumType.天气气象.暴雨 -> 9
            EnumType.天气气象.雾 -> 10
            EnumType.天气气象.小雪 -> 11
            EnumType.天气气象.中雪 -> 12
            EnumType.天气气象.大雪 -> 13
            EnumType.天气气象.暴雪 -> 14
            EnumType.天气气象.浮尘 -> 15
            EnumType.天气气象.沙尘暴 -> 16
            EnumType.天气气象.大风 -> 17
            EnumType.天气气象.雷阵雨 -> 18
            EnumType.天气气象.冰雹 -> 19
            EnumType.天气气象.雨夹雪 -> 20
            else -> 0
        }
        return index
    }

    fun getWeatherForecastBg(code: String,isNight:Boolean): Int {
        var res = R.mipmap.bg_weather_forecast_qing_night
        when (code) {
            EnumType.天气气象.晴天 -> res =
                if (isNight) R.mipmap.bg_weather_forecast_qing_night else R.mipmap.bg_weather_forecast_qing_day
            EnumType.天气气象.阴天 -> res = R.mipmap.bg_weather_forecast_yin
            EnumType.天气气象.小雨,
            EnumType.天气气象.中雨,
            EnumType.天气气象.大雨,
            EnumType.天气气象.暴雨,
            EnumType.天气气象.雷阵雨,
            EnumType.天气气象.冰雹,
            EnumType.天气气象.雨夹雪 -> res = R.mipmap.bg_weather_forecast_yu
            EnumType.天气气象.多云 -> res =
                if (isNight) R.mipmap.bg_weather_forecast_duoyun_night else R.mipmap.bg_weather_forecast_duoyun_day
            EnumType.天气气象.沙尘暴,
            EnumType.天气气象.浮尘->res= R.mipmap.bg_weather_forecast_mai
            EnumType.天气气象.雾,
            EnumType.天气气象.雾霾,
            EnumType.天气气象.中度雾霾,
            EnumType.天气气象.重度雾霾 -> res = R.mipmap.bg_weather_forecast_wu
            EnumType.天气气象.小雪,
            EnumType.天气气象.中雪,
            EnumType.天气气象.大雪,
            EnumType.天气气象.暴雪 -> res = R.mipmap.bg_weather_forecast_xue
            EnumType.天气气象.大风 -> res = R.mipmap.bg_weather_forecast_feng
        }
        return res
    }

    fun getWeatherForecastIcon(code: String,isNight: Boolean): Int {
        var res = when (code) {
            EnumType.天气气象.晴天 -> if (!isNight) R.mipmap.icon_weather_forecast_qing_day else R.mipmap.icon_weather_forecast_qing_night
            EnumType.天气气象.阴天 ->  R.mipmap.icon_weather_forecast_yin
            EnumType.天气气象.小雨 -> R.mipmap.icon_weather_forecast_xiaoyu
            EnumType.天气气象.中雨 -> R.mipmap.icon_weather_forecast_zhongyu
            EnumType.天气气象.大雨 -> R.mipmap.icon_weather_forecast_dayu
            EnumType.天气气象.暴雨 -> R.mipmap.icon_weather_forecast_baoyu
            EnumType.天气气象.雷阵雨-> R.mipmap.icon_weather_forecast_leizhenyu
            EnumType.天气气象.冰雹 -> R.mipmap.icon_weather_forecast_bingbao
            EnumType.天气气象.雨夹雪 -> R.mipmap.icon_weather_forecast_yujiaxue
            EnumType.天气气象.多云 -> if (!isNight) R.mipmap.icon_weather_forecast_duoyun_day else R.mipmap.icon_weather_forecast_duoyun_night
            EnumType.天气气象.雾  ->R.mipmap.icon_weather_forecast_wu
            EnumType.天气气象.雾霾,
            EnumType.天气气象.中度雾霾,
            EnumType.天气气象.重度雾霾 -> R.mipmap.icon_weather_forecast_wumai
            EnumType.天气气象.小雪-> R.mipmap.icon_weather_forecast_xiaoxue
            EnumType.天气气象.中雪-> R.mipmap.icon_weather_forecast_zhongxue
            EnumType.天气气象.大雪-> R.mipmap.icon_weather_forecast_daxue
            EnumType.天气气象.暴雪 -> R.mipmap.icon_weather_forecast_baoxue
            EnumType.天气气象.大风-> R.mipmap.icon_weather_forecast_dafeng
            EnumType.天气气象.沙尘暴-> R.mipmap.icon_weather_forecast_shachen
            EnumType.天气气象.浮尘 -> R.mipmap.icon_weather_forecast_fuchen
            else -> R.mipmap.icon_weather_forecast_qing_day
        }

        return res
    }


    open fun setSunTime(sunrise: String, sunset: String) {

        if (sunrise.isNotEmpty() && sunset.isNotEmpty()) {
            isNight = !DataUtil.judgeIsSetBiddingTime(sunrise, sunset)
        }
    }

    open fun isNight(sunrise: String, sunset: String): Boolean {

        return if (sunrise.isNotEmpty() && sunset.isNotEmpty()) {
            var data = !DataUtil.judgeIsSetBiddingTime(sunrise, sunset)
            !DataUtil.judgeIsSetBiddingTime(sunrise, sunset)
        } else {
            false
        }
    }

    open fun timeIsNight(now: String): Boolean {
        return try {
            if (getAppModel().currentWeather.value.nN().weather.nN().ybhs.nN()
                    .isNotEmpty() && now.isNotEmpty()
            ) {
                !DataUtil.judgeIsSetBiddingTime(
                    now,
                    getAppModel().currentWeather.value.nN().weather.nN().ybhs.listIndex(0).sunrise,
                    getAppModel().currentWeather.value.nN().weather.nN().ybhs.listIndex(0).sunset
                )
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    open fun iconSmall(code: String, night: Boolean): Int {
        return when (code) {
            "0" -> if (night) R.drawable.icon_weather_qing_night_small else R.drawable.icon_weather_qing_day_small
            "1" -> if (night) R.drawable.icon_weather_duoyun_night_small else R.drawable.icon_weather_duoyun_day_small
            "2" -> R.drawable.icon_weather_yin_small
            "53", "54", "55" -> R.drawable.icon_weather_mai_small
            "7" -> R.drawable.icon_weather_xiaoyu_small
            "8" -> R.drawable.icon_weather_zhongyu_small
            "9" -> R.drawable.icon_weather_dayu_small
            "10" -> R.drawable.icon_weather_baoyu_small
            "18" -> R.drawable.icon_weather_wu_small
            "14" -> R.drawable.icon_weather_xiaoxue_small
            "15" -> R.drawable.icon_weather_zhongxue_small
            "16" -> R.drawable.icon_weather_daxue_small
            "17" -> R.drawable.icon_weather_baoxue_small
            "29", "30" -> R.drawable.icon_weather_fuchen_small
            "100" -> R.drawable.icon_weather_dafeng_small
            "4" -> R.drawable.icon_weather_leizhenyu_small
            "5" -> R.drawable.icon_weather_bingbao_small
            "6" -> R.drawable.icon_weather_yujiaxue_small
            else -> R.drawable.icon_weather_qing_day_small
        }
    }


    open fun getCity(): cityBean {
        return when {
            getDatas().listIndex(0).isLocation -> {
                cityBean().apply {
                    this.latlng = LatLng(
                        getDatas().listIndex(0).latitude.parseDouble(),
                        getDatas().listIndex(0).longitude.parseDouble()
                    )
                    this.isLocation = true
                    this.regionCode = getDatas().listIndex(0).regioncode
                    this.regionName = LocationEllipsis(
                        getDatas().listIndex(0).regionname,
                        getDatas().listIndex(0).isLocation
                    )
                }
            }
            getAppModel().pushCity.code.isNotBlank() -> {
                var data = getDatas().filter {
                    it.regioncode == getAppModel().pushCity.code
                }
                if (data.isNotEmpty()) {
                    cityBean().apply {
                        this.latlng = LatLng(
                            data.listIndex(0).latitude.parseDouble(),
                            data.listIndex(0).longitude.parseDouble()
                        )
                        this.isLocation = data.listIndex(0).isLocation
                        this.regionCode = data.listIndex(0).regioncode
                        this.regionName = LocationEllipsis(
                            data.listIndex(0).regionname,
                            data.listIndex(0).isLocation
                        )
                    }
                } else {
                    cityBean().apply {
                        this.latlng = LatLng(
                            getDatas().listIndex(0).latitude.parseDouble(),
                            getDatas().listIndex(0).longitude.parseDouble()
                        )
                        this.regionCode = getDatas().listIndex(0).regioncode
                        this.isLocation = getDatas().listIndex(0).isLocation
                        this.regionName = LocationEllipsis(
                            getDatas().listIndex(0).regionname,
                            getDatas().listIndex(0).isLocation
                        )
                    }
                }
            }
            else -> {
                cityBean().apply {
                    this.latlng = LatLng(
                        getDatas().listIndex(0).latitude.parseDouble(),
                        getDatas().listIndex(0).longitude.parseDouble()
                    )
                    this.regionCode = getDatas().listIndex(0).regioncode
                    this.regionCode = getDatas().listIndex(0).regioncode
                    this.regionName = LocationEllipsis(
                        getDatas().listIndex(0).regionname,
                        getDatas().listIndex(0).isLocation
                    )
                }
            }
        }
    }


}