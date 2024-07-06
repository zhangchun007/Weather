package com.zhangsheng.shunxin.weather.widget

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.maiya.thirdlibrary.utils.AppUtils

import com.zhangsheng.shunxin.databinding.LayoutHomeAnimatorViewBinding
import com.zhangsheng.shunxin.databinding.TabBottomBarBinding
import com.zhangsheng.shunxin.weather.utils.WeatherUtils

class HomeAnimatorView : RelativeLayout {

    private var mSensorManager: SensorManager? = null
    private var mSensor: Sensor? = null

    private var binding: LayoutHomeAnimatorViewBinding =
        LayoutHomeAnimatorViewBinding.inflate(LayoutInflater.from(context), this)

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        initView(attrs)
    }



    fun setWeatherStatus(status: String) {
        binding.weatherBg1.setBackgroundResource(WeatherUtils.getWeatherBg(status))
        binding.weatherBottomBg.setBackgroundResource(WeatherUtils.getWeatherBottomBg(status))

        binding.imageYun1?.cancelAnimation()
        binding.imageYun2?.cancelAnimation()
        WeatherUtils.setCloudAnimJson(status, binding.imageYun1, binding.imageYun2)
        binding.imageYun1?.playAnimation()
        binding.imageYun2?.playAnimation()

        binding.imageFull?.cancelAnimation()
        WeatherUtils.setFullAnimJson(status, binding.imageFull)
        binding.imageFull?.playAnimation()
    }

    private fun initView(attrs: AttributeSet?) {
//        LayoutInflater.from(context).inflate(R.layout.layout_home_animator_view, this)
        if (AppUtils.is4Gphone(context)) {
            mSensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            mSensor = mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        }

        binding.imageYun1?.imageAssetsFolder = "images"
        binding.imageYun1?.progress = 0f
        binding.imageYun1?.loop(true)

        binding.imageYun2?.imageAssetsFolder = "images"
        binding.imageYun2?.progress = 0f
        binding.imageYun2?.loop(true)

        binding.imageFull?.imageAssetsFolder = "images"
        binding.imageFull?.progress = 0f
        binding.imageFull?.loop(true)
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        val visible = visibility == VISIBLE && getVisibility() == VISIBLE
        if (visible) {
            mSensorManager?.registerListener(
                mSensorEventListener,
                mSensor,
                SensorManager.SENSOR_DELAY_GAME
            )
            binding.imageYun1?.playAnimation()
            binding.imageYun2?.playAnimation()
            binding.imageFull?.playAnimation()
        } else {
            mSensorManager?.unregisterListener(mSensorEventListener)
            binding.imageYun1?.pauseAnimation()
            binding.imageYun2?.pauseAnimation()
            binding.imageFull?.pauseAnimation()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding.imageYun1?.cancelAnimation()
        binding.imageYun2?.cancelAnimation()
        binding.imageFull?.cancelAnimation()
        mSensorManager?.unregisterListener(mSensorEventListener)
        mSensorManager = null
        mSensor = null
    }

    var x = 0
    var translaX = 0f
    var y = 0
    var translaY = 0f

    private val mSensorEventListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(sensorEvent: SensorEvent) {
            if (sensorEvent.accuracy != 0) {
                when (sensorEvent.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> {
                        x = sensorEvent.values[0].toInt()
                        if (x != 0) {
                            if (x > 2) x = 2
                            if (x < -2) x = -2
                            translaX = binding.weatherBottomBg.translationX - x
                            if (translaX > binding.imageModel.width) {
                                translaX = binding.imageModel.width.toFloat()
                            } else if (translaX < -binding.imageModel.width) {
                                translaX = -binding.imageModel.width.toFloat()
                            }
                            binding.weatherBottomBg.translationX = translaX
                            binding.imageYun1.translationX = translaX / 2
                            binding.imageYun2.translationX = translaX / 2
                        }

                        y = sensorEvent.values[2].toInt() - 6

                        if (y != 0) {
                            if (y > 2) y = 2
                            if (y < -2) y = -2
                            translaY = binding.weatherBottomBg.translationY - y
                            if (translaY > binding.imageModel.width) {
                                translaY = binding.imageModel.width.toFloat()
                            } else if (translaY < -binding.imageModel.width) {
                                translaY = -binding.imageModel.width.toFloat()
                            }
                            binding.weatherBottomBg.translationY = translaY
                            binding.imageYun1.translationY = translaY / 2
                            binding.imageYun2.translationY = translaY / 2
                        }
                    }
                }
            }
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

        }
    }
}