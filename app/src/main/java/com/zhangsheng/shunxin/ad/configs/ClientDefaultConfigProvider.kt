package com.zhangsheng.shunxin.ad.configs

import com.xinmeng.shadow.base.ShadowConstants
import com.xinmeng.shadow.mediation.api.IDefaultSlotConfigProvider
import com.xinmeng.shadow.mediation.config.CustomSlotConfig
import com.zhangsheng.shunxin.ad.AdConstant
import com.zhangsheng.shunxin.weather.common.Configure
import com.zhangsheng.shunxin.weather.common.EnumType

class ClientDefaultConfigProvider : IDefaultSlotConfigProvider {
    override fun provide(pgtype: String, slotType: Int): CustomSlotConfig {

        AdConstant.getDefaultConfig().find { it.pgtype == pgtype }.apply {
            return if (this != null) {
                when (this.type) {
                    EnumType.广告样式.信息流 -> addFeed(
                        pgtype,
                        this.jrId,
                        this.jrWeight,
                        this.gdtId,
                        this.gdtWeight
                    )
                    EnumType.广告样式.开屏 -> addSplash(
                        pgtype,
                        this.jrId,
                        this.jrWeight,
                        this.gdtId,
                        this.gdtWeight
                    )
                    EnumType.广告样式.激励视频 -> addVideo(
                        pgtype,
                        this.jrId,
                        this.jrWeight,
                        this.gdtId,
                        this.gdtWeight
                    )
                    EnumType.广告样式.信息流_模板 -> addFeedTemplate(
                        pgtype,
                        this.jrId,
                        this.jrWeight,
                        this.gdtId,
                        this.gdtWeight
                    )

                    else -> CustomSlotConfig.NONE
                }
            } else {
                CustomSlotConfig.NONE
            }
        }
    }


    fun addSplash(
        pgtype: String,
        jrAdID: String,
        jrWeight: Int,
        gdtId: String,
        gdtWeight: Int
    ): CustomSlotConfig {
        return CustomSlotConfig()
            .apply {
                this.add(
                    pgtype, ShadowConstants.ADV_TYPE_CSJ, ShadowConstants.SLOT_TYPE_SPLASH,
                    ShadowConstants.CATEGORY_DEFAULT,
                    Configure.csjAppId, jrAdID, 1, jrWeight, ShadowConstants.PLATFORM_CSJ
                )
                this.add(
                    pgtype,
                    ShadowConstants.ADV_TYPE_GDT,
                    ShadowConstants.SLOT_TYPE_SPLASH,
                    ShadowConstants.CATEGORY_DEFAULT,
                    Configure.gdtAppId,
                    gdtId,
                    1,
                    gdtWeight,
                    ShadowConstants.PLATFORM_GDT
                )
            }
    }

    fun addJrSplash(pgtype: String, jrAdID: String): CustomSlotConfig {
        return CustomSlotConfig()
            .apply {
                this.add(
                    pgtype, ShadowConstants.ADV_TYPE_CSJ, ShadowConstants.SLOT_TYPE_SPLASH,
                    ShadowConstants.CATEGORY_DEFAULT,
                    Configure.csjAppId, jrAdID, 1, 1000, ShadowConstants.PLATFORM_CSJ
                )
            }
    }

    fun addJrFeed(pgtype: String, jrAdID: String): CustomSlotConfig {
        return CustomSlotConfig()
            .apply {
                this.add(
                    pgtype, ShadowConstants.ADV_TYPE_CSJ, ShadowConstants.SLOT_TYPE_EMBEDDED,
                    ShadowConstants.CATEGORY_EMBEDDED_COMMON,
                    Configure.csjAppId, jrAdID, 1, 1000, ShadowConstants.PLATFORM_CSJ
                )
            }
    }


    fun addFeed(
        pgtype: String,
        jrAdID: String,
        jrWeight: Int,
        gdtId: String = "",
        gdtWeight: Int = 0
    ): CustomSlotConfig {
        return CustomSlotConfig()
            .apply {
                this.add(
                    pgtype, ShadowConstants.ADV_TYPE_CSJ, ShadowConstants.SLOT_TYPE_EMBEDDED,
                    ShadowConstants.CATEGORY_EMBEDDED_COMMON,
                    Configure.csjAppId, jrAdID, 1, jrWeight, ShadowConstants.PLATFORM_CSJ
                )
                if (gdtId.isNotEmpty()) {
                    this.add(
                        pgtype,
                        ShadowConstants.ADV_TYPE_GDT,
                        ShadowConstants.SLOT_TYPE_EMBEDDED,
                        ShadowConstants.CATEGORY_EMBEDDED_COMMON,
                        Configure.gdtAppId,
                        gdtId,
                        1,
                        gdtWeight,
                        ShadowConstants.PLATFORM_GDT
                    )
                }
            }
    }

    fun addFeedTemplate(
        pgtype: String,
        jrAdID: String,
        jrWeight: Int,
        gdtId: String = "",
        gdtWeight: Int = 0
    ): CustomSlotConfig {
        return CustomSlotConfig()
            .apply {
                this.add(
                    pgtype, ShadowConstants.ADV_TYPE_CSJ, ShadowConstants.SLOT_TYPE_EMBEDDED,
                    ShadowConstants.CATEGORY_EMBEDDED_TEMPLATE,
                    Configure.csjAppId, jrAdID, 1, jrWeight, ShadowConstants.PLATFORM_CSJ
                )
                if (gdtId.isNotEmpty()) {
                    this.add(
                        pgtype,
                        ShadowConstants.ADV_TYPE_GDT,
                        ShadowConstants.SLOT_TYPE_EMBEDDED,
                        ShadowConstants.CATEGORY_EMBEDDED_COMMON,
                        Configure.gdtAppId,
                        gdtId,
                        1,
                        gdtWeight,
                        ShadowConstants.PLATFORM_GDT
                    )
                }
            }
    }

    fun addVideo(
        pgtype: String,
        jrAdID: String,
        jrWeight: Int,
        gdtId: String,
        gdtWeight: Int
    ): CustomSlotConfig {

        return CustomSlotConfig()
            .apply {
                this.add(
                    pgtype, ShadowConstants.ADV_TYPE_CSJ, ShadowConstants.SLOT_TYPE_REWARD_VIDEO,
                    ShadowConstants.CATEGORY_REWARD_VIDEO_COMMON,
                    Configure.csjAppId, jrAdID, 1, jrWeight, ShadowConstants.PLATFORM_CSJ
                )
                this.add(
                    pgtype,
                    ShadowConstants.ADV_TYPE_GDT,
                    ShadowConstants.SLOT_TYPE_REWARD_VIDEO,
                    ShadowConstants.CATEGORY_REWARD_VIDEO_COMMON,
                    Configure.gdtAppId,
                    gdtId,
                    1,
                    gdtWeight,
                    ShadowConstants.PLATFORM_GDT
                )
            }
    }

}