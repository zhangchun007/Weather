package com.maiya.thirdlibrary.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.ResolveInfo
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.widget.Toast
import com.maiya.thirdlibrary.ext.listIndex
import com.maiya.thirdlibrary.utils.AppUtils.appInfo
import java.lang.reflect.Method


/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2019/12/3 13:45
 */
object DeviceUtil {

    fun getManufacturer(): String? {
        return Build.MANUFACTURER
    }


    fun getBrand(): String? {
        return Build.BRAND
    }

    fun getSDKVersionCode(): Int {
        return Build.VERSION.SDK_INT
    }

    fun getSDKVersionName(): String? {
        return Build.VERSION.RELEASE
    }

    fun getModel(): String? {
        var model = Build.MODEL
        return model
    }

    /**
     * 判断是否包含SIM卡
     *
     * @return 状态
     */
    fun ishasSimCard(context: Context): Boolean {
        val telMgr =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simState = telMgr.simState
        var result = true
        when (simState) {
            TelephonyManager.SIM_STATE_ABSENT, TelephonyManager.SIM_STATE_UNKNOWN -> result =
                false // 没有SIM卡
        }
        return result
    }


    fun getNetWorkType(context: Context): String { //结果返回值
        var netType = ""
        //获取手机所有连接管理对象
        val manager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //获取NetworkInfo对象
        val networkInfo = manager.activeNetworkInfo ?: return netType
        //NetworkInfo对象为空 则代表没有网络
        //否则 NetworkInfo对象不为空 则获取该networkInfo的类型
        val nType = networkInfo.type
        if (nType == ConnectivityManager.TYPE_WIFI) { //WIFI
            netType = "wifi"
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            val nSubType = networkInfo.subtype
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            //3G   联通的3G为UMTS或HSDPA 电信的3G为EVDO
            netType = if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                && !telephonyManager.isNetworkRoaming
            ) {
                "3g"
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA || (nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                        && !telephonyManager.isNetworkRoaming)
            ) {
                "3g"
                //2G 移动和联通的2G为GPRS或EGDE，电信的2G为CDMA
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS || nSubType == TelephonyManager.NETWORK_TYPE_EDGE || (nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                        && !telephonyManager.isNetworkRoaming)
            ) {
                "2g"
            } else {
                "other"
            }
        }
        return netType
    }


    /**
     * 获取设备拨号运营商
     *
     * @return ["中国电信CTCC":3]["中国联通CUCC:2]["中国移动CMCC":1]["other":0]["无sim卡":-1]
     */
    fun getSubscriptionOperatorType(): Int {
        var opeType = 0
        // No sim
        if (!hasSim()) {
            return opeType
        }
        val tm =
            AppContext.getContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val operator = tm.networkOperator
        // 中国联通
        opeType = if ("46001" == operator || "46006" == operator || "46009" == operator) {
            3
            // 中国移动
        } else if ("46000" == operator || "46002" == operator || "46004" == operator || "46007" == operator) {
            1
            // 中国电信
        } else if ("46003" == operator || "46005" == operator || "46011" == operator) {
            2
        } else {
            0
        }
        return opeType
    }


    /**
     * 判断数据流量开关是否打开
     *
     * @param context
     * @return
     */
    fun isMobileDataEnabled(context: Context): Boolean {
        return try {
            val method: Method =
                ConnectivityManager::class.java.getDeclaredMethod("getMobileDataEnabled")
            method.setAccessible(true)
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            method.invoke(connectivityManager) as Boolean
        } catch (t: Throwable) {
            false
        }
    }

    /**
     * 检查手机是否有sim卡
     */
    fun hasSim(): Boolean {
        return return if (Build.VERSION.SDK_INT == 29) {
            val tm =
                AppContext.getContext()
                    .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            tm.simState == TelephonyManager.SIM_STATE_READY
        } else {
            val tm =
                AppContext.getContext()
                    .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val operator = tm.simOperator
            !TextUtils.isEmpty(operator)
        }

    }

    /**
     * 获取apk安装时间
     */
    fun installed(): Long {
        return AppContext.getContext().packageManager.getPackageInfo(
            AppUtils.appPackageName,
            0
        ).firstInstallTime
    }

    /**
     * 获取apk 更新时间
     */
    fun updateTime(): Long {
        return AppContext.getContext().packageManager.getPackageInfo(
            AppUtils.appPackageName,
            0
        ).lastUpdateTime
    }


    /**
     * 检查imei号是否正确
     */
    fun checkIme(ime: String): Boolean {
        if (TextUtils.isEmpty(ime) || "null" == ime || ime.length < 3) {
            return false
        }
        var count = 0
        for (i in 0 until ime.length - 1) {
            if (ime[i] == ime[i + 1]) {
                count++
            }
        }
        return count != ime.length - 1
    }

    fun gpsIsOPen(context: Context): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        val gps: Boolean = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        val network: Boolean =
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return gps || network
    }



    fun isGpsEnabled(context: Context): Boolean {
        val locationManager = context
            .getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val accessibleProviders =
            locationManager.getProviders(true)
        return accessibleProviders != null && accessibleProviders.size > 0
    }

    /**
     * 跳转应用市场
     */

    fun launchAppDetail(
        context: Context,
        appPkg: String,
        marketPkg: String = ""
    ) {
        try {
            if (TextUtils.isEmpty(appPkg)) return
            val uri: Uri = Uri.parse("market://details?id=$appPkg")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            if (marketPkg.isNotEmpty()) {
                intent.setPackage(marketPkg)
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 第一种方法
     * @param context
     * @param packageName
     */
    fun goToMarket(context: Context, packageName: String) {
        try {
            val uri = Uri.parse("market://details?id=$packageName")
            val goToMarket =
                Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            Toast.makeText(context, "您的手机没有安装Android应用市场", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 第二种方法
     * 直接跳转到应用宝
     * @param context
     * @param packageName
     */
    fun goToMarketQQ(
        context: Context,
        packageName: String
    ) {
        try {
            val uri = Uri.parse("market://details?id=$packageName")
            val goToMarket =
                Intent(Intent.ACTION_VIEW, uri)
            goToMarket.setClassName(
                "com.tencent.android.qqdownloader",
                "com.tencent.pangu.link.LinkProxyActivity"
            )
            context.startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            Toast.makeText(context, "您的手机没有安装应用宝", Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * 第三种方法
     * 首先先获取手机上已经安装的应用市场
     * 获取已安装应用商店的包名列表
     * 获取有在AndroidManifest 里面注册<category android:name="android.intent.category.APP_MARKET"></category>的app
     * @param context
     * @return
     */
    fun getInstallAppMarkets(context: Context?): ArrayList<String>? {
        //默认的应用市场列表，有些应用市场没有设置APP_MARKET通过隐式搜索不到
        val pkgList: ArrayList<String> = ArrayList()
        //将我们上传的应用市场都传上去
        pkgList.add("com.xiaomi.market") //小米应用商店
        pkgList.add("com.lenovo.leos.appstore") //联想应用商店
        pkgList.add("com.oppo.market") //OPPO应用商店
        pkgList.add("com.tencent.android.qqdownloader") //腾讯应用宝
        pkgList.add("com.qihoo.appstore") //360手机助手
        pkgList.add("com.baidu.appsearch") //百度手机助手
        pkgList.add("com.huawei.appmarket") //华为应用商店
        pkgList.add("com.wandoujia.phoenix2") //豌豆荚
        pkgList.add("com.hiapk.marketpho") //安智应用商店
        val pkgs = ArrayList<String>()
        if (context == null) return pkgs
        val intent = Intent()
        intent.action = "android.intent.action.MAIN"
        intent.addCategory("android.intent.category.APP_MARKET")
        val pm = context.packageManager
        val info: List<ResolveInfo>? = pm.queryIntentActivities(intent, 0)
        if (info == null || info.isEmpty()) return pkgs
        val size = info.size
        for (i in 0 until size) {
            var pkgName = ""
            try {
                val activityInfo: ActivityInfo = info[i].activityInfo
                pkgName = activityInfo.packageName
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            if (!TextUtils.isEmpty(pkgName)) pkgs.add(pkgName)
        }
        //取两个list并集,去除重复
        pkgList.removeAll(pkgs)
        pkgs.addAll(pkgList)
        return pkgs
    }

    /**
     * 获取当前手机上的应用商店数量
     *
     * @param context
     * @return
     */
    fun getInstalledMarketPkgs(context: Context?): ArrayList<String> {
        val pkgs: ArrayList<String> = ArrayList()
        if (context == null) return pkgs
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("market://details?id=")
        val pm = context.packageManager
        // 通过queryIntentActivities获取ResolveInfo对象
        val infos = pm.queryIntentActivities(
            intent,
            0
        )
        if (infos.size == 0) return pkgs
        val size = infos.size
        for (i in 0 until size) {
            var pkgName = ""
            try {
                val activityInfo = infos[i].activityInfo
                pkgName = activityInfo.packageName
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            if (!TextUtils.isEmpty(pkgName) && pkgName != "com.android.vending") pkgs.add(pkgName)
        }
        return pkgs
    }


    /**
     * 获取当前手机上的应用商店数量
     *
     * @param context
     * @return
     */
    private var onePks = arrayListOf(
        "com.xiaomi.market",
        "com.lenovo.leos.appstore",
        "com.oppo.market",
        "com.bbk.appstore",
        "com.meizu.mstore",
        "zte.com.market",
        "com.huawei.appmarket"
    )
    private var twoPks = arrayListOf(
        "com.tencent.android.qqdownloader",
        "com.qihoo.appstore",
        "com.wandoujia.phoenix2",
        "com.baidu.appsearch",
        "com.pp.assistant"
    )

    fun getInstalledMarketPkgs(context: Context): String {
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("market://details?id=")
        val pm = context.packageManager
        // 通过queryIntentActivities获取ResolveInfo对象
        val infos = pm.queryIntentActivities(intent, 0)
        if (infos.size == 0) return ""
        return try {
            var pks = infos.filter { onePks.contains(it.activityInfo.packageName) }
            if (pks.isEmpty()) {
                pks = infos.filter { twoPks.contains(it.activityInfo.packageName) }
            }
            pks.listIndex(0).activityInfo.packageName
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 过滤出已经安装的包名集合
     * @param context
     * @param pkgs  待过滤包名集合
     * @return      已安装的包名集合
     */
    fun getFilterInstallMarkets(
        context: Context?,
        pkgs: ArrayList<String>?
    ): ArrayList<String?>? {
        val mAppInfo: MutableList<AppUtils.AppInfo?> =
            ArrayList()
        mAppInfo.clear()
        val appList: ArrayList<String?> = ArrayList()
        if (context == null || pkgs == null || pkgs.size == 0) return appList
        val pm = context.packageManager
        val installedPkgs: List<PackageInfo> = pm.getInstalledPackages(0)
        val li = installedPkgs.size
        val lj: Int = pkgs.size
        for (j in 0 until lj) {
            for (i in 0 until li) {
                var installPkg = ""
                val checkPkg = pkgs[j]
                val packageInfo: PackageInfo = installedPkgs[i]
                try {
                    installPkg = packageInfo.packageName
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
                if (TextUtils.isEmpty(installPkg)) continue
                if (installPkg == checkPkg) {
                    // 如果非系统应用，则添加至appList,这个会过滤掉系统的应用商店，如果不需要过滤就不用这个判断
                    if (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM === 0) {
                        //将应用相关信息缓存起来，用于自定义弹出应用列表信息相关用
                        val appInfo = appInfo
                        appInfo!!.name =
                            packageInfo.applicationInfo.loadLabel(context.packageManager)
                                .toString()
                        appInfo.icon =
                            packageInfo.applicationInfo.loadIcon(context.packageManager)
                        appInfo.packageName = packageInfo.packageName
                        appInfo.versionCode = packageInfo.versionCode
                        appInfo.versionName = packageInfo.versionName
                        mAppInfo.add(appInfo)
                        appList.add(installPkg)
                    }
                    break
                }
            }
        }
        return appList
    }


    /**
     * 获取已安装应用商店的包名列表
     * @param context       context
     * @return
     */
    fun queryInstalledMarketPkgs(context: Context?): ArrayList<String> {
        val pkgs: ArrayList<String> = ArrayList()
        if (context == null) return pkgs
        val intent = Intent()
        intent.action = "android.intent.action.MAIN"
        intent.addCategory("android.intent.category.APP_MARKET")
        val pm = context.packageManager
        val infos: List<ResolveInfo>? = pm.queryIntentActivities(intent, 0)
        if (infos == null || infos.isEmpty()) return pkgs
        val size = infos.size
        for (i in 0 until size) {
            var pkgName = ""
            try {
                val activityInfo: ActivityInfo = infos[i].activityInfo
                pkgName = activityInfo.packageName
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            if (!TextUtils.isEmpty(pkgName)) pkgs.add(pkgName)
        }
        return pkgs
    }

    /**
     * 过滤出已经安装的包名集合
     * @param context
     * @param pkgs 待过滤包名集合
     * @return 已安装的包名集合
     */
    fun filterInstalledPkgs(
        context: Context?,
        pkgs: ArrayList<String>?
    ): ArrayList<String?>? {
        val empty: ArrayList<String?> = ArrayList()
        if (context == null || pkgs == null || pkgs.size === 0) return empty
        val pm = context.packageManager
        val installedPkgs: List<PackageInfo> = pm.getInstalledPackages(0)
        val li = installedPkgs.size
        val lj: Int = pkgs.size
        for (j in 0 until lj) {
            for (i in 0 until li) {
                var installPkg = ""
                val checkPkg = pkgs[j]
                try {
                    installPkg = installedPkgs[i].applicationInfo.packageName
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
                if (TextUtils.isEmpty(installPkg)) continue
                if (installPkg == checkPkg) {
                    empty.add(installPkg)
                    break
                }
            }
        }
        return empty
    }

    /**
     * 检查wifi是否可用
     */
    private fun checkWifiIsEnable(): Boolean {
        val wifiManager: WifiManager =
          AppContext.getContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.isWifiEnabled
    }

    fun networkConnected(context: Context?): Boolean {
        val cm =
            context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT < 23) {
            val mWiFiNetworkInfo = cm.activeNetworkInfo
            if (mWiFiNetworkInfo != null) {
                if (mWiFiNetworkInfo.type === ConnectivityManager.TYPE_WIFI) { //WIFI
                    return true
                } else if (mWiFiNetworkInfo.type === ConnectivityManager.TYPE_MOBILE) { //移动数据
                    return true
                }
            }
        } else {
            val network = cm.activeNetwork
            if (network != null) {
                val nc = cm.getNetworkCapabilities(network)
                if (nc != null) {
                    if (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) { //WIFI
                        return true
                    } else if (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) { //移动数据
                        return true
                    }
                }
            }
        }
        return false
    }


    fun isScreenPortrait():Boolean{
        return try {
            val mConfiguration= AppContext.getContext().resources.configuration
            mConfiguration.orientation== ORIENTATION_PORTRAIT
        }catch (e:Exception){
            true
        }
    }



}