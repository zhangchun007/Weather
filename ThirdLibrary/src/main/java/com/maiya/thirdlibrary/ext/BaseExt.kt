package com.maiya.thirdlibrary.ext

import android.animation.ObjectAnimator
import android.app.Activity
import android.app.Dialog
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.maiya.thirdlibrary.BuildConfig
import com.maiya.thirdlibrary.utils.AppContext
import com.maiya.thirdlibrary.utils.AppUtils
import com.maiya.thirdlibrary.R
import com.maiya.thirdlibrary.base.FragmentBindingDelegate
import kotlinx.coroutines.*
import java.util.*

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/7/13 14:07
 */
inline fun <reified T> T?.nN(t: T? = null): T = this ?: (t ?: T::class.java.newInstance())

fun <T> List<T>?.nN(t: List<T>? = null): List<T> = this ?: (t ?: ArrayList())

inline fun <reified T> List<T>?.listIndex(index: Int): T {
    return if (this.nN().isNotEmpty() && this.nN().size - 1 >= index) {
        this?.get(index) as T
    } else {
        T::class.java.newInstance()
    }
}

fun Any?.init(func: () -> Unit) {
    if (this == null) {
        func()
    }
}

fun Any?.isNull(func: () -> Unit) {
    if (this == null) {
        func()
    }
}

fun String?.isStr(str: String = ""): String {
    return if (this == null || this.isEmpty() || this == "null") {
        str
    } else this
}

fun String?.isStr(): Boolean {
    return !(this == null || this.isEmpty() || this == "null")
}

fun String.searchCityReplace(): String {
    return this.replace("直辖市", "").replace("市", "").replace("特别行政区", "").replace("地区", "")
        .replace("行政区", "").replace("自治区", "").replace("区", "").replace("自治州", "")
        .replace("自治县", "").replace("县", "").replace("州", "")
        .replace("省", "")
}


fun runOnUi(func: () -> Unit) {
    Try {
        if (Looper.getMainLooper() === Looper.myLooper()) func() else Handler(Looper.getMainLooper()).post { func() }
    }
}


fun View?.runOnTime(time: Long, func: () -> Unit) {
    Try {
        this?.postDelayed(Runnable {
            func()
        }, time)
    }
}

fun runOnTime(time: Long, func: () -> Unit) {
    try {
        Handler().postDelayed(Runnable {
            func()
        }, time)
    } catch (e: Exception) {
        func()
    }

}

fun Try(func: () -> Unit) {
    try {
        func()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun LogE(msg: String, tag: String = AppUtils.appName.isStr("")) {
    Try {
        if (BuildConfig.DEBUG) {
            val max_str_length: Int = 2001 - tag.length
            var msg = msg
            while (msg.length > max_str_length) {
                Log.e(tag, msg.substring(0, max_str_length))
                msg = msg.substring(max_str_length)
            }
            Log.e(tag, msg)
        }
    }
}

fun LogD(msg: String, tag: String = AppUtils.appName.isStr("")) {
    if (BuildConfig.DEBUG) {
        Log.d(tag, msg)
    }
}

fun xToast(str: String, toastType: Int = Toast.LENGTH_SHORT) {
    runOnUi {
        Try {
            var toast = Toast.makeText(AppContext.getContext(), null, toastType)
            toast.view = View.inflate(AppContext.getContext(), R.layout.view_toast, null)
            var tv = toast.view as TextView
            tv.text = str
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }
}


fun isVisible(visible: Boolean, vararg views: View) {
    views.forEach {
        if (visible) {
            it.visibility = View.VISIBLE
        } else {
            it.visibility = View.GONE
        }
    }
}


fun <R> (() -> Boolean).trueReturn(trueTerm: () -> R, falseTerm: () -> R): R {
    return if (this()) {
        trueTerm()
    } else {
        falseTerm()
    }
}


//number

fun Double.scale(num: Int): Double {
    return String.format("%.2f", (this)).toDouble()
}

fun String.remove0(): String {
    var sp = this.split(".")
    return if (sp.size == 2) {
        if (sp.listIndex(1).toInt() > 0) {
            this
        } else {
            sp.listIndex(0)
        }
    } else {
        this
    }

}

fun String?.parseInt(default: Int = -1): Int {
    return try {
        if (this.isNullOrEmpty()) return default
        this.toInt()
    } catch (e: Exception) {
        default
    }
}

fun String.parseDouble(default: Double = 0.0): Double {
    return try {
        this.toDouble()
    } catch (e: Exception) {
        default
    }
}

fun String.parseFloat(default: Float = 0f): Float {
    return try {
        this.toFloat()
    } catch (e: Exception) {
        default
    }
}

fun String?.parseLong(default: Long = 0): Long {
    return try {
        this.nN("$default").toLong()
    } catch (e: Exception) {
        default
    }
}


//anim

fun View.toRotation(rotate: Float): ObjectAnimator {
    var objectAnimator = ObjectAnimator.ofFloat(this, "rotation", this.rotation + rotate)
    objectAnimator.duration = 500
    objectAnimator.start()
    return objectAnimator
}

inline fun View.setSingleClickListener(interval: Long = 1000, crossinline block: (View) -> Unit) {
    this.setOnClickListener {
        it.isClickable = false
        block(this)
        it.postDelayed({
            it.isClickable = true
        }, interval)

    }
}

fun View.isVisible(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun View.isInVisible(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}


inline fun throttleClick(wait: Long, crossinline block: (View) -> Unit): View.OnClickListener {
    return View.OnClickListener { v ->
        val current = System.currentTimeMillis()
        val lastClickTime = (v.getTag(R.id.single_click_timestamp) as? Long) ?: 0
        if (current - lastClickTime > wait) {
            v.setTag(R.id.single_click_timestamp, current)
            block(v)
        }
    }
}

fun View.getActivity(): Activity? {
    return if (this.context is Activity) context as Activity else null
}

//viewBinding
inline fun <reified VB : ViewBinding> Activity.inflate() = lazy {
    inflateBinding<VB>(layoutInflater).apply { setContentView(root) }
}

inline fun <reified VB : ViewBinding> Dialog.inflate() = lazy {
    inflateBinding<VB>(layoutInflater).apply { setContentView(root) }
}


inline fun <reified VB : ViewBinding> Fragment.bindView() =
    FragmentBindingDelegate(VB::class.java)

inline fun <reified VB : ViewBinding> Fragment.bindView(
    inflater: LayoutInflater,
    viewGroup: ViewGroup?
) = VB::class.java.getDeclaredMethod(
    "inflate",
    LayoutInflater::class.java,
    ViewGroup::class.java,
    Boolean::class.java
).invoke(null, layoutInflater, viewGroup, false) as VB

@Suppress("UNCHECKED_CAST")
inline fun <reified VB : ViewBinding> inflateBinding(layoutInflater: LayoutInflater) =
    VB::class.java.getMethod("inflate", LayoutInflater::class.java)
        .invoke(null, layoutInflater) as VB

