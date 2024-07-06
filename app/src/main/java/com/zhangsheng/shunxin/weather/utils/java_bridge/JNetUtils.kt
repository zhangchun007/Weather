package com.maiya.weather.util.java_bridge


import com.maiya.thirdlibrary.net.callback.CallResult
import com.zhangsheng.shunxin.information.bean.*
import com.zhangsheng.shunxin.weather.ext.callApi
import com.zhangsheng.shunxin.weather.ext.net
import okhttp3.RequestBody

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/5/22 19:36
 */
object JNetUtils {


    fun getTokenByOaid(
        timestamp: String?,
        signature: String?,
        nonce: String?,
        partner: String?,
        uuid: String?,
        oaid: String?, callback: CallResult<RegistBean>
    ) {
        callApi({
            net().信息流注册(
                timestamp,
                signature,
                nonce,
                partner,
                uuid,
                oaid
            )
        }, callback)
    }

    fun getInfo(
        timestamp: String?,
        signature: String?,
        nonce: String?,
        partner: String?,
        uuid: String?,
        imei: String?,
        access_token: String?,
        dt: String?,
        ip: String?,
        type: String?,
        os: String?,
        os_version: String?,
        resolution: String?, category : String?,city : String?,ac : String?,callback: CallResult<InfoBean>
    ) {
        callApi({
            net().获取信息流资讯(
                timestamp,
                signature,
                nonce,
                partner,
                uuid,
                imei,
                access_token,
                dt,
                ip,
                type,
                os,
                os_version,
                resolution,
                category,
                city,
                ac
            )
        }, callback)
    }

    fun getRecommendInfo(
        group_id: String?,
        imei_id: String?,
        timestamp: String?,
        signature: String?,
        nonce: String?,
        partner: String?,
        access_token: String?,
        count: Long?,
        callback: CallResult<InfoCommendBean>
    ) {
        callApi({
            net().获取推荐信息(
                group_id, imei_id,
                timestamp,
                signature,
                nonce,
                partner,
                access_token,
                count
            )
        }, callback)
    }

    fun getInfoAllChannel(
       
        callback: CallResult<AllChannelBean>
    ) {
        callApi({
            net().获取全部渠道()
        }, callback)
    }

    fun getLocationCity(

        callback: CallResult<LocationBean>
    ) {
        callApi({
            net().获取地理位置()
        }, callback)
    }

    fun clickPostBack(
        timestamp: String?,
        signature: String?,
        nonce: String?,
        partner: String?,
        access_token: String?,
        group_id: Long?,
        category: String?,
        event_time: String?,
       callback: CallResult<PostBackBean>
    ) {
        callApi({
            net().点击回传(
                timestamp,
                signature,
                nonce,
                partner,
                access_token,
                group_id,
                category,
                event_time
            )
        }, callback)
    }

    fun infoDetilsStayTimePostBack(
        timestamp: String?,
        signature: String?,
        nonce: String?,
        partner: String?,
        access_token: String?,
        group_id: Long?,
        category: String?,
        event_time: String?,
        stay_time: Long?,
        callback: CallResult<PostBackBean>
    ) {
        callApi({
            net().详情页停留时间(
                timestamp,
                signature,
                nonce,
                partner,
                access_token,
                group_id,
                category,
                event_time,stay_time
            )
        }, callback)
    }
    fun infoDetilsVideoPlayStartPostBack(
        timestamp: String?,
        signature: String?,
        nonce: String?,
        partner: String?,
        access_token: String?,
        group_id: Long?,
        category: String?,
        event_time: String?,
        position: String?,
        callback: CallResult<PostBackBean>
    ) {
        callApi({
            net().视频播放开始事件上报(
                timestamp,
                signature,
                nonce,
                partner,
                access_token,
                group_id,
                category,
                event_time,position
            )
        }, callback)
    }

    fun infoShowPostBack(
        timestamp: String?,
        signature: String?,
        nonce: String?,
        partner: String?,
        access_token: String?,
        info : RequestBody,
        callback: CallResult<PostBackBean>
    ) {
        callApi({
            net().客户端展现(
                timestamp,
                signature,
                nonce,
                partner,
                access_token,
                info)
        }, callback)
    }
}