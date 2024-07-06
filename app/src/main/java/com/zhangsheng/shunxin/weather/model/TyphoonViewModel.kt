package com.zhangsheng.shunxin.weather.model

import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Color
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import androidx.lifecycle.viewModelScope
import com.amap.api.maps.AMap
import com.amap.api.maps.model.*
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.listIndex
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.ext.parseDouble
import com.maiya.thirdlibrary.ext.xToast
import com.maiya.thirdlibrary.net.bean.NetStatus
import com.maiya.thirdlibrary.net.callback.CallResult
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.ext.net
import com.zhangsheng.shunxin.weather.livedata.SafeMutableLiveData
import com.zhangsheng.shunxin.weather.net.bean.CustomMarker
import com.zhangsheng.shunxin.weather.net.bean.CustomPolyline
import com.zhangsheng.shunxin.weather.net.bean.TyphoonBean
import com.zhangsheng.shunxin.weather.net.bean.cityBean
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import com.zhangsheng.shunxin.weather.widget.TyphoonMoveMarker2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay


/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2019/12/6 21:01
 */
class TyphoonViewModel : BaseViewModel() {
    val cityBean: cityBean by lazy { WeatherUtils.getCity() }
    var typhoonData: SafeMutableLiveData<List<TyphoonBean>> = SafeMutableLiveData()
    var locationMarker: Marker? = null
    var polylines: HashMap<String, CustomPolyline> = HashMap<String, CustomPolyline>()
    var markers: HashMap<String, CustomMarker> = HashMap<String, CustomMarker>()
    var zoom = 4.09f
    private var visibleZoom = 3f
    private var chooseIndex = 0

    val warningLine24: ArrayList<LatLng> by lazy {
        ArrayList<LatLng>().apply {
            add(LatLng(34.0, 127.0))
            add(LatLng(22.0, 127.0))
            add(LatLng(18.0, 119.0))
            add(LatLng(11.0, 119.0))
            add(LatLng(4.5, 113.0))
            add(LatLng(0.0, 105.0))
        }
    }
    val warningLine48: ArrayList<LatLng> by lazy {
        ArrayList<LatLng>().apply {
            add(LatLng(34.0, 132.0))
            add(LatLng(15.0, 132.0))
            add(LatLng(0.0, 120.0))
            add(LatLng(0.0, 105.0))
        }
    }

    fun location(context: Activity) {
        getAppModel().location(context.localClassName)
    }

    fun drawWarningLine(aMap: AMap) {
        aMap.apply {
            addPolyline(
                PolylineOptions().addAll(warningLine24).width(4f)
                    .color(Color.parseColor("#FF4D16"))
            )
            addPolyline(
                PolylineOptions().addAll(warningLine48).width(10f)
                    .color(Color.parseColor("#FFA91E")).setDottedLine(true)
            )

            this.addMarker(MarkerOptions().position(LatLng(29.9, 127.5)).apply {
                this.icon(
                    BitmapDescriptorFactory.fromResource(R.mipmap.icon_typhone_warning24text)
                )
            })

            this.addMarker(MarkerOptions().position(LatLng(29.9, 132.5)).apply {
                this.icon(
                    BitmapDescriptorFactory.fromResource(R.mipmap.icon_typhone_warning48text)
                )
            })
        }
    }

    fun requestDate() {
        callNativeApi({
            net().台风详情(
                "${cityBean.latlng.nN().longitude}",
                "${cityBean.latlng.nN().latitude}"

            )
        },
            object : CallResult<List<TyphoonBean>>() {

                override fun ok(result: List<TyphoonBean>?) {
                    super.ok(result)
                    typhoonData.value = result
                }

                override fun failed(code: Int, msg: String) {
                    super.failed(code, msg)
                    loadFail(code,msg)
                }
            }
        )
    }

    fun drawTyphoon(map: AMap?, chooseIndex: Int, func: (Int, isError:Boolean) -> Unit) {
        if (map == null) return
        this.chooseIndex = chooseIndex
        clearMap()

        val pastPathLocation = ArrayList<LatLng>()
        val forecastLocation = ArrayList<LatLng>()

        typhoonData.value?.forEachIndexed { i, typhoonBean ->
            val anotherPastPolylines = ArrayList<LatLng>()
            val anotherForecastPolylines = ArrayList<LatLng>()
            typhoonBean.pastPath?.forEachIndexed { index, typhoonDetail ->
                if (typhoonDetail.lat.parseDouble() != 0.0 && typhoonDetail.lng.parseDouble() != 0.0) {
                    val latlng =
                        LatLng(typhoonDetail.lat.parseDouble(), typhoonDetail.lng.parseDouble())
                    if (chooseIndex == i) {
                        pastPathLocation.add(latlng)
                    } else {
                        anotherPastPolylines.add(latlng)
                        if (index == typhoonBean.pastPath?.lastIndex) {
                            map.addMarker(
                                MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_typhoon_tp))
                                    .anchor(0.5f, 0.5f).position(latlng)
                                    .alpha(0.5f)
                            ).apply {
                                markers[this.id] = CustomMarker(i, this, 1)
                            }

                        }
                    }
                }
            }
            typhoonBean.forecast?.forEachIndexed { index, typhoonDetail ->
                if (typhoonDetail.lat.parseDouble() != 0.0 && typhoonDetail.lng.parseDouble() != 0.0) {
                    val latlng =
                        LatLng(typhoonDetail.lat.parseDouble(), typhoonDetail.lng.parseDouble())
                    if (chooseIndex == i) {
                        forecastLocation.add(latlng)
                    } else {
                        anotherForecastPolylines.add(latlng)
                        map.addMarker(MarkerOptions().apply {
                            this.icon(
                                BitmapDescriptorFactory.fromResource(
                                    R.mipmap.icon_typhoon_forecast_point
                                )
                            )
                            this.anchor(0.5f, 0.5f)
                            this.visible(zoom >= visibleZoom)
                            this.position(latlng)
                            this.alpha(0.5f)
                        }).apply {
                            markers[this.id] = CustomMarker(i, this)
                        }
                    }
                }
            }
            if (chooseIndex == i) {
                if (map == null || (forecastLocation.isEmpty() || pastPathLocation.isEmpty())) {
                    func.invoke(chooseIndex, true)
                    return
                }
                drawChooseLine(forecastLocation, pastPathLocation, map, chooseIndex, func)
            } else {
                map.addPolyline(
                    PolylineOptions().addAll(anotherPastPolylines).width(18f)
                        .color(Color.parseColor("#80088EFF"))
                ).apply {
                    polylines[this.id] = CustomPolyline(i, this)
                }

                map.addPolyline(
                    PolylineOptions().addAll(anotherForecastPolylines).width(50f)
                        .setCustomTexture(BitmapDescriptorFactory.fromResource(R.mipmap.icon_typhoon_forecast_line_alpha))

                ).apply {
                    polylines[this.id] = CustomPolyline(i, this)
                }
            }
        }

    }

    fun clearMap() {
        polylines.forEach {
            it.value.polyline?.remove()
        }
        markers.forEach {
            it.value.marker?.remove()
        }

        polylines.clear()
        markers.clear()
    }


    private fun drawChooseLine(
        forecastLocation: List<LatLng>,
        pastPathLocation: List<LatLng>,
        map: AMap,
        chooseIndex: Int,
        func: (Int, Boolean) -> Unit
    ) {
        val smoothMarker = TyphoonMoveMarker2(map)
        smoothMarker.setTotalDuration(3f)
        smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.mipmap.icon_typhoon_tp))
        smoothMarker.setPoints(pastPathLocation)
        ValueAnimator.ofFloat(0f, 360f).apply {
            repeatCount = Animation.INFINITE
            interpolator = LinearInterpolator()
            duration = 600
            addUpdateListener {
                smoothMarker.setRotate(it.animatedValue as Float)
            }
        }.start()

        smoothMarker.getMarker()?.let {
            markers[it.id] = CustomMarker(chooseIndex, it, 1)
        }

        smoothMarker.startSmoothMove()
        var forecastIndex = 0
        var oldLatlng: LatLng? = null
        smoothMarker.setMoveListener(object : TyphoonMoveMarker2.MoveListener {
            override fun move(curLatLng: LatLng?) {
                if (curLatLng != null) {
                    map.addPolyline(
                        PolylineOptions().addAll(
                            arrayListOf(
                                if (oldLatlng == null) pastPathLocation.listIndex(0) else oldLatlng,
                                curLatLng
                            )
                        )
                            .width(18f)
                            .color(Color.parseColor("#088EFF"))
                    ).apply {
                        polylines[this.id] = CustomPolyline(chooseIndex, this)
                    }

                    oldLatlng = curLatLng
                } else {
                    viewModelScope.async(Dispatchers.IO) {
                        delay(300L)
                        while (forecastIndex != forecastLocation.lastIndex && forecastLocation.isNotEmpty()) {

                            map.addPolyline(
                                PolylineOptions().addAll(
                                    arrayListOf(
                                        forecastLocation.listIndex(forecastIndex),
                                        forecastLocation.listIndex(forecastIndex + 1)
                                    )
                                ).width(50f)
                                    .setCustomTexture(BitmapDescriptorFactory.fromResource(R.mipmap.icon_typhoon_forecast_line))
                            ).apply {
                                polylines[this.id] = CustomPolyline(chooseIndex, this)
                            }
                            forecastIndex++
                            map.addMarker(MarkerOptions().apply {
                                this.icon(
                                    BitmapDescriptorFactory.fromResource(
                                        R.mipmap.icon_typhoon_forecast_point
                                    )
                                )
                                this.anchor(0.5f, 0.5f)
                                this.visible(zoom >= visibleZoom)
                                this.position(forecastLocation.listIndex(forecastIndex))
                            }).apply {
                                markers[this.id] = CustomMarker(chooseIndex, this)
                            }
                        }
                        func(chooseIndex, false)
                        cancel()
                    }
                }
            }

        })
    }

    fun zoomChanged(zoom: Float) {
//        markers.forEach {
//            it.value?.apply {
//                if (this.type == 0) {
//                    this.marker?.isVisible = zoom >= visibleZoom
//                }
//            }
//        }
    }

    fun clickMarker(map: AMap?, id: String, func: (index: Int) -> Unit) {
        markers[id]?.index?.let {
            if (chooseIndex == it) return
            func(it)
        }
    }

    fun clickPolyline(map: AMap?, id: String, func: (index: Int) -> Unit) {

        polylines[id]?.index?.let {
            if (chooseIndex == it) return
            func(it)
        }
    }

}