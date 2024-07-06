package com.zhangsheng.shunxin.weather.model

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.maiya.baselibrary.net.NetException
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.net.bean.BaseResponse
import com.maiya.thirdlibrary.net.bean.NetStatus
import com.maiya.thirdlibrary.net.callback.CallResult
import com.maiya.thirdlibrary.utils.ActivityManageTools
import com.maiya.thirdlibrary.utils.AppContext
import com.maiya.thirdlibrary.utils.CacheUtil
import com.maiya.thirdlibrary.utils.CacheUtil.putObj
import com.maiya.thirdlibrary.utils.OSUtils
import com.maiya.wallpaper.IWallpaperCallBack
import com.maiya.wallpaper.WallPaperSDK
import com.maiya.weather.util.java_bridge.JNetUtils
import com.my.sdk.stpush.STPushManager
import com.my.sdk.stpush.open.Tag
import com.necer.entity.CalendarDate2
import com.xinmeng.xm.XMMarker
import com.xm.xmcommon.XMParam
import com.zhangsheng.shunxin.calendar.db.CalendarYJData
import com.zhangsheng.shunxin.information.bean.AllChannelBean
import com.zhangsheng.shunxin.information.bean.LocationBean
import com.zhangsheng.shunxin.information.bean.TabBean
import com.zhangsheng.shunxin.information.constant.Constants
import com.zhangsheng.shunxin.weather.activity.SplashActivity
import com.zhangsheng.shunxin.weather.bottom.BottomBarItem
import com.zhangsheng.shunxin.weather.common.Configure
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.common.UrlConstant
import com.zhangsheng.shunxin.weather.db.city_db.InnerJoinResult
import com.zhangsheng.shunxin.weather.dialog.LocationPermissionDialog
import com.zhangsheng.shunxin.weather.ext.*
import com.zhangsheng.shunxin.weather.livedata.LiveDataBus
import com.zhangsheng.shunxin.weather.livedata.SafeMutableLiveData
import com.zhangsheng.shunxin.weather.net.ReCodeUtils
import com.zhangsheng.shunxin.weather.net.bean.*
import com.zhangsheng.shunxin.weather.notifycation.WidgetBroadcast
import com.zhangsheng.shunxin.weather.utils.DataUtil
import com.zhangsheng.shunxin.weather.utils.LocationUtil
import com.zhangsheng.shunxin.weather.utils.PermissionsUtils
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import com.zhangsheng.shunxin.weather.utils.alispeak.SpeakerUtils
import com.zhangsheng.shunxin.weather.wallpaper.WallPaperBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.joda.time.LocalDate
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.math.abs

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/6/18 19:25
 */

open class AppViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        val KEY_AD_POP = "key_ad_pop"
        val KEY_LOCATION_POP = "key_location_pop"
    }

    private var infoInit = false


    var lastHotSplashTime = 0L
    var mDomainNameHub: HashMap<String, String> = HashMap()
    private var pollingTime: Long = 0
    private var pollingRefreshTime: Long = 1800 * 1000
    private var systemTimeCute: Long = 0
    var LocationAuthPopup: LocationPermissionDialog? = null
    var popContrl = HashMap<String, Function?>()

    var appPageIndex = BottomBarItem.CMD_WEATHER

    /**
     * 这个变量是用来纪录日历选择年份的。当选择年份改变的时候需要请求班休接口
     */
    var selectYear = ""


    //首页天气日历数据
    var calendarDate: CalendarDate2? = null
    var calendarYJdata: CalendarYJData? = null

    //空气排行榜
    var airRank: SafeMutableLiveData<AirRankBean> =
        SafeMutableLiveData()

    //推送设置
    var pushClientId: SafeMutableLiveData<String> =
        SafeMutableLiveData()
    val pushCity by lazy { CacheUtil.getObj(Constant.SP_PUSH_CODE, PushCityBean::class.java).nN() }

    var queryTags: SafeMutableLiveData<PushBean> =
        SafeMutableLiveData()
    var setTags: SafeMutableLiveData<String> =
        SafeMutableLiveData()

    //天气全局当前数据
    var currentWeather: SafeMutableLiveData<CurrentWeatherBean> = SafeMutableLiveData()

    //全局云控数据
    var control: SafeMutableLiveData<ControlBean> = SafeMutableLiveData()

    //天气刷新状态
    var refreshAction: SafeMutableLiveData<Int> = SafeMutableLiveData()

    //40日天气
    var fortyWeatherBean: SafeMutableLiveData<FortyWeatherBean> = SafeMutableLiveData()

    //搜索城市
    var city2ndList: List<InnerJoinResult>? = null

    //日历
    var localDate: SafeMutableLiveData<LocalDate> =
        SafeMutableLiveData()

    //当前壁纸请求code
    var wallpaperCode = "wallpaper_background"

    //当isRefreshBlackModel = false时 ，心晴刷新文字 默认是黑色，公共代码默认是白色，
    //当isRefreshBlackModel = true时，心晴刷新文字 是暗色背景，此时默认是白色， 公共代码默认还是白色，暂时只有心晴UI会设置为true
    var isRefreshBlackModel = false

    private var lastSetWallpaperTime: Long = 0L; // 记录最后一次调用设置壁纸的时间

    fun report(type: String, log: String) {
        viewModelScope.async {
            net().日志上报接口(type, log)
        }
    }

    fun requestWeather(
        weatherData: WeatherBean,
        position: Int,
        mustRefresh: Boolean = false,
        func: (isOk: Boolean) -> Unit = {}
    ) {
        var isLocationRefresh = refreshAction.value == EnumType.刷新类型.重新定位
        refreshAction.value = EnumType.刷新类型.正在刷新
        var data = WeatherUtils.getWeatherBean(position)
        if (data.tc.isEmpty() || mustRefresh || isLocationRefresh || (abs(System.currentTimeMillis() - data.refreshTime) > 5 * 60 * 1000 && data.lastRequestStatus)) {
            if (weatherData.isLocation) {
                if (weatherData.regioncode.isStr("").isNotEmpty()) {
                    checkPush(weatherData)
                }
                requestWeatherByLocal(weatherData, position, func)
            } else {
                requestWeatherByCode(weatherData, position, func)
            }
            if (weatherData.isLocation || weatherData.regioncode.isNotEmpty()) {
                initScreenLock(Constant.WALLPAPER_START)
            }
        } else {
            currentWeather.value = CurrentWeatherBean().apply {
                this.position = position
                this.weather = data
            }
            refreshAction.value = EnumType.刷新类型.刷新成功
            func(true)
        }
    }


    private fun requestWeatherByCode(
        weatherData: WeatherBean,
        position: Int,
        func: (isOk: Boolean) -> Unit = {}
    ) {
        if (weatherData.regioncode.isNotEmpty()) {
            callNativeApi({
                net().天气首页数据(
                    weatherData.regioncode,
                    weatherData.regionname
                )
            }, object :
                CallResult<WeatherBean>() {
                override fun ok(result: WeatherBean?) {
                    super.ok(result)
                    if (WeatherUtils.getDatas()
                            .listIndex(position).regioncode.isNotEmpty() && weatherData.regioncode != WeatherUtils.getDatas()
                            .listIndex(position).regioncode
                    ) return

                    //首次请求天气数据成功
                    if (WeatherUtils.isWeatherDateEmpty()
                    ) {
                        showReport(
                            actentryid = EnumType.上报埋点.进入首页并成功获取到天气数据,
                            subactid = EnumType.上报埋点.非定位城市天气数据获取成功子埋点
                        )
                    }


                    var current = System.currentTimeMillis()
                    if (result.nN().time.parseLong(0) != 0L) {
                        systemTimeCute = abs(result.nN().time.parseLong(0) * 1000 - current)
                    }
                    if (systemTimeCute > 60 * 60 * 1000) {
                        refreshAction.value = EnumType.刷新类型.系统时间
                    } else {
                        refreshAction.value = EnumType.刷新类型.刷新成功
                    }

                    SpeakerUtils.clearVoice(weatherData.regioncode)
                    var data = result.nN().apply {
                        this.isLocation = weatherData.isLocation
                        this.refreshTime = current
                        this.regioncode = weatherData.regioncode
                        this.regionname = weatherData.regionname
                        this.location = weatherData.location
                        this.lastRequestStatus = refreshAction.value.nN() == EnumType.刷新类型.刷新成功
                    }
                    WeatherUtils.addWeather(data, position, weatherData.isLocation)
                    currentWeather.value = CurrentWeatherBean().apply {
                        this.position = position
                        this.weather = data
                    }
                    func(true)
                    pushWidget(data)
                }

                override fun failed(code: Int, msg: String) {
                    super.failed(code, msg)
                    WeatherUtils.upDateWeather(weatherData, position)
                    currentWeather.value = CurrentWeatherBean().apply {
                        this.position = position
                        this.weather = weatherData
                    }
                    if (code == NetStatus.netError) {
                        if (refreshAction.value.nN() != EnumType.刷新类型.网络错误) {
                            refreshAction.value = EnumType.刷新类型.网络错误
                        }
                    } else {
                        refreshAction.value = EnumType.刷新类型.刷新失败
                    }
                    func(false)
                }
            })
        } else {
            refreshAction.value = EnumType.刷新类型.初始状态
        }

    }

    private fun requestWeatherByLocal(
        weatherData: WeatherBean,
        position: Int,
        func: (isOk: Boolean) -> Unit = {}
    ) {
        callNativeApi({
            net().按地理位置获取天气首页数据(
                weatherData.location.nN().lng,
                weatherData.location.nN().lat,
                currentWeather.value.nN().weather.nN().regionname,
                weatherData.location.nN().province,
                weatherData.location.nN().city,
                weatherData.location.nN().district,
                "${weatherData.location.nN().locationType}"
            )
        }, object : CallResult<WeatherBean>() {
            override fun ok(result: WeatherBean?) {
                super.ok(result)
                if (WeatherUtils.isWeatherDateEmpty()
                ) {//首次请求天气数据成功
                    showReport(
                        actentryid = EnumType.上报埋点.进入首页并成功获取到天气数据,
                        subactid = EnumType.上报埋点.定位城市天气数据获取成功子埋点
                    )
                }
                CacheUtil.put(Constant.SP_ISLOCATION_USER, true)
                SpeakerUtils.clearVoice(weatherData.regioncode)
                var current = System.currentTimeMillis()
                if (result.nN().time.parseLong(0) != 0L) {
                    systemTimeCute = abs(result.nN().time.parseLong(0) * 1000 - current)
                }
                if (systemTimeCute > 60 * 60 * 1000) {
                    refreshAction.value = EnumType.刷新类型.系统时间
                } else {
                    refreshAction.value = EnumType.刷新类型.刷新成功
                }
                var data = result.nN().apply {
                    this.isLocation = true
                    this.refreshTime = System.currentTimeMillis()
                    this.regioncode = weatherData.regioncode
                    this.regionname = weatherData.regionname.isStr(
                        weatherData.location.nN().district.isStr(weatherData.location.nN().city)
                    )
                    this.latitude = weatherData.location.nN().lat
                    this.longitude = weatherData.location.nN().lng
                    this.location = weatherData.location
                    this.lastRequestStatus = refreshAction.value.nN() == EnumType.刷新类型.刷新成功
                }

                WeatherUtils.addWeather(data, position, data.isLocation)
                currentWeather.value = CurrentWeatherBean().apply {
                    this.position = position
                    this.weather = data
                }
                func(true)
                pushWidget(data)
            }

            override fun failed(code: Int, msg: String) {
                super.failed(code, msg)
                requestWeatherByCode(weatherData, position, func)
            }
        })
    }


    fun location(
        key: String = LocationUtil.KEY_MAIN_LOCATION,
        block: ((Boolean, permission: Boolean, gps: Boolean) -> Unit)? = null
    ) {
        PermissionsUtils.onlycheck(AppContext.getContext(), Constant.CHECK_LOCATION_PERMISSIONS) {
            if (it) {
                CacheUtil.remove(Constant.SP_WINDOW_SHOW_TIME)
                if (key == LocationUtil.KEY_MAIN_LOCATION && !isLocationUser()) return@onlycheck
                LocationUtil.startLocation(key)
                block?.invoke(true, it, true)
            } else {
                runOnTime(1000) {
                    LiveDataBus.getChannel<Location>(key)
                        .postValue(Location().apply {
                            state = LocationUtil.定位权限
                            errorCode = "62"
                            errorMessage = "缺少定位权限"
                        })
                    LocationUtil.locationState = LocationUtil.定位权限
                    block?.invoke(
                        false,
                        it,
                        PermissionsUtils.checkGpsEnabled(AppContext.getContext())
                    )
                }
            }
        }
    }

    fun requestWeatherForecast(
        region: String,
        location: Location?,
        func: (data: WeatherForecastBean) -> Unit,
        code: String
    ) {
        callNativeApi({ net().获取城市天气概况(region, "", location.nN().lng, location.nN().lat, code) },
            object : CallResult<WeatherForecastBean>() {
                override fun ok(result: WeatherForecastBean?) {
                    super.ok(result)
                    if (result != null) {
                        func(result)
                    }
                }

            })
    }

    fun requestControl() {
        if (System.currentTimeMillis() - pollingTime < pollingRefreshTime) return
        callApi({ net().获取云控信息() }, object : CallResult<ControlBean>() {
            override fun ok(result: ControlBean?) {
                super.ok(result)
                pollingTime = System.currentTimeMillis()
                pollingRefreshTime = result.nN().polling_time.parseLong(1800) * 1000
                if (pollingRefreshTime == 0L) pollingRefreshTime = 1800000
                CacheUtil.putObj(Constant.SP_POLLING_CONTROL, result.nN())
                if ("${result.nN().app_audit.nN().onoff}" == "false") {
                    CacheUtil.put(Constant.SP_CONTROL_STATE, "false")
                }
                if ("${result.nN().keep_alive.nN().onoff}" == "false") {
                    CacheUtil.put(Constant.SP_CONTROL_STATE, "false")
                }
                control.value = result

                if (result == null) {
                    return
                }
                if (!infoInit) {
                    infoInit = true
                    var info_stream = result.nN().info_stream
                    if (info_stream != null && info_stream.isNotEmpty()) {
                        if (!TextUtils.isEmpty(info_stream.listIndex(0).appid)) {
                            Constants.partner = info_stream.listIndex(0).appid
                        }
                        if (!TextUtils.isEmpty(info_stream.listIndex(0).appkey)) {
                            Constants.secure_key = info_stream.listIndex(0).appkey
                        }
                    }
                    saveInfoOrderChannelData(result)
                    updateLocalOrderChannel()
                }
            }
        })
    }

    private fun saveInfoOrderChannelData(result: ControlBean?) {
        var infoStream: ControlBean? = null
        try {
            infoStream = CacheUtil.getObj(Constants.SP_INFO_ORDER_CHANNEL, ControlBean::class.java)
        } catch (e: Exception) {
        }
        if (infoStream == null || infoStream.info_stream == null) {
            Constant.INFO_STREAM_ORDER_CHANNEL_CHANGE = true
            putObj(Constants.SP_INFO_ORDER_CHANNEL, result.nN())
        } else {
            if (result.nN() == null) {
                return
            }
            if (result.nN().info_stream == null) {
                Constant.INFO_STREAM_ORDER_CHANNEL_CHANGE = true
                putObj(Constants.SP_INFO_ORDER_CHANNEL, result.nN())
            }
            var newColumn = result.nN().info_stream.listIndex(0).column
            var oldColumn = infoStream.nN().info_stream.listIndex(0).column
            if (newColumn == null || oldColumn == null) {
                Constant.INFO_STREAM_ORDER_CHANNEL_CHANGE = true
                putObj(Constants.SP_INFO_ORDER_CHANNEL, result.nN())
                return
            }
            if (newColumn.size != oldColumn.size) {
                Constant.INFO_STREAM_ORDER_CHANNEL_CHANGE = true
                putObj(Constants.SP_INFO_ORDER_CHANNEL, result.nN())

            } else {
                for (i in newColumn.indices) {
                    if (!newColumn.get(i).equals(oldColumn.get(i))) {
                        Constant.INFO_STREAM_ORDER_CHANNEL_CHANGE = true
                        putObj(Constants.SP_INFO_ORDER_CHANNEL, result.nN())
                        return
                    }

                }
            }
        }

    }

    private fun updateLocalOrderChannel() {
        if (Constant.INFO_STREAM_ALL_CHANNEL_CHANGE || Constant.INFO_STREAM_ORDER_CHANNEL_CHANGE) {
            var result: ControlBean? = control.value ?: return
            var tabList = mutableListOf<TabBean>()
            var info_stream: List<ControlBean.InfoStream>? = result.nN().info_stream
            if (!info_stream.isNullOrEmpty()) {
                val column = info_stream.listIndex(0).column
                if (column != null) {
                    for (i in column.indices) {
                        val tabBean =
                            TabBean()
                        tabBean.code = column.nN().listIndex(i)
                        tabList.add(tabBean)
                    }
                    val jsonString: String = Gson().toJson(tabList)
                    putObj(Constants.SP_INFO_LOCAL_ORDER_CHANNEL, jsonString)
                }

            } else {
                putObj(Constants.SP_INFO_LOCAL_ORDER_CHANNEL, "")
            }


        }
    }

    fun reqeustInfo() {
        getInfoAllChannel()
        getLocationCity()
    }

    private fun getInfoAllChannel() {
        JNetUtils.getInfoAllChannel(object : CallResult<AllChannelBean>() {
            override fun ok(result: AllChannelBean?) {
                super.ok(result)
                Log.w("lpb", "getInfoAllChannel----->" + result)
                saveInfoAllChannelData(result)
                updateLocalOrderChannel()
            }

        })

    }

    private fun getLocationCity() {
        JNetUtils.getLocationCity(object : CallResult<LocationBean>() {
            override fun ok(result: LocationBean?) {
                super.ok(result)
                Log.w("lpb", "getLocationCity----->" + result)
                if (result != null) {
                    if (result.data != null || result.data.data != null) {
                        Constants.locationCity = result.data.data.country.trim()
                    }
                }
            }

        })

    }

    private fun saveInfoAllChannelData(result: AllChannelBean?) {

        var infoStream: AllChannelBean.DataBeanX? = null
        try {
            infoStream =
                CacheUtil.getObj(
                    Constants.SP_INFO_ALL_CHANNEL,
                    AllChannelBean.DataBeanX::class.java
                )
        } catch (e: Exception) {
        }
        if (infoStream == null) {
            Constant.INFO_STREAM_ALL_CHANNEL_CHANGE = true
            putObj(Constants.SP_INFO_ALL_CHANNEL, result.nN().data)
        } else {
            if (result.nN() == null) {

                return
            }
            if (result.nN().data == null || result.nN().data.data == null) {
                Constant.INFO_STREAM_ALL_CHANNEL_CHANGE = true
                putObj(Constants.SP_INFO_ALL_CHANNEL, result.nN().data)
                return
            }
            var newColumn = result.nN().data.data
            var oldColumn = infoStream.nN().data
            if (newColumn == null || oldColumn == null) {
                return
            }
            if (newColumn.size != oldColumn.size) {
                Constant.INFO_STREAM_ALL_CHANNEL_CHANGE = true
                putObj(Constants.SP_INFO_ALL_CHANNEL, result.nN().data)

            } else {
                for (i in newColumn.indices) {
                    if (!newColumn.get(i).code.equals(oldColumn.get(i).code)) {
                        Constant.INFO_STREAM_ALL_CHANNEL_CHANGE = true
                        putObj(Constants.SP_INFO_ALL_CHANNEL, result.nN().data)
                        return
                    }

                }
            }
        }
    }

    fun initUrlDomain() {
        if (Configure.Debug) {
            mDomainNameHub[UrlConstant.CONTROL] = UrlConstant.DEBUG_APP_CONTROL
            mDomainNameHub[UrlConstant.NATIVE] = UrlConstant.DEBUG_APP_NATIVE
            mDomainNameHub[UrlConstant.JRTT] = UrlConstant.DEBUG_JRTT_INFO
            mDomainNameHub[UrlConstant.WIDGET] = UrlConstant.DEBUG_APP_WIDGET
        } else {
            mDomainNameHub[UrlConstant.CONTROL] = UrlConstant.APP_CONTROL
            mDomainNameHub[UrlConstant.NATIVE] = UrlConstant.APP_NATIVE
            mDomainNameHub[UrlConstant.JRTT] = UrlConstant.JRTT_INFO
            mDomainNameHub[UrlConstant.WIDGET] = UrlConstant.APP_WIDGET
        }
    }

    fun findUrl(domain: String): String? {
        if (mDomainNameHub[domain].isNullOrEmpty()) {
            initUrlDomain()
        }
        return mDomainNameHub[domain]
    }


    private fun <M> callNativeApi(
        func: suspend () -> BaseResponse<M>,
        callBack: CallResult<M>? = null
    ) {
        viewModelScope.async(Dispatchers.Main) {
            try {
                withContext(Dispatchers.IO) {
                    var data = func()
                    withContext(Dispatchers.Main) {
                        when {
                            data.ret != 200 -> callBack?.failed(data.ret, data.msg)
                            data.nN().data.nN().code != 0 -> callBack?.failed(
                                data.nN().data.nN().code,
                                data.nN().data.nN().msg
                            )
                            else -> {
                                callBack?.ok(data.data.nN().data)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                LogE("数据异常：$e  ${e.printStackTrace()}")
                when (e) {
                    is UnknownHostException,
                    is ConnectException,
                    is SocketTimeoutException,
                    is IOException,
                    is HttpException -> {
                        callBack?.failed(NetStatus.netError)
                    }
                    is NetException -> {
                        LogE("自定义异常：${e.errorCode}  ${e.errorMsg}")
                        callBack?.failed(NetStatus.netError, e.errorMsg)
                    }
                    is JSONException -> callBack?.failed(NetStatus.netError)
                    else -> {
                        callBack?.failed(NetStatus.fail)
                    }
                }
            }
        }
    }

    private fun <M> callApi(
        func: suspend () -> M,
        callBack: CallResult<M>? = null
    ) {
        viewModelScope.async(Dispatchers.Main) {
            try {
                withContext(Dispatchers.IO) {
                    var data = func()
                    withContext(Dispatchers.Main) {
                        callBack?.ok(data)
                    }
                }
            } catch (e: Exception) {
                LogE("数据异常：$e  ${e.printStackTrace()}")
                when (e) {
                    is UnknownHostException,
                    is ConnectException,
                    is SocketTimeoutException,
                    is IOException,
                    is HttpException -> {
                        callBack?.failed(NetStatus.netError)
                    }
                    is NetException -> {
                        LogE("自定义异常：${e.errorCode}  ${e.errorMsg}")
                        callBack?.failed(NetStatus.netError, e.errorMsg)
                    }
                    is JSONException -> callBack?.failed(NetStatus.netError)
                    else -> {
                        callBack?.failed(NetStatus.fail)
                    }
                }
            }
        }
    }

    fun checkPush(weather: WeatherBean? = null) {
        if (getAppModel().pushClientId.value.nN().isNotEmpty()) {
            val pushWeather =
                weather ?: if (weather == null && WeatherUtils.getDatas().isNotEmpty()) {
                    WeatherUtils.getDatas().listIndex(0)
                } else null
            val push = CacheUtil.getObj(Constant.SP_PUSH_CODE, PushCityBean::class.java)
            if (pushWeather != null && (push.nN().isLocation || !push.nN().isChoose)) {
                if (push.nN().code.isEmpty() || push.nN().code != pushWeather.regioncode) {
                    val lastPush =
                        CacheUtil.getObj(Constant.SP_LAST_PUSH_TIME, LastPushTimeBean::class.java)
                    pushCity.apply {
                        this.code = pushWeather.regioncode
                        this.city = pushWeather.regionname
                        this.isLocation = pushWeather.isLocation
                        this.dayTime =
                            push.nN().dayTime.isStr(if (push.nN().code.isEmpty()) lastPush.nN().dayTime else "")
                        this.nightTime =
                            push.nN().nightTime.isStr(if (push.nN().code.isEmpty()) lastPush.nN().nightTime else "")
                    }
                    STPushManager.getInstance().setTag(
                        AppContext.getContext(),
                        Tag(pushCity.nN().dayTime.isStr(if (push.nN().code.isEmpty()) lastPush.nN().dayTime else "")),
                        Tag(pushCity.nN().nightTime.isStr(if (push.nN().code.isEmpty()) lastPush.nN().nightTime else "")),
                        Tag(XMParam.getAppVer()),
                        Tag(pushWeather.regioncode)
                    )
                }
            }
        }
    }


    private var widgetBean: WidgetBean? = null

    fun initWidget() {
        if (isControl()) {
            return
        }
        widgetBean = CacheUtil.getObj(Constant.SP_WIDGET_DATA, WidgetBean::class.java)
        val intent = Intent(getApplication(), WidgetBroadcast::class.java)
        intent.action = "refresh"
        val pending = PendingIntent.getBroadcast(getApplication(), 8888, intent, 0)
        var time = if (widgetBean.nN().reqTimestamp == 0L) 1800000 else widgetBean.nN().reqTimestamp
//        var time = 60*1000L
        if (widgetBean != null && widgetBean.nN().refreshTime != 0L) {
            var date = System.currentTimeMillis() - widgetBean.nN().refreshTime
            if (date < time) {
                LogE("widget->time:push")
                time -= date
                updateWidgetUi()
            } else {
                LogE("widget->time:request")
                requestWidgetLocation()
            }
        } else {
            LogE("widget->time:request")
            requestWidgetLocation()
        }
        LogE("widget->time:$time")
        var triggerAtTime: Long =
            SystemClock.elapsedRealtime() + time
        var am = AppContext.getContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                am.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME, triggerAtTime, pending)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
                am.setExact(AlarmManager.ELAPSED_REALTIME, triggerAtTime, pending)
            }
            else -> {
                am.set(AlarmManager.ELAPSED_REALTIME, triggerAtTime, pending)
            }
        }

    }

    private fun updateWidgetUi() {
        XMMarker.marker11(getApplication(), JSONObject(Gson().toJson(widgetBean.nN().apply {
            this.aqiPic =
                "${AppContext.getContext().packageName}:mipmap/${WeatherUtils.leafIconResStr(this.aqiLevel)}"
            this.aqiColor = WeatherUtils.airColor(this.aqiLevel).listIndex(0)
            this.wtStatusPic = "${AppContext.getContext().packageName}:drawable/${
            WeatherUtils.iconSmallStr(
                this.wtid,
                WeatherUtils.isNight(this.sunr, this.suns)
            )
            }"
            this.wtPic = "${AppContext.getContext().packageName}:mipmap/${
            WeatherUtils.iconBigStr(
                this.wtid,
                WeatherUtils.isNight(this.sunr, this.suns)
            )
            }"
        })))
    }

    private fun requestWidgetLocation() {
        var shouldLocation = CacheUtil.getBoolean(Constant.SP_ISLOCATION_USER, true)
        if (shouldLocation) {
            if (WeatherUtils.isWeatherDateEmpty()) return
            LocationUtil.setCallBack { local ->
                LogE("widget->requestWidgetLocation:$local")
                if (local != null && local.state == 1) {
                    if (local.lat.isNotEmpty() && local.lng.isNotEmpty()) {
                        requestWidgetDate("", local.district.isStr(local.city), local)
                    } else {
                        var data = CacheUtil.getObj(Constant.SP_PUSH_CODE, PushCityBean::class.java)
                        if (data != null && data.code.isNotEmpty() && data.city.isNotEmpty()) {
                            requestWidgetDate(data.code, data.city, Location())
                        } else {
                            var weather = WeatherUtils.getWeatherBean(0)
                            if (weather.nN().regioncode.isNotEmpty()) {
                                requestWidgetDate(
                                    weather.nN().regioncode,
                                    weather.nN().regionname.isStr(
                                        weather.nN().location.nN().district.isStr(
                                            weather.nN().location.nN().city.isStr(
                                                weather.nN().regionname
                                            )
                                        )
                                    ),
                                    weather.nN().location
                                )
                            }
                        }
                    }
                }
            }
            LocationUtil.startLocation(LocationUtil.KEY_WIDGET_LOCATION)
        } else {
            var data = CacheUtil.getObj(Constant.SP_PUSH_CODE, PushCityBean::class.java)
            if (data != null && data.code.isNotEmpty() && data.city.isNotEmpty()) {
                requestWidgetDate(data.code, data.city, Location())
            } else {
                var weather = WeatherUtils.getWeatherBean(0)
                if (weather.nN().regioncode.isNotEmpty()) {
                    requestWidgetDate(
                        weather.nN().regioncode,
                        weather.nN().regionname.isStr(
                            weather.nN().location.nN().district.isStr(weather.nN().location.nN().city)
                        ),
                        weather.nN().location
                    )
                }
            }
        }
    }


    private fun requestWidgetDate(regionCode: String, regionName: String, location: Location?) {
        LogE("widget->requestWidgetDate")
        viewModelScope.async(Dispatchers.IO) {
            try {
                var data = net().获取小部件数据(
                    regionCode,
                    location.nN().lng,
                    location.nN().lat,
                    "2",
                    location.nN().province,
                    location.nN().city,
                    location.nN().district,
                    "${location.nN().locationType}"
                )
                if (data.nN().get("code").asString == "1") {
                    widgetBean = Gson().fromJson<WidgetBean>(
                        ReCodeUtils.decode(
                            data.nN().get("data").asString
                        ), WidgetBean::class.java
                    ).apply {
                        this.location = location
                        this.regionname = regionName
                        this.regioncode = regionCode
                    }
                    LogE("widget->requestOk:${ReCodeUtils.decode(data.nN().get("data").asString)}")
                    withContext(Dispatchers.Main) {
                        updateWidgetUi()
                    }
                    CacheUtil.putObj(Constant.SP_WIDGET_DATA, widgetBean.nN().apply {
                        this.refreshTime = System.currentTimeMillis()
                    })
                }
            } catch (e: Exception) {
            }
        }

    }

    private fun pushWidget(weatherData: WeatherBean) {
        if (isControl()) {
            return
        }

        if (weatherData.isLocation
            || widgetBean == null
            || (!WeatherUtils.initData()
                .listIndex(0).isLocation && pushCity.code.isNotEmpty() && pushCity.code == weatherData.regioncode)
            || (weatherData.regioncode.isNotEmpty() && weatherData.regioncode == widgetBean.nN().regioncode)
        ) {
            widgetBean = widgetBean.nN().apply {
                this.aqi = weatherData.aqi
                this.aqiLevel = weatherData.aqiLevel
                this.tc = weatherData.tc
                this.wt = weatherData.wt
                this.wtid = weatherData.wtid
                this.tcr = weatherData.wtablesNew.nN().listIndex(0).tcr
//                this.fct=DataUtil.timeStamp2Date(weatherData.time.parseLong()*1000,"yyyy-MM-dd HH:mm:ss")
                this.fct = weatherData.homeDetail.nN().fct.isStr(
                    DataUtil.timeStamp2Date(
                        weatherData.time.parseLong() * 1000,
                        "yyyy-MM-dd HH:mm:ss"
                    )
                )
                this.sunr = weatherData.ybhs.listIndex(0).sunrise
                this.suns = weatherData.ybhs.listIndex(0).sunset
                this.regionname = weatherData.regionname
                this.regioncode = weatherData.regioncode
            }
            LogE("widget->pushWidget：${JSONObject(Gson().toJson(widgetBean.nN()))}")
            updateWidgetUi()
            CacheUtil.putObj(Constant.SP_WIDGET_DATA, widgetBean.nN().apply {
                this.refreshTime = System.currentTimeMillis()
            })
        }

    }


    /**
     * 触发壁纸
     * @param code 场景wallpaper_background/wallpaper_voice/wallpaper_start
     */
    fun initScreenLock(code: String) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O && (OSUtils.isVivo() || OSUtils.isOppo())) {
            return
        }
        Try {
            runOnTime(500) {
                if (ActivityManageTools.topActivity() is SplashActivity && ActivityManageTools.topActivity()?.isFinishing == false) {
                    needScreenLock = true
                    return@runOnTime
                }

                if (System.currentTimeMillis() - lastSetWallpaperTime <= 5000) {
                    return@runOnTime
                }
                lastSetWallpaperTime = System.currentTimeMillis()
                if (!isWallpaperOpen() || (Build.VERSION.SDK_INT > Build.VERSION_CODES.O && !hasExternalPermission() && isTargetSDKAbove26(AppContext.getContext()))) {
                    return@runOnTime
                }
                var isFirst = false

                WallPaperSDK.instance.trySetWallpager(code, object : IWallpaperCallBack {
                    override fun onFailed(p0: String?) {

                    }

                    override fun onWallpaperShow(wallPaperTitle: String?) {
                        when (code) {
                            Constant.WALLPAPER_START -> {
                                isFirst = CacheUtil.getBoolean(Constant.SP_IS_WALLPAPER_FIRST, true)
                                if (isFirst) {
                                    CacheUtil.put(Constant.SP_IS_WALLPAPER_FIRST, false)
                                    showReport(
                                        EnumType.上报埋点.新用户首次启动场景调起设置壁纸show_click,
                                        subactid = "$wallPaperTitle"
                                    )
                                } else {
                                    showReport(
                                        EnumType.上报埋点.非新用户首次启动场景调起设置壁纸show_click,
                                        subactid = "$wallPaperTitle"
                                    )
                                }
                            }
                        }
                    }

                    override fun onWallpaperSuccess(wallPaperTitle: String?) {
                        when (code) {
                            Constant.WALLPAPER_START -> {
                                if (isFirst) {
                                    clickReport(
                                        EnumType.上报埋点.新用户首次启动场景调起设置壁纸show_click,
                                        subactid = "$wallPaperTitle"
                                    )
                                } else {
                                    clickReport(
                                        EnumType.上报埋点.非新用户首次启动场景调起设置壁纸show_click,
                                        subactid = "$wallPaperTitle"
                                    )
                                }
                            }
                        }
                    }

                    override fun onWallpaperFailed() {
                    }

                })
            }
        }
    }


    fun checkScreenLock(code: String): Boolean {

        var wallpaper_sp =
            CacheUtil.getObj(code, WallPaperBean::class.java).nN(WallPaperBean().apply {
                this.wallPaperCode = code
            })
        val boolean = wallpaper_sp.totalCount < 3 && !DataUtil.isSameDay(
            System.currentTimeMillis(),
            wallpaper_sp.timestamp
        )

        if (boolean) {
            CacheUtil.put(
                wallpaper_sp.wallPaperCode,
                wallpaper_sp.apply {
                    this.totalCount = this.totalCount + 1
                    this.timestamp = System.currentTimeMillis()
                }
            )
        }
        return boolean
    }


    /**
     * 检测是否需要二次设置壁纸
     */
    private var needScreenLock = false
    fun checkScreenLock() {
        if (needScreenLock) {
            needScreenLock = false
            initScreenLock(Constant.WALLPAPER_START)
        }
    }

    /**
     * 是否有存储权限
     */
    fun hasExternalPermission(): Boolean = ContextCompat.checkSelfPermission(
        AppContext.getContext(),
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    fun isActiveUser(): Boolean = CacheUtil.getBoolean(Constant.SP_ACTIVEUSER_KEY, false)

    fun setActiveUser(isActive: Boolean) {
        CacheUtil.put(Constant.SP_ACTIVEUSER_KEY, isActive)
    }

    fun showLocationPopup(
        context: Activity,
        isAuth: Boolean,
        permissionsResult: PermissionsUtils.IPermissionsResult
    ) {
        if (null == LocationAuthPopup) {
            LocationAuthPopup = LocationPermissionDialog(context) {
                when {
                    PermissionsUtils.shouldShowRequestPermissionRationale(
                        context,
                        Constant.CHECK_LOCATION_PERMISSIONS.toMutableList()
                    ) -> {
                        PermissionsUtils.checkPermissions(
                            context,
                            Constant.CHECK_LOCATION_PERMISSIONS,
                            permissionsResult
                        )
                    }
                    !isAuth -> {
                        val packageURI = Uri.parse("package:${context.packageName}")
                        val intent =
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
                        context.startActivity(intent)
                    }

                    else -> {
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        context.startActivity(intent)
                    }
                }
            }
        }
        LocationAuthPopup?.show()
    }


    @Keep
    data class Function(var function: () -> Any? = {})
}