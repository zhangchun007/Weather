package com.zhangsheng.shunxin.weather.activity

import android.content.Context
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import com.maiya.thirdlibrary.base.AacActivity
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.utils.CacheUtil
import com.necer.utils.CalendarUtil
import com.xm.xmlog.XMLogAgent
import com.zhangsheng.shunxin.databinding.ActivityWeatherVideoBinding
import com.zhangsheng.shunxin.ad.AdConstant
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.getAppControl
import com.zhangsheng.shunxin.weather.utils.DataUtil
import org.joda.time.LocalDate
import org.koin.android.ext.android.inject
import java.util.*

class WeatherVideoActivity : AacActivity<BaseViewModel>() {
    override val vm: BaseViewModel by inject()
    override val binding by inflate<ActivityWeatherVideoBinding>()

    private var customView: View? = null
    private var fullscreenContainer: FrameLayout? = null
    private var customViewCallback: WebChromeClient.CustomViewCallback? = null
    var strUrl: String = ""
    var isAutoPlay: Boolean = false
    /**
     * 视频全屏参数
     */

    companion object {
        val COVER_SCREEN_PARAMS = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

    }


    override fun initView(savedInstanceState: Bundle?) {
        binding.title.initTitle(getAppControl().cctv.listIndex(0).title.isStr("天气预报"))
        Try {
            strUrl = intent.getStringExtra("url").isStr("")
            binding.tvCctvTime.text = intent.getStringExtra("CCTV_TIME")?.let { DataUtil.getHHmm(it).toString() } + "发布"
        }

        strUrl = strUrl.replace("http:", "https:")

        initWebView()
        loadAd()
    }

    private fun loadAd() {
        binding.cctvAd.showLeftFeedAd(this,AdConstant.SLOT_BIGTQSPXF)
    }
    /**
     * 展示网页界面
     */
    fun initWebView() {
        val wvcc = WebChromeClient()
        val webSettings = binding.webview.settings
        webSettings.javaScriptEnabled = true
        webSettings.useWideViewPort = true // 关键点
        webSettings.loadWithOverviewMode = true
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE // 不加载缓存内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSettings.mediaPlaybackRequiresUserGesture = false
        }
        binding.webview.setWebChromeClient(wvcc)
        val wvc: WebViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                url: String
            ): Boolean {
//                webView.loadUrl(url);
//                webView.loadData(getHtml(url, isAutoPlay), "text/html", "UTF-8");
                loadUrl()
                return true
            }
        }
        binding.webview.webViewClient = wvc
        binding.webview.setWebChromeClient(object : WebChromeClient() {
            /*** 视频播放相关的方法  */
            override fun getVideoLoadingProgressView(): View? {
                val frameLayout = FrameLayout(this@WeatherVideoActivity)
                frameLayout.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                return frameLayout
            }

            override fun onShowCustomView(
                view: View,
                callback: CustomViewCallback
            ) {
                showCustomView(view, callback)
            }

            override fun onHideCustomView() {
                hideCustomView()
            }
        })
        // 加载Web地址
//        webView.loadUrl(url);
        val wifiOpen: Boolean =
            isWifiOpen(Objects.requireNonNull(this))
        val string: String = CacheUtil.getString(Constant.SP_CCTV_VIDEO_WIFI)
        if (wifiOpen || !TextUtils.isEmpty(string) && CalendarUtil.isToday(
                LocalDate.parse(
                    string
                )
            )
        ) {
            isAutoPlay = true
        } else if (netIsConnected(this)) {
            //弹出提示
            isAutoPlay = false
            showDialog()
        }
        loadUrl()
    }

    private fun loadUrl() {
        binding.webview.loadData(getHtml(strUrl, isAutoPlay).isStr(""), "text/html", "UTF-8")
    }

    /**
     * 视频播放全屏
     */
    private fun showCustomView(
        view: View,
        callback: WebChromeClient.CustomViewCallback
    ) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden()
            return
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        this@WeatherVideoActivity.window.decorView
        val decor = window.decorView as FrameLayout
        fullscreenContainer = FullscreenHolder(this@WeatherVideoActivity)
        fullscreenContainer?.addView(view, WeatherVideoActivity.COVER_SCREEN_PARAMS)
        decor.addView(fullscreenContainer, WeatherVideoActivity.COVER_SCREEN_PARAMS)
        customView = view
        setStatusBarVisibility(false)
        customViewCallback = callback
    }

    /**
     * 隐藏视频全屏
     */
    private fun hideCustomView() {
        if (customView == null) {
            return
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setStatusBarVisibility(true)
        val decor = window.decorView as FrameLayout
        decor.removeView(fullscreenContainer)
        fullscreenContainer = null
        customView = null
        customViewCallback?.onCustomViewHidden()
        binding.webview.visibility = View.VISIBLE
    }

    /**
     * 全屏容器界面
     */
    internal class FullscreenHolder(ctx: Context) : FrameLayout(ctx) {
        override fun onTouchEvent(evt: MotionEvent): Boolean {
            return true
        }

        init {
            setBackgroundColor(ctx.resources.getColor(android.R.color.black))
        }
    }

    private fun setStatusBarVisibility(visible: Boolean) {
        val flag = if (visible) 0 else WindowManager.LayoutParams.FLAG_FULLSCREEN
        window.setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                /** 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面  */
                when {
                    customView != null -> {
                        hideCustomView()
                    }
                    binding.webview.canGoBack() -> {
                        binding.webview.goBack()
                    }
                    else -> {
                        finish()
                    }
                }
                true
            }
            else -> super.onKeyUp(keyCode, event)
        }
    }

    fun getHtml(url: String, isAuto: Boolean): String? {
        return """<html><head><meta charset="utf-8">
<title></title>
<meta content="width=device-width,initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no" name="viewport" /></head><body style="margin: 0;"><video name="media" controls="controls" poster="poster" controlsList="nodownload" style="width: 100%;" ${if (isAuto) "autoplay=\"autoplay\"" else ""}><source src="$url" ></video></body></html>"""
    }


    var isShowedDialog = false

    private fun showDialog() {
        isShowedDialog = if (!isShowedDialog) {
            true
        } else {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed) {
            return
        } else if (isFinishing) {
            return
        }
        AlertDialog.Builder(this).apply {
            setMessage("当前非WIFI网络，视频加载会 消耗流量；是否继续？")
            setPositiveButton("继续") { dialog, which ->
                val string = LocalDate.now().toString()
                CacheUtil.put(Constant.SP_CCTV_VIDEO_WIFI, LocalDate.now().toString())
                isAutoPlay = true
                loadUrl()
            }
            setNegativeButton("取消"){dialog, which ->
                dialog.dismiss()
            }
        }.show()
    }

    fun isWifiOpen(context: Context): Boolean {
        var isWifiConnect = false
        val cm = context.applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // check the networkInfos numbers
        val networkInfos = cm.allNetworkInfo
        for (i in networkInfos.indices) {
            if (networkInfos[i].state == NetworkInfo.State.CONNECTED) {
                if (networkInfos[i].type == ConnectivityManager.TYPE_MOBILE) {
                    isWifiConnect = false
                }
                if (networkInfos[i].type == ConnectivityManager.TYPE_WIFI) {
                    isWifiConnect = true
                }
            }
        }
        return isWifiConnect
    }

    fun netIsConnected(context: Context): Boolean {
        val connectMgr = context.applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //手机网络连接状态
        val mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        //WIFI连接状态
        val wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (mobNetInfo==null||wifiNetInfo==null) return true
        return (mobNetInfo.isConnected && wifiNetInfo.isConnected)
    }


    override fun onPause() {
        super.onPause()
        XMLogAgent.onPageEnd(EnumType.上报埋点.CCTV视频详情页)
    }

    override fun onResume() {
        super.onResume()
        XMLogAgent.onPageStart(EnumType.上报埋点.CCTV视频详情页)
    }
}