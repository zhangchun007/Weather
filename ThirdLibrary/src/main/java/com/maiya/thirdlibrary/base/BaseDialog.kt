package com.maiya.thirdlibrary.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.maiya.thirdlibrary.R
import com.maiya.thirdlibrary.utils.StatusBarUtil

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/6/19 14:49
 */

abstract class BaseDialog(val context: Activity,theme:Int=R.style.DialogAnim) :
    AppCompatDialog(context,theme) {
    private var rootView:View?=null
    abstract fun initView()
    protected abstract val binding: ViewBinding
    protected abstract fun getDimAmount(): Float
    protected open fun setWindow(window: Window) {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
        initView()
    }




    override fun setContentView(view: View) {
        super.setContentView(view)
        window?.let {
            setWindow(it)
            var layoutParams = it.attributes
            layoutParams.dimAmount = getDimAmount()
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
//            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            it.decorView.setPadding(0, 0, 0, 0)
            it.attributes = layoutParams
        }

    }

    override fun dismiss() {
        if (this.isShowing&&context!=null&&!context.isDestroyed){
            super.dismiss()
        }
    }

}