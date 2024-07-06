package com.zhangsheng.shunxin.weather.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.view.View
import androidx.lifecycle.Observer
import com.maiya.thirdlibrary.base.AacActivity
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.utils.DefaultTextWatcher
import com.maiya.thirdlibrary.utils.SoftKeyboardUtil
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.databinding.ActivityCitySelectBinding
import com.zhangsheng.shunxin.weather.adapter.CityListAdapter
import com.zhangsheng.shunxin.weather.adapter.SearchCityAdapter
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.db.AppDatabase
import com.zhangsheng.shunxin.weather.db.city_db.InnerJoinResult
import com.zhangsheng.shunxin.weather.dialog.LocationLoadingDialog
import com.zhangsheng.shunxin.weather.dialog.LocationPermissionDialog
import com.zhangsheng.shunxin.weather.ext.*
import com.zhangsheng.shunxin.weather.livedata.LiveDataBus
import com.zhangsheng.shunxin.weather.model.CitySelectViewModel
import com.zhangsheng.shunxin.weather.net.bean.Location
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.utils.LocationUtil
import com.zhangsheng.shunxin.weather.utils.PermissionsUtils
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import org.koin.android.ext.android.inject
import java.util.regex.Pattern

class CitySelectActivity : AacActivity<CitySelectViewModel>() {

    override val vm: CitySelectViewModel by inject()
    override val binding by inflate<ActivityCitySelectBinding>()
    private var locationPop: LocationLoadingDialog? = null
    private var locationPermission: LocationPermissionDialog? = null


    override fun initView(savedInstanceState: Bundle?) {
        binding.statusBar.setStatusPadding()
        Try {
            vm.isEt = intent.getBooleanExtra("isEt", false)
            if (vm.isEt) {
                binding.et.isFocusable = true
                binding.et.isFocusableInTouchMode = true
                binding.et.requestFocus()
                SoftKeyboardUtil.showSoftInputFromWindow(this, binding.et)
            }
        }

        var location = LocationEllipsis(
            WeatherUtils.getDatas().listIndex(0).regionname,
            WeatherUtils.getDatas().listIndex(0).isLocation
        )
        if (WeatherUtils.getDatas().listIndex(0).isLocation && location.trim().isNotEmpty()) {
            binding.tvLocation.text = location
        } else {
            binding.tvLocation.text = "获取定位，天气更准"
            vm.requestIpLocation()
        }
        vm.initHot()

        initAdapter()
        showReport(EnumType.上报埋点.城市添加页显示)
    }

    private fun initAdapter() {
        binding.gv.adapter = CityListAdapter(this, vm.hotCity, R.layout.item_gv_local)
        { position, code, name ->
            clickReport(EnumType.上报埋点.城市添加非ip城市, "${position + 1}")
            vm.chooseCity(this, code, name)
        }
        binding.gv2.adapter = CityListAdapter(this, vm.data.toMutableList(), R.layout.item_gv_local)
        { position, code, name ->
            clickReport(EnumType.上报埋点.城市添加非ip省份, "${position + 1}")
            searchDbData("", name) {
                if (it == null) return@searchDbData
                getAppModel().city2ndList = it
                startCity2nd(name)
            }
        }
        binding.searchList.adapter =
            SearchCityAdapter(this, vm.searchlist, R.layout.item_lv_city_list) { getSearchText() }
    }

    private fun getSearchText(): String {
        return binding.et.text.toString().trim()
    }

    private fun startCity2nd(name: String, floor: String = "2") {
        startActivityForResult(Intent().apply {
            putExtra("city", name)
            putExtra("floor", floor)
            setClass(this@CitySelectActivity, City2ndActivity::class.java)
        }, 888)
    }


    override fun initListener() {
        super.initListener()
        binding.frameLocation.ClickReport(EnumType.上报埋点.城市添加定位按钮) {
            PermissionsUtils.checkPermissions(
                this,
                Constant.CHECK_LOCATION_PERMISSIONS,
                permissionsResult
            )
        }

        binding.cancel.setSingleClickListener {
            if (WeatherUtils.initData().listIndex(0).tc.isEmpty()) {
                xToast("至少添加一个城市")
            } else {
                finishAfterTransition()
            }
        }

        binding.frameCity.setSingleClickListener {
            if (binding.frameProvince.visibility == View.GONE) clickReport(EnumType.上报埋点.城市添加ip省份) else clickReport(
                EnumType.上报埋点.城市添加ip城市
            )
            searchDbData(
                if (binding.frameProvince.visibility == View.GONE) "" else vm.IpLocation.value.nN().city.searchCityReplace(),
                if (binding.frameProvince.visibility == View.GONE) vm.IpLocation.value.nN().city.searchCityReplace() else ""
            ) {
                if (it.nN().isNotEmpty()) {
                    getAppModel().city2ndList = it
                    startCity2nd(
                        vm.IpLocation.value.nN().city,
                        if (binding.frameProvince.visibility == View.GONE) "2" else "3"
                    )
                } else {
//                    showReport("first_city_ipnomatch")
                }
            }
        }

        binding.et.setOnFocusChangeListener { view: View, hasFocus: Boolean ->
            if (hasFocus)
                clickReport(EnumType.上报埋点.城市添加页搜索)
        }

        binding.frameProvince.ClickReport(EnumType.上报埋点.城市添加ip省份) {
            searchDbData("", vm.IpLocation.value.nN().province.searchCityReplace()) {
                if (it.nN().isNotEmpty()) {
                    getAppModel().city2ndList = it
                    startCity2nd(vm.IpLocation.value.nN().province.searchCityReplace())
                } else {
//                    showReport("first_city_ipnomatch")
                }
            }

        }
        binding.searchList.setOnItemClickListener { parent, view, position, id ->
            clickReport(EnumType.上报埋点.城市添加页搜索点击搜索结果)
            vm.chooseCity(
                this,
                vm.searchlist[position].code.nN(),
                vm.searchlist[position].name_cn.toString()
            )
        }

        binding.et.addTextChangedListener(object : DefaultTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                binding.searchList.isVisible(binding.et.text.nN().isNotEmpty())
                binding.llGv.isVisible(binding.et.text.nN().isEmpty())
                if (binding.et.text.nN().isEmpty()) {
                    binding.empty.isVisible(false)
                }
                if (binding.et.text.nN().trim().isNotEmpty()) {
                    searchDbData("", "", binding.et.text.toString().trim()) {
                        vm.searchlist.clear()
                        vm.searchlist.addAll(it.nN())
                        binding.empty.isVisible(vm.searchlist.isEmpty())
                        (binding.searchList.adapter as SearchCityAdapter).notifyDataSetChanged()
                    }

                }
            }
        })
        binding.imFull.setOnTouchListener { _, _ ->
            vm.hideInputTips(binding.et)
            false
        }
    }


    private fun searchDbData(
        city: String,
        province: String,
        searchEt: String = "",
        searchFunc: (list: List<InnerJoinResult>?) -> Unit
    ) {
        if (city.isEmpty() && province.isEmpty() && searchEt.isEmpty()) return searchFunc(null)

        when {
            province.isNotEmpty() -> {
                //省份检索
                AppDatabase.getCityDao().queryCityProvcn("%${province}%").observe(this, Observer {
                    searchFunc(it)
                })
            }
            city.isNotEmpty() -> {
                AppDatabase.getCityDao().queryCityDistrict("%${city}%").observe(this, Observer {
                    searchFunc(it)
                })
            }
            Pattern.compile("[a-zA-Z]").matcher(searchEt.substring(0, 1)).matches() -> {
                //字母
                AppDatabase.getCityDao().selectCityE("%${searchEt}%")
                    .observe(this@CitySelectActivity, Observer {
                        if (it.isEmpty()) {
                            AppDatabase.getCityDao().selectProvE("%${searchEt}%")
                                .observe(this@CitySelectActivity, Observer {
                                    searchFunc(it)
                                })
                        } else {
                            searchFunc(it)
                        }
                    })

            }
            Pattern.compile("[\u4e00-\u9fa5]").matcher(searchEt.substring(0, 1)).matches() -> {
                //中文
                AppDatabase.getCityDao().selectCityZ("%${searchEt}%").observe(this, Observer {
                    if (it.isEmpty()) {
                        AppDatabase.getCityDao().selectProvZ("%${searchEt}%")
                            .observe(this@CitySelectActivity, Observer {
                                searchFunc(it)
                            })
                    } else {
                        searchFunc(it)
                    }
                })
            }
        }
    }

    override fun initObserve() {
        vm.IpLocation.safeObserve(this, Observer { data ->
            if (data == null || data.nN().province.isEmpty()) {
//                showReport("first_city_iperror", "others")
                return@Observer
            }
            searchDbData(
                if (data.city.isNotEmpty()) data.city.searchCityReplace() else "",
                if (data.city.isNotEmpty()) "" else data.nN().province.searchCityReplace()
            ) {
                if (it.nN().isNotEmpty() && it.listIndex(0).prov_cn.isStr("")
                        .contains(data.nN().province.searchCityReplace())
                ) {
                    binding.llIpCity.isVisible(true)
                    binding.tvProvince.text = data.nN().province
                    binding.tvCity.text = data.nN().city
                    binding.frameProvince.isVisible(data.nN().city.isNotEmpty() && data.nN().city.searchCityReplace() != data.nN().province.searchCityReplace())

                    if (data.city.isNotEmpty()) {
                        vm.cityDbSearch = it
                    } else {
                        vm.provinceDbSearch = it
                    }
                } else {
                    showReport(EnumType.上报埋点.城市添加ip城市匹配失败, subactid = (data.province + data.city))
                }
            }

        })

        LiveDataBus.getChannel<Location>(this@CitySelectActivity.localClassName).nN()
            .safeObserve(this@CitySelectActivity, Observer { location ->
                if (location.nN().state == 1) {
                    var str =
                        location.nN().district.isStr(location.nN().city.isStr(location.nN().province))
                    searchCity(str) { local, dbBean ->
                        WeatherUtils.addWeather(WeatherBean().apply {
                            this.isLocation = true
                            this.regioncode = dbBean.code.isStr("")
                            this.regionname =
                                location.district.isStr(location.city.isStr(location.province))
                            this.refreshTime = System.currentTimeMillis()
                            this.location = location
                        }, 0, true)
                        locationPop?.changeDialogStatus(true) {
                            intent.putExtra("position", 0)
                            setResult(200, intent)
                            finish()
                        }
                    }
                } else {
                    locationPop?.changeDialogStatus(false)
                }
            })
    }


    fun searchCity(local: String, func: (local: String, dbBean: InnerJoinResult) -> Unit) {
        if (!local.isStr()) {
            func(local, InnerJoinResult())
            return
        }
        searchDbData(local, "") { data ->
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
                        func(local, InnerJoinResult())
                    }
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        onlyCheck()
    }

    private fun onlyCheck() {
        if (locationPermission != null && locationPermission.nN().isShowing) {
            PermissionsUtils.onlycheck(this, Constant.CHECK_LOCATION_PERMISSIONS) {
                if (it) {
                    locationPermission?.dismiss()
                }
            }
        }
    }


    override fun onBackPressed() {
        if (WeatherUtils.initData().listIndex(0).tc.isEmpty()) {
            xToast("至少添加一个城市")
        } else {
            super.onBackPressed()
        }
    }


    override fun finish() {
        LiveDataBus.unRegist(this@CitySelectActivity.localClassName)
        super.finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 888) {
            if (resultCode == 200) {
                var position = data?.getIntExtra("position", WeatherUtils.getDatas().size - 1)
                intent.putExtra("position", position)
                setResult(200, intent)
                finish()
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

    private val permissionsResult: PermissionsUtils.IPermissionsResult =
        object : PermissionsUtils.IPermissionsResult {
            override fun passPermissons(isRequst: Boolean, permissions: Array<String>) {
                if (locationPop == null) {
                    locationPop = LocationLoadingDialog(this@CitySelectActivity)
                }
                locationPop?.show()
                getAppModel().location(this@CitySelectActivity.localClassName)
            }

            override fun forbitPermissons(permissions: List<String>) {
                if (locationPermission == null) {
                    locationPermission = LocationPermissionDialog(this@CitySelectActivity) {
                        if (PermissionsUtils.shouldShowRequestPermissionRationale(
                                this@CitySelectActivity,
                                Constant.CHECK_LOCATION_PERMISSIONS.toList()
                            )
                        ) {
                            PermissionsUtils.checkPermissions(
                                this@CitySelectActivity,
                                Constant.CHECK_LOCATION_PERMISSIONS,
                                this
                            )
                        } else {
                            val packageURI =
                                Uri.parse("package:${this@CitySelectActivity.packageName}")
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
                            this@CitySelectActivity.startActivity(intent)
                        }
                    }
                }
                locationPermission?.show()
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        PermissionsUtils.onDestroy()
    }
}
