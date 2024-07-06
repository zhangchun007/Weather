package com.zhangsheng.shunxin.weather.activity

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.LatLngBounds
import com.maiya.thirdlibrary.base.AacActivity
import com.maiya.thirdlibrary.adapter.ViewHolder
import com.maiya.thirdlibrary.widget.shapview.ShapeLinearLayout
import com.maiya.thirdlibrary.widget.shapview.ShapeView
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.ad.AdConstant
import com.maiya.thirdlibrary.adapter.BaseViewHolder
import com.maiya.thirdlibrary.adapter.CommonAdapter
import com.maiya.thirdlibrary.adapter.RecyclerViewAdapter
import com.maiya.thirdlibrary.ext.*
import com.zhangsheng.shunxin.weather.net.bean.Location
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.ext.skipActivity
import com.zhangsheng.shunxin.weather.livedata.LiveDataBus
import com.zhangsheng.shunxin.weather.model.AirModel
import com.zhangsheng.shunxin.weather.net.bean.Air
import com.zhangsheng.shunxin.weather.net.bean.AirBean
import com.zhangsheng.shunxin.weather.net.bean.AirPositionBean
import com.zhangsheng.shunxin.weather.net.bean.AirStationMarkerBean
import com.zhangsheng.shunxin.weather.utils.DataUtil
import com.zhangsheng.shunxin.weather.utils.LocationUtil
import com.zhangsheng.shunxin.weather.utils.MapUtils
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import com.xm.xmlog.XMLogAgent
import com.zhangsheng.shunxin.databinding.ActivityAirBinding
import com.zhangsheng.shunxin.weather.common.EnumType
import org.koin.android.ext.android.inject
import kotlin.math.abs

class AirActivity : AacActivity<AirModel>() {

    override val binding by inflate<ActivityAirBinding>()

    private var adapter: Adapter? = null
    private var fifAdapter: FiveAdapter? = null
    private var markers: ArrayList<AirStationMarkerBean> = ArrayList()
    private var isLoadAll = false
    private var zoom = 10f
    private var lastZoom = 7f
    private var isSmall = false
    private var isLocation = false
    private var name = "空气质量"
    private var regioncode = ""

    override fun onStart() {
        super.onStart()
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding.mapview.onCreate(savedInstanceState)
        Try {
            regioncode = intent.getStringExtra("code").isStr("")
            isLocation = intent.getBooleanExtra("location", false)
            name = intent.getStringExtra("name").isStr("")
        }

        initMap()
        vm.requestAirRank(true)

        binding.title.initTitle(name, "#ffffff")
        binding.title.setTitleBgColor("#FF2FB999")
        binding.llAirRank.setSingleClickListener {
            skipActivity(AirRankActivity::class.java) {
                putExtra("code", regioncode)
            }
        }


        if (isLocation)binding.title.setIcon(R.mipmap.icon_location)

        vm.requestAir(isLocation, regioncode)

        binding.llMap.isVisible(regioncode.isNotEmpty())
        if (regioncode.isNotEmpty()) {
            vm.loadMapAir(regioncode) {

                if (it.nN().isEmpty()) {
                    binding.llMap.isVisible(false)
                    return@loadMapAir
                }
                vm.airPositions.clear()
                vm.airPositions.addAll(it.nN())
                binding.airList.adapter = PositionAdapter()
                var boundsBuilder = LatLngBounds.Builder()
                it.nN().forEach {
                    var marker =
                        MapUtils.addAirMarkersToMap(
                            this,
                            binding.mapview.map,
                            LatLng(it.lat, it.lng),
                            it
                        )
                    boundsBuilder.include(marker.position)
                }
                binding.mapview.map.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        boundsBuilder.build(),
                        15
                    )
                )
            }
        }
    }

    override val vm: AirModel by inject()

    private fun initMap() {
        binding.mapview.map.apply {
            uiSettings.isMyLocationButtonEnabled = false // 设置默认定位按钮是否显示
            uiSettings.isZoomControlsEnabled = false
            uiSettings.isCompassEnabled = false
            uiSettings.isScaleControlsEnabled = false
            uiSettings.isTiltGesturesEnabled = false // 倾斜
            isMyLocationEnabled = false // 设置为true表示显示定位层并可触发定位,false表示隐藏定位层并不可触发定位,默认是false
        }
        binding.mapview.map.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChangeFinish(p0: CameraPosition?) {
            }

            override fun onCameraChange(p0: CameraPosition?) {
                zoom = p0.nN().zoom
                if (zoom <= 6.5 && !isLoadAll) {
                    lastZoom = zoom
                    isLoadAll = true
                    vm.loadMapAir("") {
                        DrawAirMarker(it)
                    }
                }
                if (markers.isNotEmpty() && abs(lastZoom - zoom) > 2.5) {
                    lastZoom = zoom
                    if (zoom < 6.5 && !isSmall) {
                        isSmall = !isSmall
                        markers.forEach {
                            it.marker?.setIcon(
                                MapUtils.getAirMarkerBitmapDescriptor(
                                    this@AirActivity,
                                    it.bean.nN(),
                                    isSmall
                                )
                            )
                        }
                    } else if (zoom > 8 && isSmall) {
                        isSmall = !isSmall
                        markers.forEach {
                            it.marker?.setIcon(
                                MapUtils.getAirMarkerBitmapDescriptor(
                                    this@AirActivity,
                                    it.bean.nN(),
                                    isSmall
                                )
                            )
                        }
                    }

                }
            }
        })
        binding.mapContainer.setScrollView( binding.scorll)

        binding.location.setSingleClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getAppModel().location(this.name)
            }
        }
    }

    override fun initObserve() {
        super.initObserve()
        getAppModel().airRank.safeObserve(this, Observer {
            try {
                var item = it.nN().rankings.nN()
                    .filter { it.areaCodes.contains(regioncode) }
                binding.rank.text = if (item.isNotEmpty()) {
                    binding.rankAll.text = " / ${it.nN().rankings.nN().size}"
                    "${item.listIndex(0).ranking}"
                } else {
                    binding.rankAll.text = ""
                    ""
                }
            } catch (e: Exception) {
                binding.rank.text = "0"
            }
        })
        vm.airBean.safeObserve(this, Observer {
            dealAir(it.nN())
            binding.airDes.text = it.nN().aqiDesc
            var air = it.nN().aqd.nN().filter { it.aqi.isStr() }
            if (air.isEmpty()) {

                binding.llAirList.isVisible(false)
            }
            if (fifAdapter == null && air.isNotEmpty()) {
                fifAdapter = FiveAdapter()
                val manager = LinearLayoutManager(this)
                manager.orientation = LinearLayoutManager.HORIZONTAL
                binding.recyclerView.layoutManager = manager
                binding.recyclerView.adapter = fifAdapter
                fifAdapter.nN().notifyDataSetChanged()
                binding.time.text = "更新于${
                    DataUtil.timeStamp2Date(
                        DataUtil.date2Long(it.nN().time, "yyyy-MM-dd HH:mm:ss"), "HH:mm"
                    )
                }"
            }
        })
        LiveDataBus.getChannel<Location>(this.name).safeObserve(this, Observer {
            if (it.nN().state == LocationUtil.定位成功) {
                binding.mapview.map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            it.nN().lat.toDouble(),
                            it.nN().lng.toDouble()
                        ), zoom
                    ),
                    618L,
                    null
                )
            }
        })

        vm.getAirRankStatar.observe(this, Observer {
            binding.llAirRank.isVisible(it)
        })
    }


    private fun dealAir(it: AirBean) {
        binding.title.setTitleBgColor(WeatherUtils.airColor(it.aqiLevel).listIndex(0))
        binding.bgAir.exeConfig( binding.bgAir.getConfig().apply {
            this.startColor = Color.parseColor(WeatherUtils.airColor(it.aqiLevel).listIndex(0))
            this.endColor = Color.parseColor(WeatherUtils.airColor(it.aqiLevel).listIndex(1))
        })
        binding.dbv.setProgressData(it.aqiLevel, it.aqi.parseInt(0))
        if (adapter == null) {
            adapter = Adapter()
            binding.gv.adapter = adapter
        }
        adapter.nN().notifyDataSetChanged()
    }

    private inner class Adapter :
        CommonAdapter<Air>(this, vm.air, R.layout.item_air_pm) {
        override fun convert(holder: ViewHolder, bean: Air, position: Int) {
            holder.setText(R.id.name, bean.name)
            holder.setText(R.id.detail, bean.content)
            holder.setText(R.id.num, "${bean.num}")
            WeatherUtils.airColor(bean.air_color, holder.getView(R.id.air_color))
            var divider = holder.getView<View>(R.id.divider)
            var title = holder.getView<TextView>(R.id.title2)
            title.text = "2"
            title.isVisible(position == 2 || position == 3 || position == 5)
            if (position == 5) title.text = "3"
            divider.isVisible(!(position == 2 || position == 5))
        }
    }


    private fun DrawAirMarker(bean: List<AirPositionBean.Postion>?) {
        binding.mapview.map.clear()
        markers.clear()
        var boundsBuilder = LatLngBounds.Builder()
        bean.nN().forEach {
            var marker =
                MapUtils.addAirMarkersToMap(this,  binding.mapview.map, LatLng(it.lat, it.lng), it, isSmall)
            markers.add(AirStationMarkerBean().apply {
                this.marker = marker
                this.bean = it
            })
            boundsBuilder.include(marker.position)
        }
        binding.mapview.map.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 15))
    }

    private inner class PositionAdapter :
        CommonAdapter<AirPositionBean.Postion>(
            this,
            vm.airPositions,
            R.layout.item_air_position
        ) {
        override fun convert(holder: ViewHolder, bean: AirPositionBean.Postion, position: Int) {
            holder.setText(R.id.name, bean.name)
            holder.setText(R.id.air, bean.aqi)
            var shap = holder.getView<ShapeView>(R.id.shape_air)
            shap.text = bean.aqiLevel
            WeatherUtils.airColor(bean.aqiLevel, shap)

            holder.getView<LinearLayout>(R.id.ll).setSingleClickListener {
                binding.mapview.map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            vm.airPositions.nN().listIndex(position).lat,
                            vm.airPositions.nN().listIndex(position).lng
                        ), zoom
                    ),
                    618L,
                    null
                )
            }
        }
    }


    private inner class FiveAdapter :
        RecyclerViewAdapter<AirBean.AqdBean>(
            vm.airBean.value.nN().aqd,
            R.layout.item_fif_air
        ) {
        override fun bindDataToItemView(
            holder: BaseViewHolder,
            item: AirBean.AqdBean,
            position: Int
        ) {
            var time = DataUtil.isToday(DataUtil.date2Long(item.time, "yyyy-MM-dd"))
            holder.setTextViewText(
                R.id.time, time
            )
            var bg = holder.findView<ShapeLinearLayout>(R.id.ll_bg)
            bg.exeConfig(bg.getConfig().apply {
                this.centerColor = Color.parseColor(if (time == "今天") "#F4F5F9" else "#00F4F5F9")
            })
            holder.setTextViewText(
                R.id.date,
                DataUtil.timeStamp2Date(
                    DataUtil.date2Long(item.time, "yyyy-MM-dd"),
                    "MM-dd"
                ).replace("-", "/")
            )

            if (item.aqi.parseInt(0) == 0) {
                holder.setTextViewText(R.id.air_color, "")
                WeatherUtils.airColor("air_color", holder.findView(R.id.air_color))

            } else {
                holder.setTextViewText(R.id.air_color, item.aqiLevel)
                WeatherUtils.airColor(item.aqiLevel, holder.findView(R.id.air_color))
            }
        }
    }

    override fun onDestroy() {
        binding.mapview.onDestroy()
        LiveDataBus.unRegist(this.name)
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        binding.advMaterialView.showLeftFeedAd(this,AdConstant.SLOT_BIGKQZLXQ)
        XMLogAgent.onPageStart(EnumType.上报埋点.空气质量详情页)
        binding.mapview.onResume()
    }

    override fun onPause() {
        super.onPause()
        XMLogAgent.onPageEnd(EnumType.上报埋点.空气质量详情页)
        binding.mapview.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapview.onSaveInstanceState(outState)
    }

    override fun retry() {
        super.retry()
        vm.requestAirRank(true)
        vm.requestAir(isLocation, regioncode)
    }

}