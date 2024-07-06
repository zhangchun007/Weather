package com.zhangsheng.shunxin.weather.model

import android.app.Activity
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.listIndex
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.net.callback.CallResult
import com.maiya.thirdlibrary.utils.CacheUtil
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.ext.net
import com.zhangsheng.shunxin.weather.net.bean.CityWeatherSearchBean
import com.zhangsheng.shunxin.weather.net.bean.MapCityWeatherBean
import com.zhangsheng.shunxin.weather.net.bean.PushCityBean
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import com.my.sdk.stpush.STPushManager
import com.my.sdk.stpush.open.Tag
import com.xm.xmcommon.XMParam

class CityManageViewModel : BaseViewModel() {

    fun requestCityWeather(
        cityCode: String,
        func: (list: List<CityWeatherSearchBean>) -> Unit
    ) {
        callNativeApi({ net().城市天气(cityCode) }, object : CallResult<List<CityWeatherSearchBean>>() {
            override fun ok(result: List<CityWeatherSearchBean>?) {
                super.ok(result)
                func(result.nN())
            }

            override fun failed(code: Int, msg: String) {
                super.failed(code, msg)
            }
        })
    }


    fun requestCityWeather(lat: String, lng: String, func: () -> Unit) {
        callNativeApi(
            { net().按地理位置获取城市天气数据(lng, lat) },
            object : CallResult<MapCityWeatherBean>() {
                override fun ok(result: MapCityWeatherBean?) {
                    super.ok(result)
                    WeatherUtils.getDatas().listIndex(0).apply {
                        this.tcmax = result.nN().tcmax
                        this.tcmin = result.nN().tcmin
                    }
                    func()
                }

            }
        )
    }


    fun checkPush(context: Activity, weather: WeatherBean? = null) {

        var pushWeather = weather ?: if (weather == null && WeatherUtils.getDatas().isNotEmpty()) {
            WeatherUtils.getDatas().listIndex(0)
        } else null
        var push = CacheUtil.getObj(Constant.SP_PUSH_CODE, PushCityBean::class.java)
        if (push.nN().code.isEmpty() && pushWeather != null) {
            getAppModel().pushCity.apply {
                this.code = "${pushWeather.regioncode}"
                this.city = pushWeather.nN().regionname
                this.isLocation = pushWeather.isLocation
                this.dayTime = "7:00"
                this.nightTime = "18:30"

            }
            STPushManager.getInstance().setTag(
                context,
                Tag("7:00"),
                Tag("18:30"),
                Tag(XMParam.getAppVer()),
                Tag("${pushWeather.regioncode}")
            )
        } else {
            if (push.nN().code.isNotEmpty() && pushWeather != null) {
                if (WeatherUtils.getDatas().none { it.regioncode == push.nN().code }) {
                    getAppModel().pushCity.apply {
                        this.code = "${pushWeather.regioncode}"
                        this.isLocation = pushWeather.isLocation
                        this.city = pushWeather.nN().regionname
                        this.dayTime = push.nN().dayTime
                        this.nightTime = push.nN().nightTime

                    }
                    STPushManager.getInstance().setTag(
                        context,
                        Tag(push.nN().dayTime),
                        Tag(push.nN().nightTime),
                        Tag(XMParam.getAppVer()),
                        Tag("${WeatherUtils.getDatas().listIndex(0).regioncode}")
                    )
                }
            }
        }
    }
}