package com.zhangsheng.shunxin.ad

import com.google.gson.Gson
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.utils.AppContext
import com.maiya.thirdlibrary.utils.CacheUtil
import com.maiya.thirdlibrary.utils.DisplayUtil
import com.zhangsheng.shunxin.BuildConfig
import com.zhangsheng.shunxin.ad.params.AdDefaultConfigBean
import com.zhangsheng.shunxin.weather.common.Configure
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/7/21 18:28
 */
object AdConstant {
//    const val APP_TYPE_ID = "100042"
//    const val APP_NAME = "顺心天气"
//    const val CSJ_APP_ID = "5112193"
//    const val GDT_APP_ID = "1111050187"
//    const val KS_APP_ID = "532000001"
//    const val BD_APP_ID = "c8bd29ef"

    /**
     * 图文
     */
    //首页-24和15之间-小图
    val SLOT_BIGBTPRE15 = "bigbtpre15"

    //首页-24和15之间-大图
    val SLOT_MIXBT24PRE15 = "Mixbt24pre15"

    //首页-弹窗
    val SLOT_BIGPOP = "bigpop"

    //顶部横幅广告
    val SLOT_BANNERTOP = "bannertop"

    //首页左-小图文
    val SLOT_BIGFC1 = "bigfc1"

    //首页左-icon图文
    val SLOT_BIGFC2 = "bigfc2"

    //首页右-icon图文
    val SLOT_BIGFC3 = "bigfc3"

    //开屏
    val SLOT_OPEN = "open"

    //首页左-浮动小图文
    val SLOT_FLOATSMALL = "floatsmall"

    //空气质量-小图
    val SLOT_BIGKQZLXQ = "bigkqzlxq"

    //实时天气-大图
    val SLOT_BIGDRAWDRTQ = "bigdrawdrtq"

    //15日-大图
    val SLOT_BIGDRAWJMXQ = "bigdrawjmxq"

    //万年历-大图
    val SLOT_BIGRLBOTTOM = "bigrlbottom"

    //黄历-大图
    val SLOT_BIGHUANGLI = "bighuangli"

    //预警-大图
    val SLOT_BIGYJEJYM = "bigyjejym"

    //设置-大图
    val SLOT_BIGSET = "bigset"

    //新闻列表1-大图
    val SLOT_BIGLIST11 = "biglist11"

    //新闻列表2-大图
    val SLOT_BIGLIST12 = "biglist12"

    //新闻列表3-大图
    val SLOT_BIGLIST13 = "biglist13"

    //新闻详情页1-大图
    val SLOT_BIGXWXQ = "bigxwxq"

    //新闻详情页1-小图
    val SLOT_SMALLXWXQ1 = "smallxwxq1"

    //首页-15和指数-大图
    val SLOT_BIGDRAWBTSHZS = "bigdrawbtshzs"

    //首页-15和指数-小图
    val SLOT_BIGDRAWBTSHZS2 = "bigdrawbtshzs2"

    //首页-指数和联播-大图
    val SLOT_BIGDRAWSHZS = "bigdrawshzs"

    //首页-指数和联播-小图
    val SLOT_BIGDRAWSHZS2 = "bigdrawshzs2"

    //城市管理-小图
    val SLOT_CITYBOTTOM = "citybottom"

    //CCTV视频内-小图
    val SLOT_BIGTQSPXF = "bigtqspxf"

    //合并Pgtype
    const val SLOT_NEWSAMLLHB = "newsamllhb"

    // 设置页-左图右文
    const val SLOT_SMALLSYXT = "smallsyxt"

    //40日底部大图
    const val SLOT_PREDICT40DAYS = "predict40days"


    val COMBINED_PGTYPE_MAP: HashMap<String, String> by lazy {
        HashMap<String, String>().apply {
            if (isTemplateApp()){
                return@apply
            }
            this[SLOT_BIGBTPRE15] = SLOT_NEWSAMLLHB
            this[SLOT_BIGPOP] = SLOT_NEWSAMLLHB
            this[SLOT_BANNERTOP] = SLOT_NEWSAMLLHB
        }
    }

    fun mappingPgtype(old: String): String? {
        return COMBINED_PGTYPE_MAP[old] ?: old
    }

    private val SP_AD_CONFIG = "sp_ad_config"
    private var configs: AdDefaultConfigBean? = null
    fun getDefaultConfig(): List<AdDefaultConfigBean.Config> {
        if (!configs?.configs.isNullOrEmpty()) return configs.nN().configs.nN()

        configs = CacheUtil.getObj(SP_AD_CONFIG, AdDefaultConfigBean::class.java)

        if (configs == null || configs.nN().update != BuildConfig.AD_DEFAULT_UPDATE) {

            var jsonStr = StringBuilder()
            try {
                val manager = AppContext.getContext().assets
                val bf = BufferedReader(InputStreamReader(manager.open("adConfig.json")))
                bf.use { bf ->
                    bf.forEachLine {
                        if (it.isNotEmpty()) {
                            jsonStr.append(it)
                        }
                    }
                }
            } catch (e: Exception) {
                jsonStr.delete(0, jsonStr.length)
            }

            Try {
                if (jsonStr.toString().trim().isNotEmpty()) {
                    configs = Gson().fromJson<AdDefaultConfigBean>(
                        jsonStr.toString(),
                        AdDefaultConfigBean::class.java
                    )
                    CacheUtil.putObj(SP_AD_CONFIG, configs)
                }
            }
        }

        return configs.nN().configs.nN()
    }


    val slowWidth = DisplayUtil.getScreenWidth()
    fun setTemplateAdWidth(oldpgtype: String): Int {
        if (!isTemplateApp()) return 0
        when (oldpgtype) {
            SLOT_BIGBTPRE15,
            SLOT_MIXBT24PRE15,
            SLOT_BIGDRAWBTSHZS,
            SLOT_BIGDRAWBTSHZS2,
            SLOT_BIGDRAWSHZS,
            SLOT_BIGDRAWSHZS2,
            SLOT_BANNERTOP -> {
                return setAppSlowWidth()
            }
            SLOT_BIGPOP -> {
                return slowWidth - DisplayUtil.dip2px(70f)
            }
            else -> {
                return slowWidth
            }
        }
    }

    private fun setAppSlowWidth(): Int = when (Configure.appTypeId) {
        "100071" -> slowWidth - DisplayUtil.dip2px(24f)
        else -> slowWidth - DisplayUtil.dip2px(24f)
    }

    fun isTemplateApp(): Boolean {
        return Configure.appTypeId == "100071"
    }
}