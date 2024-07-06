package com.zhangsheng.shunxin.weather.model

import android.app.Activity
import android.text.TextUtils
import com.liulishuo.filedownloader.FileDownloader
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.net.bean.None
import com.maiya.thirdlibrary.net.callback.CallResult
import com.maiya.thirdlibrary.utils.AppContext
import com.maiya.thirdlibrary.utils.AppUtils
import com.maiya.thirdlibrary.utils.CacheUtil
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.dialog.AppUpDateDialog
import com.zhangsheng.shunxin.weather.ext.getAppControl
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.ext.isControlShow
import com.zhangsheng.shunxin.weather.ext.net
import com.zhangsheng.shunxin.weather.fragment.ToolItemModel
import com.zhangsheng.shunxin.weather.net.bean.ControlBean

class SettingViewModel : BaseViewModel() {

    val KEY_AIR_REDPOINT_CLICKED = "key_air_redpoint_clicked"
    val KEY_TYPHOON_REDPOINT_CLICKED = "key_typhoon_redpoint_clicked"


    fun 日志上报接口(type: String, json: String) {
        callNativeApi({ net().日志上报接口(type, json) }, object : CallResult<Any>() {
            override fun ok(result: Any?) {
                super.ok(result)
                xToast("您的反馈是对我们最大的帮助，感谢您的反馈!")
            }
        })
    }

    fun initToolBox(): List<ToolItemModel> {
        var item: ToolItemModel? = null
        val list = arrayListOf<ToolItemModel>()
        if (isControlShow(getAppControl().swtich_all.listIndex(0).airlive)) {
            item = ToolItemModel()
            item.toolTitle = "空气实况"
            item.toolIcon = R.mipmap.ic_tools_air
            item.showRed = !CacheUtil.getBoolean(KEY_AIR_REDPOINT_CLICKED, false)
            item.type = ToolItemModel.TYPE_AIR_LIVE
            list.add(item)
        }

        if (isControlShow(getAppControl().swtich_all.listIndex(0).typhoon)) {
            item = ToolItemModel()
            item.toolTitle = "台风路径"
            item.toolIcon = R.mipmap.ic_tools_taifeng
            item.showRed = !CacheUtil.getBoolean(KEY_TYPHOON_REDPOINT_CLICKED, false)
            item.type = ToolItemModel.TYPE_TYPHOON
            list.add(item)
        }

        item = ToolItemModel()
        item.toolTitle = "降雨趋势"
        item.toolIcon = R.mipmap.ic_tools_rain
        item.showRed = false
        item.type = ToolItemModel.TYPE_RAIN
        list.add(item)

        item = ToolItemModel()
        item.toolTitle = "空气排行榜"
        item.toolIcon = R.mipmap.ic_tools_rank
        item.showRed = false
        item.type = ToolItemModel.TYPE_AIR_RANK
        list.add(item)

//        item = ToolItemModel()
//        item.toolTitle = "15日天气"
//        item.toolIcon = R.mipmap.ic_tools_fifday
//        item.showRed = false
//        item.type = ToolItemModel.TYPE_FIFDAY_WEATHER
//        list.add(item)

        if (!getAppModel().pushClientId.value.isNullOrEmpty()) {
            item = ToolItemModel()
            item.toolTitle = "早晚天气提醒"
            item.toolIcon = R.mipmap.ic_tools_notice
            item.showRed = false
            item.type = ToolItemModel.TYPE_WEATHER_NOTICE
            list.add(item)
        }

        return list
    }

    fun checkUpDate(context: Activity) {
        callApi({ net().获取云控信息() }, object : CallResult<ControlBean>() {
            override fun ok(control: ControlBean?) {
                super.ok(control)
                Try {
                    if (control != null && control.android_software_update.nN().update2v.isNotEmpty()) {
                        Try {
                            if (control.android_software_update.nN().update2v.nN()
                                    .replace(".", "").toInt() > AppUtils.appVersionName.nN()
                                    .replace(".", "")
                                    .toInt()
                            ) {
                                FileDownloader.setup(AppContext.getContext())
                                AppUpDateDialog(
                                    context, control,
                                    true
                                ).dialogShow()
                            } else {
                                xToast("当前是最新版本")
                            }
                        }
                    } else {
                        xToast("当前是最新版本")
                    }
                }
            }

            override fun failed(code: Int, msg: String) {
                super.failed(code, msg)
                xToast(if (TextUtils.isEmpty(msg)) "网络错误" else msg)
            }
        })
    }
}