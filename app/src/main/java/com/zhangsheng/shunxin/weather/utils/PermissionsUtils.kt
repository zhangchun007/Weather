package com.zhangsheng.shunxin.weather.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.xm.xmlog.XMLogManager

/**
 * 权限工具类
 */

object PermissionsUtils {

    private val mRequestCode = 100//权限请求码
    var showSystemSetting = true//是否跳转系统设置
    private var mPermissionsResult: IPermissionsResult? = null
    private var isRequest: Boolean = false


    //请求权限后回调的方法
    //参数： requestCode  是我们自己定义的权限请求码
    //参数： permissions  是我们请求的权限名称数组
    //参数： grantResults 是我们在弹出页面后是否允许权限的标识数组，数组的长度对应的是权限名称数组的长度，数组的数据0表示允许权限，-1表示我们点击了禁止权限

    @SuppressLint("WrongConstant")
    fun onRequestPermissionsResult(
        context: Activity, requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        var noPassPermissions = ArrayList<String>()
        if (mRequestCode == requestCode) {
            permissions.forEach {
                if (Build.VERSION.SDK_INT < 23) {
                    if (PermissionChecker.checkSelfPermission(
                            context,
                            it
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        noPassPermissions.add(it)
                    } else {
                        if (it == "android.permission.READ_PHONE_STATE") {
                            XMLogManager.getInstance().onReadPhoneStatePermissionGranted()
                        }
                    }

                } else {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            it
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        noPassPermissions.add(it)
                    } else {
                        if (it == "android.permission.READ_PHONE_STATE") {
                            XMLogManager.getInstance().onReadPhoneStatePermissionGranted()
                        }
                    }
                }
            }

            if (noPassPermissions.isEmpty()) {
                mPermissionsResult?.passPermissons(isRequest, permissions)
            } else {
                mPermissionsResult?.forbitPermissons(noPassPermissions)
            }
        }
    }


    private var isPhoneSaveReport = false


    @SuppressLint("WrongConstant")
    fun onlycheck(context: Context, permissions: Array<String>, func: (checked: Boolean) -> Unit) {
        var checked = true
        if (permissions.isNotEmpty()) {
            for (i in permissions.indices)
                if (PermissionChecker.checkSelfPermission(
                        context,
                        permissions[i]
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    checked = false
                }
        }

        func(checked)
    }

    @SuppressLint("WrongConstant")
    fun onlyCheck(context: Context, permissions: String): Boolean {
        return if (Build.VERSION.SDK_INT < 23) {
            true
        } else PermissionChecker.checkSelfPermission(
            context,
            permissions
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun shouldShowRequestPermissionRationale(
        context: Activity,
        permissions: List<String>
    ): Boolean {
        permissions.forEach {
            return ActivityCompat.shouldShowRequestPermissionRationale(context, it)
        }
        return true
    }

    fun checkPermission(
        context: Activity,
        map: Map<String, Boolean>,
        func: (isAllGranted: Boolean, unPassList: List<String>, neverList: List<String>) -> Unit
    ) {
        //通过的权限
        val grantedList = map.filterValues { it }.mapNotNull { it.key }
        //是否所有权限都通过
        val allGranted = grantedList.size == map.size

        val list = (map - grantedList).map { it.key }
        //未通过的权限
        val deniedList =
            list.filter { ActivityCompat.shouldShowRequestPermissionRationale(context, it) }
        //拒绝并且点了“不再询问”权限
        val alwaysDeniedList = list - deniedList

        func(allGranted, deniedList, alwaysDeniedList)
    }


    /*
    * 检测gps是否开启
    * */
    fun checkGpsEnabled(context: Context): Boolean {
        return try {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
            val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
            val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            return gps || network
        } catch (e: Exception) {
            false
        }
    }

    @SuppressLint("WrongConstant")
    fun checkPermissions(
        context: Activity,
        necessary: Array<String>,
        permissionsResult: IPermissionsResult
    ) {
        isRequest = false

        mPermissionsResult = permissionsResult

        val mPermissionList = ArrayList<String>()

        if (Build.VERSION.SDK_INT < 23) {

            //逐个判断你要的权限是否已经通过
            for (i in necessary.indices) {
                if (PermissionChecker.checkSelfPermission(
                        context,
                        necessary[i]
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    mPermissionList.add(necessary[i])//添加还未授予的权限
                }
            }

        } else {
            //逐个判断你要的权限是否已经通过
            for (i in necessary.indices) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        necessary[i]
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    mPermissionList.add(necessary[i])//添加还未授予的权限
                }
            }
        }
        if (mPermissionList.size > 0) {//有权限没有通过，需要申请
            isRequest = true
            ActivityCompat.requestPermissions(
                context,
                mPermissionList.toTypedArray(),
                mRequestCode
            )
        } else {
            showSystemSetting = false
            //说明权限都已经通过，可以做你想做的事情去
            permissionsResult.passPermissons(isRequest, necessary)
            return
        }

    }


    /**internal var permissions = arrayOf(
    Manifest.permission.READ_CALENDAR, //读取用户日历
    Manifest.permission.WRITE_CALENDAR, //写入用户日历
    Manifest.permission.CAMERA, //相机
    Manifest.permission.READ_CONTACTS, //读取用户联系人
    Manifest.permission.WRITE_CONTACTS, //写入用户联系人
    Manifest.permission.GET_ACCOUNTS, //访问账户列表在AccountsService
    Manifest.permission.ACCESS_FINE_LOCATION, //允许访问精良位置
    Manifest.permission.ACCESS_COARSE_LOCATION, //访问callId或wifi的粗略位置
    Manifest.permission.RECORD_AUDIO, //允许录制音频
    Manifest.permission.READ_PHONE_STATE, //获取电话状态
    Manifest.permission.CALL_PHONE, //允许打电话
    Manifest.permission.READ_CALL_LOG, //允许查看电话拨打日志
    Manifest.permission.ADD_VOICEMAIL, //语音邮件
    Manifest.permission.USE_SIP, //SIP服务
    Manifest.permission.PROCESS_OUTGOING_CALLS, //修改或终止即将离任的电话
    Manifest.permission.BODY_SENSORS, //传感器
    Manifest.permission.SEND_SMS, //信息
    Manifest.permission.RECEIVE_SMS,
    Manifest.permission.READ_SMS,
    Manifest.permission.RECEIVE_WAP_PUSH, //允许一个应用程序监视传入WAP推送消息。
    Manifest.permission.RECEIVE_MMS,
    Manifest.permission.READ_EXTERNAL_STORAGE, //外部存储器读取数据
    Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
     **/

    fun onDestroy() {
        mPermissionsResult = null
    }


    interface IPermissionsResult {
        fun passPermissons(isRequst: Boolean, permissions: Array<String>)

        fun forbitPermissons(permissions: List<String>)
    }


}
