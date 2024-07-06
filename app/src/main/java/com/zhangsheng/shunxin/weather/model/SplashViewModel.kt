package com.zhangsheng.shunxin.weather.model

import android.Manifest
import android.app.Activity
import android.os.Build
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.utils.CacheUtil
import com.maiya.thirdlibrary.utils.OSUtils
import com.xinmeng.shadow.base.ShadowConstants
import com.xinmeng.shadow.mediation.MediationManager
import com.xinmeng.shadow.mediation.api.ISplashCallback
import com.xinmeng.shadow.mediation.source.SceneInfo
import com.zhangsheng.shunxin.ad.AdConstant
import com.zhangsheng.shunxin.ad.SplashLogicUtil
import com.zhangsheng.shunxin.weather.utils.PermissionsUtils
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.dialog.AppHintDialog
import com.zhangsheng.shunxin.weather.dialog.AppPermissionCheckAgainDialog
import com.zhangsheng.shunxin.weather.dialog.ProtocolDialog
import com.zhangsheng.shunxin.weather.ext.*
import com.zhangsheng.shunxin.weather.utils.ProcessLifecycleObserver
import com.zhangsheng.shunxin.weather.utils.ThirdInitHelper


/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/7/14 21:15
 */
class SplashViewModel : BaseViewModel() {
    var isSplashOk = false
    var isInitOk = false
    var isCheckAgain = false

    var canJump = false
    var isClickAd = false
    var isAdPresent = false
    var isAdDismiss = false

    var isHotSplash = false
    private var protocolDialog: ProtocolDialog? = null
    fun checkAgree(context: Activity,  permissionsResult: PermissionsUtils.IPermissionsResult) {
        isSplashOk = true
        if (protocolDialog != null && !protocolDialog!!.isShowing) {
            protocolDialog?.show()
            return
        }
        protocolDialog = ProtocolDialog(context) {
            if (it) {
                ThirdInitHelper.backInit(context.application, true)
                PermissionsUtils.checkPermissions(
                    context,
                    Constant.CHECK_MUST_PERMISIIONS, permissionsResult
                )
            } else {
                AppHintDialog(context) { it ->
                    if (it) {
                        ThirdInitHelper.backInit(context.application, true)
                        PermissionsUtils.checkPermissions(
                            context,
                            Constant.CHECK_MUST_PERMISIIONS, permissionsResult
                        )
                    } else {
//                        checkAgree(context, permissionsResult)
                        context.finish()
                    }
                }.show()
            }
        }
        protocolDialog?.show()
    }


    fun loadSplash(context: Activity, root: ViewGroup, callback: ISplashCallback) {
        isInitOk = true
        val combinedPgtype = AdConstant.mappingPgtype(AdConstant.SLOT_OPEN)
        val splashManager = MediationManager.getInstance().createSplashManager(combinedPgtype)
        splashManager.loadSplash(context, root, SceneInfo().apply {
            this.pgtype = combinedPgtype
            addExtraParameter(ShadowConstants.EXT_PARAM_GAME_TYPE, AdConstant.SLOT_OPEN)
        }, callback)
    }


    fun checkPermission(
        context: Activity,
        permissionsResult: PermissionsUtils.IPermissionsResult,
        showSp: (show: Boolean) -> Unit
    ) {
        if (!CacheUtil.getBoolean(Constant.SP_AGREE_PRIVACY, false)) {
            ProcessLifecycleObserver.isUserFirstOpen = true
            checkAgree(context, permissionsResult)

        } else if (!CacheUtil.getBoolean(Constant.SP_PASS_PERMISSION, false)) {
            isSplashOk = true
            PermissionsUtils.checkPermissions(
                context,
                Constant.CHECK_MUST_PERMISIIONS, permissionsResult
            )
        } else {
            isInitOk = true
            showSp(SplashLogicUtil.checkSp() && !isControl())
        }
    }


    fun shouldCheckAgain(
        context: Activity,
        permissions: List<String>,
        permissionsResult: PermissionsUtils.IPermissionsResult,
        goNext: () -> Unit
    ) {
        if ((!isCheckAgain && (permissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE) || permissions.contains(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ))) && Build.VERSION.SDK_INT > Build.VERSION_CODES.O && PermissionsUtils.shouldShowRequestPermissionRationale(
                context,
                Constant.CHECK_SAVE_PERMISSIONS.toList()
            )
        ) {
            isCheckAgain = true
            AppPermissionCheckAgainDialog(context) {
                if (it) {
                    isSplashOk = true
                    checkPermissionAgain(context, permissionsResult)
                } else {
                    isInitOk = true
                    goNext()
                }
            }.show()
        } else {
            isInitOk = true
            goNext()
        }
    }

    fun checkPermissionAgain(
        context: Activity,
        permissionsResult: PermissionsUtils.IPermissionsResult
    ) {
        isSplashOk = true
        PermissionsUtils.checkPermissions(
            context,
            Constant.CHECK_MUST_PERMISIIONS_AGAIN, permissionsResult
        )
    }

}