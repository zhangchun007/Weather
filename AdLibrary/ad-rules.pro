

#广点通
-keep class com.qq.e.** {
    public protected *;
}
-keep class android.support.v4.**{
    public *;
}
-keep class android.support.v7.**{
    public *;
}

#穿山甲
-keep class com.bytedance.sdk.openadsdk.** {*;}
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
-keep class com.pgl.sys.ces.* {*;}


#快手

-keep class org.chromium.** {*;}
-keep class org.chromium.** { *; }
-keep class aegon.chrome.** { *; }
-keep class com.kwai.**{ *; }
-keep class com.kwad.**{ *; }
-keepclasseswithmembernames class * {
    native <methods>;
}
-dontwarn com.kwai.**
-dontwarn com.kwad.**
-dontwarn com.ksad.**
-dontwarn aegon.chrome.**

-keep class com.ksad.download.** { *;}

#百度
-dontwarn com.baidu.mobads.sdk.api.**
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-ignorewarnings
-keep class com.baidu.mobads.** { *; }

#聚合

-keep class com.analytics.sdk.** {*;}
-keep class com.androidquery.** {*;}
-keep class com.androidquery.callback.** {*;}

#中台广告

-keep class com.coke.Coke{*;}

-keep class com.xinmeng.xm.optimize.IAdUtils{*;}
-keep class com.xinmeng.shadow.mediation.source.Material{*;}
-keep class com.xinmeng.xm.optimize.OptimizeStrategy{*;}
-keep class kotlin.**{*;}
#体外
-keep class com.xinmeng.xm.XMMarker{*;}

#保活
-keep class com.xyz.sdk.e.keeplive.daemon.component.DaemonInstrumentation{*;}
-keep class com.xyz.sdk.e.keeplive.daemon.NativeKeepAlive{*;}
-keep class com.xyz.sdk.e.keeplive.KeepLive{*;}
-keep class com.xyz.sdk.e.keeplive.daemon.DaemonMain{
  *;
}


#eventbusstart
-keepattributes *Annotation*
-keepclassmembers class ** { @org.greenrobot.eventbus.Subscribe <methods>; }
-keep enum org.greenrobot.eventbus.ThreadMode{*;}
#OnlyrequiredifyouuseAsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent{
<init>(java.lang.Throwable);
}
#eventbusend

#ijkplayer  -- start
-keep class tv.danmaku.ijk.media.player.** {*; }
-keepclasseswithmembernames class tv.danmaku.ijk.media.player.IjkMediaPlayer{
public <fields>;
public <methods>;
}
-keepclasseswithmembernames class tv.danmaku.ijk.media.player.ffmpeg.FFmpegApi{
public <fields>;
public <methods>;
}
#ijkplayer  -- end

#腾讯X5内核start
-dontwarn dalvik.**
-dontwarn com.tencent.smtt.**

-keep class com.tencent.smtt.** {
    *;
}

-keep class com.tencent.tbs.** {
    *;
}
#腾讯X5内核end
