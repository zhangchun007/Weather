package com.zhangsheng.shunxin.weather.widget

import com.amap.api.maps.AMap
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.*
import com.amap.api.maps.model.animation.Animation
import com.autonavi.amap.mapcore.IPoint
import com.autonavi.amap.mapcore.MapProjection
import com.maiya.thirdlibrary.ext.listIndex
import kotlinx.coroutines.*
import java.util.*


class TyphoonMoveMarker {
    private val job by lazy { CoroutineScope(Dispatchers.Main + SupervisorJob()) }

    private var mAMap: AMap? = null
    private val mStepDuration = 500L
    private var freshenTime = 50L
    private val points: LinkedList<LatLng> = LinkedList()
    private val eachDistance: LinkedList<Double> = LinkedList()
    private val mLock = Any()
    private var marker: Marker? = null
    private var descriptor: BitmapDescriptor? = null
    private var index = 0
    private var useDefaultDescriptor = false
    private var moveListener: MoveListener? = null
    private var moveStatus: state? = null

    constructor(map: AMap) {
        this.moveStatus = state.prepare
        this.mAMap = map
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

                points.forEachIndexed { index, latLng ->
                    if (index != points.lastIndex) {
                        val distance =
                            AMapUtils.calculateLineDistance(points[index], points[index + 1])
                                .toDouble()
                        eachDistance.add(distance)
                    }
                }
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

    //    private float getRotate(IPoint var1, IPoint var2) {
    //        if (var1 != null && var2 != null) {
    //            double var3 = (double)var2.y;
    //            double var5 = (double)var1.y;
    //            double var7 = (double)var1.x;
    //            return (float)(Math.atan2((double)var2.x - var7, var5 - var3) / 3.141592653589793D * 180.0D);
    //        } else {
    //            return 0.0F;
    //        }
    //    }
    fun stopMove() {
        if (moveStatus == state.move) {
            moveStatus = state.stop
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
            this@TyphoonMoveMarker.moveStatus = state.start
            job.async(Dispatchers.IO) {

                while (index != points.size && moveStatus != state.stop) {
                    var curIndex = index
                    var time = 0L
                    withContext(Dispatchers.Main) {
                        this@TyphoonMoveMarker.marker?.geoPoint = getIndexPosition(curIndex)
                    }
                    if (curIndex == points.lastIndex) cancel()
                    while (curIndex == index && index != points.lastIndex) {
                        this@TyphoonMoveMarker.moveStatus = state.start
                        delay(freshenTime)
                        time += freshenTime
                        withContext(Dispatchers.Main) {
                            getCurPosition(time)?.let {
                                marker?.geoPoint = it
                            }
                        }
                    }
                }
                this@TyphoonMoveMarker.moveStatus = state.end

            }
        } catch (var5: Throwable) {
            var5.printStackTrace()
        }
    }

    private fun getIndexPosition(index: Int): IPoint {
        var latlng = points.listIndex(index)

        var point = IPoint()
        MapProjection.lonlat2Geo(latlng.longitude, latlng.latitude, point)
        moveListener?.move(index)
        return point

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
        var var2: CameraPosition
        if (marker != null && mAMap != null && mAMap!!.cameraPosition != null) {
            marker!!.rotateAngle = 360.0f - rotate + mAMap!!.cameraPosition.bearing
        }
    }

    private fun getCurPosition(curTime: Long): IPoint? {
        if (curTime >= mStepDuration) {
            index++
            return null
        }
        var rate = curTime / mStepDuration

        val curLatlng = points[index]
        val nextLatlng = points[index + 1]


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

//    private float getRotate(IPoint var1, IPoint var2) {
//        if (var1 != null && var2 != null) {
//            double var3 = (double)var2.y;
//            double var5 = (double)var1.y;
//            double var7 = (double)var1.x;
//            return (float)(Math.atan2((double)var2.x - var7, var5 - var3) / 3.141592653589793D * 180.0D);
//        } else {
//            return 0.0F;
//        }
//    }

    fun setMoveListener(var1: MoveListener?) {
        moveListener = var1
    }

    interface MoveListener {
        fun move(curIndex: Int)
    }

    private enum class state {
        prepare, start, move, stop, end
    }


}