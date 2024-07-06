package com.zhangsheng.shunxin.weather.utils

import android.text.TextUtils
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.google.gson.Gson
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.utils.AppContext
import com.maiya.thirdlibrary.utils.CacheUtil
import com.xinmeng.xm.XMMarker
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.net.bean.Location
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.livedata.LiveDataBus
import com.zhangsheng.shunxin.weather.net.bean.LocationReportBean
import org.json.JSONObject
import java.math.BigDecimal
import java.util.*


object LocationUtil {
    const val 定位成功 = 1
    const val 定位失败 = 2
    const val 定位中 = 3
    const val 百度定位中 = 4
    const val 定位权限 = 5 //失败

    private var locationOption: AMapLocationClientOption? = null
    private var locationClient: AMapLocationClient? = null
    private var bdLocationClient: LocationClient? = null

    private var func: (location: Location?) -> Unit? = {}
    private var gdLocalData: Location? = null
    private var bdLocalData: Location? = null
    var isFirstLocation = false

    private var gd_success = false
    private var gd_time: Long = 0
    private var bd_success = false
    private var bd_time: Long = 0


    private val keyStack: Stack<String> by lazy { Stack<String>() }


    init {
        locationClient = AMapLocationClient(AppContext.getContext())

    }

    var locationState = 定位失败

    const val KEY_MAIN_LOCATION = "key_main_location"

    const val KEY_WIDGET_LOCATION = "KEY_WIDGET_LOCATION"

    const val KEY_CITY_LOCATION = "key_city_location"


    private var localbean: Location? = null

    fun getLocation(): Location {
        if (localbean == null) {
            localbean = CacheUtil.getObj(Constant.SP_LOCATION_DATA, Location::class.java).nN()
        }
        return localbean.nN()
    }

    fun startLocation(key: String = KEY_MAIN_LOCATION) {
        if (!keyStack.contains(key)) {
            keyStack.push(key)
        }
        locationOption = getDefaultOption()
        locationClient?.setLocationOption(locationOption)

        if (locationState != 定位中 && locationState != 百度定位中) {
            gdLocalData = null
            bdLocalData = null
            locationState = 定位中
            // 设置定位监听
            locationClient?.setLocationListener(locationListener)
            // 启动定位
            gd_time = System.currentTimeMillis() // 开始定位时间
            locationClient?.startLocation()


            runOnTime(5000) {
                startBdLocation()
            }
        }
    }

    private fun startBdLocation() {
        if (locationState == 定位中) {
            locationState = 百度定位中
            bd_time = System.currentTimeMillis()
            initBdLocationClient()?.start()
            LogE("Location->百度定位")
        }
    }

    /**
     * 高德定位监听
     */
    private var locationListener: AMapLocationListener =
        AMapLocationListener { location ->
            gd_time = System.currentTimeMillis() - gd_time
            gdLocalData = Location().apply {
                this.address = location.address
                this.province = location.province
                this.city = location.city
                this.street = location.street
                this.country = location.country
                this.district = location.district
                this.lat = "${location.latitude}"
                this.lng = "${location.longitude}"
                this.errorCode = location?.errorCode?.toString() ?: ""
                this.errorMessage = location?.errorInfo?.toString() ?: ""
            }
            LogE("location->高德定位: Code:${location.errorCode}  location:${gdLocalData}")
            if (locationState != 定位成功) {
                if (location.errorCode == 0 && location.latitude > 0 && location.longitude > 0) {
                    if (gdLocalData.nN().district.isStr()) {
                        gd_success = true
                        postLocation(true, gdLocalData.nN(), true)
                    } else if (bdLocalData == null) {
                        gd_success = false
                        startBdLocation()
                    }
                } else {
                    gd_success = false
                    if (bdLocalData == null) {
                        startBdLocation()
                    } else {
                        postLocation(false, gdLocalData.nN(), true)
                    }
                }
            }
        }

    /**
     * 百度定位监听
     */
    private var bdLocationListener = object : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation?) {
            bd_time = System.currentTimeMillis() - bd_time
            bdLocationClient?.stop()
            bdLocalData = Location().apply {
                this.address = location?.address?.address ?: ""
                this.province = location?.province ?: ""
                this.city = location?.city ?: ""
                this.street = location?.street ?: ""
                this.country = location?.country ?: ""
                this.district = location?.district ?: ""
                this.lat = "${location?.latitude ?: ""}"
                this.lng = "${location?.longitude ?: ""}"
                this.errorCode = location?.locType?.toString() ?: ""
                this.errorMessage = location?.locTypeDescription?.toString() ?: ""
            }
            LogE("location->百度定位: Code:${location?.locType}  location:${bdLocalData}")
            if (locationState == 定位中 || locationState == 百度定位中) {
                if ((location?.locType == 161 || location?.locType == 61) && location?.latitude > 0 && location?.longitude > 0) {
                    if (bdLocalData.nN().district.isStr() || bdLocalData.nN().city.isStr() || bdLocalData.nN().province.isStr()) {
                        bd_success = true
                        postLocation(true, bdLocalData.nN(), false)
                    } else if (gdLocalData.nN().district.isStr() || gdLocalData.nN().city.isStr() || gdLocalData.nN().province.isStr()) {
                        gd_success = true
                        postLocation(true, gdLocalData.nN(), true)
                    } else if (gdLocalData != null) {
                        gd_success = false
                        bd_success = false
                        postLocation(false, bdLocalData.nN(), false)
                    }
                } else {
                    if ((gdLocalData.nN().lat.isStr() && gdLocalData.nN().lng.isStr()) && (gdLocalData.nN().district.isStr() || gdLocalData.nN().city.isStr() || gdLocalData.nN().province.isStr())) {
                        gd_success = true
                        postLocation(true, gdLocalData.nN(), true)
                    } else if (gdLocalData != null) {
                        gd_success = false
                        bd_success = false
                        postLocation(false, bdLocalData.nN(), false)
                    }
                }
            }
        }
    }

    private fun postLocation(isSuccess: Boolean, localData: Location, isGd: Boolean) {
        if (locationState == 定位中 || locationState == 百度定位中) {
            locationState = if (isSuccess) 定位成功 else 定位失败
            isFirstLocation = true
            LogE("location->定位结束")
            Try {
                XMMarker.marker12(AppContext.getContext(), JSONObject().apply {
                    put("lng",localData.lng)
                    put("lat",localData.lat)
                    put("city",localData.district.isStr(localData.city))
                })
            }
            localData.apply {
                this.state = locationState
                this.locationType = if (isGd) 1 else 2
                if (isSuccess) this.time = System.currentTimeMillis()
            }
            localbean = localData

            //定位信息上报
            report()

            CacheUtil.putObj(Constant.SP_LOCATION_DATA, localData)
            while (keyStack.isNotEmpty()) {
                var key = keyStack.pop()
                if (key == KEY_WIDGET_LOCATION) {
                    func(localData)
                } else {
                    LiveDataBus.getChannel<Location>(key).postValue(localData)
                }
            }
        }
    }

    /**
     * 默认的定位参数
     * @since 2.8.0
     */
    private fun getDefaultOption(): AMapLocationClientOption {
        val mOption = AMapLocationClientOption()
        mOption.locationMode =
            if (keyStack.size == 1 && keyStack.contains(KEY_WIDGET_LOCATION)) AMapLocationClientOption.AMapLocationMode.Battery_Saving else
                AMapLocationClientOption.AMapLocationMode.Hight_Accuracy //可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.isGpsFirst = false//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.httpTimeOut = 8000//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.interval = 2000//可选，设置定位间隔。默认为2秒
        mOption.isNeedAddress = true//可选，设置是否返回逆地理地址信息。默认是true
        mOption.isOnceLocation = true //可选，设置是否单次定位。默认是false
        mOption.isOnceLocationLatest = false//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP)//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.isSensorEnable = false//可选，设置是否使用传感器。默认是false
        mOption.isWifiScan =
            true //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.isMockEnable = true //允许模拟定位
        mOption.isLocationCacheEnable = false //可选，设置是否使用缓存定位，默认为true
        mOption.geoLanguage =
            AMapLocationClientOption.GeoLanguage.DEFAULT//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption
    }


    private fun getBDOption(): LocationClientOption {
        val option = LocationClientOption()
        option.locationMode =
            if (keyStack.size == 1 && keyStack.contains(KEY_WIDGET_LOCATION)) LocationClientOption.LocationMode.Battery_Saving else
                LocationClientOption.LocationMode.Hight_Accuracy // 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

//        option.setCoorType("bd09ll") // 可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        option.timeOut = 5000

        option.setScanSpan(0) // 可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true) // 可选，设置是否需要地址信息，默认不需要

        option.setIsNeedLocationDescribe(true) // 可选，设置是否需要地址描述

        option.setNeedDeviceDirect(false) // 可选，设置是否需要设备方向结果

        option.isLocationNotify = false // 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果

        option.setIgnoreKillProcess(true) // 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop

        option.setIsNeedLocationDescribe(true) // 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation

        option.setIsNeedLocationPoiList(true) // 可选，默认false，设置是否需要POI结果，可以在BDLocation

        option.SetIgnoreCacheException(false) // 可选，默认false，设置是否收集CRASH信息，默认收集

        option.isOpenGps = true // 可选，默认false，设置是否开启Gps定位

        option.setIsNeedAltitude(false) // 可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用

        return option
    }

    private fun initBdLocationClient(): LocationClient? {
        //声明LocationClient类
        bdLocationClient = LocationClient(AppContext.getContext())
        //注册监听函数
        bdLocationClient?.registerLocationListener(bdLocationListener)
        bdLocationClient?.locOption = getBDOption()
        return bdLocationClient
    }


    fun formatProvince(province: String): String? {
        var province = province
        if (TextUtils.isEmpty(province)) {
            return null
        }
        if (province.startsWith("内蒙古")
            || province.startsWith("黑龙江")
        ) {
            province = province.substring(0, 3)
        } else {
            if (province.length > 2) {
                province = province.substring(0, 2)
            }
        }
        return province
    }

    fun setCallBack(func: (location: Location?) -> Unit? = {}) {
        if (func != {}) {
            this.func = func
        }
    }

    /*
    * 定位失败上报
    * */
    private fun report() {
        if (gd_success && bd_success) return // 定位都成功 不上报
        if (gdLocalData == null || bdLocalData == null) return //只有两个定位 都有结果后才上报
        var locationType: String? = ""
        if (!gd_success) locationType = "type1"
        if (!bd_success) locationType = "type2"
        if (!gd_success && !bd_success) locationType = "type3"

        var lat = "null"
        var lng = "null"

        if (gdLocalData?.lat.isStr() && gdLocalData?.lng.isStr() && (BigDecimal(gdLocalData?.lat).toDouble() > 0)
            && TextUtils.isEmpty(gdLocalData?.district)
        ) {
            locationType = "type4"
            lat = gdLocalData?.lat.toString()
            lng = gdLocalData?.lng.toString()
        }

        if (bdLocalData?.lat.isStr() && bdLocalData?.lng.isStr() && (BigDecimal(bdLocalData?.lat).toDouble() > 0)
            && TextUtils.isEmpty(bdLocalData?.district)
        ) {
            locationType = "type4"
            lat = bdLocalData?.lat.toString()
            lng = bdLocalData?.lng.toString()
        }

        val locationReportBean = LocationReportBean().apply {
            this.timelog = "baidu-${bd_time},gaode-${gd_time}"
            this.locationType = locationType
            this.reportType = "0"
            this.lat = lat
            this.lng = lng
            this.errorCode = "baidu-${bdLocalData?.errorCode ?: "null"}" +
                    ",gaode-${gdLocalData?.errorCode ?: "null"}"
            this.errorMessage = "baidu-${bdLocalData?.errorMessage ?: "null"}" +
                    ",gaode-${gdLocalData?.errorMessage ?: "null"}"
        }
        val json = Gson().toJson(locationReportBean)
        LogE("location->: $json")
        getAppModel().report("1", json)
        getAppModel().setActiveUser(true)//设置活跃用户标签 有权限第一次定位失败 定义为新安装用户，
    }

    /*
    * 省市区 code匹配失败上报
    * */
    fun report(location: Location) {
        var locationType: String? = ""
        if (!gd_success) locationType = "type1"
        if (!bd_success) locationType = "type2"

        val locationReportBean = LocationReportBean().apply {
            this.lng = location.lng
            this.lat = location.lat
            this.province = location.province
            this.city = location.city
            this.district = location.district
            this.address = location.address
            this.street = location.street
            this.country = location.country
            this.locationType = locationType
        }
        val json = Gson().toJson(locationReportBean)
        LogE("location->: $json")
        getAppModel().report("1", json)
    }
}

