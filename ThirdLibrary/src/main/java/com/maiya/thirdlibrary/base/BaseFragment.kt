package com.maiya.thirdlibrary.base

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2019/10/30 20:52
 */
abstract class BaseFragment(layout:Int) : Fragment(layout) {
    private var firstLoad = true
    protected var baseActivity: BaseActivity? = null
    protected lateinit var mContext: Context
    open fun reTry() {}

    open fun onShow(action: Int = 0) {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }


    /**
     * 初始化布局
     */
    abstract fun initView()



    @TargetApi(23)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        onAttachToContext(context)
    }

    /*
    * Deprecated on API 23
    * Use onAttachToContext instead
    */
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity)
        }
    }

    protected fun onAttachToContext(context: Context) {
        mContext = context
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        baseActivity = activity as BaseActivity
    }

    protected open fun initListener() {

    }

    override fun onResume() {
        super.onResume()
        if (firstLoad) {
            firstLoad = false
            onLazyLoad()
        }else{
            onReLoad()
        }
    }


    override fun onPause() {
        super.onPause()
        onHidden()
    }

    /**
     * 懒加载
     */
    protected open fun onLazyLoad() {}

    /***
     * 第二次加载
     */
    protected open fun onReLoad(){}

    /**
     * 布局隐藏
     */
    protected open fun onHidden(){}
}