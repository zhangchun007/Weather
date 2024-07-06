package com.zhangsheng.shunxin.ad.params

import com.maiya.thirdlibrary.ext.listIndex
import com.maiya.thirdlibrary.utils.CacheUtil
import com.xinmeng.shadow.base.ICustomParams
import com.xm.xmcommon.XMParam
import com.zhangsheng.shunxin.ad.AdConstant
import com.zhangsheng.shunxin.weather.common.Configure
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.ext.getAppControl
import com.zhangsheng.shunxin.weather.ext.isControl

class ClientCustomParams : ICustomParams {
    override fun accId(): String? {
        return XMParam.getAccid()
    }

    override fun muid(): String? {
        return XMParam.getMuid()
    }

    override fun appTypeId(): String? {
        return Configure.appTypeId
    }

    override fun appQid(): String? {
        return XMParam.getAppQid()
    }

    /**
     * 1 锁屏广告
     * 2 后台弹窗
     * 3 悬浮球
     * 4 解锁激励视频
     * 5 壁纸 壁纸保活
     * 6
     * app安装与卸载
     */
    override fun isExternalSceneOn(scene: Int): Boolean {
        try {
            if (isControl()) {
                return false
            }
            if (getAppControl().swtich_all.listIndex(0).outactswitch == "2") {
                return false
            }
            return true
        } catch (e: Exception) {
            return true
        }
    }

    override fun cleanAppQid(): String? {
        return XMParam.getCleanAppQid()
    }

    override fun isTourist(): String? {
        return XMParam.getIstourist()
    }

    override fun appSmallVerInt(): String? {
        return XMParam.getAppSmallVerInt()
    }

    override fun appSmallVer(): String? {
        return XMParam.getAppSmallVer()
    }

    override fun userflag(): String {
        return ""
    }


    override fun lowGps(): Boolean = true

    override fun aaid(): String? {
        return XMParam.getAAID()
    }

    override fun userinfo(): String? {
        return XMParam.getUserInfo()
    }

    override fun extras(): String {
        return "moke:1;moke_priority:1;moke_lock_bg:xm_tw_lock_bg;"
    }

    override fun oaid(): String? {
        return XMParam.getOAID()
    }

    override fun softType(): String? {
        return null
    }

    override fun canUseMacAddress(): Boolean {
        return CacheUtil.getBoolean(Constant.SP_AGREE_PRIVACY, false)
    }

    override fun isIjkSoLoaded(): Boolean {
        return true
    }

    override fun softName(): String? {
        return null
    }

    override fun useClientPushData(): Boolean {
        return true
    }

    override fun getNotificationReceiverName(): String? {
        return "com.zhangsheng.shunxin.weather.notifycation.WidgetBroadcast"
    }

    override fun isUseCacheFirst(pgtype: String?, gametype: String?): Boolean {
        return "twsptw" == gametype
    }

    override fun firstChannel(): Int =-1
    override fun getBDAppId(): String =Configure.bdAppId


}