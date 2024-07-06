package com.zhangsheng.shunxin.weather.activity

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.Observer
import com.maiya.thirdlibrary.base.AacActivity
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.utils.CacheUtil
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.utils.OSUtils
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.net.bean.LastPushTimeBean
import com.zhangsheng.shunxin.weather.net.bean.PushBean
import com.zhangsheng.shunxin.weather.net.bean.PushCityBean
import com.zhangsheng.shunxin.weather.utils.NotificationsUtils
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import com.my.sdk.stpush.STPushManager
import com.my.sdk.stpush.open.Tag
import com.xm.xmcommon.XMParam
import com.zhangsheng.shunxin.databinding.ActivityPushSettingBinding
import com.zhangsheng.shunxin.weather.dialog.PushTimePickerDialog
import org.koin.android.ext.android.inject

class PushSettingActivity : AacActivity<BaseViewModel>() {

    override val vm by inject<BaseViewModel>()
    override val binding by inflate<ActivityPushSettingBinding>()
    private var push_day_time = "7:00"
    private var push_night_time = "18:30"
    private var cb_day_push = false
    private var cb_night_push = false
    private var push: PushCityBean? = null



    override fun initView(savedInstanceState: Bundle?) {
        push = CacheUtil.getObj(Constant.SP_PUSH_CODE, PushCityBean::class.java)
        CacheUtil.getObj(Constant.SP_LAST_PUSH_TIME, LastPushTimeBean::class.java).nN().apply {
            push_day_time = this.dayTime
            push_night_time = this.nightTime
        }
        binding.title.initTitle("早晚天气提醒")

        binding.openNotify.setSingleClickListener {
            NotificationsUtils.openSetting(this)
        }
        binding.btnOpenNotify.setSingleClickListener {
            NotificationsUtils.openSetting(this)
        }

        binding.llDayTime.setSingleClickListener {
            if (binding.cbDay.isChecked) {
                var daytime = binding.dayTime.text
                var hourIndex =
                    if (daytime.isNotEmpty()) daytime.split(":").listIndex(0).parseInt(7) else 7
                var minIndex =
                    if (daytime.isNotEmpty()) daytime.split(":").listIndex(1).parseInt(0) else 0
                if (OSUtils.isVivo()) {

                    PushTimePickerDialog(
                        this,
                        7,
                        11,
                        hourIndex - 7,
                        if (minIndex == 0) 0 else (minIndex - 0) / 10
                    ) { hour, min ->
                        binding.dayTime.text = "${hour}:${if (min == 0) "00" else "$min"}"
                        setTag()
                    }.show()
                } else {

                    PushTimePickerDialog(
                        this,
                        5,
                        11,
                        hourIndex - 5,
                        if (minIndex == 0) 0 else (minIndex - 0) / 10
                    ) { hour, min ->
                        binding.dayTime.text = "${hour}:${if (min == 0) "00" else "$min"}"
                        setTag()
                    }.show()
                }
            }
        }

        binding.llNightTime.setSingleClickListener {
            if (binding.cbNight.isChecked) {
                var nightTime = binding.nightTime.text
                var hourIndex = if (nightTime.isNotEmpty()) nightTime.split(":").listIndex(0)
                    .parseInt(18) else 18
                var minIndex = if (nightTime.isNotEmpty()) nightTime.split(":").listIndex(1)
                    .parseInt(30) else 30
                if (OSUtils.isVivo()) {

                    PushTimePickerDialog(
                        this,
                        16,
                        21,
                        hourIndex - 16,
                        if (minIndex == 0) 0 else (minIndex - 0) / 10
                    ) { hour, min ->
                        binding.nightTime.text = "${hour}:${if (min == 0) "00" else "$min"}"
                        setTag()
                    }.show()
                } else {
                    PushTimePickerDialog(
                        this,
                        16,
                        22,
                        hourIndex - 16,
                        if (minIndex == 0) 0 else (minIndex - 0) / 10
                    ) { hour, min ->
                        binding.nightTime.text = "${hour}:${if (min == 0) "00" else "$min"}"
                        setTag()
                    }.show()
                }

            }
        }

        binding.cbNight.setOnCheckedChangeListener { view, isChecked ->
            setTag()
            if (isChecked) {
                binding.nightTime.setTextColor(Color.parseColor("#41A0F9"))
            } else {
                binding.nightTime.setTextColor(Color.parseColor("#9296A0"))
            }
        }

        binding.cbDay.setOnCheckedChangeListener { view, isChecked ->
            setTag()
            if (isChecked) {
                binding.dayTime.setTextColor(Color.parseColor("#41A0F9"))
            } else {
                binding.dayTime.setTextColor(Color.parseColor("#9296A0"))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setStatus(NotificationsUtils.openNotificationChannel(this))
        STPushManager.getInstance().queryTag(this)
    }

    private fun setStatus(notificationEnabled: Boolean) {
        binding.pushPermission.isVisible(!notificationEnabled)
        binding.cbDay.isEnabled = notificationEnabled
        binding.cbNight.isEnabled = notificationEnabled
        binding.llDayTime.isEnabled = notificationEnabled
        binding.llNightTime.isEnabled = notificationEnabled
    }

    override fun initObserve() {
        push?.let {
            Try {
                binding.cbDay.setChecked(it.dayTime.isNotEmpty(), false)
                binding.cbNight.setChecked(it.nightTime.isNotEmpty(), false)
                binding.dayTime.text = it.dayTime.isStr(push_day_time)
                binding.nightTime.text = it.nightTime.isStr(push_night_time)
                if (it.dayTime.isNotEmpty()) {
                    binding.dayTime.setTextColor(Color.parseColor("#41A0F9"))
                }
                if (it.nightTime.isNotEmpty()) {
                    binding.nightTime.setTextColor(Color.parseColor("#41A0F9"))
                }
            }
        }
        getAppModel().queryTags.safeObserve(this, Observer {
            Try {
                if (it.nN().tags.nN().isNotEmpty()) {
                    it.nN().tags.nN().forEach {
                        if (it.contains(":")) {
                            var time = it.split(":")[0].toInt()
                            if (time in 5..11) {
                                push_day_time = it
                                cb_day_push = true
                                binding.dayTime.text = it
                                binding.cbDay.setChecked(true, false)
                            } else if (time in 12..22) {
                                push_night_time = it
                                cb_night_push = true
                                binding.nightTime.text = it
                                binding.cbNight.setChecked(true, false)
                            }
                        }
                    }
                    getAppModel().queryTags.value = PushBean()
                }

            }
        })

        getAppModel().setTags.safeObserve(this, Observer {
            if (it != EnumType.推送状态.成功) {
                binding.dayTime.text = push_day_time
                binding.cbDay.setChecked(cb_day_push, false)
                binding.nightTime.text = push_night_time
                binding.cbNight.setChecked(cb_night_push, false)
            } else {
                push_day_time = binding.dayTime.text.toString()
                cb_day_push = binding.cbDay.isChecked
                push_night_time = binding.nightTime.text.toString()
                cb_night_push = binding.cbNight.isChecked
                CacheUtil.putObj(Constant.SP_PUSH_CODE, getAppModel().pushCity.apply {
                    val dayTime =
                        if (this.dayTime.isEmpty()) binding.dayTime.text.toString() else this.dayTime
                    val nightTime =
                        if (this.nightTime.isEmpty()) binding.nightTime.text.toString() else this.nightTime
                    CacheUtil.putObj(Constant.SP_LAST_PUSH_TIME, LastPushTimeBean().apply {
                        this.dayTime = dayTime
                        this.nightTime = nightTime
                    })
                })
            }
        })
    }

    private fun setTag() {
        val time_day = if (binding.cbDay.isChecked) binding.dayTime.text.toString() else ""
        val time_night = if (binding.cbNight.isChecked) binding.nightTime.text.toString() else ""
        if (WeatherUtils.getDatas().isEmpty()) {
            xToast("未检测到添加城市，请前去添加城市")
        } else {
             getAppModel().pushCity.apply {
                this.dayTime = time_day
                this.nightTime = time_night
            }
            var cityCode = if (WeatherUtils.getDatas().none { it.regioncode == push.nN().code }) {
                getAppModel().pushCity.isLocation = WeatherUtils.getDatas()[0].isLocation
                WeatherUtils.getDatas()[0].regioncode
            } else {
                getAppModel().pushCity.isLocation = push.nN().isLocation
                push.nN().code
            }
            getAppModel().pushCity.code = cityCode

            val tag = STPushManager.getInstance().setTag(
                this,
                Tag(time_day),
                Tag(time_night),
                Tag(XMParam.getAppVer()),
                Tag(cityCode)
            )
            if (tag == 100005) {
                xToast("请求超时，请重新操作")
            }
        }
    }

}