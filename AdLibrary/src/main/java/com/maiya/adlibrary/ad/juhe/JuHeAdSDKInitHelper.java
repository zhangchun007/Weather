package com.maiya.adlibrary.ad.juhe;

import android.content.Context;

import com.voguetool.sdk.client.AdRequest;
import com.voguetool.sdk.client.SdkConfiguration;


public class JuHeAdSDKInitHelper {
    private static boolean initialized = false;

    public static void initSDK(Context appContext, String appName) {
        if (initialized) {
            return;
        }
        synchronized (JuHeAdSDKInitHelper.class) {
            if (initialized) {
                return;
            }
            initialized = true;
        }
        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder()
                .setAppName(appName)
                .build();
        AdRequest.init(appContext, sdkConfiguration);
    }
}