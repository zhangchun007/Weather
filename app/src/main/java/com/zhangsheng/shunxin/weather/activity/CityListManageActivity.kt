package com.zhangsheng.shunxin.weather.activity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maiya.thirdlibrary.adapter.BaseViewHolder
import com.maiya.thirdlibrary.adapter.RecyclerViewAdapter
import com.maiya.thirdlibrary.base.AacActivity
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.utils.CacheUtil
import com.maiya.thirdlibrary.utils.DeviceUtil
import com.maiya.thirdlibrary.widget.shapview.ShapeView
import com.my.sdk.stpush.STPushManager
import com.my.sdk.stpush.open.Tag
import com.xm.xmcommon.XMParam
import com.yanzhenjie.recyclerview.swipe.*
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.ad.AdConstant
import com.zhangsheng.shunxin.databinding.ActivityCityListManageBinding
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.common.EnumType.上报埋点.城市管理搜索添加城市
import com.zhangsheng.shunxin.weather.common.EnumType.上报埋点.城市管理添加按钮
import com.zhangsheng.shunxin.weather.common.EnumType.上报埋点.城市管理编辑排序
import com.zhangsheng.shunxin.weather.common.EnumType.上报埋点.城市管理编辑编辑
import com.zhangsheng.shunxin.weather.common.EnumType.上报埋点.城市管理编辑设为提醒城市
import com.zhangsheng.shunxin.weather.db.AppDatabase
import com.zhangsheng.shunxin.weather.db.city_db.InnerJoinResult
import com.zhangsheng.shunxin.weather.dialog.LocationLoadingDialog
import com.zhangsheng.shunxin.weather.dialog.LocationPermissionDialog
import com.zhangsheng.shunxin.weather.ext.ClickReport
import com.zhangsheng.shunxin.weather.ext.clickReport
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.livedata.LiveDataBus
import com.zhangsheng.shunxin.weather.model.CityManageViewModel
import com.zhangsheng.shunxin.weather.net.bean.*
import com.zhangsheng.shunxin.weather.utils.LocationUtil
import com.zhangsheng.shunxin.weather.utils.PermissionsUtils
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import org.koin.android.ext.android.inject
import java.util.*


class CityListManageActivity : AacActivity<CityManageViewModel>(), SwipeItemClickListener {

    private var locationPop: LocationLoadingDialog? = null
    private var locationPermission: LocationPermissionDialog? = null
    private var lastPushTime: LastPushTimeBean? = null
    private var mAdapter: CityListManageActivity.CityListAdapter? = null
    private var isSetting = false
    private var push: PushCityBean? = null
    private var currentPosition: Int = 0
    private var isItemMove = false //调整排序并点击完成
    private var isChangeRemindCity = false // 修改了提醒城市并点击完成

    override val vm: CityManageViewModel by inject()
    override val binding by inflate<ActivityCityListManageBinding>()


    override fun initView(savedInstanceState: Bundle?) {
        Try {
            currentPosition = intent.getIntExtra("position", 0)
            intent.putExtra("position", currentPosition)
            setResult(200, intent)
        }
        binding.title.initTitle("城市管理")
        binding.title.setRightTvStr("编辑")

        vm.checkPush(this)
        push = CacheUtil.getObj(Constant.SP_PUSH_CODE, PushCityBean::class.java)
        lastPushTime = CacheUtil.getObj(Constant.SP_LAST_PUSH_TIME, LastPushTimeBean::class.java)

        if (WeatherUtils.getDatas().isNotEmpty()) {
            binding.location.isVisible(!WeatherUtils.getDatas().listIndex(0).isLocation)
        } else {
            binding.location.isVisible(true)
        }

        mAdapter = createAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(
            DefaultItemDecoration(
                ContextCompat.getColor(this, R.color.bg_color), 4, 2
            )
        )
        binding.recyclerView.setSwipeItemClickListener(this)
        binding.recyclerView.setSwipeMenuItemClickListener(mItemMenuClickListener)
        binding.recyclerView.setSwipeMenuCreator(mSwipeMenuCreator)
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.setOnItemMoveListener(getItemMoveListener())
        binding.recyclerView.isLongPressDragEnabled = true

        loadCityWeather()
    }

    override fun initObserve() {
        super.initObserve()
        LiveDataBus.getChannel<Location>(LocationUtil.KEY_CITY_LOCATION).nN()
            .safeObserve(this@CityListManageActivity, Observer { location ->
                if (location.nN().state == 1) {
                    var str =
                        location.nN().district.isStr(location.nN().city.isStr(location.nN().province))
                    searchCity(str) { _, dbBean ->
                        WeatherUtils.addWeather(WeatherBean().apply {
                            this.isLocation = true
                            this.regioncode = dbBean.code.isStr("")
                            this.regionname =
                                location.district.isStr(location.city.isStr(location.province))
                            this.refreshTime = System.currentTimeMillis()
                            this.location = location
                        }, 0, true)
                    }
                    locationPop?.changeDialogStatus(true) {
                        intent.putExtra("position", 0)
                        setResult(200, intent)
                        finish()
                    }
                } else {
                    locationPop?.changeDialogStatus(false)
                }
            })
        getAppModel().setTags.safeObserve(this, Observer {
            if (it == EnumType.推送状态.成功) {
                CacheUtil.putObj(Constant.SP_PUSH_CODE, getAppModel().pushCity)
                push = getAppModel().pushCity
                isChangeRemindCity = true // 设置提醒城市成功
            }
            mAdapter.nN().notifyDataSetChanged()
        })
    }

    private val mItemMenuClickListener = SwipeMenuItemClickListener { menuBridge ->
        menuBridge.closeMenu()
        when (WeatherUtils.getDatas().size) {
            1 -> xToast("至少保留一个城市")
            else -> {
                var item = WeatherUtils.getDatas()[menuBridge.adapterPosition]
                if (item.isLocation) {
                    CacheUtil.put(Constant.SP_ISLOCATION_USER, false)
                    binding.location.isVisible(true)
                }
                if (currentPosition == menuBridge.adapterPosition) {
                    currentPosition = 0
                } else {
                    currentPosition -= 1
                }
                WeatherUtils.delete(menuBridge.adapterPosition)
                mAdapter.nN().notifyDataSetChanged()
                if (push.nN().code == item.regioncode) {
                    getAppModel().pushCity.apply {
                        this.code = WeatherUtils.getDatas()[0].regioncode
                        this.city = WeatherUtils.getDatas()[0].regionname
                        this.isLocation = WeatherUtils.getDatas()[0].isLocation
                        this.dayTime = push.nN().dayTime.isStr(lastPushTime.nN().dayTime)
                        this.nightTime = push.nN().nightTime.isStr(lastPushTime.nN().nightTime)
                    }
                    STPushManager.getInstance().setTag(
                        instance,
                        Tag(getAppModel().pushCity.dayTime),
                        Tag(getAppModel().pushCity.nightTime),
                        Tag(XMParam.getAppVer()),
                        Tag(getAppModel().pushCity.code)
                    )
                }
            }
        }
    }

    private fun loadCityWeather() {
        var code = ""
        for (i in WeatherUtils.getDatas().indices) {
            if (!WeatherUtils.getDatas()[i].isLocation) {
                code += if (i == WeatherUtils.getDatas().size - 1) {
                    WeatherUtils.getDatas()[i].regioncode
                } else {
                    "${WeatherUtils.getDatas()[i].regioncode},"
                }
            } else {
                vm.requestCityWeather(
                    WeatherUtils.getDatas().listIndex(i).location.nN().lat,
                    WeatherUtils.getDatas().listIndex(i).location.nN().lng
                ) {
                    mAdapter?.notifyDataSetChanged()
                }
            }
        }
        if (code.isNotEmpty()) {
            vm.requestCityWeather(code) {
                if (WeatherUtils.getDatas()[0].isLocation) {
                    for (i in 1 until WeatherUtils.getDatas().size) {
                        WeatherUtils.getDatas()[i].apply {
                            this.tcmax = it[i - 1].tcmax
                            this.tcmin = it[i - 1].tcmin
                        }
                    }
                } else {
                    for (i in WeatherUtils.getDatas().indices) {
                        WeatherUtils.getDatas()[i].apply {
                            this.tcmax = it[i].tcmax
                            this.tcmin = it[i].tcmin
                        }
                    }
                }
                mAdapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onItemClick(view: View?, adapterPosition: Int) {
        if (!isSetting) {
            intent.putExtra("position", adapterPosition)
            setResult(200, intent)
            finish()
        }
    }


    private fun createAdapter(): CityListManageActivity.CityListAdapter {
        if (mAdapter == null) {
            mAdapter = CityListAdapter(WeatherUtils.getDatas(), R.layout.item_city_list2)
        }
        return mAdapter!!
    }

    private inner class CityListAdapter(ds: MutableList<WeatherBean>, layout: Int) :
        RecyclerViewAdapter<WeatherBean>(ds, layout) {
        override fun bindDataToItemView(
            holder: BaseViewHolder,
            item: WeatherBean,
            position: Int
        ) {
            val ll_bg = holder.findView<LinearLayout>(R.id.ll_bg)
            if (currentPosition == position) {
                ll_bg.setBackgroundColor(Color.parseColor("#FFF9FAFD"))
            } else {
                ll_bg.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
            }
            holder.findView<ImageView>(R.id.img_local).isVisible(item.isLocation)
            val location_district = holder.findView<TextView>(R.id.location_district)
            val street = holder.findView<TextView>(R.id.tv_city)
            val ima_weather = holder.findView<ImageView>(R.id.img_weather)
            ima_weather.isVisible(!isSetting)
            val btn_delete = holder.findView<ImageView>(R.id.btn_delete)
            if (isSetting) {
                btn_delete.isVisible(true)

            } else {
                btn_delete.isVisible(false)
                ima_weather.setImageResource(WeatherUtils.IconBig(item.wtid))
            }
            holder.findView<TextView>(R.id.tv_temp).text = "${item.tcmax}°/${item.tcmin}°"
            holder.findView<TextView>(R.id.push_tag)
                .isVisible(item.regioncode == push.nN().code && item.isLocation == push.nN().isLocation)
            val rightSetting = holder.findView<LinearLayout>(R.id.right_setting)
            if (isSetting) {
                rightSetting.isVisible(true)
                holder.findView<ImageView>(R.id.icon_edit)?.visibility =
                    if (item.isLocation) View.INVISIBLE else View.VISIBLE
            } else {
                rightSetting.isVisible(false)
            }
            holder.findView<LinearLayout>(R.id.left_weather).isVisible(!isSetting)
            location_district.isVisible(item.isLocation)
            location_district.text = item.location.nN().district

            holder.findView<ImageView>(R.id.icon_edit).setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    binding.recyclerView.startDrag(holder)
                }
                true
            }
            if (item.isLocation) {
                street.text = item.location.nN().street.isStr(item.location.nN().district)
            } else {
                street.text = item.regionname
            }
            val push_btn = holder.findView<ShapeView>(R.id.btn_push)
            push_btn.isVisible(getAppModel().pushClientId.value.nN().isNotEmpty())
            if (item.regioncode == push.nN().code) {
                push_btn.exeConfig(push_btn.getConfig().apply {
                    this.bgColor = Color.parseColor("#FFF4F5F9")
                    push_btn.setTextColor(Color.parseColor("#FFAAAAAA"))
                    push_btn.isEnabled = false
                })
            } else {
                push_btn.exeConfig(push_btn.getConfig().apply {
                    this.bgColor = Color.parseColor("#FFFFA43B")
                    push_btn.setTextColor(Color.parseColor("#FFFFFFFF"))
                    push_btn.isEnabled = true
                })
            }

            push_btn.setSingleClickListener {
                if (DeviceUtil.networkConnected(instance)) {
                     getAppModel().pushCity.apply {
                        this.code = WeatherUtils.getDatas()[position].regioncode
                        this.city = WeatherUtils.getDatas()[position].regionname
                        this.isLocation = WeatherUtils.getDatas()[position].isLocation
                        this.isChoose = true
                        this.dayTime = push.nN().dayTime.isStr(lastPushTime.nN().dayTime)
                        this.nightTime = push.nN().nightTime.isStr(lastPushTime.nN().nightTime)
                    }
                    val tag = STPushManager.getInstance().setTag(
                        this@CityListManageActivity,
                        Tag(getAppModel().pushCity.dayTime),
                        Tag(getAppModel().pushCity.nightTime),
                        Tag(XMParam.getAppVer()),
                        Tag(getAppModel().pushCity.code)
                    )

                    if (tag == 100005) {
                        xToast("请求超时，请重新操作")
                    }
                } else {
                    xToast("当前网络不可用,请检查网络")
                }
            }

            btn_delete.setSingleClickListener {
                if (isSetting) {
                    when (WeatherUtils.getDatas().size) {
                        1 -> xToast("至少保留一个城市")
                        else -> {
                            if (item.isLocation) {
                                CacheUtil.put(Constant.SP_ISLOCATION_USER, false)
                                binding.location.isVisible(true)
                            }
                            if (currentPosition == position) {
                                currentPosition = 0
                            } else {
                                currentPosition -= 1
                            }
                            WeatherUtils.delete(position)
                            mAdapter.nN().notifyDataSetChanged()
                            if (item.regioncode == push.nN().code) {
                                getAppModel().pushCity.apply {
                                    this.code = WeatherUtils.getDatas()[0].regioncode
                                    this.city = WeatherUtils.getDatas()[0].regionname
                                    this.isLocation = WeatherUtils.getDatas()[0].isLocation
                                    this.dayTime =
                                        push.nN().dayTime.isStr(lastPushTime.nN().dayTime)
                                    this.nightTime =
                                        push.nN().nightTime.isStr(lastPushTime.nN().nightTime)
                                }
                                STPushManager.getInstance().setTag(
                                    instance,
                                    Tag(getAppModel().pushCity.dayTime),
                                    Tag(getAppModel().pushCity.nightTime),
                                    Tag(XMParam.getAppVer()),
                                    Tag(getAppModel().pushCity.code)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getItemMoveListener(): OnItemMoveListener {
        // 监听拖拽和侧滑删除，更新UI和数据源。
        return object : OnItemMoveListener {
            override fun onItemMove(
                srcHolder: RecyclerView.ViewHolder,
                targetHolder: RecyclerView.ViewHolder
            ): Boolean {
                //item 拖拽监听
                // 不同的ViewType不能拖拽换位置。
                if ((srcHolder.adapterPosition == 0 || targetHolder.adapterPosition == 0) && WeatherUtils.getDatas()
                        .listIndex(0).isLocation
                ) {
                    return false
                }

                // 真实的Position：通过ViewHolder拿到的position都需要减掉HeadView的数量。
                val fromPosition = srcHolder.adapterPosition - binding.recyclerView.headerItemCount
                val toPosition = targetHolder.adapterPosition - binding.recyclerView.headerItemCount

                Collections.swap(WeatherUtils.getDatas(), fromPosition, toPosition)
                createAdapter().notifyItemMoved(fromPosition, toPosition)
                WeatherUtils.saveData()
                isItemMove = true
                return true// 返回true表示处理了并可以换位置，返回false表示你没有处理并不能换位置。
            }

            override fun onItemDismiss(srcHolder: RecyclerView.ViewHolder) {
                //侧滑删除
            }
        }
    }

    /**
     * 菜单创建器。
     */
    private val mSwipeMenuCreator = object : SwipeMenuCreator {
        override fun onCreateMenu(
            swipeLeftMenu: SwipeMenu,
            swipeRightMenu: SwipeMenu,
            position: Int
        ) {

            val height = ViewGroup.LayoutParams.MATCH_PARENT
            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            run {
                val deleteItem =
                    SwipeMenuItem(instance).setBackgroundColor(Color.parseColor("#fd3b31"))
                        .setImage(R.mipmap.icon_city_delete)
                        .setWidth(resources.nN().getDimensionPixelSize(R.dimen.dp_56))
                        .setHeight(height)
                swipeRightMenu.addMenuItem(deleteItem)// 添加一个按钮到右侧侧菜单。
            }
        }
    }


    override fun onResume() {
        super.onResume()
        onlyCheck()
        loadAd()
    }

    private fun loadAd() {
        binding.adv.showLeftFeedAd(this, AdConstant.SLOT_CITYBOTTOM)
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

    override fun initListener() {
        super.initListener()

        binding.location.setSingleClickListener {
            PermissionsUtils.checkPermissions(
                this,
                Constant.CHECK_LOCATION_PERMISSIONS,
                permissionsResult
            )
        }

        binding.et.ClickReport(城市管理搜索添加城市) {
            var intent = Intent()
            intent.putExtra("source", "home-tjcity")
            intent.putExtra("isEt", true)
            intent.setClass(this, CitySelectActivity::class.java)
            var bundle =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, binding.et, "et")
                    .toBundle()

            startActivityForResult(intent, 888, bundle)
        }

        binding.addCity.ClickReport(城市管理添加按钮) {
            var intent = Intent()
            intent.putExtra("source", "home-tjcity")
            intent.putExtra("isEt", false)
            intent.setClass(this, CitySelectActivity::class.java)
            var bundle =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, binding.et, "et")
                    .toBundle()

            startActivityForResult(intent, 888, bundle)
        }

        binding.title.setRightTvClick {
            if (isSetting) {
                binding.title.setRightTvStr("编辑")
                binding.title.initTitle("城市管理")
                if (isItemMove) {
                    isItemMove = false
                    clickReport(城市管理编辑排序)
                }
                if (isChangeRemindCity) {
                    isChangeRemindCity = false
                    clickReport(城市管理编辑设为提醒城市)
                }
            } else {
                binding.title.initTitle("编辑城市")
                binding.title.setRightTvStr("完成")
                clickReport(城市管理编辑编辑)
            }
            binding.et.isVisible(isSetting)
            binding.addCity.isVisible(isSetting)
            isSetting = !isSetting
            mAdapter.nN().notifyDataSetChanged()
        }


    }


    override fun finish() {
        LiveDataBus.unRegist(LocationUtil.KEY_CITY_LOCATION)
        super.finish()
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
        })
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
                    locationPop = LocationLoadingDialog(this@CityListManageActivity)
                }
                locationPop?.show()
                getAppModel().location(LocationUtil.KEY_CITY_LOCATION)
            }

            override fun forbitPermissons(permissions: List<String>) {
                if (locationPermission == null) {
                    locationPermission = LocationPermissionDialog(this@CityListManageActivity) {
                        if (PermissionsUtils.shouldShowRequestPermissionRationale(
                                this@CityListManageActivity,
                                Constant.CHECK_LOCATION_PERMISSIONS.toList()
                            )
                        ) {
                            PermissionsUtils.checkPermissions(
                                this@CityListManageActivity,
                                Constant.CHECK_LOCATION_PERMISSIONS,
                                this
                            )
                        } else {
                            val packageURI =
                                Uri.parse("package:${this@CityListManageActivity.packageName}")
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
                            this@CityListManageActivity.startActivity(intent)
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