package com.zhangsheng.shunxin.information.InfoDetails;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * @Author:liupengbing
 * @Data: 2020/5/23 5:32 PM
 * @Email:aliupengbing@163.com
 */
public class InfoWebView extends WebView {
    public InfoWebView(Context context) {
        super(context);
        initDatas();
    }

    public InfoWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDatas();
    }

    public InfoWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDatas();

    }

    public interface OnOverScrollListener {
        void onOverScrolled(InfoWebView v, boolean onBottom);
    }
    private OnOverScrollListener mOnOverScrollListener;

    public void setOnOverScrollListener(OnOverScrollListener listener) {
        this.mOnOverScrollListener = listener;
    }
    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (mOnOverScrollListener != null) {
            // clampedY=true的前提下，scrollY=0时表示滑动到顶部，scrollY!=0时表示到底部
            mOnOverScrollListener.onOverScrolled(this, scrollY != 0 && clampedY);
        }
    }

    private void initDatas(){

        WebSettings webSettings = getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
//        设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
//        webSettings.setTextZoom(100);
//        支持屏幕缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);

        // 5.1以上默认禁止了https和http混用，以下方式是开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }

        //其他操作
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //不加载缓存内容
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
//        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setDomStorageEnabled(true);

        webSettings.setTextSize(WebSettings.TextSize.NORMAL);


        webSettings.setBlockNetworkImage(true);

    }



    public void onDestroy() {
        // 避免WebView的内存泄漏
        loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        clearHistory();
        ViewGroup webViewParent = (ViewGroup) getParent();
        webViewParent.removeView(this);
        destroy();
    }

    public void onTrimMemory(int level) {
        if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) {
            clearCache(false);
        }
    }

}
