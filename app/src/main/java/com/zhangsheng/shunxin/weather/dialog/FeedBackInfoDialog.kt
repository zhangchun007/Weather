package com.zhangsheng.shunxin.weather.dialog

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.maiya.thirdlibrary.base.BaseDialog
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.utils.CacheUtil
import com.xm.xmcommon.XMCommonManager
import com.xm.xmlog.XMLogAgent
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.databinding.WindowFeedbackInformationViewBinding
import com.zhangsheng.shunxin.databinding.WindowHintViewBinding
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.ClickReport
import com.zhangsheng.shunxin.weather.ext.clickReport
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.ext.showReport
import com.zhangsheng.shunxin.weather.net.bean.LocationReportBean
import com.zhangsheng.shunxin.weather.utils.LocationUtil
import kotlinx.android.synthetic.main.window_feedback_information_view.view.*

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2021/3/24 9:47
 */
class FeedBackInfoDialog(context: Activity, val report: (msg: String) -> Unit) :
    BaseDialog(context) {
    private var selectNum: Int = 0
    override val binding: WindowFeedbackInformationViewBinding by inflate()


    private val watcher = object : TextWatcher {
        override fun afterTextChanged(editable: Editable) {
            if (editable.isNotEmpty()) {
                binding.tvFeedback.setBackgroundResource(R.drawable.shape_blue_bg)
            } else {
                binding.tvFeedback.setBackgroundResource(R.drawable.rect_solid_97daff_border_corner_24)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    }

    private val focus = object : View.OnFocusChangeListener {
        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            if (selectNum == 3) {
                editTextLength(binding.etCity.text.toString())
            } else if (selectNum == 4) {
                editTextLength(binding.etQuestion.text.toString())
            }
        }

    }

    /**
     * 当edit重新获取光标时，如果有内容tvFeedback按钮不置灰
     */
    private fun editTextLength(content: String) {
        if (!TextUtils.isEmpty(content))
            binding.tvFeedback.setBackgroundResource(R.drawable.shape_blue_bg)
        else
            binding.tvFeedback.setBackgroundResource(R.drawable.rect_solid_97daff_border_corner_24)
    }

    override fun initView() {

        binding.rel01.setSingleClickListener {
            selectItem(1)
        }
        binding.rel02.setSingleClickListener {
            selectItem(2)
        }
        binding.rel03.setSingleClickListener {
            selectItem(3)
        }

        binding.rel04.setSingleClickListener {
            selectItem(4)
        }

        binding.etQuestion.addTextChangedListener(watcher)
        binding.etQuestion.setOnFocusChangeListener(focus)

        binding.etCity.addTextChangedListener(watcher)
        binding.etCity.setOnFocusChangeListener(focus)

        binding.tvFeedback.setSingleClickListener {
            if (selectNum == 0) return@setSingleClickListener

            if (selectNum == 3 && TextUtils.isEmpty(binding.etCity.text) || (selectNum == 4 && TextUtils.isEmpty(
                    binding.etQuestion.text
                ))
            ) return@setSingleClickListener

            var reportType: String = "null"
            var feedbackCity: String = "null"
            when (selectNum) {
                1 -> {
                    reportType = "1"
                    feedbackCity = "null"
                }
                2 -> {
                    reportType = "2"
                    feedbackCity = "null"
                }
                3 -> {
                    reportType = "3"
                    feedbackCity = binding.etCity.text.toString()
                }
                4 -> {
                    reportType = "4"
                    feedbackCity = binding.etQuestion.text.toString()
                }

            }

            val location = LocationUtil.getLocation()
            val bean = LocationReportBean().apply {
                this.reportType = reportType
                this.feedbackCity = feedbackCity

                this.lng = location.lng
                this.lat = location.lat
                this.province = location.province
                this.city = location.city
                this.district = location.district
                this.address = location.address
                this.street = location.street
                this.country = location.country
                this.Livetemperature = getAppModel().currentWeather.value.nN().weather.nN().tc
                this.Liveweather = getAppModel().currentWeather.value.nN().weather.nN().wt
                if (getAppModel().currentWeather.value.nN().weather.nN().wtablesNew.nN()
                        .isNotEmpty()
                ) {
                    this.Todaytemperature =
                        getAppModel().currentWeather.value.nN().weather.nN().wtablesNew.nN()
                            .listIndex(0).tcr
                    this.Todayweather =
                        getAppModel().currentWeather.value.nN().weather.nN().wtablesNew.nN()
                            .listIndex(0).wt
                }
            }
            val json = Gson().toJson(bean)
            LogD(json)

            report(json)
            dismiss()
        }

        binding.tvCancel.setSingleClickListener {
            dismiss()
        }
    }

    private fun selectItem(item: Int) {
        clearSelet()
        selectNum = item
        when (item) {
            1 -> {
                binding.tvFeedback.setBackgroundResource(R.drawable.shape_blue_bg)
                binding.rel01.setBackgroundResource(R.drawable.rect_solid_blue_border_corner_8)
                binding.tv01.setTextColor(ContextCompat.getColor(context, R.color.color_379bff))
                binding.im01.visibility = View.VISIBLE
            }
            2 -> {
                binding.tvFeedback.setBackgroundResource(R.drawable.shape_blue_bg)
                binding.rel02.setBackgroundResource(R.drawable.rect_solid_blue_border_corner_8)
                binding.tv02.setTextColor(ContextCompat.getColor(context, R.color.color_379bff))
                binding.im02.visibility = View.VISIBLE
            }
            3 -> {
                binding.rel03.setBackgroundResource(R.drawable.rect_solid_blue_border_corner_8)
                binding.tv03.setTextColor(ContextCompat.getColor(context, R.color.color_379bff))
                binding.tv03.visibility = View.GONE
                binding.etCity.visibility = View.VISIBLE
                showInputTips(binding.etCity)
            }

            4 -> {
                binding.rel04.setBackgroundResource(R.drawable.rect_solid_blue_border_corner_8)
                binding.tv04.setTextColor(ContextCompat.getColor(context, R.color.color_379bff))
                binding.tv04.visibility = View.GONE
                binding.etQuestion.visibility = View.VISIBLE
                showInputTips(binding.etQuestion)
            }
        }
    }

    private fun clearSelet() {
        binding.tvFeedback.setBackgroundResource(R.drawable.rect_solid_97daff_border_corner_24)

        binding.rel01.setBackgroundResource(R.drawable.shape_f2f2f2_bg)
        binding.tv01.setTextColor(ContextCompat.getColor(context, R.color.color_333))
        binding.im01.visibility = View.INVISIBLE

        binding.rel02.setBackgroundResource(R.drawable.shape_f2f2f2_bg)
        binding.tv02.setTextColor(ContextCompat.getColor(context, R.color.color_333))
        binding.im02.visibility = View.INVISIBLE

        binding.rel03.setBackgroundResource(R.drawable.shape_f2f2f2_bg)
        binding.tv03.setTextColor(ContextCompat.getColor(context, R.color.color_333))
        binding.tv03.visibility = View.VISIBLE
        binding.etCity.visibility = View.GONE

        binding.rel04.setBackgroundResource(R.drawable.shape_f2f2f2_bg)
        binding.tv04.setTextColor(ContextCompat.getColor(context, R.color.color_333))
        binding.tv04.visibility = View.VISIBLE
        binding.etQuestion.visibility = View.GONE

        hideInputTips(binding.etCity)
        hideInputTips(binding.etQuestion)
    }

    private fun showInputTips(et_text: EditText) {
        et_text.isFocusable = true
        et_text.isFocusableInTouchMode = true
        et_text.requestFocus()
        val inputManager = et_text.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(et_text, 0)
    }

    private fun hideInputTips(et_text: EditText) {
        val imm = et_text.context.getSystemService(Context.INPUT_METHOD_SERVICE)
                as? InputMethodManager ?: return
        imm.hideSoftInputFromWindow(et_text.windowToken, 0)
    }


    override fun getDimAmount(): Float = 0.8f

    override fun show() {
        super.show()
//        XMLogAgent.onPageStart(EnumType.上报埋点.意见反馈)
    }

    override fun dismiss() {
        super.dismiss()
//        XMLogAgent.onPageEnd(EnumType.上报埋点.意见反馈)
    }
}