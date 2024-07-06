package com.maiya.adlibrary.ad.csj;

import android.content.Context;

import com.bykv.vk.openvk.TTVfConfig;
import com.bykv.vk.openvk.TTVfConstant;
import com.bykv.vk.openvk.TTVfManager;
import com.bykv.vk.openvk.TTVfSdk;

/**
 * 可以用一个单例来保存TTAdManager实例
 */
public class CSJAdManagerHolder {
    private static boolean sInit;

    public static TTVfManager getInstance() {
        return TTVfSdk.getVfManager();
    }

    public static void init(Context context, String appId, String appName) {
        doInit(context, appId, appName);
    }


    //step1:接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
    private static void doInit(Context context, String appId, String appName) {
        if (!sInit) {
            TTVfSdk.init(context, buildConfig(context, appId, appName));
            sInit = true;
        }
    }

    private static TTVfConfig buildConfig(Context context, String appId, String appName) {
        return new TTVfConfig.Builder()
                .appId(appId)
                .useTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                .appName(appName)
                .titleBarTheme(TTVfConstant.TITLE_BAR_THEME_LIGHT)
                .allowShowNotify(true) //是否允许sdk展示通知栏提示
                .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                .debug(false) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .directDownloadNetworkType() //允许直接下载的网络状态集合
                .supportMultiProcess(false)
                .build();
    }
}
