package com.maiya.adlibrary.ad.ks;

import android.content.Context;

import com.kwad.sdk.api.KsAdSDK;
import com.kwad.sdk.api.SdkConfig;

/**
 * 广告sdk 初始化工具
 */
public final class KSAdSDKInitHelper {
    private static boolean initialized = false;

    public static void initSDK(Context appContext, String appName, String appId) {
        if (initialized) {
            return;
        }
        synchronized (KSAdSDKInitHelper.class) {
            if (initialized) {
                return;
            }
            initialized = true;
        }
        KsAdSDK.init(appContext, new SdkConfig.Builder()
                .appId(appId) // 测试aapId，请联系快手平台申请正式AppId，必填
                .appName(appName) // 测试appName，请填写您应用的名称，非必填
                .showNotification(true) // 是否展示下载通知栏
                .debug(false)
                .build());
    }
}
