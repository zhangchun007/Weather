package com.zhangsheng.shunxin.weather.dialog

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import com.maiya.thirdlibrary.base.BaseDialog
import com.maiya.thirdlibrary.base.BasePriorityDialog
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.utils.CacheUtil
import com.xm.xmcommon.XMCommonManager
import com.zhangsheng.shunxin.databinding.WindowAppupdateViewBinding
import com.zhangsheng.shunxin.databinding.WindowHintViewBinding
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.common.UrlConstant
import com.zhangsheng.shunxin.weather.ext.ClickReport
import com.zhangsheng.shunxin.weather.ext.clickReport
import com.zhangsheng.shunxin.weather.ext.showReport
import com.zhangsheng.shunxin.weather.net.bean.ControlBean
import com.zhangsheng.shunxin.weather.utils.UploadUtils
import kotlinx.android.synthetic.main.window_appupdate_view.view.*

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2021/3/24 9:47
 */
class AppUpDateDialog(context: Activity, control: ControlBean, canClose: Boolean = false) :
    BasePriorityDialog(context) {
    override val dialogLevels: Int = EnumType.弹窗优先级.升级弹窗
    private var canClose = canClose
    private var control = control
    override val binding: WindowAppupdateViewBinding by inflate()

    override fun initView() {
        binding.tvCancel.isVisible(
            control.nN().android_software_update.nN().update_type.parseInt() == 1 || canClose
        )
        binding.tvCancel.ClickReport(EnumType.上报埋点.非强制升级弹窗_关闭) {
            CacheUtil.put(
                Constant.SP_UPDATE_TIME,
                System.currentTimeMillis()
            )
            this.dismiss()
        }

        binding.tvVersionName.text = "V" + control?.android_software_update?.update2v

        binding.tvContent.text =
            if (TextUtils.isEmpty(control?.android_software_update?.des)) "是否更新至最新版本？"
            else control?.android_software_update?.des?.replace("\\n", "\n")
        binding.tvContent.movementMethod = ScrollingMovementMethod.getInstance()

        binding.tvOk.setSingleClickListener {
            if (isForceUpdate()) clickReport(EnumType.上报埋点.强制升级弹窗_立即升级) else clickReport(EnumType.上报埋点.非强制升级弹窗_立即升级)
            binding.tvOk.isVisible(false)
            binding.tvCancel.isVisible(false)
            binding.flProgress.isVisible(true)
            binding.progressTv.text = "开始下载"
            binding.webDownload.isVisible(true)
            UploadUtils.startUpdate(control) { cancel, progress ->
                if (cancel) {
                    binding.progressTv.text = "下载失败"
                    this.dismiss()
                } else {
                    binding.progressbar.progress = progress
                    binding.progressTv.text = "${progress}%"
                    if (progress == 100) {
                        binding.tvOk.isVisible(true)
                        binding.flProgress.isVisible(false)
                        binding.tvOk.text = "安装"
                        binding.tvOk.setSingleClickListener {
                            UploadUtils.startInstall()
                        }
                    }
                }
            }
        }
        binding.webDownload.setSingleClickListener {
            if (isForceUpdate()) clickReport(EnumType.上报埋点.强制升级弹窗_立即升级) else clickReport(EnumType.上报埋点.非强制升级弹窗_立即升级)
            val uri: Uri = Uri.parse("${UrlConstant.HAND_UPDATE_URL}${System.currentTimeMillis()}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        }
    }

    override fun getDimAmount(): Float = 0.8f

    override fun show() {
        super.show()
        if (isForceUpdate()) showReport(EnumType.上报埋点.强制升级弹窗_展示) else showReport(EnumType.上报埋点.非强制升级弹窗_展示)
    }

    override fun dismiss() {
        super.dismiss()
    }


    /**
     *  //强更 2   //非强制更新 1
     */
    private fun isForceUpdate(): Boolean {
        return control.nN().android_software_update.nN().update_type.parseInt() == 2
    }


}