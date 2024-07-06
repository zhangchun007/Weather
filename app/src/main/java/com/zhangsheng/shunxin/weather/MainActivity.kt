package com.zhangsheng.shunxin.weather

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import com.maiya.thirdlibrary.base.AacActivity
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.utils.*
import com.xinmeng.xm.XMMarker
import com.xyz.sdk.e.keeplive.KeepLive
import com.zhangsheng.shunxin.databinding.ActivityMainBinding
import com.zhangsheng.shunxin.information.utils.NetworkUtil
import com.zhangsheng.shunxin.weather.bottom.BottomBarItem
import com.zhangsheng.shunxin.weather.bottom.BottomItemManage
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.db.AppDatabase
import com.zhangsheng.shunxin.weather.db.city_db.InnerJoinResult
import com.zhangsheng.shunxin.weather.dialog.RequestLocationPermissionDialog
import com.zhangsheng.shunxin.weather.ext.*
import com.zhangsheng.shunxin.weather.fragment.WeatherFragment
import com.zhangsheng.shunxin.weather.livedata.LiveDataBus
import com.zhangsheng.shunxin.weather.model.MainViewModel
import com.zhangsheng.shunxin.weather.net.bean.Location
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.page.WeatherLocationLoadingPage
import com.zhangsheng.shunxin.weather.utils.*
import com.zhangsheng.shunxin.weather.widget.BottomTabLayout
import org.koin.android.ext.android.inject

/**
 * 说明：首页activity
 * 作者：刘鹏
 * 添加时间：2021/4/28 16:49
 * 修改人：liupe
 * 修改时间：2021/4/28 16:49
 */
class MainActivity : AacActivity<MainViewModel>() {
    override val vm: MainViewModel by inject()
    override val binding by inflate<ActivityMainBinding>()
    private var firstBackTime: Long = 0L
    private var locationShow = false

    // 当前选择的tab对应的key
    private var currentItemCMD: String? = null


    override fun setContentView(view: View?) {
        super.setContentView(view)
        StatusBarUtil.setStatusBarDarkTheme(this, false)
    }

    override fun initView(savedInstanceState: Bundle?) {
        initBottomTab(0)
        if (!isControl()) {
            KeepLive.init2(this, null)
            XMMarker.marker4()
        }

        if (!NotificationsUtils.openNotificationChannel(this) &&
            !DataUtil.isTodayBoolean(CacheUtil.getLong(Constant.SP_REPORT_LACK_NOTIFICATION))
        ) {
            clickReport(EnumType.上报埋点.缺少通知权限上报)
            CacheUtil.put(Constant.SP_REPORT_LACK_NOTIFICATION, System.currentTimeMillis())
        }
    }

    /**
     * 说明：判断底部tab是否改变，如果过了审核期，云控数据下发后需要实时变更
     * 作者：刘鹏
     * 添加时间：2021/4/28 16:46
     * 修改人：liupe
     * 修改时间：2021/4/28 16:46
     */
    private fun checkBottomChange() {
        if (isControl()) {
            // 是否审核期，审核期不随云控改变
            return
        }
        // 获取最新的底部tab数据
        val list = BottomItemManage.instance.initBottomBarList()
        if (vm.bottombarHasChange(list)) {
            // 最新的tab数据和当前展示的数据对比，已经有改变，需要重新设置底部tab和fragment
            // 重新设置底部tab
            vm.resetBottomItems(list)
            // 根据之前展示的tab，获取最新的item
            val item = vm.getBottomItemByCMD(currentItemCMD)
            var position = 0
            if (item != null) {
                // 判断底部tab刷新后，应该展示哪个tab，如果之前展示的tab已经消失，则默认展示第一个tab
                position = item.getPosition()
            }
            // 移除原来的fragment
            FragmentUtils.removeAll(supportFragmentManager)
            // 重新初始化底部tab
            initBottomTab(position)
        }
    }

    /**
     * 说明：初始化底部tab
     * 作者：刘鹏
     * 添加时间：5/8/21 4:15 PM
     * 修改人：liupe
     * 修改时间：5/8/21 4:15 PM
     */
    private fun initBottomTab(displayPos: Int) {
        // 添加底部tab对应的fragment
        FragmentUtils.add(
            supportFragmentManager,
            vm.getFragments(),
            binding.fragmentContainer.id,
            vm.getCurrentFragmentTags(),
            displayPos
        )
        // 刷新底部tabview
        binding.tabBottomBar.resetItems(vm.getBottomItems())
        // 保存当前对应的tab
        currentItemCMD = vm.getCMDByPosition(displayPos)
        // 设置默认选中的tab
        binding.tabBottomBar.setSelectedItemView(displayPos)
    }

    /**
     * 说明：选中底部对应的tab
     * 作者：刘鹏
     * 添加时间：5/6/21 4:19 PM
     * 修改人：liupe
     * 修改时间：5/6/21 4:19 PM
     */
    private fun changeTab(item: BottomBarItem) {
        if (vm.isXinQingWeaher(MainActivity@ this)) {
            if (item.getPosition() != 0) {
                StatusBarUtil.setStatusBarDarkTheme(this@MainActivity, true)
            } else {
                getFragment()?.setStatusBarDarkTheme()
            }
        } else {
            StatusBarUtil.setStatusBarDarkTheme(this@MainActivity, item.getPosition() != 0)
        }
        FragmentUtils.showHide(item.getPosition(), vm.getFragments())
        binding.tabBottomBar.setSelectedItemView(item.getEventCMD())
        currentItemCMD = item.getEventCMD()
    }

    override fun initListener() {
        super.initListener()
        binding.tabBottomBar.setOnItemClickListener(object : BottomTabLayout.OnItemClickListener {
            override fun onClick(cmd: String?, v: View?, position: Int) {
                if (v?.tag is BottomBarItem) {
                    val item = v.tag as BottomBarItem
                    changeTab(item)
                }
            }
        })
        Try {
            val from = intent?.getStringExtra("from")
            when (from) {
                "refresh" -> clickReport(EnumType.上报埋点.常驻通知栏首页点击)
            }
        }
    }

    override fun initObserve() {
        super.initObserve()
        getAppModel().currentWeather.safeObserve(this, Observer {
            if (!it.weather?.tc.isNullOrEmpty()) {
                vm.checkPushService(this@MainActivity)
            }
        })

        //刷新定位成功后的触发
        LiveDataBus.getChannel<Location>(LocationUtil.KEY_MAIN_LOCATION)
            .safeObserve(this, Observer { location ->
                LocationUtil.isFirstLocation = true
                showLocationPageState(location.state == LocationUtil.定位成功) {
                    if (location.state == LocationUtil.定位成功) {
                        val str =
                            location.nN().district.isStr(location.nN().city.isStr(location.nN().province))
                        searchCity(str) { _, dbBean ->
                            val weather = (if (WeatherUtils.initData()
                                    .listIndex(0).isLocation
                            ) WeatherUtils.initData().listIndex(0) else WeatherBean()).apply {
                                this.regioncode = dbBean?.code.isStr("")
                                this.isLocation = true
                                this.location = location
                            }
                            getAppModel().requestWeather(weather, 0, true)
//                            getAppModel().initScreenLock(Constant.WALLPAPER_START)
                        }
                    } else {
                        when {
                            WeatherUtils.getDatas().listIndex(0).isLocation -> {
//                                getAppModel().initScreenLock(Constant.WALLPAPER_START)
                                getAppModel().requestWeather(
                                    WeatherUtils.getDatas().listIndex(0).apply {
                                        this.location.nN().state = location.state
                                    }, 0
                                )
                            }
                            WeatherUtils.initData().listIndex(0).tc.isEmpty() -> {
                                (vm.getFragments()
                                    .listIndex(0) as WeatherFragment).startCitySelect()
                                if (!CacheUtil.getBoolean(
                                        Constant.SP_FIRST_LOCATION_COMPLETE,
                                        false
                                    ) && CacheUtil.getBoolean(
                                        Constant.SP_LOCATION_PERMISSION_GET,
                                        false
                                    )
                                ) {
                                    // 如果之前没有完成过定位流程，且有定位权限
                                    when {
                                        !PermissionsUtils.checkGpsEnabled(instance) -> {
                                            showReport(
                                                EnumType.上报埋点.有定位权限但定位失败,
                                                subactid = EnumType.上报埋点.系统GPS位置服务关闭
                                            )
                                        }
                                        !NetworkUtil.isNetworkUp(instance) -> {
                                            showReport(
                                                EnumType.上报埋点.有定位权限但定位失败,
                                                subactid = EnumType.上报埋点.无网络
                                            )
                                        }
                                        TextUtils.isEmpty(location.lat) ||
                                                TextUtils.isEmpty(location.lng) ||
                                                TextUtils.isEmpty(location.district) ||
                                                TextUtils.isEmpty(location.province) -> {
                                            showReport(
                                                EnumType.上报埋点.有定位权限但定位失败,
                                                subactid = EnumType.上报埋点.未获取到GPS信息
                                            )
                                        }
                                        else -> {
                                            showReport(EnumType.上报埋点.有定位权限但定位失败)
                                        }
                                    }

                                }
                            }

                        }
                    }
                    CacheUtil.put(Constant.SP_FIRST_LOCATION_COMPLETE, true)
                }
            })

        locationShow = WeatherUtils.initData().listIndex(0).tc.isEmpty() && !CacheUtil.getBoolean(
            Constant.SP_IS_LOCATION_PERMISSION,
            false
        )
        if (locationShow) {

            RequestLocationPermissionDialog(this) { isAllow ->
                CacheUtil.put(Constant.SP_IS_LOCATION_PERMISSION, true)
                if (isAllow) {
                    PermissionsUtils.checkPermissions(
                        this,
                        Constant.CHECK_LOCATION_PERMISSIONS,
                        object : PermissionsUtils.IPermissionsResult {
                            override fun passPermissons(
                                isRequst: Boolean,
                                permissions: Array<String>
                            ) {
                                showReport(EnumType.上报埋点.新用户流程_定位授权弹窗_授权成功)
                                CacheUtil.put(Constant.SP_LOCATION_PERMISSION_GET, true)
                                loadingHelp.setContentView(WeatherLocationLoadingPage(instance))
                                    .showView()
                                requestLocation()
                                runOnTime(10000) {
                                    if (LocationUtil.locationState != LocationUtil.定位成功 && !LocationUtil.isFirstLocation) {
                                        (vm.getFragments()
                                            .listIndex(0) as WeatherFragment).startCitySelect()
                                        if (!CacheUtil.getBoolean(
                                                Constant.SP_FIRST_LOCATION_COMPLETE,
                                                false
                                            )
                                        ) {
                                            // 如果之前没有完成过定位流程，且有定位权限
                                            showReport(
                                                EnumType.上报埋点.有定位权限但定位失败,
                                                subactid = EnumType.上报埋点.超10秒未返回定位结果
                                            )
                                        }
                                    }
                                }
                            }

                            override fun forbitPermissons(permissions: List<String>) {
                                showReport(EnumType.上报埋点.新用户流程_定位授权弹窗_授权失败)
                                (vm.getFragments()
                                    .listIndex(0) as WeatherFragment).startCitySelect()
                            }
                        }
                    )
                } else {
                    (vm.getFragments().listIndex(0) as WeatherFragment).startCitySelect()
                }
            }.show()

        } else {
            requestLocation()
        }

        getAppModel().control.safeObserve(this, Observer {
            if (!locationShow) {
                vm.checkUpDate(this, it)
            }
            checkBottomChange()
        })

        loadingHelp.setDismissListener {
            locationShow = false
            vm.checkUpDate(this, getAppModel().control.value)
        }
    }


    private fun requestLocation() {
        getAppModel().location { location, isAuth, opendGps ->
            if (location) {
                getAppModel().LocationAuthPopup?.dismiss()
            } else {
                showLocationPop(isAuth)
            }
            // 记录是否有定位权限
            CacheUtil.put(Constant.SP_LOCATION_PERMISSION_GET, isAuth)

            //记录首次启动的时间戳
            if (Constant.isFirstInstall()) {
                CacheUtil.put(Constant.SP_INSTALL_TIME, System.currentTimeMillis())
            }
        }
    }

    fun hideBottomBar(hide: Boolean = false) {
        binding.tabBottomBar.hideBottomBar(hide)
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            checkStorage()
        }
        getAppModel().checkScreenLock()
        checkLocationPop()
        vm.checkAllPermissionReport(this)
        getAppModel().reqeustInfo()
    }

    private fun checkLocationPop() {
        if (getAppModel().LocationAuthPopup?.isShowing == true) {
            getAppModel().location { location, isAuth, opendGps ->
                if (location)
                    getAppModel().LocationAuthPopup?.dismiss()
                else
                    showLocationPop(isAuth)
            }
        }
    }

    private fun checkStorage() {
        vm.checkStorage(this) {
            if (isPermissionDenied()) {
                PermissionsUtils.checkPermissions(
                    this,
                    Constant.CHECK_SAVE_PERMISSIONS,
                    permissionsResult
                )
            } else {
                startActivityForResult(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:${packageName}")
                    ), vm.START_SAVE_PERMISSION_SETTING
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Try {
            val from = intent?.getStringExtra("from")
            when (from) {
                "main" -> getAppModel().requestControl()
                "refresh" -> clickReport(EnumType.上报埋点.常驻通知栏首页点击)
            }
            if (!TextUtils.isEmpty(intent?.action) && intent?.action.equals("NotifyResidentService")) {
                clickReport(EnumType.上报埋点.常驻通知栏_点击)
            }
        }
    }

    private fun showLocationPop(isAuth: Boolean) {
        if (getAppModel().LocationAuthPopup?.isShowing == true) return
        if (vm.locationPermission(WeatherUtils.getDatas().listIndex(0).isLocation)) {
            getAppModel().showLocationPopup(this, isAuth, permissionsResult)
        }
    }

    private fun showLocationPageState(state: Boolean, func: () -> Unit) {
        Try {
            if (loadingHelp.isShowView) {
                loadingHelp.setViewStatus(if (state) WeatherLocationLoadingPage.LOCATION_SUCCESS else WeatherLocationLoadingPage.LOCATION_FAIL)
                runOnTime(2000) {
                    func()
                    if (state) {
                        loadingHelp.dismiss()
                    }
                }
            } else {
                func()
            }
        }
    }

    fun hiddenLoading() {
        if (loadingHelp != null && loadingHelp.isShowView) {
            loadingHelp.dismiss()
        }
    }

    override fun onBackPressed() {
        var secondTime = System.currentTimeMillis()
        if (secondTime - firstBackTime > 2000) {
            xToast("再按一次退出程序")
            firstBackTime = secondTime
        } else {
            finish()
            ActivityManageTools.finishAll()
            XMMarker.marker6()
        }
    }

    fun getFragment(): WeatherFragment {
        return (vm.getFragments().listIndex(0) as WeatherFragment)
    }

    fun searchCity(local: String, func: (local: String, dbBean: InnerJoinResult) -> Unit) {
        if (!local.isStr()) {
            func(local, InnerJoinResult())
            return
        }
        AppDatabase.getCityDao().selectCityZ("%${local}%").observe(this, Observer { data ->
            var location = LocationUtil.getLocation()
            if (data.nN().isNotEmpty()) {
                var list = data.nN().filter {
                    it.code != null && it.code != "NULL" && it.code.nN()
                        .isNotEmpty() && location.province.contains(
                        it.prov_cn.nN().replace("市", "").replace("省", "")
                    )
                }
                run breaking@{
                    list.forEachIndexed { index, it ->
                        if (it.district_search_cn.nN()
                                .contains(location.city.searchCityReplace()) && it.prov_cn.nN()
                                .contains(location.province.searchCityReplace())
                        ) {
                            func(local, it)
                            return@breaking
                        } else if (index == list.size - 1) {
                            LocationUtil.report(location)
                            func(local, InnerJoinResult())
                        }
                    }
                }
            } else {
                when (local) {
                    location.district -> {
                        searchCity(location.city.isStr(location.province.nN()), func)
                    }
                    location.city -> {
                        searchCity(location.province.nN(), func)
                    }
                    else -> {
                        LocationUtil.report(location)
                        func(local, InnerJoinResult())
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        XMMarker.marker5()
        LiveDataBus.unRegist(LocationUtil.KEY_MAIN_LOCATION)
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            vm.START_SAVE_PERMISSION_SETTING -> {
                PermissionsUtils.checkPermissions(
                    this,
                    Constant.CHECK_SAVE_PERMISSIONS,
                    permissionsResult
                )
            }
        }
    }

    private var permissionsResult: PermissionsUtils.IPermissionsResult =
        object : PermissionsUtils.IPermissionsResult {
            override fun passPermissons(isRequest: Boolean, permissions: Array<String>) {
                if (Constant.CHECK_SAVE_PERMISSIONS[0] in permissions || Constant.CHECK_SAVE_PERMISSIONS[1] in permissions) {
                    getAppModel().initScreenLock(Constant.WALLPAPER_START)
                    clickReport(EnumType.上报埋点.app内向系统申请存储授权挽留弹窗去开启授权成功)
                }

                if (Constant.CHECK_LOCATION_PERMISSIONS[0] in permissions || Constant.CHECK_LOCATION_PERMISSIONS[1] in permissions) {
                    getAppModel().location()
                }
            }

            override fun forbitPermissons(permissions: List<String>) {
                if (Constant.CHECK_SAVE_PERMISSIONS[0] in permissions || Constant.CHECK_SAVE_PERMISSIONS[1] in permissions) {
                    clickReport(EnumType.上报埋点.app内向系统申请存储授权挽留弹窗去开启授权失败)
                }
            }
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionsUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }
}