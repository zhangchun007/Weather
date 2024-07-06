package com.maiya.thirdlibrary.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2020/1/13 9:41
 */
object `SoftKeyboardUtil` {
    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment)
     */
    fun hideSoftKeyboard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val inputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    /**
     * 隐藏软键盘(可用于Activity，Fragment)
     */
    fun hideSoftKeyboard(
        context: Context,
        viewList: List<View>?
    ) {
        if (viewList == null) return
        val inputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        for (v in viewList) {
            inputMethodManager.hideSoftInputFromWindow(
                v.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
    fun showSoftInputFromWindow(
        activity: Activity,
        editText: EditText
    ) {
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
        //显示软键盘
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        //如果上面的代码没有弹出软键盘 可以使用下面另一种方式
        //InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.showSoftInput(editText, 0);
    }


    fun addLayoutListener(main: View, scroll: View) {
        main.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val rect = Rect()
                main.getWindowVisibleDisplayFrame(rect)
                val mainInvisibleHeight: Int = main.rootView.height - rect.bottom
                if (mainInvisibleHeight > 100) {
                    val location = IntArray(2)
                    scroll.getLocationInWindow(location)
                    val srollHeight: Int = location[1] + scroll.height - rect.bottom
                    main.scrollTo(0, srollHeight)
                } else {
                    main.scrollTo(0, 0)
                }
            }
        })
    }

}