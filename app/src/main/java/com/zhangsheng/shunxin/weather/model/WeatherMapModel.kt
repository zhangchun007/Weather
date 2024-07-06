package com.zhangsheng.shunxin.weather.model

import android.app.Activity
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.FutureTarget
import com.bumptech.glide.request.target.Target
import com.google.gson.JsonObject
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.ext.xToast
import com.maiya.thirdlibrary.net.bean.NetStatus
import com.maiya.thirdlibrary.net.callback.CallResult
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.ext.net
import com.zhangsheng.shunxin.weather.livedata.SafeMutableLiveData
import com.zhangsheng.shunxin.weather.net.bean.MapCityWeatherBean
import com.zhangsheng.shunxin.weather.net.bean.WeatherRainBean
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2019/12/4 15:05
 */
class WeatherMapModel() : BaseViewModel() {

    var fall: SafeMutableLiveData<JsonObject> =
        SafeMutableLiveData()
    var rain: SafeMutableLiveData<WeatherRainBean> =
        SafeMutableLiveData()
    var weather: SafeMutableLiveData<MapCityWeatherBean> =
        SafeMutableLiveData()

    fun requestWeatherCountryRain(lat: String, lng: String, level: String) {
        callNativeApi(
            { net().雷达图数据(lng, lat, level) },
            object : CallResult<JsonObject>() {
                override fun ok(result: JsonObject?) {
                    super.ok(result)
                    fall.value = result
                }

            }
        )
    }
    fun requestCityWeather(lat: String, lng: String) {
        callNativeApi(
            { net().按地理位置获取城市天气数据(lng, lat) },
            object : CallResult<MapCityWeatherBean>() {
                override fun ok(result: MapCityWeatherBean?) {
                    super.ok(result)
                    weather.value = result
                }

                override fun failed(code: Int, msg: String) {
                    super.failed(code, msg)
                    if (code== NetStatus.netError){
                        xToast("网络断开，请检查网络状态")
                    }
                }

            }
        )
    }

    fun requestWeatherRain(lat: String, lng: String, regionname: String) {
        callNativeApi(
            { net().按地理位置获取降雨(lng, lat, regionname) },
            object : CallResult<WeatherRainBean>() {
                override fun ok(result: WeatherRainBean?) {
                    super.ok(result)
                    rain.value = result.nN()
                }

                override fun failed(code: Int, msg: String) {
                    super.failed(code, msg)
                }
            }
        )
    }


    fun location(context: Activity) {
        getAppModel().location(context.localClassName)
    }

    var bitmaps: HashMap<Int, Bitmap> = HashMap()
    private var futureTarget: FutureTarget<Bitmap>? = null
    fun downLoadPic(context: Activity, index: Int) {
        Try {
            if (index < fall.value.nN().getAsJsonArray("images").size()) {
                GlobalScope.async {
                    try {

                     var bitmap=Glide
                         .with(context)
                         .asBitmap()
                         .skipMemoryCache(true)
                         .diskCacheStrategy(DiskCacheStrategy.NONE)
                         .load(fall.value.nN().getAsJsonArray("images")
                             .get(index).asJsonArray.get(0).asString)
                         .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()

                        bitmap.apply {
                            if (this!=null){
                                bitmaps[index] = bitmap!!
                            }
                            downLoadPic(context, index + 1)
                        }
                    } catch (e: Exception) {
                        downLoadPic(context, index + 1)
                    }
                    Glide.with(context).clear(futureTarget)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        bitmaps.clear()
    }
}