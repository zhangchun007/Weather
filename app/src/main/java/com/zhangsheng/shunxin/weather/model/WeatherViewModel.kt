package com.zhangsheng.shunxin.weather.model

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Looper
import android.util.Log
import android.view.View
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.listIndex
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.ext.parseLong
import com.maiya.thirdlibrary.net.callback.CallResult
import com.maiya.thirdlibrary.utils.AppContext
import com.maiya.thirdlibrary.utils.CacheUtil
import com.xm.xmcommon.base.XMGlobal.getApplication
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.utils.PermissionsUtils
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.ext.net
import com.zhangsheng.shunxin.weather.net.bean.TtsTokenBean
import com.zhangsheng.shunxin.weather.notifycation.WidgetBroadcast
import com.zhangsheng.shunxin.weather.utils.LocationUtil
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import kotlin.math.abs

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/7/15 15:07
 */
class WeatherViewModel : BaseViewModel() {
    fun requestTtsToken(func: (token: TtsTokenBean?) -> Unit) {
        var data = CacheUtil.getObj(Constant.SP_STT_REFRESH_TOKEN, TtsTokenBean::class.java)
        if (data != null && data.expire > System.currentTimeMillis()) {
            func(data)
        } else {
            callNativeApi({ net().语音合成Token() }, object : CallResult<TtsTokenBean>() {
                override fun ok(result: TtsTokenBean?) {
                    super.ok(result)

                    Try {
                        var data = result.nN().apply {
                            this.expire =
                                System.currentTimeMillis() + result.nN().vaildtime.parseLong(0) * 1000
                        }
                        CacheUtil.putObj(Constant.SP_STT_REFRESH_TOKEN, data)
                        func(data)
                    }
                }

                override fun failed(code: Int, msg: String) {
                    super.failed(code, msg)
                    func(null)
                }
            })
        }
    }

    fun checkStatus(key: String = LocationUtil.KEY_MAIN_LOCATION) {
        when (getAppModel().refreshAction.value) {
            EnumType.刷新类型.定位权限, EnumType.刷新类型.定位失败 -> {
                PermissionsUtils.onlycheck(
                    AppContext.getContext(),
                    Constant.CHECK_LOCATION_PERMISSIONS
                ) {
                    if (it && PermissionsUtils.checkGpsEnabled(AppContext.getContext())) {
                        LocationUtil.startLocation(key)
                    }
                }
            }
            EnumType.刷新类型.系统时间 -> {
                var bean = getAppModel().currentWeather.value.nN().weather.nN()
                if (abs((System.currentTimeMillis() - bean.refreshTime + bean.time.parseLong(0) * 1000) - System.currentTimeMillis()) <= 60 * 60 * 1000) {
                    getAppModel().refreshAction.value = EnumType.刷新类型.初始状态
                }
            }
        }
    }

    fun speakText(): String {

        return if (WeatherUtils.isNight) {
            """
                <speak>
                <break time="500ms"/>
                ${AppContext.getContext().getString(R.string.speak_app_title)},
                ${getAppModel().currentWeather.value.nN().weather.nN().regionname},
                今天夜间,${getAppModel().currentWeather.value.nN().weather.nN().ybds.listIndex(1).wtn},
                <break time="300"/>
                最低温度 ${getAppModel().currentWeather.value.nN().weather.nN().ybds.listIndex(1).tcn}摄氏度, "
                ${getAppModel().currentWeather.value.nN().weather.nN().ybds.listIndex(1).wdir}  ${
                getAppModel().currentWeather.value.nN().weather.nN().ybds.listIndex(
                    1
                ).ws
            },
                空气质量  ${getAppModel().currentWeather.value.nN().weather.nN().ybds.listIndex(1).aqiLevel} 。
                </speak>
                 """
        } else {
            """
                <speak><break time="500ms"/>
                ${AppContext.getContext().getString(R.string.speak_app_title)},
                ${getAppModel().currentWeather.value.nN().weather.nN().regionname},
                ,今天白天,
                ${getAppModel().currentWeather.value.nN().weather.nN().ybds.listIndex(1).wtd},
                <break time="300"/>
                最高温度${getAppModel().currentWeather.value.nN().weather.nN().ybds.listIndex(1).tcd}摄氏度,
                ,夜间,
                ${getAppModel().currentWeather.value.nN().weather.nN().ybds.listIndex(1).wtn},
                最低温度${getAppModel().currentWeather.value.nN().weather.nN().ybds.listIndex(1).tcn}摄氏度,
                ${getAppModel().currentWeather.value.nN().weather.nN().ybds.listIndex(1).wdir}${
                getAppModel().currentWeather.value.nN().weather.nN().ybds.listIndex(
                    1
                ).ws
            },
                空气质量${getAppModel().currentWeather.value.nN().weather.nN().ybds.listIndex(1).aqiLevel} 。
                </speak>
                 """
        }

    }

    fun startWidgetService(context: Context) {
        Try {
            Looper.myQueue().addIdleHandler {
                if (WeatherUtils.getDatas().isNotEmpty()) {
                    val intent = Intent(getApplication(), WidgetBroadcast::class.java)
                    intent.action = "refresh"
                    context.sendBroadcast(intent)
                }
                false
            }
        }
    }

    fun createViewBitmap(v: View): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            v.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            bitmap = Bitmap.createBitmap(
                v?.getWidth(), v?.getHeight(),
                Bitmap.Config.ARGB_4444
            )
            val canvas = Canvas(bitmap)
            v?.draw(canvas)
        } catch (e: Exception) {
            Log.w("lpb", "Exception:" + e)
        }
        return bitmap
    }

}