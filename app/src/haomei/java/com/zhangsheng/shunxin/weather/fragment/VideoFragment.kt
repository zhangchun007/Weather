package com.zhangsheng.shunxin.weather.fragment

import com.bytedance.sdk.dp.DPSdk
import com.bytedance.sdk.dp.DPWidgetDrawParams
import com.bytedance.sdk.dp.IDPDrawListener
import com.bytedance.sdk.dp.IDPWidget
import com.maiya.thirdlibrary.base.BaseFragment
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.bottom.BottomBarItem
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.utils.DataLogUtils
import java.lang.Exception

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2020/8/28 15:50
 */
class VideoFragment : BaseFragment(R.layout.fragment_video) {
    private var mIDPWidget: IDPWidget? = null

    override fun initView() {
        try {
            initDrawWidget()
            mIDPWidget?.let {
                var transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.draw_style1_frame, it.fragment)
                transaction?.commitAllowingStateLoss()

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        getAppModel().appPageIndex = BottomBarItem.CMD_WEATHER
        mIDPWidget?.destroy()
        mIDPWidget = null
    }

    private fun initDrawWidget() {
        if (mIDPWidget == null) {
            mIDPWidget = DPSdk.factory().createDraw(
                DPWidgetDrawParams.obtain()
                    .adCodeId("945628184")
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
        if (getAppModel().appPageIndex == BottomBarItem.CMD_VIDEO) {
            DataLogUtils.showOrStop("video", true)
        }
    }

    override fun onPause() {
        super.onPause()
        if (mIDPWidget?.fragment != null) {
            mIDPWidget!!.fragment.onPause()
        }
        if (getAppModel().appPageIndex == BottomBarItem.CMD_VIDEO) {
            DataLogUtils.showOrStop("video", false)
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
        DataLogUtils.showOrStop("video", !hidden)
    }

}