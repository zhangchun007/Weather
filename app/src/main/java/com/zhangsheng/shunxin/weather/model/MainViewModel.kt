package com.zhangsheng.shunxin.weather.model


import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.text.format.DateUtils
import androidx.fragment.app.Fragment
import com.liulishuo.filedownloader.FileDownloader
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.listIndex
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.ext.parseInt
import com.maiya.thirdlibrary.utils.*
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.bottom.BottomBarItem
import com.zhangsheng.shunxin.weather.bottom.BottomItemManage
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.dialog.AppStorageDialog
import com.zhangsheng.shunxin.weather.dialog.AppUpDateDialog
import com.zhangsheng.shunxin.weather.dialog.PushAlertDialog
import com.zhangsheng.shunxin.weather.ext.clickReport
import com.zhangsheng.shunxin.weather.ext.getAppControl
import com.zhangsheng.shunxin.weather.ext.isPermissionDenied
import com.zhangsheng.shunxin.weather.fragment.CalendarFragment
import com.zhangsheng.shunxin.weather.fragment.NewsFragment
import com.zhangsheng.shunxin.weather.fragment.SettingFragment
import com.zhangsheng.shunxin.weather.fragment.WeatherFragment
import com.zhangsheng.shunxin.weather.net.bean.ControlBean
import com.zhangsheng.shunxin.weather.utils.DataUtil
import com.zhangsheng.shunxin.weather.utils.NotificationsUtils
import com.zhangsheng.shunxin.weather.utils.PermissionsUtils
import com.zhangsheng.shunxin.weather.utils.WeatherUtils


/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/7/14 21:15
 */
class MainViewModel : BaseViewModel() {

    val START_SAVE_PERMISSION_SETTING = 0x30


    /**
     * 说明：首页fragment集合
     */
    private var mFragments: ArrayList<Fragment>? = null

    /**
     * 首页fragment对应tag集合
     */
    private var fragmentTags: Array<String>? = null

    /**
     * 首页底部tab集合
     */
    private var bottomItems: List<BottomBarItem>? = null

    /**
     * 说明：初始化底部tab配置
     * 作者：刘鹏
     * 添加时间：2021/4/29 16:18
     * 修改人：liupe
     * 修改时间：2021/4/29 16:18
     */
    private fun initBottomItems() {
        bottomItems = BottomItemManage.instance.initBottomBarList()
    }

    /**
     * 说明：获取首页fragment集合
     * 作者：刘鹏
     * 添加时间：2021/4/29 16:19
     * 修改人：liupe
     * 修改时间：2021/4/29 16:19
     */
    fun getFragments(): ArrayList<Fragment> {
        if (mFragments == null) {
            mFragments = arrayListOf()
            if (bottomItems == null) {
                initBottomItems()
            }
            for (item in bottomItems!!) {
                mFragments!!.add(item.createFragment())
            }
        }
        return mFragments!!
    }

    fun getCurrentFragmentTags(): Array<String> {
        if (fragmentTags == null) {
            initFragmentTags()
        }

        return fragmentTags!!
    }

    fun getBottomItems(): List<BottomBarItem> {
        if (bottomItems == null) {
            initBottomItems()
        }
        return bottomItems!!
    }

    /**
     * 说明：重置首页底部tab
     * 作者：刘鹏
     * 添加时间：2021/4/29 16:19
     * 修改人：liupe
     * 修改时间：2021/4/29 16:19
     */
    fun resetBottomItems(list: List<BottomBarItem>) {
        mFragments?.clear()
        fragmentTags = null
        mFragments = null
        bottomItems = list
        initFragmentTags()
    }

    /**
     * 说明：判断首页底部tab是否被改变
     * 作者：刘鹏
     * 添加时间：2021/4/29 16:19
     * 修改人：liupe
     * 修改时间：2021/4/29 16:19
     */
    fun bottombarHasChange(list: List<BottomBarItem>?): Boolean {
        if (bottomItems != null && list != null) {
            if (bottomItems.nN().size != list.size) {
                return true
            }
            for (i in bottomItems.nN().indices) {
                if (!TextUtils.equals(bottomItems.nN()[i].getEventCMD(), list[i].getEventCMD())) {
                    return true
                }
            }
            return false
        } else {
            return bottomItems != list && list != null
        }
    }

    /**
     * 说明：根据cmd获取对应的tab项
     * 作者：刘鹏
     * 添加时间：2021/4/29 16:19
     * 修改人：liupe
     * 修改时间：2021/4/29 16:19
     */
    fun getBottomItemByCMD(cmd: String?): BottomBarItem? {
        bottomItems?.forEach {
            if (cmd == it.getEventCMD()) {
                return it
            }
        }
        return null
    }

    fun getCMDByPosition(position: Int): String? {
        if (fragmentTags != null && position >= 0 && position < fragmentTags!!.size) {
            return fragmentTags?.get(position)
        }
        return null
    }

    private fun initFragmentTags() {
        val list: ArrayList<String> = arrayListOf()
        if (bottomItems == null) {
            initBottomItems()
        }
        for (item in bottomItems!!) {
            list.add(item.getEventCMD().nN())
        }
        fragmentTags = list.toTypedArray()
    }


    /*
    * 首页弹窗弹出时机校验
    * */
    fun locationPermission(isLocation: Boolean): Boolean {

        /*
        * 第一次安装 return  不弹出
        * */
        val installTime = CacheUtil.getLong(Constant.SP_INSTALL_TIME)
        if (installTime == 0L) {
            return false
        }

        val showTime = CacheUtil.getLong(Constant.SP_WINDOW_SHOW_TIME, 0) //上一次弹出的时间

        val nowTime = System.currentTimeMillis() // 当前时间

        if (DataUtil.isSameDay(showTime, nowTime)) {
            return false
        }

        /*
        * 有定位城市
        * */
        if (isLocation) {
            CacheUtil.put(Constant.SP_WINDOW_SHOW_TIME, System.currentTimeMillis())
            CacheUtil.remove("numberOfPopups")
            return true
        }

        /*
        * 没有定位城市
        * */

        var numberOfPopups = CacheUtil.getInt("numberOfPopups", 0) // 本地的弹窗记录次数

        val locationPopBean = getAppControl().local_pop.listIndex(0) // 云控定位弹窗的对象

        val times = locationPopBean.times?.toInt() ?: 3  //弹窗次数

        val mday = locationPopBean.mday?.toInt() ?: 3   // 第几天弹窗

        val days = locationPopBean.days?.toInt() ?: 10  // 间隔几天弹窗

        if (numberOfPopups >= times) {
            return false
        }

        if (numberOfPopups == 0) {
            val timeDifference = DataUtil.daysBetween(nowTime, installTime)
            if (timeDifference >= mday) {
                showPopup(numberOfPopups)
                return true
            }
            return false
        }

        val timeDifference = DataUtil.daysBetween(nowTime, showTime)
        if (timeDifference >= days) {
            showPopup(numberOfPopups)
            return true
        }

        return false
    }

    private fun showPopup(numberOfPopups: Int) {
        var numberOfPopups1 = numberOfPopups
        numberOfPopups1 += 1
        CacheUtil.put(Constant.SP_WINDOW_SHOW_TIME, System.currentTimeMillis())
        CacheUtil.put("numberOfPopups", numberOfPopups1)
    }

    fun checkAllPermissionReport(context: Context) {
        if (!DataUtil.isSameDay(
                System.currentTimeMillis(),
                CacheUtil.getLong(Constant.SP_CHECK_ALL_PERMISSION, 0L)
            )
        ) {
            CacheUtil.put(Constant.SP_CHECK_ALL_PERMISSION, System.currentTimeMillis())
            PermissionsUtils.onlycheck(context, Constant.CHECK_LOCATION_PERMISSIONS) {
                if (!it) {
                    clickReport(EnumType.上报埋点.缺少定位权限上报)
                }
            }

            PermissionsUtils.onlycheck(context, Constant.CHECK_PHONE_STATE_PERMISSIONS) {
                if (!it) {
                    clickReport(EnumType.上报埋点.缺少电话权限上报)
                }
            }
            PermissionsUtils.onlycheck(context, Constant.CHECK_SAVE_PERMISSIONS) {
                if (!it) {
                    clickReport(EnumType.上报埋点.缺少存储权限上报)
                }
            }
        }
    }

    fun checkUpDate(context: Activity, control: ControlBean?) {
        if (null == control) return
        if (control.android_software_update.nN().update2v.isNotEmpty() && control.android_software_update.nN().update_type.parseInt() != 3 && control.android_software_update.nN().update2v.nN()
                .replace(".", "").toInt() > AppUtils.appVersionName.nN().replace(".", "")
                .toInt()
        ) {
            Try {
                when {
                    control.android_software_update.nN().update_type.parseInt() == 2 -> {//强更
                        FileDownloader.setup(AppContext.getContext())
                        AppUpDateDialog(context, control).forceShow()
                    }

                    System.currentTimeMillis() - CacheUtil.getLong(Constant.SP_UPDATE_TIME) > control.android_software_update.nN().remind_days.parseInt(
                        1
                    ) * 24 * 60 * 60 * 1000 -> {//软更
                        FileDownloader.setup(AppContext.getContext())
                        AppUpDateDialog(context, control).dialogShow()
                    }
                }
            }
        }
    }

    fun checkStorage(context: Activity, checkPermission: () -> Unit) {
        //存储权限挽留弹窗
        PermissionsUtils.onlycheck(AppContext.getContext(), Constant.CHECK_SAVE_PERMISSIONS) {
            if (!it) {
                if (WeatherUtils.initData().size > 0 && applyStoragePermission()) {

                    AppStorageDialog(context) { isAgree ->
                        if (isAgree) {
                            checkPermission()
                        }
                    }.dialogShow()
                }
            }
        }
    }

    /**
     * 文件读写权限申请
     */
    private fun applyStoragePermission(): Boolean {
        //云控没有不展示
        if (getAppControl().save_pop.isNullOrEmpty()) return false
        //判断云控展示机型
        val savePopBean = getAppControl().save_pop.listIndex(0)
        if (TextUtils.equals("2", savePopBean.brand)) {
            if (!OSUtils.isEmui()) return false
        }
        //当天展示过不展示
        val showTime = CacheUtil.getLong(Constant.SP_STROGE_WINDOW_SHOW_TIME) //上一次弹出的时间
        val nowTime = System.currentTimeMillis() // 当前时间
        if (DateUtils.isToday(showTime)) return false

        var numberOfPopups = CacheUtil.getInt(Constant.SP_STROGE_WINDOW_SHOW_NUMBERS, 0)
        //总限制弹窗次数
        if (numberOfPopups >= savePopBean.times.toInt()) return false
        //安装后第几天弹
        val mday = savePopBean.make_start_days.toInt()    // 第几天弹窗
        val installTime = DataUtil.daysBetween(nowTime, DeviceUtil.installed())
        if (installTime < mday) return false
        //间隔几天弹
        val days = savePopBean.days.toInt()   // 间隔几天弹窗


        val timeDifference = DataUtil.daysBetween(nowTime, showTime)
        if (timeDifference < days) return false

        return true
    }

    fun checkPushService(context: Activity) {
        if (!CacheUtil.getBoolean(
                Constant.SP_PUSH_ALERT,
                false
            ) && !NotificationsUtils.openNotificationChannel(context)
        ) {
            PushAlertDialog(context) {
                NotificationsUtils.openSetting(context)
            }.show()
        }
    }


    /**
     * 当前是否是心晴天气
     */
     fun isXinQingWeaher(activity: Activity): Boolean {
        if (activity == null) return false
        if ("心晴天气".equals(activity.resources.getText(R.string.app_name)))
            return true
        return false

    }

}