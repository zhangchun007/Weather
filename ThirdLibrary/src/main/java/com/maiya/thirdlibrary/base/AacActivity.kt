package com.maiya.thirdlibrary.base

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.maiya.thirdlibrary.net.bean.NetStatus
import com.maiya.thirdlibrary.utils.ActivityManageTools
import com.maiya.thirdlibrary.widget.BaseLoadingView

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/7/14 19:33
 */
abstract class AacActivity<VM : BaseViewModel> : BaseActivity() {

    protected abstract val vm: VM

    protected abstract val binding: ViewBinding





    fun getModel(): ViewModel {
        return vm
    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        ActivityManageTools.addActivity(this)
    }

    override fun initObserve() {
        super.initObserve()
        vm.pageAction.observe(this, Observer {
            when (it.code) {
                NetStatus.success -> {
                    loadingHelp.dismiss()
                }
                NetStatus.showLoading -> {
                    loadingHelp.setViewStatus(BaseLoadingView.LOADING)
                    loadingHelp.showView()
                }
                else -> {
                    onError(it.code,it.msg)
                    loadingHelp.setHintText(it.msg)
                    loadingHelp.setViewStatus(BaseLoadingView.ERROR)
                }
            }
        })
    }



}