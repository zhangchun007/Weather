package com.zhangsheng.shunxin.ad

import android.app.Activity
import android.os.Build
import android.os.Looper
import com.maiya.adlibrary.ad.listener.ShowFeedListener
import com.maiya.thirdlibrary.base.BaseDialog
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.utils.CacheUtil
import com.maiya.thirdlibrary.utils.DeviceUtil
import com.maiya.thirdlibrary.utils.DialogPriorityUtil
import com.xinmeng.shadow.base.ShadowConstants
import com.xinmeng.shadow.mediation.MediationManager
import com.xinmeng.shadow.mediation.api.IInterstitialListener
import com.xinmeng.shadow.mediation.api.IMaterialInteractionListener
import com.xinmeng.shadow.mediation.api.MediationAdListener
import com.xinmeng.shadow.mediation.display.MaterialViewSpec
import com.xinmeng.shadow.mediation.source.IEmbeddedMaterial
import com.xinmeng.shadow.mediation.source.IInterstitialMaterial
import com.xinmeng.shadow.mediation.source.LoadMaterialError
import com.xinmeng.shadow.mediation.source.SceneInfo
import com.zhangsheng.shunxin.ad.AdConstant.SLOT_BIGBTPRE15
import com.zhangsheng.shunxin.ad.AdConstant.SLOT_BIGFC1
import com.zhangsheng.shunxin.ad.AdConstant.SLOT_BIGPOP
import com.zhangsheng.shunxin.ad.AdConstant.SLOT_NEWSAMLLHB
import com.zhangsheng.shunxin.ad.widget.ExtAdMaterialView
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.dialog.AdMouldDialog
import com.zhangsheng.shunxin.weather.dialog.AdPopDialog
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.ext.getOpenTimes
import com.zhangsheng.shunxin.weather.ext.getPopControl
import com.zhangsheng.shunxin.weather.ext.isAdControlShow
import com.zhangsheng.shunxin.weather.net.bean.AdPopBean
import com.zhangsheng.shunxin.weather.utils.DataUtil
import com.zhangsheng.shunxin.weather.utils.ProcessLifecycleObserver
import java.lang.ref.WeakReference
import kotlin.math.abs
import kotlin.random.Random


/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/7/21 19:00
 */
object AdUtils {
    var popAdShouldLoad = false
    var floatTopHeight: Int = 0

    fun loadPreAd() {
        Looper.myQueue().addIdleHandler {
            if (AdConstant.isTemplateApp()) {
                MediationManager.getInstance().loadInterstitialMaterial(
                    SLOT_BIGPOP,
                    SceneInfo().apply {
                        pgtype = SLOT_BIGPOP
                        isUseCacheFirst = true
                        slotWidth = AdConstant.setTemplateAdWidth(SLOT_BIGPOP)
                        addExtraParameter(ShadowConstants.EXT_PARAM_GAME_TYPE, SLOT_BIGPOP)
                    },
                    object : MediationAdListener<IInterstitialMaterial> {
                        override fun onLoad(p0: IInterstitialMaterial?): Boolean {
                            return false
                        }

                        override fun onError(p0: LoadMaterialError?) {
                        }
                    }
                )
                MediationManager.getInstance().loadEmbeddedMaterial(
                    SLOT_BIGBTPRE15,
                    SceneInfo().apply {
                        pgtype = SLOT_BIGBTPRE15
                        isUseCacheFirst = true
                        slotWidth = AdConstant.setTemplateAdWidth(SLOT_BIGBTPRE15)
                        addExtraParameter(ShadowConstants.EXT_PARAM_GAME_TYPE, SLOT_BIGBTPRE15)
                    },
                    object : MediationAdListener<IEmbeddedMaterial> {
                        override fun onLoad(p0: IEmbeddedMaterial?): Boolean {
                            return false
                        }

                        override fun onError(p0: LoadMaterialError?) {
                        }
                    }
                )
            } else {
                MediationManager.getInstance().loadEmbeddedMaterial(
                    SLOT_NEWSAMLLHB,
                    SceneInfo().apply {
                        pgtype = SLOT_NEWSAMLLHB
                        isUseCacheFirst = true
                        addExtraParameter(ShadowConstants.EXT_PARAM_GAME_TYPE, SLOT_NEWSAMLLHB)
                    },
                    object : MediationAdListener<IEmbeddedMaterial> {
                        override fun onLoad(p0: IEmbeddedMaterial?): Boolean {
                            return false
                        }

                        override fun onError(p0: LoadMaterialError?) {
                        }
                    }
                )
            }

            MediationManager.getInstance().loadEmbeddedMaterial(
                SLOT_BIGFC1,
                SceneInfo().apply {
                    pgtype = SLOT_BIGFC1
                    isUseCacheFirst = true
                    addExtraParameter(ShadowConstants.EXT_PARAM_GAME_TYPE, SLOT_BIGFC1)
                },
                object : MediationAdListener<IEmbeddedMaterial> {
                    override fun onLoad(p0: IEmbeddedMaterial?): Boolean {
                        return false
                    }

                    override fun onError(p0: LoadMaterialError?) {
                    }
                }
            )
            false
        }
    }


    /**
     * 加载信息流广告
     */
    fun showFeedAd(
        pgType: String,
        activity: Activity?,
        adView: ExtAdMaterialView,
        listener: ShowFeedListener = ShowFeedListener(),
        radiusDp: Float = 4f
    ) {
        if (isAdControlShow(pgType)) {
            val context = WeakReference(activity)
            val combinedPgtype = AdConstant.mappingPgtype(pgType)
            val slowWidth = AdConstant.setTemplateAdWidth(pgType)
            MediationManager.getInstance().loadEmbeddedMaterial(combinedPgtype,
                SceneInfo().apply {
                    pgtype = combinedPgtype
                    isUseCacheFirst = true
                    if (slowWidth != 0) {
                        slotWidth = slowWidth
                    }
                    addExtraParameter(ShadowConstants.EXT_PARAM_GAME_TYPE, pgType)
                },
                object : MediationAdListener<IEmbeddedMaterial> {
                    override fun onLoad(material: IEmbeddedMaterial?): Boolean {
                        if (!isActivityAlive(context.get())) return false
                        listener.onLoad(material)
                        val materialViewSpec = MaterialViewSpec()
                        materialViewSpec.context = context.get()
                        materialViewSpec.displayOrder =
                            intArrayOf(
                                ShadowConstants.SHOW_STYLE_TEMPLATE_VIDEO,
                                ShadowConstants.SHOW_STYLE_LARGE
                            )
                        materialViewSpec.radiusDp = radiusDp
                        materialViewSpec.scaleType = MaterialViewSpec.SCALE_TYPE_FIX_CENTER
                        material?.render(adView.apply {
                            setAdInfo(material)
                        }, materialViewSpec, if (!AdConstant.isTemplateApp()) null else object :IMaterialInteractionListener{
                            override fun onAdShow() {

                            }

                            override fun onAdClick() {
                            }

                            override fun onCreativeButtonClick() {
                            }

                            override fun onAdvClose() {
                                listener.adClose()
                            }

                            override fun onDislikeSelect() {
                                adView?.isVisible(false)
                                listener.adClose()
                                if (pgType.isNotEmpty()) {
                                    CacheUtil.put(pgType, System.currentTimeMillis())
                                }
                            }

                        })
                        adView.isVisible(true)
                        return true
                    }

                    override fun onError(p0: LoadMaterialError?) {
                        listener.onError(p0)
                        adView?.isVisible(false)
                        LogE("ad->$pgType:${p0?.message}")
                    }
                }
            )
        } else {
            adView.isVisible(false)
        }
    }

    var isAdPopLoading = false
    var adPop: BaseDialog? = null
    fun showPopAd(activity: Activity) {
        if (adPop != null && adPop!!.isShowing) return
        if (!isAdPopLoading && isAdControlShow(AdConstant.SLOT_BIGPOP) && !ProcessLifecycleObserver.isUserFirstOpen) {
            isAdPopLoading = true
            val oldPgtype = SLOT_BIGPOP
            val context = WeakReference(activity)
            val combinedPgtype = AdConstant.mappingPgtype(oldPgtype)
            if (AdConstant.isTemplateApp()) {
                loadInterstitialMaterial(oldPgtype, context)
                return
            }
            MediationManager.getInstance().loadEmbeddedMaterial(combinedPgtype,
                SceneInfo().apply {
                    pgtype = combinedPgtype
                    isUseCacheFirst = true
                    addExtraParameter(ShadowConstants.EXT_PARAM_GAME_TYPE, oldPgtype)
                },
                object : MediationAdListener<IEmbeddedMaterial> {
                    override fun onLoad(material: IEmbeddedMaterial?): Boolean {
                        if (material == null || !isActivityAlive(context.get())) return false
                        adPop = AdPopDialog(context.get().nN(), material).apply {
                            this.dialogShow()
                        }
                        isAdPopLoading = false
                        return true
                    }

                    override fun onError(p0: LoadMaterialError?) {
                        isAdPopLoading = false
                        LogE("AdPop:->onError:${p0?.message} ${p0?.code}")
                    }
                })
        }
    }

    fun showleftRadiusPicAd(
        pgType: String,
        activity: Activity?,
        adView: ExtAdMaterialView,
        listener: ShowFeedListener = ShowFeedListener(),
        radiusDp: Float = 0f,
        rightDp: Float = 0f
    ) {
        if (isAdControlShow(pgType)) {
            val oldPgtype = pgType
            val context = WeakReference(activity)
            val combinedPgtype = AdConstant.mappingPgtype(oldPgtype)
            val slowWidth = AdConstant.setTemplateAdWidth(pgType)
            MediationManager.getInstance().loadEmbeddedMaterial(combinedPgtype,
                SceneInfo().apply {
                    pgtype = combinedPgtype
                    if (oldPgtype == "bigbtpre15" || oldPgtype == "bigdrawbtshzs2" || oldPgtype == "bigdrawshzs2" || oldPgtype == "bigkqzlxq" || oldPgtype == "bigyjejym") {
                        isUseCacheFirst = true
                    }
                    if (slowWidth != 0) {
                        slotWidth = slowWidth
                    }
                    addExtraParameter(ShadowConstants.EXT_PARAM_GAME_TYPE, oldPgtype)
                },
                object : MediationAdListener<IEmbeddedMaterial> {
                    override fun onLoad(material: IEmbeddedMaterial?): Boolean {
                        if (!isActivityAlive(context.get())) return false
                        listener.onLoad(material)
                        var materialViewSpec = MaterialViewSpec();
                        materialViewSpec.context = context.get()
                        materialViewSpec.displayOrder =
                            intArrayOf(
                                ShadowConstants.SHOW_STYLE_LARGE
                            )
                        materialViewSpec.scaleType = MaterialViewSpec.SCALE_TYPE_FIX_CENTER
                        materialViewSpec.radiusDpArray = floatArrayOf(radiusDp, rightDp, rightDp, radiusDp)
                        material?.render(adView.apply {
                            setAdInfo(material)
                        }, materialViewSpec,  if (!AdConstant.isTemplateApp()) null else object :
                            IMaterialInteractionListener {
                            override fun onAdShow() {

                            }

                            override fun onAdClick() {

                            }

                            override fun onCreativeButtonClick() {

                            }

                            override fun onAdvClose() {

                            }

                            override fun onDislikeSelect() {
                                adView?.isVisible(false)
                                listener.adClose()
                                if (pgType.isNotEmpty()) {
                                    CacheUtil.put(pgType, System.currentTimeMillis())
                                }
                            }
                        })
                        adView?.isVisible(true)
                        return true
                    }

                    override fun onError(p0: LoadMaterialError?) {
                        listener.onError(p0)
                        adView?.isVisible(false)
                        LogE("ad->$pgType:${p0?.message}")
                    }
                }
            )
        } else {
            adView.isVisible(false)
        }
    }

    fun showIcon(
        pgType: String,
        activity: Activity?,
        adView: ExtAdMaterialView,
        listener: ShowFeedListener = ShowFeedListener()
    ) {
        if (!checkLoadTime()) return
        if (isAdControlShow(pgType)) {
            val context = WeakReference(activity)
//            val listener=WeakReference(listener)
            val oldPgtype = pgType
            val combinedPgtype = AdConstant.mappingPgtype(oldPgtype)
            val slowWidth = AdConstant.setTemplateAdWidth(pgType)
            MediationManager.getInstance().loadEmbeddedMaterial(combinedPgtype,
                SceneInfo().apply {
                    pgtype = combinedPgtype
                    if (oldPgtype == "bigfc1" || oldPgtype == "bigfc3") {
                        isUseCacheFirst = true
                    }
                    if (slowWidth != 0) {
                        slotWidth = slowWidth
                    }
                    addExtraParameter(ShadowConstants.EXT_PARAM_GAME_TYPE, oldPgtype)
                },
                object : MediationAdListener<IEmbeddedMaterial> {
                    override fun onLoad(material: IEmbeddedMaterial?): Boolean {
                        if (!isActivityAlive(context.get())) return false
                        listener.onLoad(material)
                        if (material != null) {
                            var materialViewSpec = MaterialViewSpec()
                            materialViewSpec.context = context.get()
                            materialViewSpec.displayOrder =
                                intArrayOf(ShadowConstants.SHOW_STYLE_LARGE)
                            materialViewSpec.radiusDp = 12f
                            materialViewSpec.scaleType =
                                if (!material?.iconUrl.isNullOrEmpty()) MaterialViewSpec.SCALE_TYPE_AUTO_SIZE else MaterialViewSpec.SCALE_TYPE_FIXXY
                            adView?.isVisible(true)
                            material?.render(adView, materialViewSpec, null)
                            adView.setAdInfo(material)

                        }
                        return true
                    }

                    override fun onError(p0: LoadMaterialError?) {
                        listener.onError(p0)
                        adView?.isVisible(false)
                        LogE("ad->$pgType:${p0?.message}")
                    }
                }
            )

        } else {
            adView.isVisible(false)
        }
    }

    /***
     * 加载插屏广告弹窗
     */
    private fun loadInterstitialMaterial(pgType: String, context: WeakReference<Activity>) {
        MediationManager.getInstance().loadInterstitialMaterial(
            pgType,
            SceneInfo().apply {
                pgtype = pgType
                isUseCacheFirst = true
                slotWidth = AdConstant.setTemplateAdWidth(pgType)
                addExtraParameter(ShadowConstants.EXT_PARAM_GAME_TYPE, pgType)
            },
            object : MediationAdListener<IInterstitialMaterial> {
                override fun onLoad(material: IInterstitialMaterial?): Boolean {
                    if (material == null || !isActivityAlive(context.get())) return false
                    context.get()?.let {
                        AdMouldDialog(it,material).dialogShow()
                    }
                    isAdPopLoading = false
                    return true
                }

                override fun onError(p0: LoadMaterialError?) {
                    isAdPopLoading = false
                    LogE("AdPop:->onError:${p0?.message} ${p0?.code}")
                }
            }
        )
    }

    fun getPeopleNumStr(): String? {
        val num: Float = Random.nextInt(100, 10000) / 100f
        return String.format("%.2fw人浏览", num)
    }

    fun clearPopStatus() {
        isAdPopLoading = false
    }

    /**
     * 热起引起的多次加载广告问题
     */
    fun checkLoadTime(): Boolean {
        return abs(System.currentTimeMillis() - getAppModel().lastHotSplashTime) > 10 * 1000
    }


    fun checkAdPop(): Boolean {
        if (!popAdShouldLoad) return false
        var popControl = getPopControl()
        if (DataUtil.daysBetween(
                DeviceUtil.installed(),
                System.currentTimeMillis()
            ) >= popControl.nN().first_open_interval.nN().parseInt(1)
            && getOpenTimes() >= popControl.launch_times.nN().parseInt()
        ) {
            var pop = CacheUtil.getObj(Constant.SP_AD_POP, AdPopBean::class.java).nN()
            return if (pop.showMainPopTimes < popControl.everyday_show_times.parseInt(1)) {
                pop.showMainPopShowTime == 0L || abs(pop.showMainPopShowTime - System.currentTimeMillis()) > popControl.open_inerval.parseLong(
                    36000
                ) * 1000
            } else {
                false
            }
        }
        return false
    }


    fun isActivityAlive(activity: Activity?): Boolean {
        return activity != null && !activity.isFinishing && (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 || !activity.isDestroyed)
    }

}