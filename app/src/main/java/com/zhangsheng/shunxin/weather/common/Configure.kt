package com.zhangsheng.shunxin.weather.common

import com.maiya.thirdlibrary.ext.parseInt
import com.maiya.thirdlibrary.utils.AppContext
import com.zhangsheng.shunxin.BuildConfig
import com.zhangsheng.shunxin.R

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/7/13 13:57
 */

class Configure {
    companion object {

        const val Debug: Boolean = "release" != BuildConfig.BUILD_TYPE

//        const val Debug: Boolean = false

        const val appQid = BuildConfig.APP_QID

        const val appTypeId = BuildConfig.APP_TYPE_ID

        const val buglyAppId: String = BuildConfig.BUGLY_APP_ID

        const val ksScenePosId: String = BuildConfig.KS_SCENE_POSID

        val appName: String = AppContext.getContext().resources.getString(R.string.app_name)

        const val csjAppId: String = BuildConfig.CSJ_APP_ID

        const val gdtAppId: String = BuildConfig.GDT_APP_ID

        const val ksAppId: String = BuildConfig.KS_APP_ID

        const val bdAppId: String = BuildConfig.BD_APP_ID

        //sp sdk对应的bug上报标签
        const val spBuglyTag: String = BuildConfig.SP_BUGLY_TAG
    }

}