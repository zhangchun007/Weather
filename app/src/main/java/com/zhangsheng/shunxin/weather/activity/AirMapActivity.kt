package com.zhangsheng.shunxin.weather.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.maiya.thirdlibrary.base.AacActivity
import com.maiya.thirdlibrary.ext.*
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.databinding.ActivityAirMapBinding
import com.zhangsheng.shunxin.weather.ext.LocationEllipsis
import com.zhangsheng.shunxin.weather.livedata.LiveDataBus
import com.zhangsheng.shunxin.weather.model.AirMapViewModel
import com.zhangsheng.shunxin.weather.net.bean.Location
import com.zhangsheng.shunxin.weather.utils.LocationUtil
import com.zhangsheng.shunxin.weather.utils.MapUtils
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class AirMapActivity : AacActivity<AirMapViewModel>(), AMap.OnMapClickListener,
    GeocodeSearch.OnGeocodeSearchListener {
    private var locationMarker: Marker? = null
    private var position: Int = 0
    private var lastOverlay: GroundOverlay? = null
    private var nowOverlay: GroundOverlay? = null
    private var zoom = 4.17f
    private var isPlay = false
    private var clickMarker: Marker? = null
    private val cityBean by lazy { WeatherUtils.getCity() }


    override val vm: AirMapViewModel by inject()
    override val binding: ActivityAirMapBinding by inflate()

    override fun initView(savedInstanceState: Bundle?) {

        binding.mapview.onCreate(savedInstanceState)

        initMap()
        binding.title.initTitle(cityBean.regionName)
        if (cityBean.isLocation) {
            binding.title.setIcon(R.mipmap.icon_location_black)
        }

        vm.requestAirMapDate("${cityBean.latlng?.latitude}", "${cityBean.latlng?.longitude}")


        binding.location.setSingleClickListener {
            if (cityBean.isLocation) {
                vm.location(this)
            } else {
                clickMarker?.remove()

                binding.title.initTitle(cityBean.regionName)
                vm.requestCityWeather(
                    "${cityBean.latlng?.latitude}",
                    "${cityBean.latlng?.longitude}"
                )
                clickMarker = binding.mapview.map.addMarker(
                    MapUtils.drawMarker(
                        cityBean.latlng.nN(),
                        R.mipmap.icon_click_marker
                    )
                )
                binding.mapview.map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(cityBean.latlng.nN(), zoom),
                    618L,
                    null
                )
            }
        }

        binding.seekPlay.setSingleClickListener {
            if (isPlay) {
                isPlay = false
                binding.seekPlay.setImageResource(R.mipmap.icon_air_map_play_bar)
            } else {
                Try {
                    isPlay = true
                    binding.seekPlay.setImageResource(R.mipmap.icon_air_map_pause_bar)
                    anim()
                }

            }
        }
        binding.zoomAdd.setSingleClickListener {
            var position = if (clickMarker != null) clickMarker!!.position else cityBean.latlng.nN()
            binding.mapview.map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    position,
                    zoom + 1
                )
            )
        }
        binding.zoomCut.setSingleClickListener {
            var position = if (clickMarker != null) clickMarker!!.position else cityBean.latlng.nN()
            binding.mapview.map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    position,
                    zoom - 1
                )
            )
        }
    }

    private fun initMap() {
        binding.mapview?.map?.apply {
            this.setCustomMapStyle(CustomMapStyleOptions().apply {
                this.isEnable = true
                this.styleData = MapUtils.getAssetsStyle(this@AirMapActivity, "style.data")
                this.styleExtraData =
                    MapUtils.getAssetsStyle(this@AirMapActivity, "style_extra.data")
            })
            uiSettings?.isMyLocationButtonEnabled = false // 设置默认定位按钮是否显示
            uiSettings?.isZoomControlsEnabled = false
            uiSettings?.isCompassEnabled = false
            uiSettings?.isScaleControlsEnabled = false
            uiSettings?.isRotateGesturesEnabled = false
            uiSettings?.isTiltGesturesEnabled = false // 倾斜
            isMyLocationEnabled = false // 设置为true表示显示定位层并可触发定位,false表示隐藏定位层并不可触发定位,默认是false
        }

        binding.mapview?.map?.setOnMapClickListener(this)
        binding.mapview?.map?.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChangeFinish(p0: CameraPosition?) {

            }

            override fun onCameraChange(p0: CameraPosition?) {
                zoom = p0.nN().zoom
                LogE("zoom:$zoom")
            }

        })

        binding.mapview?.post {
            binding.mapview.map.setPointToCenter(
                binding.mapview.measuredWidth / 2,
                (binding.mapview.measuredHeight - binding.llInfo.measuredHeight) / 2
            )
            vm.requestCityWeather(
                "${cityBean.latlng.nN().latitude}",
                "${cityBean.latlng.nN().longitude}"
            )
            clickMarker = binding.mapview.map.addMarker(
                MapUtils.drawMarker(
                    cityBean.latlng.nN(),
                    if (!cityBean.isLocation) R.mipmap.icon_click_marker else R.mipmap.icon_location_marker
                )
            )
            binding.mapview.map.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition(
                        cityBean.latlng.nN(),
                        zoom,
                        0f,
                        0f
                    )
                )
            )

        }
    }

    override fun initObserve() {
        super.initObserve()
        vm.weather.safeObserve(this, Observer {
            binding.llInfo.visibility =
                if (it.nN().tc.isNotEmpty()) View.VISIBLE else View.INVISIBLE
            binding.weatherTemp.text = it.nN().tc + "°"
            binding.weatherStatus.text = it.nN().wt
            WeatherUtils.airColor(it.nN().aqiLevel, binding.airColor)
            binding.airColor.isVisible(it.nN().aqi != "0")
            binding.air.isVisible(it.nN().aqi != "0")
            binding.air.text = "${it.nN().aqiLevel} ${it.nN().aqi}"
            binding.cloud.text = "${it.nN().wdir} ${it.nN().ws}"
            binding.weight.text = "湿度 ${it.nN().rh}%"
            binding.iconWeather.setBackgroundResource(WeatherUtils.IconBig(it.nN().wtid))
        })

        LiveDataBus.getChannel<Location>(this.localClassName).safeObserve(this, Observer {
            if (it.nN().state == LocationUtil.定位成功) {
                locationMarker?.remove()
                var latlng = LatLng(it.nN().lat.toDouble(), it.nN().lng.toDouble())

                binding.title.initTitle(
                    LocationEllipsis(
                        it.nN().district,
                        true
                    )
                )
                vm.requestCityWeather("${latlng.latitude}", "${latlng.longitude}")
                locationMarker = binding.mapview.map.addMarker(
                    MapUtils.drawMarker(
                        latlng,
                        R.mipmap.icon_location_marker
                    )
                )
                binding.mapview.map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(latlng, zoom),
                    618L,
                    null
                )
            } else if (it.nN().state == LocationUtil.定位失败) {
                xToast("定位失败，请检查GPS")
            }
        })

        vm.airBean.safeObserve(this, Observer {
            Try {
                binding.seekPlay.setImageResource(R.mipmap.icon_air_map_pause_bar)
                Try {
                    var list = ArrayList<Long>()
                    it.getAsJsonArray("images").forEach {
                        list.add(it.asJsonArray.get(1).asString.parseLong() * 1000)
                    }
                    binding.seekBar.setDate(list)
                }
                var max = it.getAsJsonArray("images").size()
                if (max > 0) {
                    vm.downLoadPic(this) { index, isLoaded ->
                        if (this.isFinishing || this.isDestroyed) return@downLoadPic
                        position = index
                        isPlay = true
                        if (isLoaded) {
                            anim()
                        } else {
                            if (vm.bitmaps[position] != null) {
                                binding.seekBar.setProgress(position)
                                addOverLayToMap(vm.bitmaps[position], position)
                            }
                        }
                    }
                }
            }
        })
    }

    var job: Deferred<Unit>? = null
    private fun anim() {
        Try {
            vm.viewModelScope.async(Dispatchers.IO) {
                async {
                    while (isPlay && isActive) {

                        withContext(Dispatchers.Main) {
                            if (vm.bitmaps[position] != null) {
                                binding.seekBar.setProgress(position)
                                addOverLayToMap(vm.bitmaps[position], position)
                            }
                            position += 1
                            if (position >= vm.airBean.value.nN().getAsJsonArray("images").size()) {
                                position = 0
                            }
                        }
                        delay(100)
                    }
                }
            }
        }
    }


    private fun addOverLayToMap(bitmap: Bitmap?, index: Int) {
        var bounds = LatLngBounds.Builder()//设置显示在屏幕中的地图地理范围,地图中点显示为两个点的中点
            .include(
                LatLng(
                    vm.airBean.value.nN().getAsJsonArray("images")
                        .get(index).asJsonArray.get(2).asJsonArray.get(0).asString.parseDouble(),
                    vm.airBean.value.nN().getAsJsonArray("images")
                        .get(index).asJsonArray.get(2).asJsonArray.get(1).asString.parseDouble()
                )
            )
            .include(
                LatLng(
                    vm.airBean.value.nN().getAsJsonArray("images")
                        .get(index).asJsonArray.get(2).asJsonArray.get(2).asString.parseDouble(),
                    vm.airBean.value.nN().getAsJsonArray("images")
                        .get(index).asJsonArray.get(2).asJsonArray.get(3).asString.parseDouble()
                )
            ).build()
        nowOverlay = binding.mapview.map.addGroundOverlay(
            GroundOverlayOptions()
                .transparency(0.0f)//设置覆盖物的透明度，范围：0.0~1.0
                .zIndex(0f)//设置覆盖物的层次，zIndex值越大越在上层；
                .image(BitmapDescriptorFactory.fromBitmap(bitmap))//覆盖物图片
                .positionFromBounds(bounds)
        )
        lastOverlay?.remove()
        lastOverlay = nowOverlay
    }


    override fun onMapClick(p0: LatLng?) {
        clickMarker?.remove()
        p0?.let {
            clickMarker = binding.mapview.map.addMarker(
                MapUtils.drawMarker(
                    p0,
                    R.mipmap.icon_click_marker
                )
            )
            getAddressByLatlng(p0)
            binding.mapview.map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(p0, zoom),
                618L,
                null
            )
            vm.requestCityWeather("${p0.latitude}", "${p0.longitude}")
        }
    }

    private fun getAddressByLatlng(latLng: LatLng) {

        var geocodeSearch = GeocodeSearch(this)
        geocodeSearch.setOnGeocodeSearchListener(this)

        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
        var latLonPoint = LatLonPoint(latLng.latitude, latLng.longitude)
        var query = RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP)
        //异步查询
        geocodeSearch.getFromLocationAsyn(query)
    }


    override fun onRegeocodeSearched(result: RegeocodeResult?, p1: Int) {
        var title = "${result.nN().regeocodeAddress.city} ${result.nN().regeocodeAddress.district}"

        if (title.trim().isEmpty()) {
            title = MapUtils.GooglePlace(clickMarker.nN().position, this)
        }
        binding.title.initTitle(title)
    }

    override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.onDestroy()
        job?.cancel()
        binding.mapview.onDestroy()
        isPlay = false
        LiveDataBus.unRegist(this.localClassName)
    }

    override fun onResume() {
        super.onResume()
        binding.mapview.onResume()
    }

    override fun onPause() {
        super.onPause()
        isPlay = false
        binding.seekPlay.setImageResource(R.mipmap.icon_air_map_play_bar)
        binding.mapview.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapview.onSaveInstanceState(outState)
    }
}