package com.zhangsheng.shunxin.weather.service

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.maiya.thirdlibrary.ext.*
import com.zhangsheng.shunxin.weather.MainActivity
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.utils.NotificationsUtils
import com.my.sdk.stpush.STPushManager
import com.my.sdk.stpush.open.STIntentService
import com.my.sdk.stpush.open.TPenetrateMsgStatics
import com.my.sdk.stpush.open.message.*
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.clickReport
import com.zhangsheng.shunxin.weather.ext.crashReport


/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2020/2/28 11:10
 */
class PushService : STIntentService() {

    override fun onReceiveMessageData(p0: Context, p1: STPenetrateMessage) {
        //透传消息处理
        LogE("push回调：透传消息->${p1}  ${String(p1.nN().penetrate)}")
        //p1.nN().isFormThirdPlatform 点击
        if (!p1.nN().isFormThirdPlatform && String(p1.nN().penetrate).isNotEmpty()) {
            Try {
                runOnUi {
                    var json = Gson().fromJson<JsonObject>(
                        String(p1.nN().penetrate),
                        JsonObject::class.java
                    )
                    NotificationsUtils.show(
                        p0,
                        json.get("title").asString,
                        json.get("desc").asString,
                        MainActivity::class.java
                    )
                }
            }
        } else {
            STPushManager.getInstance().reportPenetrateMsg(p0, TPenetrateMsgStatics.CLICK, p1)
        }
    }

    override fun onNotificationMessageArrived(p0: Context?, p1: STNotificationMessage?) {
        //通知到达
    }

    override fun onNotificationMessageClicked(p0: Context?, p1: STNotificationMessage?) {
        //通知被点击
        if (p1?.title?.startsWith("早间天气") == true) {
            clickReport(EnumType.上报埋点.早间天气推送首页点击)
        }

        if (p1?.title?.startsWith("晚间天气") == true) {
            clickReport(EnumType.上报埋点.晚间天气推送首页点击)
        }
    }

    override fun onReceiveCommandResult(p0: Context?, p1: STCmdMessage?) {
        //事件处理回调
        crashReport("onReceiveCommandResult", {
            LogE("push回调：事件回调->${p1.nN()}")
            if (p1 is QueryTagsCmdMessage) {
                var tags = p1 as QueryTagsCmdMessage

                getAppModel().queryTags.postValue(getAppModel().queryTags.value.nN().apply {
                    this.code = p1.code
                    tags.tags?.let {
                        this.tags = tags.tags.asList()
                    }
                })
            } else if (p1 is SetTagsCmdMessage) {
                getAppModel().setTags.postValue(p1.code)
            }
        })
    }

    override fun onReceiveClientId(p0: Context?, p1: String?) {
        //clientId
        LogE("push回调:clientId->$p1")
        getAppModel().pushClientId.postValue(p1)
    }


}