package com.zhangsheng.shunxin.ad.configs

import com.maiya.thirdlibrary.ext.LogE
import com.xinmeng.shadow.base.IHostLinks
import com.zhangsheng.shunxin.weather.common.Configure
import com.zhangsheng.shunxin.weather.common.UrlConstant

open class BaseClientHostLinks : IHostLinks {
    /*** 归因渠道接口 ** @return  */
    override fun vtaInfoUrl(): String {
        return if (TEST_SERVER) "${UrlConstant.TEST_UREC}/querydata/query/getUserData" else "${UrlConstant.UREC}/querydata/query/getUserData"
    }

    /*** 位置信息接口 ** @return  */
    override fun locationInfoUrl(): String {
        //祥瑞天气locationInfo方法实现在子类中
        return if (TEST_SERVER) "${UrlConstant.TEST_ADCTRLPRE}/app-fix/adv/areaCode.data" else "${UrlConstant.ADCTRLPRE}/app-fix/adv/areaCode.data"
    }

    /*** 历史位置信息 ** @return  */
    override fun historyLocationInfoUrl(): String {
        LogE("是正式还是测试？？？？？$TEST_SERVER")
        return if (TEST_SERVER) "${UrlConstant.TEST_HISPOS}/zt_userinfo/query/getUserSphereOfActivity" else "${UrlConstant.HISPOS}/zt_userinfo/query/getUserSphereOfActivity"
    }

    /*** 序言泽 dsp 请求接口 ** @return  */
    override fun dspRequestUrl(): String {
        LogE("是正式还是测试？？？？？$TEST_SERVER")
        return if (TEST_SERVER) "${UrlConstant.TEST_NATIVE_SDK}/sdknative/appmaterial" else "${UrlConstant.NATIVE_SDK}/sdknative/appmaterial"
    }

    /*** union api 广告请求接口 ** @return  */
    override fun unionRequestUrl(): String? {
        return null
    }

    /*** sdk 通用上报接口 ** @return  */
    override fun sdkCommonReportUrl(): String {
        return if (TEST_SERVER) "${UrlConstant.TEST_ADV_SDK_REPORT}/apppubliclogs/sdkreport" else "${UrlConstant.ADV_SDK_REPORT}//apppubliclogs/sdkreport"
    }

    /*** dsp 用户画像接口 ** @return  */
    override fun hbaseLinkUrl(): String {
        return if (TEST_SERVER) "${UrlConstant.TEST_USER_PORTRAIT}/infonative/hbaselink" else "${UrlConstant.USER_PORTRAIT}/infonative/hbaselink"
    }

    /*** sdk 请求上报接口 ** @return  */
    override fun sdkRequestReportUrl(): String {
        return if (TEST_SERVER) "${UrlConstant.TEST_ADV_SDK_REPORT}/apppubliclogs/sdknewrequest" else "${UrlConstant.ADV_SDK_REPORT}/apppubliclogs/sdknewrequest"
    }

    /**
     * sdk 返回上报接口 ** @return
     */
    override fun sdkReturnReportUrl(): String {
        return if (TEST_SERVER) "${UrlConstant.TEST_ADV_SDK_REPORT}/apppubliclogs/sdkreturn" else "${UrlConstant.ADV_SDK_REPORT}/apppubliclogs/sdkreturn"
    }

    /*** sdk 展示上报接口 ** @return  */
    override fun sdkShowReportUrl(): String {
        return if (TEST_SERVER) "${UrlConstant.TEST_ADV_SDK_REPORT}/apppubliclogs/sdkshow" else "${UrlConstant.ADV_SDK_REPORT}/apppubliclogs/sdkshow"
    }

    /*** sdk 点击上报接口 ** @return  */
    override fun sdkClickReportUrl(): String {
        return if (TEST_SERVER) "${UrlConstant.TEST_ADV_SDK_REPORT}/apppubliclogs/sdkclick" else "${UrlConstant.ADV_SDK_REPORT}/apppubliclogs/sdkclick"
    }

    override fun sdkActivityUrl(): String {
        return if (TEST_SERVER) "${UrlConstant.TEST_ADV_SDK_REPORT}/apppubliclogs/sdkactivity" else "${UrlConstant.ADV_SDK_REPORT}/apppubliclogs/sdkactivity"
    }

    /*** 广告云控接口 ** @return  */
    override fun pollingUrl(): String {
        return if (TEST_SERVER) "${UrlConstant.TEST_AD_CTRLBSC}/advertisement-cloud-api/data/adv.data" else "${UrlConstant.AD_CTRLBSC}/advertisement-cloud-api/data/adv.data"
    }

    /*** 广告云控前置接口 ** @return  */
    override fun extInfoUrl(): String {
        return if (TEST_SERVER) "${UrlConstant.TEST_ADCTRLPRE}/app-fix/adv/advFix.data" else "${UrlConstant.ADCTRLPRE}/app-fix/adv/advFix.data"
    }

    /*** 广告云控前置接口 ** @return  */
    override fun appListUrl(): String {
        return if (TEST_SERVER) "${UrlConstant.TEST_ADV_APP_LIST}/applist/applist.report" else "${UrlConstant.ADV_APP_LIST}/applist/applist.report"
    }

    /*** 体外打点日志 ** @return  */
    override fun externalLogUrl(): String {
        return if (TEST_SERVER) "${UrlConstant.TEST_EXTER_LOG}/apppubliclogs/exterlog" else "${UrlConstant.EXER_LOG}/apppubliclogs/exterlog"
    }

    /***  体外广告云控 ** @return  */
    override fun externalCtrlUrl(): String {
        return if (TEST_SERVER) "${UrlConstant.TEST_AD_CTRL_EXT}/external-adv-cloud-api/config/adv.config" else "${UrlConstant.AD_CTRL_EXT}/external-adv-cloud-api/config/adv.config"
    }

    override fun triggerReportUrl(): String {
        return if (TEST_SERVER) "${UrlConstant.TEST_ADV_SDK_REPORT}/apppubliclogs/sdktrigger" else "${UrlConstant.ADV_SDK_REPORT}/apppubliclogs/sdktrigger"
    }

    override fun finalPlayUrl(): String {
        return if (TEST_SERVER) "${UrlConstant.TEST_ADV_SDK_REPORT}/apppubliclogs/sdkfinalplay" else "${UrlConstant.ADV_SDK_REPORT}/apppubliclogs/sdkfinalplay"
    }

    companion object {
        val TEST_SERVER = Configure.Debug
    }
}