package com.zhangsheng.shunxin.weather.widget

import com.amap.api.maps.AMap
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.*
import com.amap.api.maps.model.animation.Animation
import com.autonavi.amap.mapcore.DPoint
import com.autonavi.amap.mapcore.IPoint
import com.autonavi.amap.mapcore.MapProjection
import com.maiya.thirdlibrary.ext.listIndex
import kotlinx.coroutines.*
import java.util.*


class TyphoonMoveMarker2 {
    private val job by lazy { CoroutineScope(Dispatchers.Main + SupervisorJob()) }

    private var mAMap: AMap? = null
    private var duration = 10000L
    private val mStepDuration = 20L
    private val points: LinkedList<LatLng> = LinkedList()
    private val eachDistance: LinkedList<Double> = LinkedList()
    private var totalDistance = 0.0
    private var remainDistance = 0.0
    private val mLock = Any()
    private var marker: Marker? = null
    private var descriptor: BitmapDescriptor? = null
    private var index = 0
    private var useDefaultDescriptor = false
    private var moveListener: MoveListener? = null
    private var moveStatus: state? = null
    private var pauseMillis: Long = 0
    private var mAnimationBeginTime: Long = 0

    constructor(map: AMap) {
        this.moveStatus = state.prepare
        mAnimationBeginTime = System.currentTimeMillis()
        this.mAMap = map
    }

    fun setTotalDuration(var1: Float) {
        duration = (var1 * 1000).toLong()
    }

    fun setPoints(dates: List<LatLng?>?) {
        synchronized(mLock) {
            try {
                if (dates == null || dates.size < 2) {
                    return
                }
                this.stopMove()
                points.clear()
                val it = dates.iterator()
                while (it.hasNext()) {
                    it.next()?.let {
                        points.add(it)
                    }
                }
                eachDistance.clear()
                totalDistance = 0.0
                points.forEachIndexed { index, latLng ->
                    if (index != points.lastIndex) {
                        val distance =
                            AMapUtils.calculateLineDistance(points[index], points[index + 1])
                                .toDouble()
                        eachDistance.add(distance)
                        totalDistance += distance
                    }
                }
                remainDistance = totalDistance
                val latlng = points[0]
                if (marker != null) {
                    marker?.position = latlng
                    this.checkMarkerIcon()
                } else {
                    if (descriptor == null) {
                        useDefaultDescriptor = true
                    }
                    marker = mAMap?.addMarker(
                        MarkerOptions().belowMaskLayer(true).position(latlng).icon(descriptor)
                            .anchor(0.5f, 0.5f)
                    )
                }
                this.reset()
            } catch (var5: Throwable) {
                var5.printStackTrace()
            }
        }
    }

    fun stopMove() {
        if (moveStatus == state.move) {
            moveStatus = state.stop
            pauseMillis = System.currentTimeMillis()
        }
    }

    private fun reset() {
        try {
            if (moveStatus == state.move || moveStatus == state.stop) {
                if (marker != null) {
                    marker!!.setAnimation(null as Animation?)
                }
                moveStatus = state.prepare
            }
        } catch (var1: Exception) {
            var1.printStackTrace()
        }
    }

    private fun checkMarkerIcon() {
        if (useDefaultDescriptor) {
            if (descriptor == null) {
                useDefaultDescriptor = true
                return
            }
            marker!!.setIcon(descriptor)
            useDefaultDescriptor = false
        }
    }


    fun startSmoothMove() {
        if (moveStatus == state.stop) {
            val var1 = System.currentTimeMillis() - pauseMillis
            mAnimationBeginTime += var1
            moveStatus = state.move
        } else {
            if (moveStatus == state.prepare || moveStatus == state.end) {
                if (points.isEmpty()) return
                index = 0
                try {
                    MarkerMoveTask()
                } catch (var3: Throwable) {
                    var3.printStackTrace()
                }
            }
        }
    }

    private fun MarkerMoveTask() {
        try {
            if (points.isEmpty()) return
            mAnimationBeginTime = System.currentTimeMillis()
            moveStatus = state.start
            job.async(Dispatchers.IO) {

                while (index != points.size && moveStatus != state.stop) {
                    delay(mStepDuration)
                    withContext(Dispatchers.Main) {
                        val var2: Long = System.currentTimeMillis() - mAnimationBeginTime
                        getCurPosition(var2)?.let {
                            marker?.geoPoint = it
                            var dPoint = DPoint()
                            MapProjection.geo2LonLat(it.x, it.y, dPoint)
                            moveListener?.move(LatLng(dPoint.y, dPoint.x))
                        }
                    }
                    if (index==points.lastIndex) job.cancel()
                }
                this@TyphoonMoveMarker2.moveStatus = state.end

            }
        } catch (var5: Throwable) {
            var5.printStackTrace()
        }
    }

    fun setVisible(var1: Boolean) {
        marker?.isVisible = var1
    }

    fun setDescriptor(pic: BitmapDescriptor?) {
        descriptor?.recycle()
        descriptor = pic
        marker?.setIcon(pic)

    }

    fun removeMarker() {
        if (marker != null) {
            marker!!.remove()
            marker = null
        }
        points.clear()
        eachDistance.clear()
    }

    fun destroy() {
        try {
            reset()
            job.cancel()
            if (descriptor != null) {
                descriptor!!.recycle()
            }
            if (marker != null) {
                marker!!.destroy()
                marker = null
            }
            synchronized(mLock) {
                points.clear()
                eachDistance.clear()
            }
        } catch (var4: Throwable) {
            var4.printStackTrace()
        }
    }


    fun getMarker(): Marker? {
        return marker
    }

    fun getPosition(): LatLng? {
        return if (marker == null) null else marker!!.position
    }

    fun getIndex(): Int {
        return index
    }

    fun resetIndex() {
        index = 0
    }

    fun setPosition(latlng: LatLng) {
        if (marker != null) {
            marker!!.position = latlng
            checkMarkerIcon()
        } else {
            if (descriptor == null) {
                useDefaultDescriptor = true
            }
            marker = mAMap!!.addMarker(
                MarkerOptions().belowMaskLayer(true).position(latlng).icon(
                    descriptor
                ).anchor(0.5f, 0.5f)
            )
        }
    }

    fun setRotate(rotate: Float) {
        if (marker != null && mAMap != null && mAMap!!.cameraPosition != null) {
            marker!!.rotateAngle = 360.0f - rotate + mAMap!!.cameraPosition.bearing
        }
    }

    private fun getCurPosition(curTime: Long): IPoint? {
        if (curTime > duration) {
            val pointt = IPoint()
            index = points.size - 1
            val var16 = points[index] as LatLng
            --index
            index = Math.max(index, 0)
            remainDistance = 0.0
            MapProjection.lonlat2Geo(var16.longitude, var16.latitude, pointt)
            moveListener?.move(null)
            job.cancel()
            return pointt
        } else {
            var distance = curTime.toDouble() * totalDistance / duration.toDouble()
            remainDistance = totalDistance - distance

            var rate = 1.0
            var curIndex = 0
            kotlin.run {
                this.eachDistance.forEachIndexed { index, ed ->

                    if (distance <= ed) {
                        if (ed > 0.0) {
                            rate = distance / ed
                        }
                        curIndex = index
                        return@run
                    }
                    distance -= ed
                }
            }
            val curLatlng = points[curIndex]
            val nextLatlng = points[curIndex + 1]
            val curPoint = IPoint()
            val nextPoint = IPoint()

            MapProjection.lonlat2Geo(curLatlng.longitude, curLatlng.latitude, curPoint)
            MapProjection.lonlat2Geo(nextLatlng.longitude, nextLatlng.latitude, nextPoint)


            var xDistance = nextPoint.x - curPoint.x
            var yDistance = nextPoint.y - curPoint.y

            return IPoint(
                (curPoint.x.toDouble() + xDistance.toDouble() * rate).toInt(),
                (curPoint.y.toDouble() + yDistance.toDouble() * rate).toInt()
            )
        }
    }


    fun setMoveListener(var1: MoveListener?) {
        moveListener = var1
    }

    interface MoveListener {
        fun move(curLatLng: LatLng?)
    }

    private enum class state {
        prepare, start, move, stop, end
    }


}