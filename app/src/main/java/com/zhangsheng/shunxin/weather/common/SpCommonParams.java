package com.zhangsheng.shunxin.weather.common;

import com.my.sp.http.ISpCommonParams;
import com.zhangsheng.shunxin.weather.ext.AppExtKt;

public class SpCommonParams implements ISpCommonParams {
    // 项目的polling接口
    @Override
    public String getConfigUrl() {
        return (Configure.Debug? UrlConstant.DEBUG_APP_CONTROL : UrlConstant.APP_CONTROL) + "/app-control/polling.config";
    }

    @Override
    public boolean isAppControl() {
        return AppExtKt.isControl();
    }

    // 项目app typeid
//    @Override
//    public String getAppTypeId() {
//        return "100034";
//    }
//    // appQid
//    @Override
//    public String getAppQid() {
//        return "xytqgf1";
//    }
//    // CleanAppQid
//    @Override
//    public String getCleanAppQid() {
//        return "yybxytq1";
//    }
//
//    @Override
//    public String getAAID() {
//        return null;
//    }
//
//    @Override
//    public String getOAID() {
//        return null;
//    }
//
//    @Override
//    public String getAccid() {
//        return null;
//    }
//
//    @Override
//    public String getMuid() {
//        return null;
//    }
//
//    @Override
//    public String getLongitude() {
//        return null;
//    }
//
//    @Override
//    public String getLatitude() {
//        return null;
//    }
//
//    @Override
//    public String getProvince() {
//        return null;
//    }
//
//    @Override
//    public String getCity() {
//        return null;
//    }
//
//    @Override
//    public String getDistrict() {
//        return null;
//    }
//
//    @Override
//    public String getOpenBatchId() {
//        return null;
//    }
//
//    @Override
//    public String getSrcqid() {
//        return null;
//    }
//
//    @Override
//    public String getSrcplat() {
//        return null;
//    }
}
