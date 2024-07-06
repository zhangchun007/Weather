package com.maiya.thirdlibrary.utils

import android.app.Activity
import androidx.annotation.Keep
import com.maiya.thirdlibrary.base.BasePriorityDialog
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.listIndex
import com.maiya.thirdlibrary.utils.priority.IPriorityDialog
import java.util.ArrayList

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2021/4/9 9:30
 */
object DialogPriorityUtil {
    private var currentDialog: PriorityDialog? = null
    private var ForceDialog: IPriorityDialog? = null
    private val dialogs: ArrayList<PriorityDialog?> by lazy { ArrayList() }


    fun dialogShow(
        context: Activity?,
        dialog: IPriorityDialog,
        level: Int,
        isForce: Boolean = false
    ): Boolean {
        if (isForce) {
            return if ((ForceDialog != null && ForceDialog?.isShowing()==true) || (context == null || context.isFinishing)) {
                false
            } else {
                ForceDialog = dialog
                true
            }
        }
        if (context == null || context.isFinishing) dialogDismiss()
        val priD = PriorityDialog(dialog, level)
        if (dialogs.any { it?.level == priD.level }) return false
        return if ((currentDialog == null || currentDialog?.dialog?.isShowing() != true) && (ForceDialog == null || ForceDialog?.isShowing()!=true)) {
            currentDialog = priD
            insert(priD)
            true
        } else {
            insert(priD)
            false
        }
    }

    fun dialogDismiss(isForce: Boolean = false) {
        when {
            isForce -> ForceDialog = null
            dialogs.contains(currentDialog) -> {
                dialogs.remove(currentDialog)
                currentDialog = null
            }

        }

        if (dialogs.isEmpty()) return
        currentDialog = dialogs.listIndex(0)
        val context = currentDialog?.dialog?.getTargetContext()
        if (context?.isFinishing == true) dialogDismiss()
        currentDialog?.dialog?.reShow()
    }

    private fun insert(priD: PriorityDialog) {
        var cIndex = -1
        run forEach@{
            dialogs.forEachIndexed { index, priorityDialog ->
                if (priorityDialog != null && priD.level <= priorityDialog.level) {
                    cIndex = index
                    return@forEach
                }
            }
        }
        Try {
            if (cIndex >= dialogs.size || cIndex == -1) {
                dialogs.add(priD)
            } else {
                dialogs.add(cIndex, priD)
            }
        }
    }

    @Keep
    data class PriorityDialog(val dialog: IPriorityDialog, val level: Int = -1)
}