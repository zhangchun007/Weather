package com.zhangsheng.shunxin.weather.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bytedance.sdk.dp.DPSdk
import com.bytedance.sdk.dp.DPWidgetDrawParams
import com.bytedance.sdk.dp.IDPDrawListener
import com.bytedance.sdk.dp.IDPWidget
import com.maiya.thirdlibrary.base.AacFragment
import com.maiya.thirdlibrary.base.BaseFragment
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.bindView
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.databinding.FragmentVideoBinding
import com.zhangsheng.shunxin.weather.MainActivity
import com.zhangsheng.shunxin.weather.bottom.BottomBarItem
import com.zhangsheng.shunxin.weather.ext.getAppModel
import org.koin.android.ext.android.inject
import java.lang.Exception

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2020/8/28 15:50
 */
class VideoFragment : AacFragment<BaseViewModel,FragmentVideoBinding>(R.layout.fragment_video) {
    override val vm: BaseViewModel by inject()
    private var mIDPWidget: IDPWidget? = null
    override fun initView() {
        try {
            initDrawWidget()
            mIDPWidget?.let {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.draw_style1_frame, it.fragment)
                    ?.commitAllowingStateLoss()

            }
        }catch (e:Exception){
//            (activity as MainActivity).chooseTab(0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        getAppModel().appPageIndex = BottomBarItem.CMD_WEATHER
        mIDPWidget?.destroy()
        mIDPWidget = null
        clearBinding()
    }


    private fun initDrawWidget() {
        if (mIDPWidget == null) {
            mIDPWidget = DPSdk.factory().createDraw(
                DPWidgetDrawParams.obtain()
                    .adCodeId("945435986")
                    .adOffset(0)
                    .progressBarStyle(DPWidgetDrawParams.PROGRESS_BAR_STYLE_DARK)
                    .hideClose(true, null)
                    .listener(object : IDPDrawListener() {
                        override fun onDPRefreshFinish() {
                        }

                        override fun onDPPageChange(position: Int) {
                        }

                        override fun onDPVideoPlay(map: Map<String, Any>) {
                        }

                        override fun onDPVideoOver(map: Map<String, Any>) {
                        }

                        override fun onDPVideoCompletion(map: Map<String, Any>) {
                        }

                        override fun onDPClose() {
                        }

                        override fun onDPReportResult(isSucceed: Boolean) {
                        }
                    })
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (mIDPWidget?.fragment != null) {
            mIDPWidget!!.fragment.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mIDPWidget?.fragment != null) {
            mIDPWidget!!.fragment.onPause()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (mIDPWidget?.fragment != null) {
            mIDPWidget!!.fragment.userVisibleHint = isVisibleToUser
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (mIDPWidget?.fragment != null) {
            mIDPWidget!!.fragment.onHiddenChanged(hidden)
        }

    }

    override fun injectBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentVideoBinding =FragmentVideoBinding.inflate(inflater,viewGroup,false)


}