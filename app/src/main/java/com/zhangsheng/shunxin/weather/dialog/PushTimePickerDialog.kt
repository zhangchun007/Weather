package com.zhangsheng.shunxin.weather.dialog

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import com.maiya.thirdlibrary.base.BaseDialog
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.inflate
import com.maiya.thirdlibrary.ext.xToast
import com.maiya.thirdlibrary.utils.CacheUtil
import com.xm.xmcommon.XMCommonManager
import com.zhangsheng.shunxin.databinding.DialogSpeakLoadingBinding
import com.zhangsheng.shunxin.databinding.DialogTimePickerBinding
import com.zhangsheng.shunxin.databinding.WindowHintViewBinding
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.ClickReport
import com.zhangsheng.shunxin.weather.ext.clickReport
import com.zhangsheng.shunxin.weather.ext.showReport
import kotlinx.android.synthetic.main.dialog_speak_loading.view.*
import kotlinx.android.synthetic.main.dialog_time_picker.view.*

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2021/3/24 9:47
 */
class PushTimePickerDialog(context: Activity, val startTime: Int,val  endTime: Int,val hourIndex:Int,val minIndex:Int,val func: (selectedItemData: Int, selectedItemData1: Int)->Unit) :
    BaseDialog(context) {
    override val binding:DialogTimePickerBinding by inflate()


    override fun initView() {
        var hour = ArrayList<Int>()
        var minute = ArrayList<Int>()
        for (i in startTime..endTime) {
            hour.add(i)
        }
        for (i in 0..50 step 10) {
            minute.add(i)
        }
        binding.pickerHour.data = hour.toList()
        binding.pickerMinute.data = minute.toList()
        binding.pickerHour.selectedItemPosition=hourIndex
        binding.pickerMinute.selectedItemPosition=minIndex

        binding.tvCancel.setOnClickListener {
            this.dismiss()
        }
        binding.tvOk.setOnClickListener {
            func(binding.pickerHour.selectedItemData as Int, binding.pickerMinute.selectedItemData as Int)
            this.dismiss()
        }

    }

    override fun getDimAmount(): Float =0.8f

}