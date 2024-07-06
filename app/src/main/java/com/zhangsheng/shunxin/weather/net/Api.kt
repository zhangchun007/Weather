package com.zhangsheng.shunxin.weather.net

import com.google.gson.JsonObject
import com.maiya.thirdlibrary.net.bean.BaseResponse
import com.maiya.thirdlibrary.net.bean.None
import com.zhangsheng.shunxin.information.bean.*
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.UrlConstant
import com.zhangsheng.shunxin.weather.net.bean.*
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/7/13 22:36
 */
interface Api {


    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.NATIVE}")
    @POST("?service=App.Tools.GetTtsToken")
    suspend fun 语音合成Token(@Field("type") type: String = "ali"): BaseResponse<TtsTokenBean>


    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.CONTROL}")
    @POST("app-control/polling.config")
    suspend fun 获取云控信息(@Field("a") a: String = ""): ControlBean

    /**
     *天气APi
     */
    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.NATIVE}")
    @POST("?service=App.Weather.IndexData")
    suspend fun 天气首页数据(
        @Field("regioncode") regioncode: String,
        @Field("regionname") regionname: String,
        @Field("predictionhour") predictionhour: String = "24",
        @Field("predictionday") predictionday: String = "15"
    ): BaseResponse<WeatherBean>

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.NATIVE}")
    @POST("?service=App.Weather.IndexDataExt")
    suspend fun 按地理位置获取天气首页数据(
        @Field("lng") lng: String,
        @Field("lat") lat: String,
        @Field("regionname") regionname: String, @Field("provinceCn") provinceCn: String,
        @Field("cityCn") cityCn: String,
        @Field("countryCn") countryCn: String,
        @Field("originType") originType: String
    ): BaseResponse<WeatherBean>

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.NATIVE}")
    @POST("?service=App.Weather.AirQuality")
    suspend fun 空气质量数据(
        @Field("regioncode") regioncode: String,
        @Field("predictionhour") predictionhour: String = "1",
        @Field("predictionday") predictionday: String = "5"
    ): BaseResponse<AirBean>

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.NATIVE}")
    @POST("?service=App.Weather.AirQualityMap")
    suspend fun 空气质量检测站点地图数据(
        @Field("regioncode") regioncode: String = ""
    ): BaseResponse<List<AirPositionBean.Postion>>

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.NATIVE}")
    @POST("?service=App.Weather.AirQualityRank")
    suspend fun 空气质量排名数据(
        @Field("regioncode") regioncode: String = ""
    ): BaseResponse<AirRankBean>

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.NATIVE}")
    @POST("?service=App.Weather.AirQualityExt")
    suspend fun 按地理位置获取空气质量数据(
        @Field("lat") lat: String,
        @Field("lng") lng: String,
        @Field("regionname") regionname: String
    ): BaseResponse<AirBean>

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.NATIVE}")
    @POST("?service=App.Weather.FallRsMaps")
    suspend fun 雷达图数据(
        @Field("lng") lng: String,
        @Field("lat") lat: String,
        @Field("level") level: String = "2"
    ): BaseResponse<JsonObject>

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.NATIVE}")
    @POST("?service=App.Weather.GetTyphoonInfo")
    suspend fun 台风详情(
        @Field("lng") lng: String,
        @Field("lat") lat: String
    ): BaseResponse<List<TyphoonBean>>


    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.NATIVE}")
    @POST("?service=App.Weather.GetAirQualitySmogMap")
    suspend fun 空气质量雾霾实况地图(
        @Field("lng") lng: String,
        @Field("lat") lat: String
    ): BaseResponse<JsonObject>

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.NATIVE}")
    @POST("?service=App.Weather.CityMapData")
    suspend fun 按地理位置获取城市天气数据(
        @Field("lng") lng: String,
        @Field("lat") lat: String
    ): BaseResponse<MapCityWeatherBean>

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.NATIVE}")
    @POST("?service=App.Weather.FallRsInfos")
    suspend fun 按地理位置获取降雨(
        @Field("lng") lng: String,
        @Field("lat") lat: String,
        @Field("regionname") regionname: String
    ): BaseResponse<WeatherRainBean>

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.NATIVE}")
    @POST("?service=App.Weather.CityData")
    suspend fun 城市天气(@Field("sts") sts: String): BaseResponse<List<CityWeatherSearchBean>>

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.NATIVE}")
    @POST("?service=App.Tools.Uplog")
    suspend fun 日志上报接口(@Field("type") type: String, @Field("log") log: String): BaseResponse<Any>

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.WIDGET}")
    @POST("app-weather-plugin/data/cityCardWeather.data")
    suspend fun 获取小部件数据(
        @Field("regioncode") regioncode: String,
        @Field("lng") lng: String,
        @Field("lat") lat: String,
        @Field("reqType") reqType: String = "2",
        @Field("provinceCn") provinceCn: String,
        @Field("cityCn") cityCn: String,
        @Field("countryCn") countryCn: String,
        @Field("originType") originType: String,
        @Field("reqSource") reqSource: String = "1"
    ): JsonObject

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.WIDGET}")
    @POST("app-weather-plugin/data/city-fall-remind")
    suspend fun 城市降雨提醒(
        @Field("lng") lng: String,
        @Field("lat") lat: String,
        @Field("provinceCn") provinceCn: String,
        @Field("cityCn") cityCn: String,
        @Field("countryCn") countryCn: String,
        @Field("originType") originType: String
    ): JsonObject

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.NATIVE}")
    @POST("?service=App.Tools.IpLocation")
    suspend fun 获取IP定位(@Field("none") type: String = ""): BaseResponse<IpLocation>

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.NATIVE}")
    @POST("?service=App.Weather.GetCityWeatherOV")
    suspend fun 获取城市天气概况(
        @Field("region") region: String = "",
        @Field("sts") sts: String = "",
        @Field("lng") lng: String = "",
        @Field("lat") lat: String = "",
        @Field("type") type: String = ""
    ): BaseResponse<WeatherForecastBean>


    //==============================日历=============================================//
    @GET
    suspend fun 获取指定年份调班调休日(@Url url: String): BaseResponse<JsonObject>


    //==============================信息流=============================================//
//    @FormUrlEncoded
//    @Headers("${Constant.DOMAIN}:${Constant.CONTROL}")
//    @POST("app-control/polling.config")
//    suspend fun 获取云控信息(@Field("a") a: String = ""): ControlBean

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.JRTT}")
    @POST("access_token/register/oaid/v1/")
    suspend fun 信息流注册(
        @Field("timestamp") timestamp: String?,
        @Field("signature") signature: String?,
        @Field("nonce") nonce: String?,
        @Field("partner") partner: String?,
        @Field("uuid") uuid: String?,
        @Field("oaid") oaid: String?
    ): RegistBean

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.JRTT}")
    @POST("data/stream/v3/")
    suspend fun 获取信息流资讯(
        @Field("timestamp") timestamp: String?,
        @Field("signature") signature: String?,
        @Field("nonce") nonce: String?,
        @Field("partner") partner: String?,
        @Field("uuid") uuid: String?,
        @Field("imei") imei: String?,
        @Field("access_token") access_token: String?,
        @Field("dt") dt: String?,
        @Field("ip") ip: String?,
        @Field("type") type: String?,
        @Field("os") os: String?,
        @Field("os_version") os_version: String?,
        @Field("resolution") resolution: String?,
        @Field("category") category: String?,
        @Field("city") city: String?,
        @Field("ac") ac: String?,
        @Field("https") https: String = "1"

    ): InfoBean

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.JRTT}")
    @POST("data/stream/related/v1/{group_id}/{imei_id}/")
    suspend fun 获取推荐信息(
        @Path("group_id") group_id: String?,
        @Path("imei_id") imei_id: String?,
        @Field("timestamp") timestamp: String?,
        @Field("signature") signature: String?,
        @Field("nonce") nonce: String?,
        @Field("partner") partner: String?,
        @Field("access_token") access_token: String?,
        @Field("count") count: Long?
    ): InfoCommendBean

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.NATIVE}")
    @POST("?service=App.InfoStream.GetChannel")
    suspend fun 获取全部渠道(@Field("signl") sign: String = ""): AllChannelBean

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.NATIVE}")
    @POST("?service=App.Tools.Location")
    suspend fun 获取地理位置(@Field("signl") sign: String = ""): LocationBean

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.JRTT}")
    @POST("user/action/log/click/v1/")
    suspend fun 点击回传(
        @Field("timestamp") timestamp: String?,
        @Field("signature") signature: String?,
        @Field("nonce") nonce: String?,
        @Field("partner") partner: String?,
        @Field("access_token") access_token: String?,

        @Field("group_id") group_id: Long?,
        @Field("category") category: String?,
        @Field("event_time") event_time: String?
    ): PostBackBean

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.JRTT}")
    @POST("user/action/log/stay/v1/")
    suspend fun 详情页停留时间(
        @Field("timestamp") timestamp: String?,
        @Field("signature") signature: String?,
        @Field("nonce") nonce: String?,
        @Field("partner") partner: String?,
        @Field("access_token") access_token: String?,

        @Field("group_id") group_id: Long?,
        @Field("category") category: String?,
        @Field("event_time") event_time: String?,
        @Field("stay_time") stay_time: Long?
    ): PostBackBean

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.JRTT}")
    @POST("user/action/log/video_auto_play/v1/")
    suspend fun 视频播放开始事件上报(
        @Field("timestamp") timestamp: String?,
        @Field("signature") signature: String?,
        @Field("nonce") nonce: String?,
        @Field("partner") partner: String?,
        @Field("access_token") access_token: String?,

        @Field("group_id") group_id: Long?,
        @Field("category") category: String?,
        @Field("event_time") event_time: String?,
        @Field("position") position: String?
    ): PostBackBean

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.JRTT}")
    @POST("user/action/log/video_auto_play/v1/")
    suspend fun 视频播放结束事件上报(
        @Field("timestamp") timestamp: String?,
        @Field("signature") signature: String?,
        @Field("nonce") nonce: String?,
        @Field("partner") partner: String?,
        @Field("access_token") access_token: String?,

        @Field("group_id") group_id: Long?,
        @Field("category") category: String?,
        @Field("event_time") event_time: String?,
        @Field("position") position: String?
    ): PostBackBean


    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.JRTT}")
    @POST("user/action/log/show/v1/")
    suspend fun 客户端展现(
        @Query("timestamp") timestamp: String?,
        @Query("signature") signature: String?,
        @Query("nonce") nonce: String?,
        @Query("partner") partner: String?,
        @Query("access_token") access_token: String?,
        @Body() info: RequestBody?
    ): PostBackBean

    @FormUrlEncoded
    @Headers("${UrlConstant.DOMAIN}:${UrlConstant.NATIVE}")
    @POST("?service=App.Weather.GetMorePrediction")
    suspend fun 四十日天气预报(
        @Field("regioncode") regioncode: String,
        @Field("regionname") regionname: String ,
    ): BaseResponse<FortyWeatherBean>
}