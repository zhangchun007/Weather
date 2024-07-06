package com.zhangsheng.shunxin.weather.activity

import android.Manifest
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.maiya.thirdlibrary.base.AacActivity
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.inflate
import com.maiya.thirdlibrary.ext.isVisible
import com.maiya.thirdlibrary.utils.AppContext
import com.maiya.thirdlibrary.utils.CacheUtil
import com.xinmeng.shadow.mediation.api.ISplashCallback
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.databinding.ActivitySplashBinding
import com.zhangsheng.shunxin.weather.MainActivity
import com.zhangsheng.shunxin.ad.SplashLogicUtil
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.clickReport
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.ext.skipActivity
import com.zhangsheng.shunxin.weather.model.SplashViewModel
import com.zhangsheng.shunxin.weather.utils.PermissionsUtils
import com.zhangsheng.shunxin.weather.utils.ProcessLifecycleObserver
import com.zhangsheng.shunxin.weather.utils.ThirdInitHelper
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import org.koin.android.ext.android.inject

class SplashActivity : AacActivity<SplashViewModel>(), ISplashCallback,
    SplashLogicUtil.WarmSplashProhibition {
    override val vm: SplashViewModel by inject()
    override val binding: ActivitySplashBinding by inflate()

    override fun initView(savedInstanceState: Bundle?) {
        Try {
            vm.isHotSplash = intent.getBooleanExtra("HotSplash", false)
        }
        if (!vm.isHotSplash && intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT !== 0) {
            finish()
            return
        }
        if (vm.isHotSplash) {
            getAppModel().lastHotSplashTime = System.currentTimeMillis()
            vm.loadSplash(this, binding.root, this)
            return
        } else {
            ProcessLifecycleObserver.AdConfig()
        }

        ProcessLifecycleObserver.isFirstOpen = true

        vm.checkPermission(this, permissionsResult) { show ->
            if (show) {
                vm.loadSplash(this, binding.root, this)
            } else {
                splashNext()
            }
        }
    }


    private fun splashNext() {
        vm.isSplashOk = true
        next()
    }


    private fun next() {
        if (vm.isInitOk && vm.isSplashOk) {
            lifecycleScope.cancel()
            if (!vm.isHotSplash) {
                skipActivity(MainActivity::class.java)
                finish()
            } else {
                ProcessLifecycleObserver.splashNext(this)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (vm.isAdPresent && vm.isClickAd && !vm.isAdDismiss) {
            splashNext()
            return
        }

        if (vm.canJump) {
            splashNext()
        } else {
            vm.canJump = true
        }
    }

    override fun onPause() {
        super.onPause()
        vm.canJump = false
        lifecycleScope.cancel()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            R.anim.anim_splash_alpha_in,
            R.anim.anim_splash_alpha_out
        )
    }

    override fun onAdDismiss() {
        if (vm.canJump) {
            splashNext()
        } else {
            vm.canJump = true
        }
        vm.isAdDismiss = true
    }

    override fun onAdSkip() {
        splashNext()
    }

    override fun onCoinRange(p0: String?) {

    }

    override fun onAdPresent() {
        binding.btm.isVisible(true)
        CacheUtil.put(Constant.SP_SPLASH_SHOW_TIME, System.currentTimeMillis())
        lifecycleScope.async {
            delay(6000)
            splashNext()
        }
        vm.isAdPresent = true
    }

    override fun onAdClick() {
        lifecycleScope.cancel()
        vm.isClickAd = true
    }

    override fun onTimeout() {
        splashNext()
    }

    override fun onError() {
        splashNext()
    }

    override fun onDestroy() {
        ProcessLifecycleObserver.isFirstOpen = false
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionsUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    private var permissionsResult: PermissionsUtils.IPermissionsResult =
        object : PermissionsUtils.IPermissionsResult {
            override fun passPermissons(isRequest: Boolean, permissions: Array<String>) {
                CacheUtil.put(Constant.SP_PASS_PERMISSION, true)
                vm.isInitOk = true
                if (!vm.isCheckAgain) {//首次发起权限申请上报
                    permissionReport(Constant.CHECK_MUST_PERMISIIONS.toList())
                } else {
                    clickReport(EnumType.上报埋点.存储权限挽留弹窗再次发起存储授权授权成功)
                }
                next()
            }

            override fun forbitPermissons(permissions: List<String>) {
                CacheUtil.put(Constant.SP_PASS_PERMISSION, true)
                if (!vm.isCheckAgain) { //首次发起权限申请上报
                    //通过的权限
                    permissionReport(Constant.CHECK_MUST_PERMISIIONS.toList() - permissions)
                    unPermissionReport(permissions)
                } else {
                    clickReport(EnumType.上报埋点.存储权限挽留弹窗再次发起存储授权授权失败)
                }
                //再次发起存储权限申请
                checkUnPassPermission(permissions)
            }
        }


    /**
     * 同意权限的上报
     */
    private fun permissionReport(okPermissions: List<String>) {
        okPermissions.forEach {
            when (it) {
                Manifest.permission.READ_PHONE_STATE -> clickReport(EnumType.上报埋点.新用户协议同意发起电话授权授权成功)
                Manifest.permission.READ_EXTERNAL_STORAGE -> clickReport(
                    EnumType.上报埋点.新用户协议同意发起存储授权授权成功
                )
            }
        }
    }

    /**
     * 没同意权限的上报
     */
    private fun unPermissionReport(unPermissions: List<String>) {
        unPermissions.forEach {
            when (it) {
                Manifest.permission.READ_PHONE_STATE -> clickReport(EnumType.上报埋点.新用户协议同意发起电话授权授权失败)
                Manifest.permission.READ_EXTERNAL_STORAGE -> clickReport(
                    EnumType.上报埋点.新用户协议同意发起存储授权授权失败
                )
            }
        }
    }

    private fun checkUnPassPermission(unPassPermissions: List<String>) {
        vm.shouldCheckAgain(this@SplashActivity, unPassPermissions, permissionsResult) {
            next()
        }
    }


}