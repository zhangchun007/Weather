package com.zhangsheng.shunxin.weather.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.maiya.thirdlibrary.base.AacActivity
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.widget.smartlayout.adapter.SmartViewHolder
import com.maiya.thirdlibrary.widget.smartlayout.listener.SmartRecycleListener
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.databinding.ActivityTyphoonBinding
import com.zhangsheng.shunxin.weather.ext.LocationEllipsis
import com.zhangsheng.shunxin.weather.livedata.LiveDataBus
import com.zhangsheng.shunxin.weather.model.TyphoonViewModel
import com.zhangsheng.shunxin.weather.net.bean.CommonListBean
import com.zhangsheng.shunxin.weather.net.bean.Location
import com.zhangsheng.shunxin.weather.net.bean.TyphoonBean
import com.zhangsheng.shunxin.weather.utils.*
import org.koin.android.ext.android.inject


class TyphoonActivity : AacActivity<TyphoonViewModel>() {
    override val vm: TyphoonViewModel by inject()
    override val binding: ActivityTyphoonBinding by inflate()

    private var landMarker: Marker? = null

    override fun initView(savedInstanceState: Bundle?) {
        binding.mapview.onCreate(savedInstanceState)
        initMap()

        binding.title.initTitle(vm.cityBean.regionName)
        if (vm.cityBean.isLocation) {
            binding.title.setIcon(R.mipmap.icon_title_location_blue)
        }

        vm.locationMarker = binding.mapview.map.addMarker(
            MarkerOptions().position(vm.cityBean.latlng)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_title_location_blue))
        )

        vm.requestDate()

        binding.gv.setSmartListener(object : SmartRecycleListener() {
            override fun AutoAdapter(holder: SmartViewHolder, item: Any, position: Int) {
                super.AutoAdapter(holder, item, position)
                var bean = item as CommonListBean
                holder.setTextViewText(R.id.title, bean.title)
                holder.setTextViewText(R.id.desc, bean.desc)
            }
        })

        binding.zoomAdd.setSingleClickListener {
            binding.mapview.map.animateCamera(CameraUpdateFactory.zoomTo(vm.zoom + 1))
        }


        binding.btnLocation.setSingleClickListener {
            if (vm.cityBean.isLocation) {
                vm.location(this)
            } else {
                vm.locationMarker?.remove()

                binding.title.initTitle(
                    vm.cityBean.regionName
                )
                vm.locationMarker = binding.mapview.map.addMarker(
                    MarkerOptions().position(
                        vm.cityBean.latlng
                    ).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_title_location_blue))
                )
                binding.mapview.map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(vm.cityBean.latlng, vm.zoom),
                    618L,
                    null
                )
            }
        }

        binding.zoomCut.setSingleClickListener {
            binding.mapview.map.animateCamera(CameraUpdateFactory.zoomTo(vm.zoom - 1))
        }
        binding.mapview.map.addOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChange(p0: CameraPosition) {
                vm.zoom = p0.zoom
                LogE("zoom:${vm.zoom}")

                vm.zoomChanged(p0.zoom)
            }

            override fun onCameraChangeFinish(p0: CameraPosition) {

            }

        })

    }


    private fun initMap() {
        binding.mapview.map.apply {
            this.setCustomMapStyle(CustomMapStyleOptions().apply {
                this.isEnable = true
                this.styleData = MapUtils.getAssetsStyle(this@TyphoonActivity, "style.data")
                this.styleExtraData =
                    MapUtils.getAssetsStyle(this@TyphoonActivity, "style_extra.data")
            })
            uiSettings.isMyLocationButtonEnabled = false // 设置默认定位按钮是否显示
            uiSettings.isZoomControlsEnabled = false
            uiSettings.isCompassEnabled = false
            uiSettings.isScaleControlsEnabled = false
            uiSettings.isRotateGesturesEnabled = false
            uiSettings.isTiltGesturesEnabled = false // 倾斜
            isMyLocationEnabled = false // 设置为true表示显示定位层并可触发定位,false表示隐藏定位层并不可触发定位,默认是false

            vm.drawWarningLine(this)

        }

        binding.mapview.map.setOnPolylineClickListener {
            vm.clickPolyline(binding.mapview.map, it.id) { index ->
                startTyphoonAnim(index)
            }
        }

        binding.mapview.map.addOnMarkerClickListener { it ->
            Boolean
            vm.clickMarker(binding.mapview.map, it.id) { index ->
                startTyphoonAnim(index)
            }
            true
        }
    }

    private var landInfo: TyphoonBean.LandInfo? = null

    override fun initObserve() {
        super.initObserve()
        vm.typhoonData.safeObserve(this, Observer { list ->
            when {
                list.isNullOrEmpty() -> {
                    errorOrEmpty(false)
                }
                list.any { it.pastPath?.last()?.name.isNullOrEmpty() } -> {
                    errorOrEmpty(true)
                }
                list.any { it.pastPath.isNullOrEmpty() || it.forecast.isNullOrEmpty() } -> {
                    errorOrEmpty(true)
                }

                list.any {
                    it.landInfo.nN().landAddress.isNullOrEmpty() && it.landInfo.nN().landTime.isNullOrEmpty()
                            && it.pastPath?.last()?.pressuer.isNullOrEmpty() && it.pastPath?.last()?.lat.isNullOrEmpty()
                            && it.pastPath?.last()?.lng.isNullOrEmpty() && it.pastPath?.last()?.speedDirection.isNullOrEmpty()
                } -> {
                    errorOrEmpty(true)
                }

                else -> {
                    startTyphoonAnim(0)
                }

            }
        })

        LiveDataBus.getChannel<Location>(this.localClassName).safeObserve(this, Observer {
            if (it.nN().state == LocationUtil.定位成功) {
                vm.locationMarker?.remove()
                vm.cityBean.apply {
                    latlng = LatLng(it.nN().lat.toDouble(), it.nN().lng.toDouble())
                    regionName = LocationEllipsis(it.nN().district, true)
                }

                binding.title.initTitle(
                    vm.cityBean.regionName
                )
                vm.locationMarker = binding.mapview.map.addMarker(
                    MarkerOptions().position(
                        vm.cityBean.latlng
                    ).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_title_location_blue))
                )
                binding.mapview.map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(vm.cityBean.latlng, vm.zoom),
                    618L,
                    null
                )
            } else if (it.nN().state == LocationUtil.定位失败) {
                xToast("定位失败，请检查GPS")
            }
        })
    }


    override fun onError(code: Int, msg: String) {
        super.onError(code, msg)
        errorOrEmpty(true)
    }

    private fun startTyphoonAnim(chooseIndex: Int) {
        landMarker?.remove()
        var listIndex = vm.typhoonData.value.listIndex(chooseIndex)
        landInfo = listIndex.landInfo
        binding.llInfo.visibility = View.INVISIBLE
        binding.layoutNoTyphoon.isVisible(false)
        binding.rlDetail.isVisible(true)
        if (vm.cityBean.isLocation) {
            binding.tvDesc.isVisible(vm.cityBean.isLocation && listIndex.desc.isNotEmpty())
        } else {
            binding.tvDesc.isVisible(false)
        }
        binding.tvDesc.text = listIndex.desc
        if (!listIndex.pastPath.isNullOrEmpty()) {
            dealTyphoonUi(listIndex.pastPath.nN().last())
        }

//        var boundsBuilder = LatLngBounds.Builder()
//
//        var list = listIndex.forecast.nN() + listIndex.pastPath.nN()
//            .subList(0, listIndex.pastPath.nN().lastIndex - 1)

//        list.forEach {
//            boundsBuilder.include(LatLng(it.lat.parseDouble(0.0), it.lng.parseDouble(0.0)))
//        }
//        binding.mapview.map.animateCamera(
//            CameraUpdateFactory.newLatLngBounds(
//                boundsBuilder.build(),
//                200
//            ),800L,null
//        )

        removeToCenter(listIndex)


        vm.drawTyphoon(binding.mapview.map, chooseIndex) { _, isError ->
            runOnUi {
                binding.llInfo.apply {
                    visibility = View.VISIBLE
                    if (!isError) {
                        startAnimation(AnimationUtil.moveToViewLocation(350))
                        if (listIndex.landInfo.nN().landLocation.nN().lat.parseDouble() != 0.0 && listIndex.landInfo.nN().landLocation.nN().lng.parseDouble() != 0.0) {
                            landMarker = binding.mapview.map.addMarker(
                                MarkerOptions().position(
                                    LatLng(
                                        listIndex.landInfo.nN().landLocation.nN().lat.parseDouble(),
                                        listIndex.landInfo.nN().landLocation.nN().lng.parseDouble()
                                    )
                                ).apply {
                                    this.icon(
                                        BitmapDescriptorFactory.fromResource(R.mipmap.icon_typhoon_land)
                                    )
                                })
                        }
                    } else {
                        errorOrEmpty(isError)
                    }
                }
            }
        }

    }


    private fun errorOrEmpty(isError: Boolean) {
        vm.clearMap()
        binding.llInfo.isVisible(true)
        binding.layoutNoTyphoon.isVisible(true)
        binding.rlDetail.isVisible(false)
        binding.tvDesc.isVisible(false)

        binding.tvDate.text = DataUtil.curFormat("yyyy/MM/dd")
        if (isError) {
            binding.tvContent.text = "数据获取异常，请稍后再试"
        } else {
            binding.tvContent.text = "当前无台风"
        }
        if (vm.cityBean.latlng?.latitude!=0.0){
            binding.mapview.map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    vm.cityBean.latlng,
                    vm.zoom
                ), 1000L, null
            )
        }

    }

    private fun removeToCenter(listIndex: TyphoonBean) {
        binding.mapview.map.setPointToCenter(
            binding.mapview.measuredWidth / 2,
            (binding.mapview.measuredHeight - binding.llInfo.measuredHeight) / 2
        )

        binding.mapview.map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    listIndex.location.nN().lat.toDouble(),
                    listIndex.location.nN().lng.toDouble()
                ), vm.zoom
            ), 1000L, null
        )
    }

    private fun dealTyphoonUi(bean: TyphoonBean.TyphoonDetail) {
        if (bean.name.isNotEmpty()) {
            binding.name.text = bean.name
        }

        binding.strong.text =
            "${bean.strong} ${if (bean.power.isNotEmpty()) "${bean.power}级" else ""}"


        val list = ArrayList<CommonListBean>().apply {
            if (!bean.nN().power.isNullOrEmpty()) {
                this.add(CommonListBean("风力级别 ", "${bean.nN().power}级"))
            }

            if (bean.lat.isNotEmpty() && bean.lng.isNotEmpty()) {
                this.add(CommonListBean("中心位置", "${bean.lat}°N/${bean.lng}° E"))
            }
            if (bean.pressuer.isNotEmpty()) {
                this.add(CommonListBean("中心气压", bean.pressuer))
            }
            if (bean.speedDirection.isNotEmpty()) {
                this.add(CommonListBean("移速移向", bean.speedDirection))
            }

            if (!landInfo?.landAddress.isNullOrEmpty()) {
                this.add(CommonListBean("登陆地点", landInfo.nN().landAddress))
            }

            if (!landInfo?.landTime.isNullOrEmpty()) {
                this.add(CommonListBean("登陆时间", landInfo.nN().landTime))
            }

        }
        binding.gv.notifyData(list)
    }


    override fun onDestroy() {
        LiveDataBus.unRegist(this.localClassName)
        super.onDestroy()
        binding.mapview.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        binding.mapview.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapview.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapview.onSaveInstanceState(outState)
    }
}