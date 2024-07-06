package com.zhangsheng.shunxin.weather.ext


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.gson.Gson
import com.maiya.baselibrary.net.NetException
import com.maiya.thirdlibrary.net.callback.CallResult
import com.maiya.encrypt.util.NativeUtils
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.net.bean.NetStatus
import com.maiya.thirdlibrary.utils.*
import com.maiya.thirdlibrary.widget.shapview.ShapeView
import com.meituan.android.walle.WalleChannelReader
import com.xm.xmlog.XMLogManager
import com.xm.xmlog.bean.XMActivityBean
import com.zhangsheng.shunxin.BuildConfig
import com.zhangsheng.shunxin.weather.activity.WebActivity
import com.zhangsheng.shunxin.weather.app.App
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.model.AppViewModel
import com.zhangsheng.shunxin.weather.net.Api
import com.zhangsheng.shunxin.weather.net.ReCodeUtils
import com.zhangsheng.shunxin.weather.net.RetrofitFactory
import com.zhangsheng.shunxin.weather.net.bean.BaseResponse1
import com.zhangsheng.shunxin.weather.net.bean.ControlBean
import com.zhangsheng.shunxin.weather.net.bean.ReBootTimesBean
import com.zhangsheng.shunxin.weather.utils.DataUtil
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/7/14 14:07
 */


/**
 * 网络模块
 */
fun net(): Api {
    return RetrofitFactory.create()
}

fun String.Base64(): String {
    return ReCodeUtils.encode(this, -1)
}

fun String.encrypt(): String {
    return NativeUtils.encrypt(this)
}

inline fun <reified T> BaseResponse1<T>.dataConvert(): T {
    if (this.ret != 200) {
        throw NetException(this.ret, this.msg)
    }
    if (this.data.nN().code != 0) {
        throw NetException(this.data.nN().code, this.data.nN().msg)
    }
    return this.data.nN().data.nN()
}

fun <M> callApi(
    func: suspend () -> M,
    callBack: CallResult<M>? = null
) {
    GlobalScope.async(Dispatchers.Main) {
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
//----------------------------------------------------------------------------------------------------------------------


/**
 * 业务模块
 */
fun clickReport(
    actentryid: String,
    materialid: String = "null",
    subactid: String = "null",
    entrytype: String = XMActivityBean.ENTRY_TYPE_ENTRY
) {
    uploadAppActive(actentryid, entrytype, "null", subactid, materialid, XMActivityBean.TYPE_CLICK)
}

fun View.ClickReport(
    actentryid: String,
    subactid: String = "null",
    entrytype: String = XMActivityBean.ENTRY_TYPE_ENTRY, func: () -> Unit
) {
    this.setSingleClickListener {
        uploadAppActive(
            actentryid,
            entrytype,
            "null",
            subactid,
            "null",
            XMActivityBean.TYPE_CLICK
        )
        func()
    }
}

fun showReport(
    actentryid: String,
    materialid: String = "null",
    subactid: String = "null",
    entrytype: String = XMActivityBean.ENTRY_TYPE_ENTRY
) {
    uploadAppActive(actentryid, entrytype, "null", subactid, materialid, XMActivityBean.TYPE_SHOW)
}

fun closeReport(
    actentryid: String,
    materialid: String = "null",
    subactid: String = "null",
    entrytype: String = XMActivityBean.ENTRY_TYPE_ENTRY
) {
    uploadAppActive(actentryid, entrytype, "null", subactid, materialid, XMActivityBean.TYPE_CLOSE)
}

fun sdkReport( actentryid: String,
               entrytype: String,
               actid: String,
               subactid: String,
               materialid: String,
               type: String){

    uploadAppActive(actentryid,entrytype,actid,subactid,materialid,type)

}


/**
 * app应用埋点
 */
fun uploadAppActive(
    actentryid: String,
    entrytype: String,
    actid: String,
    subactid: String,
    materialid: String,
    type: String
) {
    val bean = XMActivityBean.Builder()
        .setActentryid(actentryid)
        .setEntrytype(entrytype)
        .setActid(actid)
        .setSubactid(subactid)
        .setMaterialid(materialid)
        .setType(type)
        .build()
    XMLogManager.getInstance().reportActivityLog(bean)
}


inline fun skipActivity(cls: Class<*>, block: Intent.() -> Unit = {}) {

    AppContext.getContext().startActivity(Intent(AppContext.getContext(), cls).apply {
        this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        block.invoke(this)
    })
}

fun isLocationUser(): Boolean {
    return CacheUtil.getBoolean(Constant.SP_ISLOCATION_USER, true)
}

fun getAppModel(): AppViewModel {
    return App.getAppModel()
}

fun jumpWeb(url: String, title: String = "", postUrl: String = "") {
    skipActivity(WebActivity::class.java) {
        if (title.isNotEmpty()) {
            putExtra("title", title)
        }
        if (postUrl.isNotEmpty()) {
            putExtra("postUrl", postUrl)
        }
        putExtra("url", url)
    }

}

fun getAppControl(): ControlBean {
    return CacheUtil.getObj(Constant.SP_POLLING_CONTROL, ControlBean::class.java)
        .nN(getAppModel().control.value.nN())
}

fun isControlShow(control: String): Boolean {
    //1:全部用户显示2:全部用户关闭3:新用户展示4:老用户展示
    if (isControl()) return false
    return try {
        when (control) {
            "1" -> true
            "2" -> false
            "3" -> isNewUser()
            "4" -> !isNewUser()
            else -> true
        }
    } catch (e: Exception) {
        true
    }
}


fun isNewUser(): Boolean {
    return DataUtil.daysBetween(
        System.currentTimeMillis(),
        DeviceUtil.installed()
    ) <= getAppControl().new_old_days.parseInt(3)
}


fun getSplashControl(): ControlBean.AdvBootBean {
    return getAppControl().adv_boot.listIndex(0)
}

fun getPopControl(): ControlBean.AdvPopBean {
    return getAppControl().adv_pop.listIndex(0)
}

fun getOpenTimes(): Int {
    return CacheUtil.getObj(Constant.SP_REBOOT_TIME, ReBootTimesBean::class.java).nN().times.nN(1)
}

fun getSplashShowTimes(): Long {
    return CacheUtil.getLong(Constant.SP_SPLASH_SHOW_TIME, 0)
}

fun TextView.LocationEllipsis(provin: String, isLocation: Boolean) {
    if (!isLocation) {
        if (provin.isNotEmpty()) {
            this.text = provin.isStr("")
        }
    } else {
        var location = WeatherUtils.getDatas().listIndex(0).location.nN()
        this.text =
            "${location.district.isStr(location.city).ellipsis(4)} ${location.street.ellipsis(6)}"
    }
}

fun LocationEllipsis(provin: String, isLocation: Boolean): String {
    return if (!isLocation) {
        provin
    } else {
        var location = WeatherUtils.getDatas().listIndex(0).location.nN()
        "${location.district.isStr(location.city).ellipsis(4)} ${
            WeatherUtils.getDatas()
                .listIndex(0).location.nN().street.ellipsis(6)
        }"
    }
}

fun ShapeView.ellipsis(size: Int, text: String) {
    try {
        if (text.length > size) {
            var count = text.length - size
            var tv = text.replaceRange(text.length - (count + 2), text.length - 1, "...")
            this.text = tv
        } else {
            this.text = text
        }
    } catch (e: Exception) {
        this.text = text
    }

}

fun String.ellipsis(size: Int): String {
    return try {
        if (this.length > size) {
            var count = this.length - size
            var tv = this.replaceRange(this.length - (count + 2), this.length - 1, "...")
            tv
        } else {
            this
        }
    } catch (e: Exception) {
        this
    }
}

fun TextView.ellipsis(size: Int, text: String) {
    try {
        if (text.length > size) {
            var count = text.length - size
            var tv = text.replaceRange(text.length - (count + 2), text.length - 1, "...")
            this.text = tv
        } else {
            this.text = text
        }
    } catch (e: Exception) {
        this.text = text
    }

}

fun isControl(): Boolean {
    return try {
        when {
            WalleChannelReader.get(AppContext.getContext(), "checkTime")
                .parseInt(0) > 0 && System.currentTimeMillis() - getBuildTime() <= WalleChannelReader.get(
                AppContext.getContext(),
                "checkTime"
            )
                .parseInt(0) * 60 * 60 * 1000 -> true
            CacheUtil.getString(Constant.SP_CONTROL_STATE, "") == "false" -> false
            else -> WalleChannelReader.get(AppContext.getContext(), "time").parseInt(
                BuildConfig.CONTROL_TIME
            ) > 0
                    && System.currentTimeMillis() - getBuildTime() <= WalleChannelReader.get(
                AppContext.getContext(),
                "time"
            ).parseInt(
                BuildConfig.CONTROL_TIME
            ) * 60 * 60 * 1000
        }
    } catch (e: Exception) {
        false
    }
}

fun isWallpaperOpen():Boolean{
    return WalleChannelReader.get(AppContext.getContext(), "wallpaper").isStr("1")=="1"
}

fun isAdControl(): Boolean {
    return if (isControl()) {
        WalleChannelReader.get(AppContext.getContext(), "ctl_ad").parseInt(2) == 2
    } else {
        false
    }
}

fun isVideoControl(): Boolean {
    return try {
        WalleChannelReader.get(AppContext.getContext(), "videoTime")
            .parseInt(0) > 0 && System.currentTimeMillis() - getBuildTime() <= WalleChannelReader.get(
            AppContext.getContext(),
            "videoTime"
        ).parseInt(0) * 60 * 60 * 1000
    } catch (e: Exception) {
        false
    }
}


fun isAdControlShow(key: String?): Boolean {
    if (key.isNullOrEmpty()) return false
    return try {
        val b = try {
            JSONObject(Gson().toJson(getAppControl().ad_location.listIndex(0))).get(key)
                .nN("1") == "1"
        } catch (e: Exception) {
            true
        }
        !isAdControl() && b && !DataUtil.isSameDay(
            System.currentTimeMillis(),
            CacheUtil.getLong(key, 0L)
        )
    } catch (e: Exception) {
        true
    }
}


fun getBuildTime(): Long {
    return if (WalleChannelReader.get(AppContext.getContext(), "buildTime")
            .parseLong(0) > 0
    ) WalleChannelReader.get(AppContext.getContext(), "buildTime").parseLong(0)
    else DataUtil.date2Long(
        BuildConfig.BUILD_TIME,
        "yyyy-MM-dd HH:mm:ss"
    )
}


//----------------------------------------------------------------------------------------------------------------------

//========================================================================================//
/**
 * View模块
 */

fun Float.dp2Px(): Int {
    return DisplayUtil.dip2px(this)
}


fun View.setStatusPadding() {
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val p =
            this.layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(
            0,
            StatusBarUtil.getStatusBarHeight(AppContext.getContext()),
            0,
            0
        )
        this.requestLayout()
    }
}

fun crashReport(msg: String, func: () -> Unit, doSome: () -> Unit = {}) {
    try {
        func()
    } catch (e: Exception) {
//        if (Bugly.enable){
//            CrashReport.postCatchedException(Throwable(msg,e))
//        }
        doSome()
    }
}


fun Context?.openService(intent: Intent) {
    Try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this?.startForegroundService(intent)
        } else {
            this?.startService(intent)
        }
    }
}


fun Activity.isPermissionDenied(): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(
        this,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) && ActivityCompat.shouldShowRequestPermissionRationale(
        this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
}

fun isTargetSDKAbove26(context: Context?): Boolean {
    if (context != null) {
        var targetsdk = context.applicationInfo?.targetSdkVersion
        if (targetsdk != null) {
            return targetsdk > 26
        }
    }
    return true
}