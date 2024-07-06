package com.zhangsheng.shunxin.weather.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.SeekBar
import androidx.lifecycle.Observer
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
import com.xm.xmlog.XMLogAgent
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.databinding.ActivityWeatherMapBinding
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.net.bean.Location
import com.zhangsheng.shunxin.weather.ext.LocationEllipsis
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.livedata.LiveDataBus
import com.zhangsheng.shunxin.weather.model.WeatherMapModel
import com.zhangsheng.shunxin.weather.net.bean.cityBean
import com.zhangsheng.shunxin.weather.utils.DataUtil
import com.zhangsheng.shunxin.weather.utils.LocationUtil
import com.zhangsheng.shunxin.weather.utils.MapUtils
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import org.koin.android.ext.android.inject

class WeatherMapActivity : AacActivity<WeatherMapModel>(), AMap.OnMapClickListener,
    GeocodeSearch.OnGeocodeSearchListener {
    override val vm: WeatherMapModel by inject()
    override val binding by inflate<ActivityWeatherMapBinding>()
    private var locationMarker: Marker? = null
    private var position: Int = 0
    private var lastOverlay: GroundOverlay? = null
    private var nowOverlay: GroundOverlay? = null
    private var zoom = 8f
    private var isPlay = false
    private var isDismiss = false
    private var clickMarker: Marker? = null
    private var cityBean: cityBean = WeatherUtils.getCity()
    override fun initView(savedInstanceState: Bundle?) {
        binding.mapview.onCreate(savedInstanceState)

        Try {
            val isFromTab = intent.getBooleanExtra("isFromTab", false)

            if (!isFromTab) {
                cityBean.apply {
                    this.regionName =
                        getAppModel().currentWeather.value.nN().weather.nN().regionname
                    this.regionCode =
                        getAppModel().currentWeather.value.nN().weather.nN().regioncode
                    this.isLocation =
                        getAppModel().currentWeather.value.nN().weather.nN().isLocation
                    this.latlng = LatLng(
                        getAppModel().currentWeather.value.nN().weather.nN().latitude.parseDouble(),
                        getAppModel().currentWeather.value.nN().weather.nN().longitude.parseDouble()
                    )

                }
            }
        }

        initMap()
        binding.title.initTitle(
            cityBean.regionName
        )
        if (cityBean.isLocation) {
            binding.title.setIcon(R.mipmap.icon_location_black)
        }


        binding.btn.setSingleClickListener {
            xToast("点击地图查看地域降水量")
        }
        vm.requestWeatherCountryRain(
            "${cityBean.latlng?.latitude}",
            "${cityBean.latlng?.longitude}",
            "2"
        )
        vm.requestWeatherRain(
            "${cityBean.latlng?.latitude}",
            "${cityBean.latlng?.longitude}", cityBean.regionName
        )
        binding.dismiss.setSingleClickListener {
            isDismiss = true
            binding.chart.isVisible(false)
            binding.dismiss.isVisible(false)
            binding.des.text = "点击查看分钟级降雨"
        }

        binding.des.setSingleClickListener {
            if (isDismiss) {
                isDismiss = false
                binding.chart.isVisible(true)
                binding.dismiss.isVisible(true)
                binding.des.text = vm.rain.value.nN().desc
            }
        }

        binding.location.setSingleClickListener {
            if (cityBean.isLocation) {
                vm.location(this)
            } else {
                clickMarker?.remove()

                binding.title.initTitle(
                    cityBean.regionName
                )
                vm.requestCityWeather(
                    "${cityBean.latlng?.latitude}",
                    "${cityBean.latlng?.longitude}"
                )
                vm.requestWeatherRain(
                    "${cityBean.latlng?.latitude}",
                    "${cityBean.latlng?.longitude}",
                    cityBean.regionName
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
                binding.seekPlay.setImageResource(R.mipmap.icon_rain_seek_play)
            } else {
                Try {
                    if (vm.fall.value.nN().get("status").asString == "ok") {
                        isPlay = true
                        binding.seekPlay.setImageResource(R.mipmap.icon_rain_seek_resume)
                        anim()
                    } else {
                        vm.requestWeatherCountryRain(
                            "${cityBean.latlng?.latitude}",
                            "${cityBean.latlng?.longitude}",
                            "2"
                        )
                    }
                }

            }
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (vm.bitmaps[progress] != null) {
                    addOverLayToMap(vm.bitmaps[progress], progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isPlay = false
                binding.seekPlay.setImageResource(R.mipmap.icon_rain_seek_play)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }

    private fun anim() {
        Try {
            if (isPlay) {
                binding.mapview.postDelayed(Runnable {
                    position += 1
                    if (position == vm.fall.value.nN().getAsJsonArray("images").size()) {
                        position = 0
                    }
                    binding.seekBar.progress = position
                    anim()
                }, 100)
            }
        }
    }

    private fun initMap() {
        binding.mapview?.map?.apply {
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
            }

        })

        binding.mapview?.post {
            binding.mapview.map.setPointToCenter(
                binding.mapview.measuredWidth / 2,
                (binding.mapview.measuredHeight - binding.llInfo.measuredHeight) / 2
            )
            vm.requestCityWeather("${cityBean.latlng?.latitude}", "${cityBean.latlng?.longitude}")
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

    @SuppressLint("SetTextI18n")
    override fun initObserve() {
        vm.weather.safeObserve(this, Observer {
            binding.llWeatherDetail.isVisible(it.nN().tc.isNotEmpty())
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
                vm.requestWeatherRain(
                    "${latlng.latitude}",
                    "${latlng.longitude}", it.nN().district
                )
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

        vm.rain.safeObserve(this, Observer {
            if (it.nN().rss.nN().isNotEmpty()) {
                binding.des.text = it.nN().desc
                binding.chart.isVisible(true)
                binding.dismiss.isVisible(true)
                isDismiss = false
                binding.chart.refreshData(it.nN().rss.nN())
            } else {
                binding.chart.isVisible(false)
            }
        })

        vm.fall.safeObserve(this, Observer {
            Try {
                if (it.nN().get("status").asString == "ok") {
                    isPlay = true
                    binding.seekPlay.setImageResource(R.mipmap.icon_rain_seek_resume)
                    anim()
                    binding.time2.text = DataUtil.timeStamp2Date(
                        vm.fall.value.nN()
                            .getAsJsonArray("images")[6].asJsonArray[1].asLong * 1000,
                        "HH:mm"
                    )
                    binding.time3.text = DataUtil.timeStamp2Date(
                        vm.fall.value.nN()
                            .getAsJsonArray("images")[12].asJsonArray[1].asLong * 1000,
                        "HH:mm"
                    )
                    binding.time4.text = DataUtil.timeStamp2Date(
                        vm.fall.value.nN()
                            .getAsJsonArray("images")[18].asJsonArray[1].asLong * 1000,
                        "HH:mm"
                    )
                    var max = vm.fall.value.nN().getAsJsonArray("images").size()
                    if (max > 0) {
                        vm.downLoadPic(this, 0)
                    }
                }


            }
        })
    }

    private fun addOverLayToMap(bitmap: Bitmap?, index: Int) {
        var bounds = LatLngBounds.Builder()//设置显示在屏幕中的地图地理范围,地图中点显示为两个点的中点
            .include(
                LatLng(
                    vm.fall.value.nN().getAsJsonArray("images")
                        .get(index).asJsonArray.get(2).asJsonArray.get(0).asDouble,
                    vm.fall.value.nN().getAsJsonArray("images")
                        .get(index).asJsonArray.get(2).asJsonArray.get(1).asDouble
                )
            )
            .include(
                LatLng(
                    vm.fall.value.nN().getAsJsonArray("images")
                        .get(index).asJsonArray.get(2).asJsonArray.get(2).asDouble,
                    vm.fall.value.nN().getAsJsonArray("images")
                        .get(index).asJsonArray.get(2).asJsonArray.get(3).asDouble
                )
            ).build()
        nowOverlay = binding.mapview.map.addGroundOverlay(
            GroundOverlayOptions()
                .transparency(0.0f)//设置覆盖物的透明度，范围：0.0~1.0
                .zIndex(0f)//设置覆盖物的层次，zIndex值越大越在上层；
                .image(BitmapDescriptorFactory.fromBitmap(bitmap))//覆盖物图片
                .visible(zoom <= 16)
                .positionFromBounds(bounds)
        )
        lastOverlay?.remove()
        lastOverlay = nowOverlay
    }

    override fun onMapClick(latlng: LatLng?) {
        clickMarker?.remove()
        latlng?.let {
            clickMarker = binding.mapview.map.addMarker(
                MapUtils.drawMarker(
                    latlng,
                    R.mipmap.icon_click_marker
                )
            )
            getAddressByLatlng(latlng)
            binding.mapview.map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(latlng, zoom),
                618L,
                null
            )
            vm.requestCityWeather("${latlng.latitude}", "${latlng.longitude}")
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

    override fun onRegeocodeSearched(result: RegeocodeResult?, rCode: Int) {
        var title = "${result.nN().regeocodeAddress.city} ${result.nN().regeocodeAddress.district}"

        if (title.trim().isEmpty()) {
            title = MapUtils.GooglePlace(clickMarker.nN().position, this)
        }
        binding.title.initTitle(title)
        vm.requestWeatherRain(
            "${clickMarker.nN().position.latitude}",
            "${clickMarker.nN().position.longitude}", result.nN().regeocodeAddress.city
        )
    }

    override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        isPlay = false
        LiveDataBus.unRegist(this.localClassName)
        binding.mapview.onDestroy()
    }

    override fun onResume() {
        XMLogAgent.onPageStart(EnumType.上报埋点.分钟级降雨详情页)
        super.onResume()
        binding.mapview.onResume()
    }

    override fun onPause() {
        XMLogAgent.onPageEnd(EnumType.上报埋点.分钟级降雨详情页)
        isPlay = false
        super.onPause()
        binding.seekPlay.setImageResource(R.mipmap.icon_rain_seek_play)
        binding.mapview.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapview.onSaveInstanceState(outState)
    }
}