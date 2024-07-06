package com.maiya.thirdlibrary.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.AppOpsManager
import android.app.Application
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Process
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import com.maiya.thirdlibrary.ext.*
import java.io.*
import java.lang.reflect.InvocationTargetException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.jvm.Throws
import kotlin.system.exitProcess

/**
 * <pre>
 * author: Blankj
 * blog  : http://blankj.com
 * time  : 2016/08/02
 * desc  : utils about app
</pre> *
 */
object AppUtils {


    class AppInfo(
        packageName: String?,
        name: String?,
        icon: Drawable?,
        packagePath: String?,
        versionName: String?,
        versionCode: Int,
        isSystem: Boolean
    ) {
        var packageName: String? = null
        var name: String? = null
        var icon: Drawable? = null
        var packagePath: String? = null
        var versionName: String? = null
        var versionCode = 0
        var system = false

        override fun toString(): String {
            return "{" +
                    "\n  pkg name: " + packageName +
                    "\n  app icon: " + icon +
                    "\n  app name: " + name +
                    "\n  app path: " + packagePath +
                    "\n  app v name: " + versionName +
                    "\n  app v code: " + versionCode +
                    "\n  is system: " + system +
                    "}"
        }

        init {
            this.name = name
            this.icon = icon
            this.packageName = packageName
            this.packagePath = packagePath
            this.versionName = versionName
            this.versionCode = versionCode
            this.system = isSystem
        }
    }


    /**
     * Install the app.
     *
     * Target APIs greater than 25 must hold
     * `<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />`
     *
     * @param filePath The path of file.
     */
    fun installApp(filePath: String) {
        installApp(getFileByPath(filePath))
    }

    /**
     * Install the app.
     *
     * Target APIs greater than 25 must hold
     * `<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />`
     *
     * @param file The file.
     */

    private fun getAppContext(): Context {
        return AppContext.getContext()
    }

    fun installApp(file: File?) {
        if (!isFileExists(file)) return
        getAppContext().startActivity(getInstallAppIntent(file, true))
    }

    /**
     * Install the app.
     *
     * Target APIs greater than 25 must hold
     * `<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />`
     *
     * @param activity    The activity.
     * @param filePath    The path of file.
     * @param requestCode If &gt;= 0, this code will be returned in
     * onActivityResult() when the activity exits.
     */
    fun installApp(activity: Activity, filePath: String, requestCode: Int) {
        installApp(
            activity,
            getFileByPath(filePath),
            requestCode
        )
    }

    /**
     * Install the app.
     *
     * Target APIs greater than 25 must hold
     * `<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />`
     *
     * @param activity    The activity.
     * @param file        The file.
     * @param requestCode If &gt;= 0, this code will be returned in
     * onActivityResult() when the activity exits.
     */
    fun installApp(
        activity: Activity,
        file: File?,
        requestCode: Int
    ) {
        if (!isFileExists(file)) return
        activity.startActivityForResult(
            getInstallAppIntent(file),
            requestCode
        )
    }

    /**
     * Uninstall the app.
     *
     * @param packageName The name of the package.
     */
    fun uninstallApp(packageName: String) {
        if (isSpace(packageName)) return
        getAppContext()
            .startActivity(getUninstallAppIntent(packageName, true))
    }

    /**
     * Uninstall the app.
     *
     * @param activity    The activity.
     * @param packageName The name of the package.
     * @param requestCode If &gt;= 0, this code will be returned in
     * onActivityResult() when the activity exits.
     */
    fun uninstallApp(
        activity: Activity,
        packageName: String,
        requestCode: Int
    ) {
        if (isSpace(packageName)) return
        activity.startActivityForResult(
            getUninstallAppIntent(packageName),
            requestCode
        )
    }

    /**
     * Return whether it is a debug application.
     *
     * @return `true`: yes<br></br>`false`: no
     */
    val isAppDebug: Boolean get() = isAppDebug(getAppContext().packageName)

    /**
     * Return whether it is a debug application.
     *
     * @param packageName The name of the package.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isAppDebug(packageName: String?): Boolean {
        return if (isSpace(packageName)) false else try {
            val pm: PackageManager = getAppContext().packageManager
            val ai = pm.getApplicationInfo(packageName.isStr(""), 0)
            ai != null && ai.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Return whether it is a system application.
     *
     * @return `true`: yes<br></br>`false`: no
     */
    val isAppSystem: Boolean
        get() = isAppSystem(getAppContext().getPackageName())

    /**
     * Return whether it is a system application.
     *
     * @param packageName The name of the package.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isAppSystem(packageName: String?): Boolean {
        return if (isSpace(packageName)) false else try {
            val pm: PackageManager = getAppContext().packageManager
            val ai = pm.getApplicationInfo(packageName.isStr(""), 0)
            ai != null && ai.flags and ApplicationInfo.FLAG_SYSTEM != 0
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Return whether application is foreground.
     *
     * Target APIs greater than 21 must hold
     * `<uses-permission android:name="android.permission.PACKAGE_USAGE_STATS" />`
     *
     * @param packageName The name of the package.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isAppForeground(packageName: String): Boolean {
        return !isSpace(packageName) && packageName == foregroundProcessName
    }

    /**
     * Return whether application is running.
     *
     * @param pkgName The name of the package.
     * @return `true`: yes<br></br>`false`: no
     */
    @SuppressLint("NewApi")
    fun isAppRunning(pkgName: String): Boolean {
        val uid: Int
        val packageManager: PackageManager =
            getAppContext().getPackageManager()
        uid = try {
            val ai =
                packageManager.getApplicationInfo(pkgName, 0) ?: return false
            ai.uid
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return false
        }
        val am =
            getAppContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (am != null) {
            val taskInfo =
                am.getRunningTasks(Int.MAX_VALUE)
            if (taskInfo != null && taskInfo.size > 0) {
                for (aInfo in taskInfo) {
                    if (pkgName == aInfo.baseActivity!!.packageName) {
                        return true
                    }
                }
            }
            val serviceInfo =
                am.getRunningServices(Int.MAX_VALUE)
            if (serviceInfo != null && serviceInfo.size > 0) {
                for (aInfo in serviceInfo) {
                    if (uid == aInfo.uid) {
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * Launch the application.
     *
     * @param packageName The name of the package.
     */
    fun launchApp(packageName: String) {
        if (isSpace(packageName)) return
        val launchAppIntent =
            getLaunchAppIntent(packageName, true)
        if (launchAppIntent == null) {
            Log.e("AppUtils", "Didn't exist launcher activity.")
            return
        }
        getAppContext().startActivity(launchAppIntent)
    }

    /**
     * Launch the application.
     *
     * @param activity    The activity.
     * @param packageName The name of the package.
     * @param requestCode If &gt;= 0, this code will be returned in
     * onActivityResult() when the activity exits.
     */
    fun launchApp(
        activity: Activity,
        packageName: String,
        requestCode: Int
    ) {
        if (isSpace(packageName)) return
        val launchAppIntent =
            getLaunchAppIntent(packageName)
        if (launchAppIntent == null) {
            Log.e("AppUtils", "Didn't exist launcher activity.")
            return
        }
        activity.startActivityForResult(launchAppIntent, requestCode)
    }
    /**
     * Relaunch the application.
     *
     * @param isKillProcess True to kill the process, false otherwise.
     */
    /**
     * Relaunch the application.
     */
    @JvmOverloads
    fun relaunchApp(isKillProcess: Boolean = false) {
        val intent =
            getLaunchAppIntent(getAppContext().packageName, true)
        if (intent == null) {
            Log.e("AppUtils", "Didn't exist launcher activity.")
            return
        }
        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        getAppContext().startActivity(intent)
        if (!isKillProcess) return
        Process.killProcess(Process.myPid())
        exitProcess(0)
    }
    /**
     * Launch the application's details settings.
     *
     * @param packageName The name of the package.
     */
    /**
     * Launch the application's details settings.
     */
    @JvmOverloads
    fun launchAppDetailsSettings(packageName: String = getAppContext().getPackageName()) {
        if (isSpace(packageName)) return
        val intent =
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        getAppContext()
            .startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }


    /**
     * Return the application's icon.
     *
     * @return the application's icon
     */
    val appIcon: Drawable?
        get() = getAppIcon(getAppContext().getPackageName())

    /**
     * Return the application's icon.
     *
     * @param packageName The name of the package.
     * @return the application's icon
     */
    fun getAppIcon(packageName: String?): Drawable? {
        return if (isSpace(packageName)) null else try {
            val pm: PackageManager = getAppContext().packageManager
            val pi = pm.getPackageInfo(packageName.isStr(""), 0)
            pi?.applicationInfo?.loadIcon(pm)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Return the application's package name.
     *
     * @return the application's package name
     */
    val appPackageName: String
        get() = getAppContext().packageName

    val getAppPackageManager: PackageManager
        get() = getAppContext().packageManager

    /**
     * Return the application's name.
     *
     * @return the application's name
     */
    val appName: String? get() = getAppName(getAppContext().packageName)

    /**
     * Return the application's name.
     *
     * @param packageName The name of the package.
     * @return the application's name
     */
    fun getAppName(packageName: String): String? {
        return if (isSpace(packageName)) "" else try {
            val pm: PackageManager = getAppContext().packageManager
            val pi = pm.getPackageInfo(packageName.isStr(""), 0)
            pi?.applicationInfo?.loadLabel(pm)?.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * Return the application's path.
     *
     * @return the application's path
     */
    val appPath: String?
        get() = getAppPath(getAppContext().getPackageName())

    /**
     * Return the application's path.
     *
     * @param packageName The name of the package.
     * @return the application's path
     */
    fun getAppPath(packageName: String): String? {
        return if (isSpace(packageName)) "" else try {
            val pm: PackageManager = getAppContext().packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            pi?.applicationInfo?.sourceDir
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * Return the application's version name.
     *
     * @return the application's version name
     */
    val appVersionName: String
        get() = getAppVersionName(getAppContext().packageName)

    /**
     * Return the application's version name.
     *
     * @param packageName The name of the package.
     * @return the application's version name
     */
    fun getAppVersionName(packageName: String): String {
        return if (isSpace(packageName)) "" else try {
            val pm: PackageManager = getAppContext().packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            pi.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * Return the application's version code.
     *
     * @return the application's version code
     */
    val appVersionCode: Int
        get() = getAppVersionCode(getAppContext().packageName)

    /**
     * Return the application's version code.
     *
     * @param packageName The name of the package.
     * @return the application's version code
     */
    fun getAppVersionCode(packageName: String): Int {
        return if (isSpace(packageName)) -1 else try {
            val pm: PackageManager = getAppContext().packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            pi?.versionCode ?: -1
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            -1
        }
    }

    /**
     * Return the application's signature.
     *
     * @return the application's signature
     */
    val appSignature: Array<Signature>?
        get() = getAppSignature(getAppContext().getPackageName())

    /**
     * Return the application's signature.
     *
     * @param packageName The name of the package.
     * @return the application's signature
     */
    fun getAppSignature(packageName: String): Array<Signature>? {
        return if (isSpace(packageName)) null else try {
            val pm: PackageManager = getAppContext().getPackageManager()
            @SuppressLint("PackageManagerGetSignatures") val pi =
                pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            pi?.signatures
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Return the application's user-ID.
     *
     * @return the application's signature for MD5 value
     */
    val appUid: Int
        get() = getAppUid(getAppContext().getPackageName())

    /**
     * Return the application's user-ID.
     *
     * @param pkgName The name of the package.
     * @return the application's signature for MD5 value
     */
    fun getAppUid(pkgName: String): Int {
        try {
            val ai: ApplicationInfo =
                getAppContext().getPackageManager().getApplicationInfo(pkgName, 0)
            if (ai != null) {
                return ai.uid
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return -1
    }


    /**
     * Return the application's information.
     *
     *  * name of package
     *  * icon
     *  * name
     *  * path of package
     *  * version name
     *  * version code
     *  * is system
     *
     *
     * @return the application's information
     */
    val appInfo: AppInfo?
        get() = getAppInfo(getAppContext().packageName)

    /**
     * Return the application's information.
     *
     *  * name of package
     *  * icon
     *  * name
     *  * path of package
     *  * version name
     *  * version code
     *  * is system
     *
     *
     * @param packageName The name of the package.
     * @return the application's information
     */
    fun getAppInfo(packageName: String): AppInfo? {
        return try {
            val pm: PackageManager = getAppContext().getPackageManager() ?: return null
            getBean(pm, pm.getPackageInfo(packageName, 0))
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Return the applications' information.
     *
     * @return the applications' information
     */
    val appsInfo: List<AppInfo>
        get() {
            val list: MutableList<AppInfo> = ArrayList()
            val pm: PackageManager = getAppContext().getPackageManager() ?: return list
            val installedPackages =
                pm.getInstalledPackages(0)
            for (pi in installedPackages) {
                val ai = getBean(pm, pi) ?: continue
                list.add(ai)
            }
            return list
        }

    /**
     * Return the application's package information.
     *
     * @return the application's package information
     */
    fun getApkInfo(apkFile: File?): AppInfo? {
        return if (apkFile == null || !apkFile.isFile || !apkFile.exists()) null else getApkInfo(
            apkFile.absolutePath
        )
    }

    /**
     * Return the application's package information.
     *
     * @return the application's package information
     */
    fun getApkInfo(apkFilePath: String): AppInfo? {
        if (isSpace(apkFilePath)) return null
        val pm: PackageManager = getAppContext().getPackageManager() ?: return null
        val pi = pm.getPackageArchiveInfo(apkFilePath, 0) ?: return null
        val appInfo = pi.applicationInfo
        appInfo.sourceDir = apkFilePath
        appInfo.publicSourceDir = apkFilePath
        return getBean(pm, pi)
    }

    private fun getBean(
        pm: PackageManager,
        pi: PackageInfo?
    ): AppInfo? {
        if (pi == null) return null
        val ai = pi.applicationInfo
        val packageName = pi.packageName
        val name = ai.loadLabel(pm).toString()
        val icon = ai.loadIcon(pm)
        val packagePath = ai.sourceDir
        val versionName = pi.versionName
        val versionCode = pi.versionCode
        val isSystem =
            ApplicationInfo.FLAG_SYSTEM and ai.flags != 0
        return AppInfo(packageName, name, icon, packagePath, versionName, versionCode, isSystem)
    }

    ///////////////////////////////////////////////////////////////////////////
// other utils methods
///////////////////////////////////////////////////////////////////////////
    private fun isFileExists(file: File?): Boolean {
        return file != null && file.exists()
    }

    private fun getFileByPath(filePath: String): File? {
        return if (isSpace(filePath)) null else File(filePath)
    }

    private fun isSpace(s: String?): Boolean {
        if (s == null) return true
        var i = 0
        val len = s.length
        while (i < len) {
            if (!Character.isWhitespace(s[i])) {
                return false
            }
            ++i
        }
        return true
    }

    private val HEX_DIGITS = charArrayOf(
        '0',
        '1',
        '2',
        '3',
        '4',
        '5',
        '6',
        '7',
        '8',
        '9',
        'A',
        'B',
        'C',
        'D',
        'E',
        'F'
    )

    private fun hashTemplate(
        data: ByteArray?,
        algorithm: String
    ): ByteArray? {
        return if (data == null || data.size <= 0) null else try {
            val md =
                MessageDigest.getInstance(algorithm)
            md.update(data)
            md.digest()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            null
        }
    }

    private fun getInstallAppIntent(file: File?): Intent {
        return getInstallAppIntent(file, false)
    }

    private fun getInstallAppIntent(
        file: File?,
        isNewTask: Boolean
    ): Intent {
        val intent =
            Intent(Intent.ACTION_VIEW)
        val data: Uri
        val type = "application/vnd.android.package-archive"
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            data = Uri.fromFile(file)
        } else {
            val authority: String =
                getAppContext().getPackageName().toString() + ".utilcode.provider"
            data = FileProvider.getUriForFile(getAppContext(), authority, file!!)
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        getAppContext().grantUriPermission(
            getAppContext().getPackageName(),
            data,
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
        intent.setDataAndType(data, type)
        return if (isNewTask) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) else intent
    }

    private fun getUninstallAppIntent(packageName: String): Intent {
        return getUninstallAppIntent(packageName, false)
    }

    private fun getUninstallAppIntent(
        packageName: String,
        isNewTask: Boolean
    ): Intent {
        val intent =
            Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:$packageName")
        return if (isNewTask) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) else intent
    }

    private fun getLaunchAppIntent(packageName: String): Intent? {
        return getLaunchAppIntent(packageName, false)
    }

    private fun getLaunchAppIntent(
        packageName: String,
        isNewTask: Boolean
    ): Intent? {
        val launcherActivity =
            getLauncherActivity(packageName)
        if (!launcherActivity.isEmpty()) {
            val intent =
                Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            val cn =
                ComponentName(packageName, launcherActivity)
            intent.component = cn
            return if (isNewTask) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) else intent
        }
        return null
    }

    private fun getLauncherActivity(pkg: String): String {
        val intent =
            Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.setPackage(pkg)
        val pm: PackageManager = getAppContext().getPackageManager()
        val info =
            pm.queryIntentActivities(intent, 0)
        val size = info.size
        if (size == 0) return ""
        for (i in 0 until size) {
            val ri = info[i]
            if (ri.activityInfo.processName == pkg) {
                return ri.activityInfo.name
            }
        }
        return info[0].activityInfo.name
    }

    // Access to usage information.
    private val foregroundProcessName: String?
        private get() {
            val am =
                getAppContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val pInfo =
                am.runningAppProcesses
            if (pInfo != null && pInfo.size > 0) {
                for (aInfo in pInfo) {
                    if (aInfo.importance
                        == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    ) {
                        return aInfo.processName
                    }
                }
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                val pm: PackageManager = getAppContext().getPackageManager()
                val intent =
                    Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                val list =
                    pm.queryIntentActivities(
                        intent,
                        PackageManager.MATCH_DEFAULT_ONLY
                    )
                Log.i("ProcessUtils", list.toString())
                if (list.size <= 0) {
                    Log.i(
                        "ProcessUtils",
                        "getForegroundProcessName: noun of access to usage information."
                    )
                    return ""
                }
                try { // Access to usage information.
                    val info =
                        pm.getApplicationInfo(getAppContext().getPackageName(), 0)
                    val aom =
                        getAppContext().getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
                    if (aom.checkOpNoThrow(
                            AppOpsManager.OPSTR_GET_USAGE_STATS,
                            info.uid,
                            info.packageName
                        ) != AppOpsManager.MODE_ALLOWED
                    ) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        getAppContext().startActivity(intent)
                    }
                    if (aom.checkOpNoThrow(
                            AppOpsManager.OPSTR_GET_USAGE_STATS,
                            info.uid,
                            info.packageName
                        ) != AppOpsManager.MODE_ALLOWED
                    ) {
                        Log.i(
                            "ProcessUtils",
                            "getForegroundProcessName: refuse to device usage stats."
                        )
                        return ""
                    }
                    val usageStatsManager = getAppContext()
                        .getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
                    var usageStatsList: List<UsageStats>? =
                        null
                    if (usageStatsManager != null) {
                        val endTime = System.currentTimeMillis()
                        val beginTime = endTime - 86400000 * 7
                        usageStatsList = usageStatsManager
                            .queryUsageStats(
                                UsageStatsManager.INTERVAL_BEST,
                                beginTime, endTime
                            )
                    }
                    if (usageStatsList == null || usageStatsList.isEmpty()) return null
                    var recentStats: UsageStats? = null
                    for (usageStats in usageStatsList) {
                        if (recentStats == null
                            || usageStats.lastTimeUsed > recentStats.lastTimeUsed
                        ) {
                            recentStats = usageStats
                        }
                    }
                    return recentStats?.packageName
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
            }
            return ""
        }

    @SuppressLint("MissingPermission")
    fun makeDeviceId(context: Context): String {
        val deviceId = StringBuilder()
        var debug = ""
        try { //IMEI（imei）
            var tm =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            var imei = tm.deviceId
            if (!imei.isEmpty()) {
                deviceId.append("imei")
                deviceId.append(imei)
                debug = "imei:$deviceId"
                return deviceId.toString()
            }
            //序列号（sn）
            @SuppressLint("MissingPermission") val sn =
                tm.simSerialNumber
            if (!sn.isEmpty()) {
                deviceId.append("sn")
                deviceId.append(sn)
                debug = "sn:$deviceId"
                return deviceId.toString()
            }
            //如果上面都没有， 则生成一个id：随机码
            deviceId.append("id").append(UUID.randomUUID().toString())
            debug = "UUID:$deviceId"
            return deviceId.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            deviceId.append("id").append(UUID.randomUUID().toString())
        } finally {
            Log.e("makeDeviceId===", debug)
        }
        return deviceId.toString()
    }

//    fun getDeviceId(context: Context): String? {
//        var deviceId: String? = ""
//        var debug = ""
//        try {
//            deviceId = CacheUtil.getString(
//                Configure.SP_DEVICE_ID, ""
//            )
//            if (deviceId.isNotEmpty()) {
//                debug = "LoadSp:$deviceId"
//                return deviceId
//            }
//            deviceId = readDeviceId()
//            if (deviceId!!.isNotEmpty()) {
//                debug = "LoadCachebefore:$deviceId"
//                CacheUtil.put(Configure.SP_DEVICE_ID, deviceId)
//                return deviceId
//            }
//            CacheUtil.put(Configure.SP_DEVICE_ID, deviceId)
//            saveDeviceId(deviceId)
//            debug = "makeId:$deviceId"
//        } catch (e: Exception) {
//            return deviceId
//        } finally {
//            Log.e("deviceId===", debug)
//        }
//        return deviceId
//    }

    @Throws(IOException::class)
    fun saveDeviceId(deviceId: String?) { // 创建目录
//获取内部存储状态
        val state = Environment.getExternalStorageState()
        //如果状态不是mounted，无法读写
        if (state != Environment.MEDIA_MOUNTED) {
            return
        }
        val sdCardDir =
            Environment.getExternalStorageDirectory().absolutePath
        val appDir = File(sdCardDir, "CaChe")
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val fileName = "xyWeather" + ".txt"
        val file = File(appDir, fileName)
        if (!file.exists()) {
            file.createNewFile()
        }
        //保存android唯一表示符
        try {
            val fw = FileWriter(file)
            fw.write(deviceId)
            fw.flush()
            fw.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    fun readDeviceId(): String? { // 创建目录
//获取内部存储状态
        val state = Environment.getExternalStorageState()
        //如果状态不是mounted，无法读写
        if (state != Environment.MEDIA_MOUNTED) {
            return null
        }
        val sdCardDir =
            Environment.getExternalStorageDirectory().absolutePath
        val appDir = File(sdCardDir, "CaChe")
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val fileName = "xyWeather" + ".txt"
        val file = File(appDir, fileName)
        if (!file.exists()) {
            file.createNewFile()
        }
        var reader: BufferedReader? = null
        var content: StringBuilder? = null
        try {
            val fr = FileReader(file)
            content = StringBuilder()
            reader = BufferedReader(fr)
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                content.append(line)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return content.toString()
    }

    fun isNotificationEnabled(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val notificationManagerCompat =
                NotificationManagerCompat.from(context)
            return notificationManagerCompat.areNotificationsEnabled()
        }
        val CHECK_OP_NO_THROW = "checkOpNoThrow"
        val OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION"
        val mAppOps =
            context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val appInfo = context.applicationInfo
        val pkg = context.applicationContext.packageName
        val uid = appInfo.uid
        var appOpsClass: Class<*>? = null
        /* Context.APP_OPS_MANAGER */try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                appOpsClass =
                    Class.forName(AppOpsManager::class.java.name)
            }
            val checkOpNoThrowMethod = appOpsClass.nN().getMethod(
                CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                String::class.java
            )
            val opPostNotificationValue =
                appOpsClass.nN().getDeclaredField(OP_POST_NOTIFICATION)
            val value = opPostNotificationValue[Int::class.java] as Int
            return checkOpNoThrowMethod.invoke(
                mAppOps,
                value,
                uid,
                pkg
            ) as Int == AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun gotoSet() {
        val intent = Intent()
        if (Build.VERSION.SDK_INT >= 26) { // android 8.0引导
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra(
                "android.provider.extra.APP_PACKAGE",
                appPackageName
            )
        } else if (Build.VERSION.SDK_INT >= 21) { // android 5.0-7.0
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("app_package", appPackageName)
            intent.putExtra("app_uid", getAppContext().applicationInfo.uid)
        } else { // 其他
            intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            intent.data = Uri.fromParts(
                "package",
                appPackageName,
                null
            )
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        getAppContext().startActivity(intent)
    }


    fun isServiceRunning(context: Context, serviceName: String): Boolean {
        val myManager: ActivityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningService: ArrayList<ActivityManager.RunningServiceInfo> =
            myManager.getRunningServices(1000) as ArrayList<ActivityManager.RunningServiceInfo>
        for (i in 0 until runningService.size) {
            if (runningService.listIndex(i).service.className == serviceName) {
                return true
            }
        }
        return false
    }

    fun getApplicationByReflect(): Application? {
        try {
            @SuppressLint("PrivateApi") val activityThread =
                Class.forName("android.app.ActivityThread")
            val thread = activityThread.getMethod("currentActivityThread").invoke(null)
            val app = activityThread.getMethod("getApplication").invoke(thread)
                ?: throw NullPointerException("u should init first")
            return app as Application
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        throw NullPointerException("u should init first")
    }


    fun getPrivateKey(context: Context){
        var result = ""
        try {
            val packageName: String = context.packageName
            val packageInfo: PackageInfo = context.packageManager.getPackageInfo(
                packageName, PackageManager.GET_SIGNATURES
            )
            val signs = packageInfo.signatures
            val sign = signs[0]
            result = sign.toCharsString().substring(0, 10)

            LogE("privateKey->$result")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        getPrivateKey(getAppContext())
    }

    fun is4Gphone(context: Context): Boolean {
        var is4G = false
        Try {
            //获得ActivityManager服务的对象
            val mActivityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            //获得MemoryInfo对象
            val memoryInfo = ActivityManager.MemoryInfo()
            //获得系统可用内存，保存在MemoryInfo对象上
            mActivityManager.getMemoryInfo(memoryInfo)
            val memSize = memoryInfo.totalMem
            //字符类型转换
            is4G = memSize.minus(4L * 1024 * 1024 * 1024) >= 0
        }
        return is4G
    }


}