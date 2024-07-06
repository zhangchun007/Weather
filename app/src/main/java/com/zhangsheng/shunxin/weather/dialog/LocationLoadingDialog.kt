package com.zhangsheng.shunxin.weather.dialog

import android.app.Activity
import android.graphics.Color
import android.os.Handler
import androidx.core.os.HandlerCompat.postDelayed
import com.bumptech.glide.Glide
import com.maiya.thirdlibrary.base.BaseDialog
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.inflate
import com.maiya.thirdlibrary.ext.runOnTime
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.databinding.WindowLocationViewBinding
import kotlinx.android.synthetic.main.window_location_view.view.*

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/6/19 15:14
 */
class LocationLoadingDialog(context: Activity) : BaseDialog(context) {
    override val binding: WindowLocationViewBinding by inflate()
    override fun initView() {
        setCancelable(false)
    }

    override fun onStart() {
        super.onStart()
        loading()
    }



    private fun loading(){
        Glide.with(context).asGif().load(R.drawable.small_location_loading).into(binding.icon)
        binding.tvHint.text="正在定位，请稍后..."
        binding.tvHint.setTextColor(Color.parseColor("#9296A0"))
        runOnTime(10 * 1000) {
            if (this.isShowing) {
                dismiss()
            }
        }
    }


    fun changeDialogStatus(isOk: Boolean, func: () -> Unit = {}) {
        if (!this.isShowing) return
        if (isOk) {
            binding.icon.setImageResource(R.mipmap.im_location_success)
            binding.tvHint.text = "定位成功"
            binding.tvHint.setTextColor(context.resources.getColor(R.color.color_379bff))
        } else {
            binding.icon.setImageResource(R.mipmap.im_location_failure)
            binding.tvHint.text = "定位失败"
            binding.tvHint.setTextColor(context.resources.getColor(R.color.color_fe5044))
        }
        runOnTime(1000) {
            if (this.isShowing){
                dismiss()
            }
            func()
        }

    }


    override fun getDimAmount(): Float = 0.7f
}