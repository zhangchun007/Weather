package com.zhangsheng.shunxin.weather.widget.refresh

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.provider.Settings
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import com.maiya.thirdlibrary.ext.*
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.utils.AnimationUtil
import com.zhangsheng.shunxin.weather.utils.LocationUtil
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import kotlinx.android.synthetic.main.view_refresh_status.view.*

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/8/6 10:22
 */
class RefreshStatusView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_refresh_status, this, true)
        this.isVisible(false)

        this.setSingleClickListener {
            val netStatus = getAppModel().refreshAction.value.nN()
            when (netStatus) {
                EnumType.刷新类型.网络错误 -> {
                    val intent = Intent(Settings.ACTION_SETTINGS)
                    context.startActivity(intent)
                }
                EnumType.刷新类型.定位失败 -> {
                    val intent = Intent(Settings.ACTION_SETTINGS)
                    context.startActivity(intent)
                }
                EnumType.刷新类型.系统时间 -> {
                    val intent = Intent(Settings.ACTION_SETTINGS)
                    context.startActivity(intent)
                }
                EnumType.刷新类型.定位权限 -> {
                    val intent = Intent(Settings.ACTION_SETTINGS)
                    context.startActivity(intent)
                }
            }
        }
    }

    fun setStatus(status: Int, index: Int) {
        LogE("refreshStatus->$status")
        when (status) {
            EnumType.刷新类型.初始状态 -> {
                this?.let {
                    if (it.visibility == View.VISIBLE) {
                        AnimationUtil.deleteItem(this, this.measuredHeight)
                    }
                }
                refresh_icon?.apply {
                    clearAnimation()
                }
            }
            EnumType.刷新类型.开始刷新 -> {
                try {
                    val bean = WeatherUtils.getDatas().listIndex(index)
                    if (bean.isLocation) {
                        getAppModel().refreshAction.value = EnumType.刷新类型.重新定位
                    } else {
                        getAppModel().requestWeather(bean, index, true)
                    }
                } catch (e: Exception) {
                    getAppModel().refreshAction.value = EnumType.刷新类型.刷新失败
                }
            }
            EnumType.刷新类型.正在刷新 -> {
                mainView1?.background = null
                refresh_icon?.apply {
                    if (getAppModel().isRefreshBlackModel) {
                        setImageResource(R.mipmap.base_black_model_loading)
                    } else {
                        setImageResource(R.mipmap.base_loading)
                    }
                    startAnimation(
                        AnimationUtils.loadAnimation(
                            context,
                            R.anim.refresh_route
                        )
                    )
                }
                refresh_content?.apply {
                    if (getAppModel().isRefreshBlackModel) {//暗黑背景刷新颜色为白色
                        this.setTextColor(context.resources.getColor(R.color.color_blackmodle_refresh_loading_complete))
                    } else {
                        this.setTextColor(context.resources.getColor(R.color.color_refresh_loading_complete))
                    }
                    text = "正在加载中..."
                }
                this?.let {
                    if (this.visibility == View.GONE) {
                        AnimationUtil.addItem(this, this.measuredHeight)
                    }
                }
            }
            EnumType.刷新类型.系统时间 -> {
                mainView1?.setBackgroundResource(R.drawable.shape20r_red)
                refresh_icon?.apply {
                    clearAnimation()
                    if (getAppModel().isRefreshBlackModel) {
                        setImageResource(R.mipmap.icon_refresh_black_model_net_error)
                    } else {
                        setImageResource(R.mipmap.icon_refresh_net_error)
                    }
                }
                refresh_content?.apply {
                    this.text = "您的时间设置有误，请调整"
                    if (getAppModel().isRefreshBlackModel) {
                        this.setTextColor(context.resources.getColor(R.color.color_blackmodle_refresh_loading_complete))
                    } else {
                        this.setTextColor(context.resources.getColor(R.color.refresh_error_color))
                    }

                }
            }
            EnumType.刷新类型.网络错误 -> {
                mainView1?.setBackgroundResource(R.drawable.shape20r_red)
                refresh_icon?.apply {
                    clearAnimation()
                    if (getAppModel().isRefreshBlackModel) {
                        setImageResource(R.mipmap.icon_refresh_black_model_net_error)
                    } else {
                        setImageResource(R.mipmap.icon_refresh_net_error)
                    }
                }
                refresh_content?.apply {
                    this.text = "当前网络不可用 试试看刷新页面"
                    if (getAppModel().isRefreshBlackModel) {
                        this.setTextColor(context.resources.getColor(R.color.color_blackmodle_refresh_loading_complete))
                    } else {
                        this.setTextColor(context.resources.getColor(R.color.refresh_error_color))
                    }
                }
            }
            EnumType.刷新类型.定位失败 -> {
                mainView1?.setBackgroundResource(R.drawable.shape20r_red)
                refresh_icon?.apply {
                    clearAnimation()
                    if (getAppModel().isRefreshBlackModel) {
                        setImageResource(R.mipmap.icon_refresh_black_model_net_error)
                    } else {
                        setImageResource(R.mipmap.icon_refresh_net_error)
                    }
                }
                refresh_content?.apply {
                    this.text = "未成功获取最新定位信息"
                    if (getAppModel().isRefreshBlackModel) {
                        this.setTextColor(context.resources.getColor(R.color.color_blackmodle_refresh_loading_complete))
                    } else {
                        this.setTextColor(context.resources.getColor(R.color.refresh_error_color))
                    }
                }
                this?.let {
                    if (it.visibility == View.GONE) {
                        AnimationUtil.addItem(this, this.measuredHeight)
                    }
                }
            }
            EnumType.刷新类型.重新定位 -> {
                mainView1?.background = null
                getAppModel().location()
                refresh_icon?.apply {
                    if (getAppModel().isRefreshBlackModel) {
                        setImageResource(R.mipmap.base_black_model_loading)
                    } else {
                        setImageResource(R.mipmap.base_loading)
                    }
                    startAnimation(
                        AnimationUtils.loadAnimation(
                            context,
                            R.anim.refresh_route
                        )
                    )
                }
                refresh_content?.apply {
                    if (getAppModel().isRefreshBlackModel) {//暗黑背景刷新颜色为白色
                        this.setTextColor(context.resources.getColor(R.color.color_blackmodle_refresh_loading_complete))
                    } else {
                        this.setTextColor(context.resources.getColor(R.color.color_refresh_loading_complete))
                    }
                    text = "正在定位中..."
                }
                this?.let {
                    if (this.visibility == View.GONE) {
                        AnimationUtil.addItem(this, this.measuredHeight)
                    }
                }
            }
            EnumType.刷新类型.刷新成功 -> {
                mainView1?.background = null
                val data = getAppModel().currentWeather.value.nN().weather.nN()
                if (data.isLocation && data.location.nN().state != LocationUtil.定位成功) {
                    if (data.location.nN().state == LocationUtil.定位权限) {
                        getAppModel().refreshAction.value = EnumType.刷新类型.定位权限
                    } else {
                        getAppModel().refreshAction.value = EnumType.刷新类型.定位失败
                    }
                } else {
                    if (getAppModel().isRefreshBlackModel) {
                        refresh_icon?.setImageResource(R.mipmap.icon_refresh_black_model_ok)
                    } else {
                        refresh_icon?.setImageResource(R.mipmap.icon_refresh_ok)
                    }
                    refresh_content?.apply {
                        this.text = "加载成功"
                        if (getAppModel().isRefreshBlackModel) {//暗黑背景刷新颜色为白色
                            this.setTextColor(context.resources.getColor(R.color.color_blackmodle_refresh_loading_complete))
                        } else {
                            this.setTextColor(context.resources.getColor(R.color.color_refresh_loading_complete))
                        }
                    }
                    this?.let {
                        AnimationUtil.refreshAnim(refresh_icon) {
                            Try {
                                this?.let {
                                    if (this.visibility == VISIBLE) {
                                        AnimationUtil.deleteItem(this, this.measuredHeight)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            EnumType.刷新类型.刷新失败 -> {
                mainView1?.setBackgroundResource(R.drawable.shape20r_red)
                refresh_content?.apply {
                    this.text = "加载失败，请重试"
                    if (getAppModel().isRefreshBlackModel) {
                        this.setTextColor(context.resources.getColor(R.color.color_blackmodle_refresh_loading_complete))
                    } else {
                        this.setTextColor(context.resources.getColor(R.color.refresh_error_color))
                    }
                }
                refresh_icon?.apply {
                    clearAnimation()
                    if (getAppModel().isRefreshBlackModel) {
                        setImageResource(R.mipmap.icon_refresh_black_model_net_error)
                    } else {
                        setImageResource(R.mipmap.icon_refresh_net_error)
                    }
                }
            }
            EnumType.刷新类型.定位权限 -> {
                mainView1?.setBackgroundResource(R.drawable.shape20r_red)
                refresh_icon?.apply {
                    clearAnimation()
                    if (getAppModel().isRefreshBlackModel) {
                        setImageResource(R.mipmap.icon_refresh_black_model_net_error)
                    } else {
                        setImageResource(R.mipmap.icon_refresh_net_error)
                    }
                }
                refresh_content?.apply {
                    this.text = "定位失败，请开启定位权限"
                    if (getAppModel().isRefreshBlackModel) {
                        this.setTextColor(context.resources.getColor(R.color.color_blackmodle_refresh_loading_complete))
                    } else {
                        this.setTextColor(context.resources.getColor(R.color.refresh_error_color))
                    }
                }
                this?.let {
                    if (it.visibility == View.GONE) {
                        AnimationUtil.addItem(this, this.measuredHeight)
                    }
                }

            }
        }
    }

    /**
     * 动态设置刷新颜色
     */
    fun setRefeshTextColor(isBlackModel:Boolean){
        if (getAppModel().isRefreshBlackModel) {
            refresh_content?.setTextColor(context.resources.getColor(R.color.color_blackmodle_refresh_loading_complete))
        } else {
            refresh_content?.setTextColor(context.resources.getColor(R.color.refresh_error_color))
        }
    }


}