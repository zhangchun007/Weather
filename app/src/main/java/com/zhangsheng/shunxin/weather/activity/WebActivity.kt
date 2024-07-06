package com.zhangsheng.shunxin.weather.activity

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import androidx.viewbinding.ViewBinding
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.maiya.thirdlibrary.base.AacActivity
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.*
import com.xm.xmlog.XMLogAgent
import com.zhangsheng.shunxin.databinding.ActivityWebBinding
import org.koin.android.ext.android.inject


class WebActivity : AacActivity<BaseViewModel>() {
    override val vm: BaseViewModel by inject()
    override val binding by inflate<ActivityWebBinding>()

    var url = ""
    private var postData = ""
    var title = ""
    var postUrl=""


    override fun initView(savedInstanceState: Bundle?) {
        Try { url = intent.getStringExtra("url").isStr("") }

        Try { title = intent.getStringExtra("title").isStr("") }

        Try { postUrl = intent.getStringExtra("postUrl").isStr("") }

        binding.title.initTitle(title)
        initWebView()
        if (postData.isNotEmpty()) {
            binding.webView.postUrl(url, postData.toByteArray())
        } else {
            binding.webView.loadUrl(url)
        }
    }

    override fun initObserve() {

    }


    private fun initWebView() {
        var setting =binding. webView.settings
        setting.javaScriptEnabled = true
        setting.defaultTextEncodingName = "UTF-8"
        setting.javaScriptCanOpenWindowsAutomatically = true
        setting.useWideViewPort = true//关键点
        setting.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        setting.displayZoomControls = false
        setting.allowFileAccess = true // 允许访问文件
        setting.builtInZoomControls = true // 设置显示缩放按钮
        setting.setSupportZoom(true) // 支持缩放
        setting.textZoom = 100

        //缓存相关
        val appCacheDir = this.applicationContext.getDir("cache", Context.MODE_PRIVATE).path
        setting.setAppCachePath(appCacheDir)
        setting.cacheMode = WebSettings.LOAD_NO_CACHE  //设置 缓存模式
        // 开启 DOM storage API 功能
        setting.domStorageEnabled = true
        //开启 database storage API 功能
        setting.databaseEnabled = true
        //开启 Application Caches 功能
        setting.setAppCacheEnabled(false)
        Try {
            binding.webView.overScrollMode = View.OVER_SCROLL_NEVER
        }
        binding. webView.removeJavascriptInterface("accessibility")
        binding.webView.removeJavascriptInterface("accessibilityTraversal")
        binding.webView.removeJavascriptInterface("searchBoxJavaBridge_")
        binding.webView.addJavascriptInterface(JsBridge(this), "xyWeather")
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                url: String?
            ): Boolean {
                // 重写此方法表明点击网页里面的链接不调用系统浏览器，而是在本WebView中显示
                val tag = "tel:"
                if (url?.startsWith(tag).nN(false)) {
                    val mobile = url?.substring(url.lastIndexOf("/") + 1)
                    val uri = Uri.parse("tel:$mobile")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                    //这个超连接,java已经处理了，webview不要处理了
                    return true
                }

                if (url?.startsWith("weixin://wap/pay?").nN(false)) {
                    if (!isWeixinAvilible(this@WebActivity)
                    ) {
                        xToast("检查到您手机没有安装微信，请安装后使用该功能")
                        return true
                    }
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                    return true
                }

                if (url?.startsWith("weixin://").nN(false)) {
                    getWechatApi()
                    return true
                }

                if (url?.startsWith("alipays:").nN(false) || url?.startsWith("alipay").nN(false)) {
                    try {
                        startActivity(Intent("android.intent.action.VIEW", Uri.parse(url)))
                    } catch (e: Exception) {
                        xToast("未检测到支付宝客户端，请安装后重试。")
                    }
                    return true
                }

                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                if (null != handler) {
                    handler.proceed()
                } else {
                    super.onReceivedSslError(view, handler, error)
                }
            }
        }

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (newProgress >= 100) {
                    binding.progress.isVisible(false)

                } else {
                    binding.progress.isVisible(true)
                    binding. progress.progress = newProgress
                }
                super.onProgressChanged(view, newProgress)
            }
        }

        binding.webView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event!!.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK && binding.webView.canGoBack()) {  //表示按返回键
                    binding.webView.goBack()   //后退
                    return@OnKeyListener true
                }
            }
            false
        })
    }

    override fun onPause() {
        super.onPause()
        if (postUrl.isNotEmpty()){
//            XMLogAgent.onPageEnd(postUrl)
        }
    }

    override fun onResume() {
        super.onResume()
        if (postUrl.isNotEmpty()){
//            XMLogAgent.onPageStart(postUrl)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        clear()
    }

    fun clear() {
        binding.webView.webChromeClient = null
        binding.webView.removeAllViews()
        binding.webView.destroy()
    }

    inner class JsBridge(mcontext: WebActivity) {
        private var context = mcontext

        @JavascriptInterface
        fun backApp() {
            finish()
        }

        @JavascriptInterface
        fun callApp(json: String) {
            runOnUi {
                LogE("callApp->body:$json ")
                var obj = JsonParser().parse(json) as JsonObject
                when (obj.get("action").asString) {
                  "goSetting"->{
                      val packageURI = Uri.parse("package:${context.packageName}")
                      val intent =
                          Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
                      context.startActivity(intent)
                  }
                }
            }
        }
    }


    fun isWeixinAvilible(context: Context): Boolean {
        val packageManager = context.packageManager // 获取packagemanager
        val pinfo =
            packageManager.getInstalledPackages(0) // 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (i in pinfo.indices) {
                val pn = pinfo.listIndex(i).packageName
                if ("com.tencent.mm" == pn) {
                    return true
                }
            }
        }
        return false
    }


    private fun getWechatApi() {
        if (!isWeixinAvilible(this)
        ) {
            xToast("检查到您手机没有安装微信，请安装后使用该功能")
            return
        }
        try {
            val intent = Intent(Intent.ACTION_MAIN)
            val cmp = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.component = cmp
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            xToast("检查到您手机没有安装微信，请安装后使用该功能")
        }
    }
}
