package com.zhangsheng.shunxin.weather.dialog

import android.app.Activity
import com.maiya.thirdlibrary.base.BasePriorityDialog
import com.maiya.thirdlibrary.ext.inflate
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.ext.setSingleClickListener
import com.maiya.thirdlibrary.utils.CacheUtil
import com.xinmeng.shadow.base.ShadowConstants
import com.xinmeng.shadow.mediation.display.MaterialViewSpec
import com.xinmeng.shadow.mediation.source.IEmbeddedMaterial
import com.zhangsheng.shunxin.databinding.WindowAdPopBinding
import com.zhangsheng.shunxin.ad.AdUtils
import com.zhangsheng.shunxin.databinding.DialogPushAlertBinding
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.net.bean.AdPopBean

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2021/3/24 9:47
 */
class PushAlertDialog(context: Activity, val openPush: () -> Unit) :
    BasePriorityDialog(context) {
    override val binding: DialogPushAlertBinding by inflate()
    override val dialogLevels: Int = EnumType.弹窗优先级.消息通知

    override fun initView() {
        setCancelable(false)
        binding.tvOpenNotification.setSingleClickListener {
            dismiss()
            openPush()
        }
        binding.close.setSingleClickListener {
            dismiss()
        }

    }

    override fun show() {
        super.show()
        CacheUtil.put(Constant.SP_PUSH_ALERT,true)
    }

    override fun getDimAmount(): Float = 0.8f


}