package com.zhangsheng.shunxin.ad.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.*
import com.maiya.adlibrary.ad.listener.ShowFeedListener
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.utils.CacheUtil
import com.xinmeng.shadow.mediation.display.BaseMaterialView
import com.xinmeng.shadow.mediation.source.IEmbeddedMaterial
import com.xinmeng.shadow.mediation.source.LoadMaterialError
import com.zhangsheng.shunxin.ad.AdUtils

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/9/15 17:20
 */
open abstract class ExtAdMaterialView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseMaterialView(context, attrs, defStyleAttr) {
    private var closeview: View? = null
    private var adMaterial: IEmbeddedMaterial? = null
    private var pageType: String = ""
    private var canLoadAd: Boolean = true
    private var showFeedListener: ShowFeedListener? = null


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        bindLifecycle()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        closeview = null
        adMaterial = null
        showFeedListener = null
        this.findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }

    private fun bindLifecycle() {
        this.findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
            lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        }
    }

    private val lifecycleObserver = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume() {
            adResume()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPause() {
            adPause()
        }
    }

    open fun setPageType(pageType: String) {
        this.pageType = pageType
    }

    open fun setAdInfo(adInfo: IEmbeddedMaterial) {
        adMaterial = adInfo
        closeView?.setSingleClickListener {
            this@ExtAdMaterialView.visibility = View.GONE
            showFeedListener?.adClose()
            if (pageType.isNotEmpty()) {
                CacheUtil.put(pageType, System.currentTimeMillis())
            }
        }
    }

    fun loadIconAd(pageType: String) {
        this.setPageType(pageType)
        AdUtils.showIcon(pageType, getActivity(), this)
    }


    fun setAdListener(listener: ShowFeedListener) {
        this.showFeedListener = listener
    }

    fun adResume() {
        adMaterial?.resumeVideo()
        adMaterial?.onResume()
    }

    fun adPause() {
        adMaterial?.onPause()
        adMaterial?.pauseVideo()
    }

    open fun setAdLoadable(enable: Boolean) {
        canLoadAd = enable
    }

    open fun getAdLoadable(): Boolean {
        return canLoadAd
    }

    open fun showFeedAd(
        activity: Activity?,
        pageType: String?,
        radiusDp: Float = 4f,
        nextCanLoad: Boolean = true,
        listener: ShowFeedListener? = null
    ) {
        this.pageType = pageType.isStr("")
        adResume()
        if (canLoadAd) {
            canLoadAd = nextCanLoad
            listener?.let { setAdListener(it) }
            AdUtils.showFeedAd(
                this.pageType,
                activity,
                this,
                object : ShowFeedListener() {
                    override fun onLoad(p0: IEmbeddedMaterial?): Boolean {
                        listener?.onLoad(p0)
                        return super.onLoad(p0)
                    }

                    override fun onError(p0: LoadMaterialError?) {
                        super.onError(p0)
                        listener?.onError(p0)
                    }

                    override fun adClose() {
                        super.adClose()
                        listener?.adClose()
                    }
                }, radiusDp
            )
        }
    }

    open fun showLeftFeedAd(
        activity: Activity?,
        pageType: String?,
        nextCanLoad: Boolean = true,
        listener: ShowFeedListener? = null,
        radiusDp: Float = 4f,
        rightRadiusDp: Float = 4f
    ) {
        this.pageType = pageType.isStr("")
        adResume()
        if (canLoadAd) {
            canLoadAd = nextCanLoad
            listener?.let { setAdListener(it) }
            AdUtils.showleftRadiusPicAd(
                this.pageType,
                activity,
                this,
                object : ShowFeedListener() {
                    override fun onLoad(p0: IEmbeddedMaterial?): Boolean {
                        listener?.onLoad(p0)
                        return super.onLoad(p0)
                    }

                    override fun onError(p0: LoadMaterialError?) {
                        super.onError(p0)
                        listener?.onError(p0)
                    }

                    override fun adClose() {
                        super.adClose()
                        listener?.adClose()
                    }
                }, radiusDp, rightRadiusDp
            )
        }
    }


    open fun setCloseView(closeView: View) {
        this.closeview = closeView
    }


    override fun getCloseView(): View? {

        return closeview
    }
}