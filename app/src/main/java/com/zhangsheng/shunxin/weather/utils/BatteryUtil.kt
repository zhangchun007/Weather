package com.zhangsheng.shunxin.weather.utils

import android.content.Context.BATTERY_SERVICE
import android.os.BatteryManager
import com.maiya.thirdlibrary.utils.AppContext

object BatteryUtil {

    private val mBatteryManager: BatteryManager by lazy {
        AppContext.getContext().getSystemService(BATTERY_SERVICE) as BatteryManager
    }

    /**
     * 总电量
     */
    fun getBatteryTotal(): Int {
        return mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER) / 1000
    }

    /**
     * 剩余电量
     */
    fun getBatteryNow(): Int {
        return mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) / 100 * getBatteryTotal()
    }

    /**
     * 获取不同使用场景的 剩余时间
     */
    fun getPhoneStatusTime(style: PhoneStatus): Float {
        when (style) {
            PhoneStatus.IDLE -> {//空闲
                return getBatteryNow().toFloat() / (3 + 10 + 31)
            }
            PhoneStatus.VIDEO -> {//视频
                return getBatteryNow().toFloat() / (50 + 300 + 31)
            }
            PhoneStatus.AUDIO -> {//声音
                return getBatteryNow().toFloat() / (3 + 10 + 31 + 10)
            }
            PhoneStatus.CAMERA -> {//相机
                return getBatteryNow().toFloat() / (600 + 300 + 10 + 31)
            }
        }
        return 0f
    }


    enum class PhoneStatus {
        IDLE, VIDEO, AUDIO, CAMERA
    }

}