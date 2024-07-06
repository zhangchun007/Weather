package com.maiya.adlibrary.ad.bd;

import android.content.Context;

import com.baidu.mobads.sdk.api.BDAdConfig;
import com.baidu.mobads.sdk.api.MobadsPermissionSettings;


public final class BDAdSdkInitHelper {

    public static void init(Context app, String appId) {
        BDAdConfig bdAdConfig = new BDAdConfig.Builder()
                // 2、应用在mssp平台申请到的appsid，和包名一一对应，此处设置等同于在AndroidManifest.xml里面设置
                .setAppsid(appId)
                .build(app);
        bdAdConfig.init();
        // 设置SDK可以使用的权限，包含：设备信息、定位、存储、APP LIST
        // 注意：建议授权SDK读取设备信息，SDK会在应用获得系统权限后自行获取IMEI等设备信息
        // 授权SDK获取设备信息会有助于提升ECPM
        MobadsPermissionSettings.setPermissionReadDeviceID(true);
        MobadsPermissionSettings.setPermissionLocation(true);
        MobadsPermissionSettings.setPermissionStorage(true);
        MobadsPermissionSettings.setPermissionAppList(true);
    }
}
