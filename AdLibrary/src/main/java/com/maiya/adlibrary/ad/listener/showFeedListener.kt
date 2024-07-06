package com.maiya.adlibrary.ad.listener

import com.xinmeng.shadow.mediation.api.MediationAdListener
import com.xinmeng.shadow.mediation.source.IEmbeddedMaterial
import com.xinmeng.shadow.mediation.source.LoadMaterialError

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/7/21 19:07
 */
open class ShowFeedListener : MediationAdListener<IEmbeddedMaterial> {

    override fun onLoad(p0: IEmbeddedMaterial?): Boolean {
        return false
    }

    override fun onError(p0: LoadMaterialError?) {

    }

    open fun adClose(){

    }
}