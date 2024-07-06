package com.zhangsheng.shunxin.weather.model

import android.app.Activity
import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
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
import kotlinx.coroutines.*

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2019/12/6 21:01
 */
class AirMapViewModel : BaseViewModel() {
    var weather: SafeMutableLiveData<MapCityWeatherBean> =
        SafeMutableLiveData()
    var airBean: SafeMutableLiveData<JsonObject> =
        SafeMutableLiveData()


    fun requestAirMapDate(lat: String, lng: String) {
        callNativeApi({ net().空气质量雾霾实况地图(lng, lat) },
            object : CallResult<JsonObject>() {
                override fun ok(result: JsonObject?) {
                    super.ok(result)
                    airBean.value = result
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
                    if (code == NetStatus.netError) {
                        xToast("网络断开，请检查网络状态")
                    }
                }

            }
        )
    }

    fun location(context: Activity) {
        getAppModel().location(context.localClassName)
    }

    var bitmaps: HashMap<Int, Bitmap> = HashMap()
    private var job: Deferred<Unit>?=null
    fun downLoadPic(context: Activity, play: (index: Int, isLoaded: Boolean) -> Unit) {
        Try {
            job = viewModelScope.async(Dispatchers.IO) {
                var context = context

                repeat(airBean.value.nN().getAsJsonArray("images").size()) { index ->
                    if (isActive) {
                        try {
                            loadBitMap(context, index).let {
                                withContext(Dispatchers.Main) {
                                    if (it != null) {
                                        bitmaps[index] = it
                                        play(index, false)
                                    }

                                }
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            job?.invokeOnCompletion {
                play(airBean.value.nN().getAsJsonArray("images").size() - 1, true)
            }
        }
    }

    private suspend fun loadBitMap(context: Activity, index: Int): Bitmap? {
        return Glide
            .with(context)
            .asBitmap()
            .load(
                airBean.value.nN().getAsJsonArray("images")
                    .get(index).asJsonArray.get(0).asString
            )
            .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()
    }


    fun onDestroy(){
        bitmaps.clear()
        job?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        bitmaps.clear()
        job?.cancel()
    }




}