package com.maiya.thirdlibrary.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.maiya.thirdlibrary.utils.LoadingHelp
import com.maiya.thirdlibrary.utils.StatusBarUtil
import com.maiya.thirdlibrary.widget.LoadingView
import kotlin.properties.Delegates

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/7/13 18:35
 */
abstract class BaseActivity : FragmentActivity() {

    protected var instance: Context by Delegates.notNull()

    protected var mActivity: Activity by Delegates.notNull()


    protected val loadingHelp by lazy {
        LoadingHelp().apply {
            register(this@BaseActivity)
            setContentView(LoadingView(instance))
            setReloadListener {
                retry()
            }
        }
    }

    override fun setContentView(view: View?) {
        setStatusBar()
        super.setContentView(view)
    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        instance = this
        mActivity = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(savedInstanceState)
        initObserve()
        initListener()
    }


    protected open fun setStatusBar() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, false)
        StatusBarUtil.setTranslucentStatus(this)
        StatusBarUtil.setStatusBarDarkTheme(this, true)
    }


    protected open fun setStatusPading(view: View) {
        view.setPadding(0, StatusBarUtil.getStatusBarHeight(this), 0, 0)
    }
    abstract fun initView(savedInstanceState: Bundle?)
    protected open fun initObserve() {}
    protected open fun initListener() {}
    protected open fun retry() {}
    protected open fun onError(code:Int,msg:String) {}

    override fun onDestroy() {
        super.onDestroy()
        loadingHelp.detach()
    }

}