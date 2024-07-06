package com.maiya.thirdlibrary.utils.priority

import android.app.Activity

interface IPriorityDialog {

    fun reShow()

    fun dialogPresent()

    fun forceShow()

    fun isShowing():Boolean

    fun getDialogLevel():Int


    fun dialogDismiss()

    open fun dialogShow()

    fun getTargetContext(): Activity
}