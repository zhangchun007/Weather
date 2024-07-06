package com.maiya.thirdlibrary.base

import android.app.Activity
import android.content.Context
import androidx.viewbinding.ViewBinding
import com.maiya.thirdlibrary.utils.DialogPriorityUtil
import com.maiya.thirdlibrary.utils.priority.IPriorityDialog

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2021/4/9 11:22
 */
abstract class BasePriorityDialog(context: Activity) : BaseDialog(context),IPriorityDialog {
    protected var isRetryShow = false //优先级第二次展示
    protected var isForceShow = false //强制展示

    abstract val dialogLevels: Int


    override fun getDialogLevel(): Int =dialogLevels

    override fun reShow() {
        isRetryShow = true
        show()
    }

    override fun getTargetContext(): Activity =context

    override fun show() {
        when {
            isRetryShow -> {
                if (this.context==null||this.context.isFinishing || this.context.isDestroyed) return
                super.show()
                dialogPresent()
            }
            DialogPriorityUtil.dialogShow(context, this, getDialogLevel(), isForceShow) -> {
                if (this.context==null||this.context.isFinishing || this.context.isDestroyed) return
                dialogPresent()
                super.show()
            }
        }
    }


    override fun dialogPresent() {
    }

    override fun dialogDismiss() {
        dismiss()
    }

    override fun dialogShow() {
        show()
    }

    override fun forceShow() {
        isForceShow = true
        show()
    }


    override fun dismiss() {
        super.dismiss()
        DialogPriorityUtil.dialogDismiss(isForceShow)
    }

}