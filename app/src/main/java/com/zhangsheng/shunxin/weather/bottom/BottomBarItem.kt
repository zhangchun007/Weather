package com.zhangsheng.shunxin.weather.bottom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.kwad.sdk.api.KsAdSDK
import com.kwad.sdk.api.KsContentPage
import com.kwad.sdk.api.KsScene
import com.maiya.thirdlibrary.ext.parseLong
import com.maiya.thirdlibrary.ext.isVisible
import com.maiya.thirdlibrary.utils.CacheUtil
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.common.Configure
import com.zhangsheng.shunxin.weather.fragment.*

/**
 * 说明：底部tab单一项配置，负责创建对应的view及fragment
 * 作者：刘鹏
 * 添加时间：2021/4/28 15:00
 * 修改人：liupe
 * 修改时间：2021/4/28 15:00
 */
class BottomBarItem : FragmentTab() {

    companion object {
        // 天气tab
        const val CMD_WEATHER = "tab_weather"
        // 短视频tab
        const val CMD_VIDEO = "tab_video"
        // 信息流tab
        const val CMD_STREAM = "tab_stream"
        // 日历tab
        const val CMD_CALENDAR = "tab_calendar"
        // 设置tab
        const val CMD_SETTING = "tab_setting"
        // 实用工具tab
        const val CMD_TOOLBOX = "tab_toolbox"

        // 新功能提醒pre
        const val KEY_TAB_NEW_TIP_PRE = "key_tab_newtip_pre_"
    }

    private var eventCMD : String? = ""
    private var hideTitle : Boolean = false

    private var mSelf : Fragment? = null

    private var topDrawableId : Int = 0

    private var isSelected : Boolean = false

    private var mConvertView : View? = null

    private var mBottomText : TextView? = null

    private var mTabImageView : ImageView? = null
    // 新tab提醒
    private var mIvTabTip: ImageView? = null
    // 是否展示新tab提醒
    private var showNewTip: Boolean = false

    private var clickEvent: String? = null

    private var mLottieView: LottieAnimationView? = null

    // lottie动画对应的json文件
    private var lottieFileName: String? = null
    // lottie动画对应的image路径
    private var lottieImageFolder: String? = null
    // 是否是lottie动画的tab
    private var lottieTab: Boolean = false

    /**
     * 说明：创建对应的fragment
     * 作者：刘鹏
     * 添加时间：2021/4/28 15:50
     * 修改人：liupe
     * 修改时间：2021/4/28 15:50
     */
    override fun createFragment(): Fragment {

        var f : Fragment? = null

        if (mSelf != null) {
            f = mSelf!!
        } else {
            when(eventCMD) {
                CMD_WEATHER -> {
                    f = WeatherFragment()
                }
                CMD_CALENDAR -> f = CalendarFragment()
                CMD_VIDEO -> {
                    val adScene = KsScene.Builder(Configure.ksScenePosId.parseLong()).build()
                    val contentPage: KsContentPage = KsAdSDK.getLoadManager().loadContentPage(adScene)
                    contentPage.apply {
                        setAddSubEnable(true) // 允许插入第三方广告；
                        addPageLoadListener(object : KsContentPage.OnPageLoadListener {
                            override fun onLoadStart(page: KsContentPage, pageCount: Int) {

                            }

                            override fun onLoadFinish(page: KsContentPage, pageCount: Int) {}
                            override fun onLoadError(page: KsContentPage) {
                                page.tryToRefresh()
                            }
                        })
                    }
                    f = contentPage.fragment
                }
                CMD_STREAM -> f = NewsFragment()
                CMD_SETTING -> f = SettingFragment()
                CMD_TOOLBOX -> f = ToolboxFragment()
                else -> f = WeatherFragment()
            }
        }
        return f
    }

    /**
     * 说明：创建对应item的view
     * 作者：刘鹏
     * 添加时间：2021/4/28 15:50
     * 修改人：liupe
     * 修改时间：2021/4/28 15:50
     */
    fun inflateView(parent: ViewGroup) : View? {
        if (lottieTab) {
            initLottieTabView(parent)
        } else {
            initDefTabView(parent)
        }
        return mConvertView
    }

    /**
     * 说明：创建默认上图下文的view
     * 作者：刘鹏
     * 添加时间：2021/4/28 15:51
     * 修改人：liupe
     * 修改时间：2021/4/28 15:51
     */
    private fun initDefTabView(parent : ViewGroup) {

        mConvertView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_bottombaritem_def, parent, false)
        mBottomText =
            mConvertView?.findViewById<View>(R.id.bottomtext) as TextView
        mTabImageView = mConvertView?.findViewById<ImageView>(R.id.bottom_img)
        mIvTabTip = mConvertView?.findViewById(R.id.iv_tab_tip)
        mBottomText?.isSelected = isSelected

        mTabImageView?.setImageResource(topDrawableId)
        mTabImageView?.isSelected = isSelected
        mBottomText?.text = getTitle()
        mIvTabTip?.isVisible(showNewTip)

        mConvertView?.tag = this
    }

    private fun initLottieTabView(parent : ViewGroup) {

        mConvertView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_bottombaritem_lottie, parent, false)
        mLottieView = mConvertView!!.findViewById(R.id.lottie_tab)
        mLottieView?.imageAssetsFolder = lottieImageFolder
        mLottieView?.setAnimation(lottieFileName)
        mIvTabTip = mConvertView?.findViewById(R.id.iv_tab_tip)
        mIvTabTip?.isVisible(showNewTip)
        mConvertView!!.tag = this
    }

    fun getEventCMD(): String? {
        return eventCMD
    }

    fun setEventCMD(eventCMD: String?) {
        this.eventCMD = eventCMD
    }

    fun getTopDrawableId(): Int {
        return topDrawableId
    }

    fun setTopDrawableId(@DrawableRes topDrawableId: Int) {
        this.topDrawableId = topDrawableId
    }

    fun isSelected(): Boolean {
        return isSelected
    }


    fun setSelected(selected: Boolean) {
        isSelected = selected
        if (mConvertView == null) {
            return
        }
        if (lottieTab) {
            if (selected) {
                mLottieView?.playAnimation()
            } else {
                mLottieView?.cancelAnimation()
                mLottieView?.progress = 0f
            }
        } else {
            mTabImageView?.setImageResource(topDrawableId)
            mTabImageView?.isSelected = isSelected

            mBottomText?.isSelected = isSelected
            mBottomText?.text = getTitle()
        }
        if (selected) {
            if (showNewTip) {
                CacheUtil.put(KEY_TAB_NEW_TIP_PRE + eventCMD, true)
            }
            showNewTip = false
            mIvTabTip?.isVisible(false)
        }
    }

    fun getView() : View? {
        return mConvertView
    }

    fun setClickEvent(event: String?) {
        this.clickEvent = event
    }

    fun getClickEvent(): String? {
        return clickEvent
    }

    fun setShowNewTabTip() {
        this.showNewTip = true
    }

    fun setLottieTab(lottieTab: Boolean) {
        this.lottieTab = lottieTab
    }

    fun setLottieConfig(imageFolder: String, lottieFile: String) {
        this.lottieFileName = lottieFile
        this.lottieImageFolder = imageFolder
    }

    fun setSelfFragment(fragment: Fragment) {
        this.mSelf = fragment
    }
}