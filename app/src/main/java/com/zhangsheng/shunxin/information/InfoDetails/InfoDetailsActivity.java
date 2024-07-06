package com.zhangsheng.shunxin.information.InfoDetails;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.maiya.adlibrary.ad.WindowFocusRelativeLayout;
import com.maiya.adlibrary.ad.listener.ShowFeedListener;
import com.maiya.thirdlibrary.net.bean.None;
import com.maiya.thirdlibrary.net.callback.CallResult;
import com.maiya.thirdlibrary.utils.CacheUtil;
import com.maiya.thirdlibrary.utils.StatusBarUtil;
import com.maiya.weather.util.java_bridge.JNetUtils;
import com.xinmeng.shadow.mediation.source.IEmbeddedMaterial;
import com.xinmeng.shadow.mediation.source.LoadMaterialError;
import com.xm.xmlog.XMLogAgent;
import com.xm.xmlog.bean.XMActivityBean;
import com.zhangsheng.shunxin.R;
import com.zhangsheng.shunxin.ad.AdConstant;
import com.zhangsheng.shunxin.ad.widget.B2PictureAdMaterialView;
import com.zhangsheng.shunxin.information.adapter.InfoDetailsAdapter;
import com.zhangsheng.shunxin.information.bean.InfoCommendBean;
import com.zhangsheng.shunxin.information.bean.SignatureBean;
import com.zhangsheng.shunxin.information.constant.Constants;
import com.zhangsheng.shunxin.information.utils.ActivityUtil;
import com.zhangsheng.shunxin.information.utils.CommUtil;
import com.zhangsheng.shunxin.information.utils.DensityUtil;
import com.zhangsheng.shunxin.information.utils.DeviceUtil;
import com.zhangsheng.shunxin.information.utils.JrttPostBackUtils;
import com.zhangsheng.shunxin.information.utils.NetworkUtil;
import com.zhangsheng.shunxin.weather.app.App;
import com.zhangsheng.shunxin.weather.bottom.BottomBarItem;
import com.zhangsheng.shunxin.weather.common.EnumType;
import com.zhangsheng.shunxin.weather.ext.AppExtKt;
import com.zhangsheng.shunxin.weather.livedata.LiveDataBus;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public class InfoDetailsActivity extends AppCompatActivity {

    private InfoWebView mWebView;
    private InfoScrollView mScrollView;
    private FrameLayout mContainer;
    private LinearLayout mOthersLayout;
    private View mAdLine1;
    private View mAdLine2;
    private String article_url;
    private ImageView iv_back;
    private String item_id;
    private String group_id;
    private RecyclerView recycle_view;
    private LinearLayoutManager layoutManager;
    private InfoDetailsAdapter mInfoDetailsAdapter;
    private boolean shouldLoadAd = true;
    private LinearLayout comm_loading_rlyt;
    private LinearLayout ll_container;
    private boolean isVideo = false;
    private TextView tuijian_title;
    private ImageView ivClose;
    private LottieAnimationView lottieAnim;
    private LinearLayout ll_no_net;
    private TextView reload;
    /**
     * 视频全屏参数
     */
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS
            = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    private View customView;
    private FrameLayout fullscreenContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private static long startTime;
    private View view_line;
    private long pauseTime = 0L;
    private B2PictureAdMaterialView adv_infodetails_material_view;
    private WindowFocusRelativeLayout window;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);

        if (DeviceUtil.isMeizu()) {
            StatusBarUtil.setStatusBarFontIconDark(this, StatusBarUtil.TYPE_FLYME, true);
        } else {
            StatusBarUtil.setStatusBarFontIconDark(this, StatusBarUtil.TYPE_M, true);
        }
        setContentView(R.layout.activity_info_details);

        initView();

        ActivityUtil.getInstance().add(this);

        article_url = getIntent().getStringExtra("Article_url");
        Log.w("lpb", "article_url:" + article_url);
        item_id = getIntent().getStringExtra("item_id");
        group_id = getIntent().getStringExtra("group_id");

        isVideo = getIntent().getBooleanExtra("has_video", false);
        if (NetworkUtil.isNetworkActive(this)) {
            ll_no_net.setVisibility(View.GONE);
            mWebView.loadUrl(article_url);
            requestData();
        } else {
            comm_loading_rlyt.setVisibility(View.GONE);
            ll_no_net.setVisibility(View.VISIBLE);
        }
        //头条回传参数
        startTime = System.currentTimeMillis();
        Log.w("lpb", "startTime:" + startTime);

        if (BottomBarItem.CMD_WEATHER.equals(App.Companion.getAppModel().getAppPageIndex())) {
            AppExtKt.showReport(EnumType.上报埋点.INSTANCE.get首页信息流频道详情页展示(), "null", "null", XMActivityBean.TYPE_SHOW);
        } else {
            AppExtKt.showReport(EnumType.上报埋点.INSTANCE.get底部信息流tab详情页展示(), "null", "null", XMActivityBean.TYPE_SHOW);
        }
    }

    private void initView() {
        mScrollView = findViewById(R.id.info_scroll_view);
        mWebView = findViewById(R.id.info_web_view);
        mContainer = findViewById(R.id.info_container);
        ll_container = findViewById(R.id.ll_container);
        mOthersLayout = findViewById(R.id.info_others_layout);
        iv_back = findViewById(R.id.iv_back);
        recycle_view = findViewById(R.id.recycle_view);
        comm_loading_rlyt = findViewById(R.id.comm_loading_rlyt);
        tuijian_title = findViewById(R.id.tuijian_title);
        ivClose = findViewById(R.id.iv_clsoe);
        lottieAnim = findViewById(R.id.lottli_anim_loading);
        lottieAnim.playAnimation();

        view_line = findViewById(R.id.view_line);

        ll_no_net = findViewById(R.id.ll_no_net);
        reload = findViewById(R.id.reload);

        adv_infodetails_material_view = findViewById(R.id.adv_infodetails_material_view);
        window = findViewById(R.id.window_focus);

        mAdLine1 = findViewById(R.id.ad_line1);
        mAdLine2 = findViewById(R.id.ad_line2);

        initWebView();

        initRecycleView();
        initListener();

        int titleBar = DeviceUtil.getStatusBarHeight(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.height = titleBar;
        view_line.setLayoutParams(params);
    }

    private void initWebView() {
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                loadDismiss();
                mWebView.getSettings().setBlockNetworkImage(false);
                // 视频加载后自动播放
                mWebView.loadUrl("javascript:(function() { document.getElementsByTagName('video')[0].play(); })()");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    String command = "document.body.style.backgroundColor=\"#FFFFFF\"";
                    mWebView.evaluateJavascript(command, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {

                        }
                    });

                    //禁止点击图片
                    mWebView.evaluateJavascript("var objs = document.getElementsByTagName(\"img\");           \n" +
                            "            for(var i=0;i<objs.length;i++)\n" +
                            "            {\n" +
                            "                objs[i].setAttribute('style','pointer-events: none;');\n" +
                            "            \n" +
                            "            }", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {

                        }
                    });
                    //去掉展开全文
                    String objs = "var objs = document.getElementsByClassName(\"unfold-btn\")[0];   \n" +
                            "objs.click()";
                    mWebView.evaluateJavascript(objs, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {

                        }
                    });
                    //去掉下拉展示按钮
                    String objs1 = "var objs = document.getElementsByClassName(\"video-title\");for(var i=0;i<objs.length;i++){objs[i].setAttribute('style','pointer-events: none;background:none;');}";
                    mWebView.evaluateJavascript(objs1, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {

                        }
                    });
                }

            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                loadDismiss();
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                // 这个方法在6.0才出现
                int statusCode = errorResponse.getStatusCode();
                if (403 == statusCode) {
                    view.loadUrl("about:blank");// 避免出现默认的错误界面
//                    errorUI();
                    finish();
//                    view.loadUrl(errorUrl_404);
                }
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                if (newProgress == 100) {
                    Log.w("lpb", "loadAd---big--33333");
                    if (shouldLoadBigAd()) {
                        Log.w("lpb", "loadAd---big--1");
                        loadAd();
                        shouldLoadAd = false;
                    }
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //171016 处理404错误 android 6.0 以下通过title获取
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    if (title.contains("403")) {
                        view.loadUrl("about:blank");// 避免出现默认的错误界面
                        finish();
//                        view.loadUrl(errorUrl_404);
                    }
                }
            }


            /*** 视频播放相关的方法 **/
            @Override
            public View getVideoLoadingProgressView() {
                FrameLayout frameLayout = new FrameLayout(InfoDetailsActivity.this);
                frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                return frameLayout;
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
//                Log.i("lpb","onShowCustomView");
                showCustomView(view, callback);
            }

            @Override
            public void onHideCustomView() {
//                    Log.i("lpb","onHideCustomView");
                hideCustomView();
            }
        });
    }


    private void initRecycleView() {
        mInfoDetailsAdapter = new InfoDetailsAdapter(this);
        recycle_view.setNestedScrollingEnabled(false);
        recycle_view.setAdapter(mInfoDetailsAdapter);
        layoutManager = new LinearLayoutManager(this);
        recycle_view.setLayoutManager(layoutManager);
    }

    private void initListener() {
        window.setWindowFocusChangeListener(new WindowFocusRelativeLayout.WindowFocusChangeListener() {

            @Override
            public void onWindowFocusChanged(boolean hasWindowFocus) {
                if (hasWindowFocus) {
                    adv_infodetails_material_view.adResume();
                } else {
                    adv_infodetails_material_view.adPause();
                }
            }
        });

        // 监听网页是否滑到底
        mWebView.setOnOverScrollListener(new InfoWebView.OnOverScrollListener() {
            @Override
            public void onOverScrolled(InfoWebView v, boolean onBottom) {

                mScrollView.setIsWebViewOnBottom(onBottom);
            }
        });
        // 没有这段代码的话WebView会滑不动
        mWebView.post(() -> {
            if (mWebView != null) {
                // WebView设置固定高度，避免各种嵌套问题
                ViewGroup.LayoutParams lp = mWebView.getLayoutParams();
                // 注意这里的mContainer就是文章开头讲那个最外层父布局
                if (!isVideo) {
                    lp.height = ll_container.getHeight();
                } else {
                    mScrollView.isVideo(isVideo);
                    lp.height = DensityUtil.dip2px(getApplicationContext(), 325);
                }
                mWebView.setLayoutParams(lp);
            }
        });

        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                mScrollView.setIsOthersLayoutShow(isViewShowReally(mOthersLayout));
                if (shouldLoadBigAd()) {
                    loadAd();
                    shouldLoadAd = false;
                    CacheUtil.INSTANCE.put("InfoDetailsRecommend", System.currentTimeMillis());
                }
            }

        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click();

            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                report();
                ActivityUtil.getInstance().closeAll();
                infoDetilsStayTimePostBack();
            }
        });

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtil.isNetworkActive(InfoDetailsActivity.this)) {
                    ll_no_net.setVisibility(View.GONE);
                    mWebView.loadUrl(article_url);
                    requestData();
                } else {
                    comm_loading_rlyt.setVisibility(View.GONE);
                    ll_no_net.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void click() {
        report();
        infoDetilsStayTimePostBack();
        jrttPostBack(0);
        this.finish();
    }

    public static void jrttPostBack(int position) {
        Long duration = System.currentTimeMillis() - CacheUtil.INSTANCE.getLong("InfoDetailsRecommend", 0L);
        JrttPostBackUtils.getInstance().infoDetailsShowPostBack(duration, position);
    }

    private void report() {
//        ReportUtils.INSTANCE.uploadAppOnline(
//                ReportUtils.INSTANCE.getPageId(),
//                ReportUtils.INSTANCE.getPageUrl(),
//                System.currentTimeMillis() - ReportUtils.INSTANCE.getPageStartTime()
//        );
    }

    /**
     * 判断View是否部分或完全可见
     *
     * @return 一点可见即返回true，完全隐藏时才返回false
     */
    boolean isViewShowReally(View view) {
        try {
            if (view != null && view.getVisibility() == View.VISIBLE) {
                return view.getGlobalVisibleRect(new Rect());
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 请求推荐信息
     */
    private void requestData() {


        String access_token = CacheUtil.INSTANCE.getString(Constants.ACCESS_TOKEN, "");
        if (null == access_token || access_token.length() == 0) {
            //获取token
//            getTokenByOaid();
        } else {
            //用本地的token
            getComCommendInfo(access_token);
        }
    }

    private void getComCommendInfo(String access_token) {
        SignatureBean signBean = null;
        try {
            signBean = CommUtil.getSignature();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (signBean == null) {
            return;
        }
        String timestamp = signBean.timestamp;
        String nonce = signBean.nonce;
        String signature = signBean.signature;
        long count = 6;

        JNetUtils.INSTANCE.getRecommendInfo(group_id, item_id, timestamp, signature, nonce, Constants.partner,
                access_token, count,

                new CallResult<InfoCommendBean>() {
                    @Override
                    public void ok(@NotNull InfoCommendBean result) {
                        super.ok(result);
                        if (result == null) {
                            return;
                        }
                        if (result.getRet() == 0) {
                            List<InfoCommendBean.DataBean> newDataList = result.getData();
                            if (newDataList == null || newDataList.size() == 0) {
                                return;
                            }
                            tuijian_title.setVisibility(View.VISIBLE);
                            mInfoDetailsAdapter.addData(newDataList, true);
                        }
                    }

                    @Override
                    public void failed(int code, @NotNull String msg) {
                        super.failed(code, msg);
                        Log.w("lpb--->", msg);
                    }
                });
    }

    private void loadDismiss() {
        comm_loading_rlyt.setVisibility(View.GONE);
        lottieAnim.clearAnimation();
    }

    private void loadAd() {
        adv_infodetails_material_view.showFeedAd(this, AdConstant.INSTANCE.getSLOT_BIGXWXQ(), 4f, true, new ShowFeedListener() {
            @Override
            public boolean onLoad(@Nullable IEmbeddedMaterial p0) {
                mAdLine1.setVisibility(View.VISIBLE);
                return super.onLoad(p0);
            }

            @Override
            public void onError(@Nullable LoadMaterialError p0) {
                mAdLine1.setVisibility(View.GONE);
                super.onError(p0);
            }
        });
    }

    private boolean shouldLoadBigAd() {
        return shouldLoadAd && isAdViewVisible();
    }

    private boolean isAdViewVisible() {
        try {
            return mOthersLayout.getGlobalVisibleRect(new Rect());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.onDestroy();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        mWebView.onTrimMemory(level);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN
                && KeyEvent.KEYCODE_BACK == keyCode) {
            /** 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面 */
            if (customView != null) {
                hideCustomView();
            } else if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                click();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    /**
     * 视频播放全屏
     **/
    private void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }

        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(InfoDetailsActivity.this);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        customViewCallback = callback;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }

    /**
     * 隐藏视频全屏
     */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        mWebView.setVisibility(View.VISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtil.setStatusBarDarkTheme(this, false);
    }

    /**
     * 全屏容器界面
     */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }

    public static void infoDetilsStayTimePostBack() {
        Long stayTime = System.currentTimeMillis() - startTime;
        JrttPostBackUtils.getInstance().infoDetilsStayTimePostBack(stayTime);
    }

    @Override
    public void onPause() {
        super.onPause();
        XMLogAgent.onPageEnd(EnumType.上报埋点.INSTANCE.get新闻详情页());
        pauseTime = System.currentTimeMillis();
        Log.w("lpb", "infoDetail onPause---" + pauseTime);

    }

    @Override
    public void onResume() {
        super.onResume();
        XMLogAgent.onPageStart(EnumType.上报埋点.INSTANCE.get新闻详情页());
        if (pauseTime != 0) {
            Long leadTime = System.currentTimeMillis() - pauseTime;
            Log.w("lpb", "infoDetail onResume--leadTime--" + leadTime);
            if (leadTime >= 60 * 60 * 1000) {
                Log.w("lpb", "infoDetail leadTime >= 60 * 60 * 1000--" + leadTime);
                ActivityUtil.getInstance().closeAll();
                LiveDataBus.INSTANCE.getChannel("ScreenBean").postValue(new None());
            }
        }
    }
}
