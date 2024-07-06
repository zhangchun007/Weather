  #完整报告
#-printconfiguration ~/tmp/full-r8-config.txt
-ignorewarnings
#指定压缩级别
-optimizationpasses 7
#-dontoptimize
-dontshrink
#不跳过非公共的库的类成员
-dontskipnonpubliclibraryclassmembers

#混淆时采用的算法
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-optimizations !code/simplification/*,!field/*,!class/merging/*,!method/removal/parameter,!method/propagation/*,!method/marking/static,!class/unboxing/enum,!code/removal/advanced,!code/allocation/variable

#把混淆类中的方法名也混淆了
-useuniqueclassmembernames

#优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification

#保留行号
-keepattributes SourceFile,LineNumberTable
#保持泛型
-keepattributes Signature
#保持所有实现 Serializable 接口的类成员
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclasseswithmembernames class * {
    native <methods>;
}


-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
#保留R类
-keep class **.R$* {
 *;
}

#保持自定义控件不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

#kotlin
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}
-keepclassmembernames class kotlinx.* {
    volatile <fields>;
}

-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}

-keep class kotlin.* { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

-dontwarn kotlinx.coroutines.flow.**
-dontwarn com.bytedance.article.common.nativecrash.NativeCrashInit


-keepattributes *Annotation*
-keepattributes *JavascriptInterface*
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
 }

-keep class * extends java.lang.annotation.Annotation {*;}


#Bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}


#---------------瞬推 推送-------------------------
-dontwarn com.my.sdk.**
-keep class com.my.sdk.** { *; }
-dontwarn com.my.sdk.stpush.**
-keep class com.my.sdk.stpush.** { *; }
-keep class com.adplus.sdk.AdPlusManager { *; }
#---------------华为 推送-------------------------
-ignorewarnings-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.huawei.hianalytics.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}
#---------------小米 推送-------------------------
-keep class com.xiaomi.** { *; }
-dontwarn com.xiaomi.**
-keep class org.apache.thrift.** { *; }
#---------------魅族 推送-------------------------
-keep class com.meizu.** { *; }
-dontwarn com.meizu.**#---------------oppo 1.5.0  sdk 1.0.7之前的版本-------------------------
-keep class com.coloros.mcssdk.** { *; }
-dontwarn com.coloros.mcssdk.**
#---------------oppo 2.1.0  sdk 1.0.7及后续版本-------------------------
-keep class com.heytap.msp.** { *;}
-dontwarn  com.heytap.msp.**
#---------------vivo 推送-------------------------
-keep class com.vivo.** { *; }
-dontwarn com.vivo.**


#-------xpopup----------
-dontwarn com.lxj.xpopup.widget.**
-keep class com.lxj.xpopup.widget.**{*;}

#xmlog
-keep class com.xm.xmcommon.** {*;}
-keep class com.xm.xmlog.** {*;}
-keep class com.xm.xmantiaddiction.** {*;}

# bean
-keep class com.zhangsheng.shunxin.weather.net.bean.**{*;}

-keep class com.maiya.baselibrary.net.bean.**{*;}

-keep class com.zhangsheng.shunxin.information.bean.**{*;}

-keep class com.maiya.thirdlibrary.net.bean.**{*;}

-keep class com.zhangsheng.shunxin.calendar.data.bean.**{*;}

#清理
-keep class com.preface.clean.**{*;}

# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#---------阿里云语音---------
#-keep class com.alibaba.idst.util.*{*;}
-keep class com.alibaba.idst.nui.*{*;}

#baidu map
-keep class com.baidu.** {*;}
-keep class mapsdkvi.com.** {*;}
-dontwarn com.baidu.**

#移动联盟
-keep class com.bun.miitmdid.core.** {*;}

# 数美
-keep class com.ishumei.** {*;}


#高德地图
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.**{*;}
-keep   class com.amap.api.trace.**{*;}
-keep class com.mapbox.mapboxsdk.**{ *; }

-keep class com.amap.api.mapcore.util.**{*;}

#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

#搜索
-keep   class com.amap.api.services.**{*;}


#本地
-keep class com.maiya.weather.data.bean.**{*;}

-keep class com.maiya.weather.net.**{*;}

-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-dontnote com.google.android.material.**
-dontwarn androidx.**

#广告
-keep class com.zhangsheng.shunxin.ad.**{*;}

#glide
-keep class com.bumptech.glide.** {*;}
-keep class com.bumptech.glide.Glide {*;}
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#retrofit2  混淆
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }


-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8
-keepattributes Exceptions

-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**

-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn okhttp3.internal.platform.ConscryptPlatform
-dontwarn org.codehaus.mojo.animal_sniffer.*

#穿山甲
-keep class com.bytedance.sdk.openadsdk.** { *; }
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
-keep class com.pgl.sys.ces.* {*;}

#广点通
-keep class com.qq.e.** {
    public protected *;
}
-keep class android.support.v7.**{
    public *;
}
-keep class com.tencent.smtt.** { *; }
-dontwarn dalvik.**
-dontwarn com.tencent.smtt.**

# 快手  -- start
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

# 快手  -- end

#swipe recycle
-dontwarn com.yanzhenjie.recyclerview.swipe.**
-keep class com.yanzhenjie.recyclerview.swipe.** {*;}


#eventbusstart
-keepattributes *Annotation*
-keepclassmembers class ** { @org.greenrobot.eventbus.Subscribe <methods>; }
-keep enum org.greenrobot.eventbus.ThreadMode{*;}
#OnlyrequiredifyouuseAsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent{
<init>(java.lang.Throwable);
}
#eventbusend
#广告中台start
-keep class com.xinmeng.xm.XMMarker{*;}
-keep class com.coke.Coke{*;}
#广告中台end

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




#体外
-keep class com.xyz.sdk.e.keeplive.daemon.component.DaemonInstrumentation{*;}
-keep class com.xyz.sdk.e.keeplive.daemon.NativeKeepAlive{*;}
-keepnames class com.xyz.sdk.e.keeplive.daemon.DaemonMain{
     public static void main(java.lang.String[]);
}

-keep class com.xyz.sdk.e.keeplive.daemon.component.DaemonInstrumentation{*;}
-keep class com.xyz.sdk.e.keeplive.daemon.NativeKeepAlive{*;}
-keep class com.xyz.sdk.e.keeplive.KeepLive{*;}
-keep class com.xyz.sdk.e.keeplive.daemon.DaemonMain{
  *;
}

#shareinstall
-keep class com.sh.sdk.shareinstall.** {*;}

#聚合
-keep class com.analytics.sdk.** {*;}
-keep class com.androidquery.** {*;}
-keep class com.androidquery.callback.** {*;}
-keep class com.alive.impl.ExportInstrumentation{*;}
-keep class com.alive.impl.NativeLeoric{*;}
-keep class com.bytedance.sdk.openadsdk.** { *; }
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}

#百度联盟
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

-keep class androidx.core.app.CoreComponentFactory { *; }


-keep class com.zhangsheng.shunxin.databinding.* {*;}

-keepclassmembers class * implements androidx.viewbinding.ViewBinding {
  public static ** inflate(...);
  public static ** bind(***);
}
#pag sdk
-keep class org.libpag.* {*;}

# 壁纸sdk混淆配置
-keep class com.maiya.wallpaper.http.bean.**{*;}
-keepclasseswithmembernames class * {    native <methods>;}
#okttp3混淆配置
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**
#glide
-keep class com.bumptech.glide.Glide {*;}
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {  **[] $VALUES;  public *;}

#保活
-keep class com.maiya.libdaemon.daemon.**{*;}
-keepclasseswithmembernames class * {    native <methods>;}
-keep class com.my.** {*;}

-keep class com.google.gson.**{*;}
-keep class com.maiya.sdk.httplibrary.http.** { *; }
-keep class com.zhangsheng.shunxin.weather.exception.CapturedException {*;}

-keep class com.voguetool.sdk.*.** {*;}
-keep class com.sunrisehelper.sdk.*.** {*;}