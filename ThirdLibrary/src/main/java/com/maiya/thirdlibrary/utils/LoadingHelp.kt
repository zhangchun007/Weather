package com.maiya.thirdlibrary.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.setSingleClickListener
import com.maiya.thirdlibrary.widget.BaseLoadingView
import com.maiya.thirdlibrary.widget.LoadingView

class LoadingHelp {
    private var rootView: ViewGroup? = null
    private var loadingView: BaseLoadingView? = null
    var isShowView = false
    private lateinit var mContext: Context

    var pageStatus = BaseLoadingView.LOADING

    fun register(activity: Activity): LoadingHelp {
        mContext = activity
        rootView = activity.findViewById<View>(android.R.id.content) as? ViewGroup
        return this
    }

    fun register(fragment: Fragment): LoadingHelp {
        mContext = fragment.requireContext()
        rootView = fragment.requireView().parent as? ViewGroup
        return this
    }

    fun setContentView(loadingView: BaseLoadingView = LoadingView(mContext)): LoadingHelp {
        this.loadingView = loadingView
        return this
    }

    fun setHintText(string: String) {
        loadingView?.setHintText(string)
    }

    fun setViewStatus(status: String): LoadingHelp {
        if (isShowView) {
            pageStatus = status
            loadingView?.setViewStatus(status)
        }
        return this
    }

    fun setReloadListener(listener: () -> Unit): LoadingHelp {
        loadingView?.getErrorClickView?.setSingleClickListener {
            setViewStatus(BaseLoadingView.LOADING)
            listener.invoke()
        }
        return this
    }

    fun showView() {
        if (null == rootView) {
            throw Exception(" LoadingHelp must be register ")
        }
        if (!isShowView) {
            rootView?.removeView(loadingView)
            rootView?.addView(loadingView)
            isShowView = true
        }
    }

    var mListener: (() -> Unit)? = null

    fun setDismissListener(mListener: () -> Unit) {
        this.mListener = mListener
    }

    fun dismiss() {
        Try {
            rootView?.removeView(loadingView)
            isShowView = false
            mListener?.invoke()
        }
    }

    fun detach() {
        dismiss()
        loadingView = null
    }
}