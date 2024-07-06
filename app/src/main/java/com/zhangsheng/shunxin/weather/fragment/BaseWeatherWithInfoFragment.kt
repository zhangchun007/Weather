package com.zhangsheng.shunxin.weather.fragment

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.maiya.thirdlibrary.ext.isVisible
import com.maiya.thirdlibrary.ext.listIndex
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.ext.setSingleClickListener
import com.xm.xmlog.XMLogAgent
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.information.InformationFragment
import com.zhangsheng.shunxin.information.utils.JrttPostBackUtils
import com.zhangsheng.shunxin.weather.MainActivity
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import com.zhangsheng.shunxin.weather.widget.CustomViewPager

/**
 * 说明：首页带信息流的天气tab
 * 作者：刘鹏
 * 添加时间：5/14/21 4:52 PM
 * 修改人：liupe
 * 修改时间：5/14/21 4:52 PM
 */
abstract class BaseWeatherWithInfoFragment : BaseWeatherFragment() {

    protected var mLlInfoBarBack: LinearLayout? = null
    protected var mTvInfoTemp: TextView? = null
    protected var mTvInfoCity: TextView? = null
    protected var mIvInfoLocation: ImageView? = null
    protected var mIvInfoWeather: ImageView? = null
    protected var mRlInfoTopBar: RelativeLayout? = null
    protected var vp: CustomViewPager? = null


    override fun initView() {
        super.initView()
        mRlInfoTopBar = view?.findViewById(R.id.rl_info_top_bar)
        mLlInfoBarBack = view?.findViewById(R.id.ll_info_back)
        mTvInfoTemp = view?.findViewById(R.id.info_temp)
        mTvInfoCity = view?.findViewById(R.id.tv_info_city)
        mIvInfoLocation = view?.findViewById(R.id.iv_info_location)
        mIvInfoWeather = view?.findViewById(R.id.iv_info_weather)
        vp = view?.findViewById(R.id.vp)
        mLlInfoBarBack?.setSingleClickListener {
            getCurFragment(index)?.upToScroll()
        }
    }

    /**
     * 说明：更新顶部tab数据
     * 作者：刘鹏
     * 添加时间：5/17/21 10:19 AM
     * 修改人：liupe
     * 修改时间：5/17/21 10:19 AM
     */
    override fun updateInfoBarData(data: WeatherBean) {
        mTvInfoTemp?.text = data.nN().tc
        mTvInfoCity?.text = binding.tvLocation.text.toString()
        mIvInfoLocation?.isVisible(
            WeatherUtils.getDatas().listIndex(index).isLocation
        )
        mIvInfoWeather?.setImageResource(WeatherUtils.IconBig(data.nN().wtid))
    }


    /**
     * 说明：信息流tab置顶或离开
     * 作者：刘鹏
     * 添加时间：5/14/21 4:54 PM
     * 修改人：liupe
     * 修改时间：5/14/21 4:54 PM
     */
    open fun showInfoBar(show: Boolean) {
        if (!isAdded) {
            return
        }
        vp?.setScroll(!show)
        mRlInfoTopBar?.isVisible(show)
        if (activity != null && activity is MainActivity) {
            (activity as MainActivity).hideBottomBar(show)
        }
        if (show) {
            XMLogAgent.onPageStart(EnumType.上报埋点.信息流主页)
        } else {
            XMLogAgent.onPageEnd(EnumType.上报埋点.信息流主页)
            refreshMainData()
            JrttPostBackUtils.getInstance().infoSahowPostBack(
                InformationFragment.lastVisibleItem
            )
        }
    }

}