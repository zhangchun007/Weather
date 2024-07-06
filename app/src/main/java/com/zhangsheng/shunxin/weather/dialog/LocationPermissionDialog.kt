package com.zhangsheng.shunxin.weather.dialog

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.maiya.thirdlibrary.base.BaseDialog
import com.maiya.thirdlibrary.base.BasePriorityDialog
import com.maiya.thirdlibrary.ext.inflate
import com.zhangsheng.shunxin.databinding.WindowHintViewBinding
import com.zhangsheng.shunxin.databinding.WindowLocationAuthViewBinding
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.utils.PermissionsUtils

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/6/19 15:14
 */
class LocationPermissionDialog(context: Activity, val locationClick:()->Unit) : BasePriorityDialog(context) {
    override val dialogLevels: Int=EnumType.弹窗优先级.定位权限
    override val binding: WindowLocationAuthViewBinding by inflate()
    override fun initView() {
        this.setCancelable(false)
        binding.tvCancel.setOnClickListener {
            dismiss()
        }

        binding.tvLocation.setOnClickListener {
            locationClick()
            dismiss()
        }
    }


    override fun getDimAmount(): Float = 0.7f
}