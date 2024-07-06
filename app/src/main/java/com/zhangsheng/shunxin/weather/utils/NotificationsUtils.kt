package com.zhangsheng.shunxin.weather.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.*
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.core.app.NotificationManagerCompat

import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.utils.AppContext
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.MainActivity


/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2020/2/28 14:51
 */
object NotificationsUtils {

    private val RequestCode = 1
    private val WIDGET_MESSAGE = "widget_message"
    private val NEW_MESSAGE = "chat"
    private val Ticker = AppContext.getContext().resources.getString(R.string.app_name)
    private val CHECK_OP_NO_THROW = "checkOpNoThrow"
    private val OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION"
    private var notifyId = 0
    private var CHANNEL = ""

    private var NOMAL_PUSH_CHANNEL_ID = "chat"
    private var NOMAL_PUSH_CHANNEL_NAME = "天气提醒"

    var WIDGET_NOTIFY_CHANNEL_ID = "widget_message"
    var WIDGET_NOTIFY_CHANNEL_NAME = "天气常驻提醒"

    fun setNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(
                context,
                NOMAL_PUSH_CHANNEL_ID,
                NOMAL_PUSH_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )

            var manager = context.getSystemService(NOTIFICATION_SERVICE) as (NotificationManager)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                var importance = NotificationManager.IMPORTANCE_LOW
                var notificationChannel = NotificationChannel(
                    WIDGET_NOTIFY_CHANNEL_ID,
                    WIDGET_NOTIFY_CHANNEL_NAME,
                    importance
                )
                notificationChannel.description = ""
                notificationChannel.enableLights(false)
                notificationChannel.setSound(null, null)
                notificationChannel.enableVibration(false)
                manager?.createNotificationChannel(notificationChannel)
            }
        }
    }

    /**
     * 创建配置通知渠道
     * @param channelId   渠道id
     * @param channelName 渠道nanme
     * @param importance  优先级
     */
    @TargetApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(
        context: Context,
        channelId: String,
        channelName: String,
        importance: Int
    ) {
        var channel = NotificationChannel(channelId, channelName, importance)
        channel.setShowBadge(false)//禁止该渠道使用角标
        // 配置通知渠道的属性
//        channel .setDescription("渠道的描述")
        // 设置通知出现时的闪灯（如果 android 设备支持的话）
        channel.enableLights(true)
        // 设置通知出现时的震动（如果 android 设备支持的话）
        channel.enableVibration(false)
        //如上设置使手机：静止1秒，震动2秒，静止1秒，震动3秒
        channel.vibrationPattern = longArrayOf(1000, 2000, 1000, 3000)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC//设置锁屏是否显示通知
        channel.lightColor = Color.BLUE
        channel.setBypassDnd(true)//设置是否可以绕过请勿打扰模式
        var notificationManager = context.getSystemService(
            NOTIFICATION_SERVICE
        ) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    /**
     * 创建渠道组(若通知渠道比较多的情况下可以划分渠道组)
     * @param groupId
     * @param groupName
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    fun createNotifycationGroup(context: Context, groupId: String, groupName: String) {
        var group = NotificationChannelGroup(groupId, groupName)
        var notificationManager = context.getSystemService(
            NOTIFICATION_SERVICE
        ) as (NotificationManager)
        notificationManager.createNotificationChannelGroup(group)
    }

    /**
     * TODO: 发送通知（刷新前面的通知）
     *
     * @param context
     * @param contentTitle 标题
     * @param contentText  内容
     */
    fun show(context: Context, contentTitle: String, contentText: String, clazz: Class<*>?) {
        show(context, contentTitle, contentText, null, 0, NEW_MESSAGE, clazz)
    }

    /**
     * TODO: 发送自定义通知（刷新前面的通知）
     *
     * @param context
     * @param contentTitle 标题
     * @param contentText  内容
     */
    fun show(
        context: Context,
        contentTitle: String,
        contentText: String,
        views: RemoteViews,
        clazz: Class<*>?
    ) {
        show(context, contentTitle, contentText, views, 0, NEW_MESSAGE, clazz)
    }

    /**
     * 发送通知（刷新前面的通知，指定通知渠道）
     *
     * @param contentTitle 标题
     * @param contentText  内容
     * @param channelId    渠道id
     */
    fun show(
        context: Context,
        contentTitle: String,
        contentText: String,
        channelId: String,
        clazz: Class<*>?
    ) {
        show(context, contentTitle, contentText, null, 0, channelId, clazz)
    }

    /**
     * 发送自定义通知（刷新前面的通知，指定通知渠道）
     *
     * @param contentTitle 标题
     * @param contentText  内容
     * @param channelId    渠道id
     */
    fun show(
        context: Context,
        contentTitle: String,
        contentText: String,
        views: RemoteViews,
        channelId: String,
        clazz: Class<*>?
    ) {
        show(context, contentTitle, contentText, views, 0, channelId, clazz)
    }

    /**
     * 发送多条通知（默认通知渠道）
     *
     * @param contentTitle 标题
     * @param contentText  内容
     */
    fun showMuch(context: Context, contentTitle: String, contentText: String, clazz: Class<*>?) {
        show(context, contentTitle, contentText, null, ++notifyId, NEW_MESSAGE, clazz)
    }

    /**
     * 发送多条自定义通知（默认通知渠道）
     *
     * @param contentTitle 标题
     * @param contentText  内容
     */
    fun showMuch(
        context: Context,
        contentTitle: String,
        contentText: String,
        views: RemoteViews,
        clazz: Class<*>?
    ) {
        show(context, contentTitle, contentText, views, ++notifyId, NEW_MESSAGE, clazz)
    }

    /**
     * 发送多条通知（指定通知渠道）
     *
     * @param contentTitle 标题
     * @param contentText  内容
     * @param channelId    渠道id
     */
    fun showMuch(
        context: Context,
        contentTitle: String,
        contentText: String,
        channelId: String,
        clazz: Class<*>?
    ) {
        show(context, contentTitle, contentText, null, ++notifyId, channelId, clazz)
    }


    /**
     * 发送多条自定义通知（指定通知渠道）
     *
     * @param contentTitle 标题
     * @param contentText  内容
     * @param channelId    渠道id
     */
    fun showMuch(
        context: Context,
        contentTitle: String,
        contentText: String,
        channelId: String,
        views: RemoteViews,
        clazz: Class<*>?
    ) {
        show(context, contentTitle, contentText, views, ++notifyId, channelId, clazz)
    }

    /**
     * 发送通知（设置默认：大图标/小图标/小标题/副标题/优先级/首次弹出文本）
     *
     * @param contentTitle 标题
     * @param contentText  内容
     * @param notifyId     通知栏id
     * @param channelId    设置渠道id
     * @param cls          意图类
     */
    fun show(
        context: Context,
        contentTitle: String,
        contentText: String,
        views: RemoteViews?,
        notifyId: Int,
        channelId: String,
        clazz: Class<*>?
    ) {
        show(
            context,
            0,
            0,
            contentTitle,
            null,
            contentText,
            PRIORITY_MAX,
            null,
            views,
            notifyId,
            channelId,
            clazz
        )
    }

    /**
     * 发送通知
     *
     * @param largeIcon    大图标
     * @param smallIcon    小图标
     * @param contentTitle 标题
     * @param subText      小标题/副标题
     * @param contentText  内容
     * @param priority     优先级
     * @param ticker       通知首次弹出时，状态栏上显示的文本
     * @param notifyId     定义是否显示多条通知栏
     * @param cls          意图类
     */
    fun show(
        context: Context,
        largeIcon: Int,
        smallIcon: Int,
        contentTitle: String,
        subText: String?,
        contentText: String,
        priority: Int,
        ticker: String?,
        view: RemoteViews?,
        notifyId: Int,
        channelId: String,
        clazz: Class<*>?
    ) {
        //flags
        // FLAG_ONE_SHOT:表示此PendingIntent只能使用一次的标志
        // FLAG_IMMUTABLE:指示创建的PendingIntent应该是不可变的标志
        // FLAG_NO_CREATE : 指示如果描述的PendingIntent尚不存在，则只返回null而不是创建它。
        // FLAG_CANCEL_CURRENT :指示如果所描述的PendingIntent已存在，则应在生成新的PendingIntent,取消之前PendingIntent
        // FLAG_UPDATE_CURRENT : 指示如果所描述的PendingIntent已存在，则保留它，但将其额外数据替换为此新Intent中的内容
        var pendingIntent: PendingIntent? = null
        //添加隐示意图
        if (clazz != null) {
            var intent = Intent(context, clazz)
            pendingIntent =
                PendingIntent.getActivity(context, RequestCode, intent, FLAG_UPDATE_CURRENT)
        }
        //获取通知服务管理器
        var manager = context.getSystemService(NOTIFICATION_SERVICE) as (NotificationManager)
        //判断应用通知是否打开
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if (!openNotificationChannel(context, manager, channelId)) return;
//        }

        //创建 NEW_MESSAGE 渠道通知栏  在API级别26.1.0中推荐使用此构造函数 Builder(context, 渠道名)
        var builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationCompat.Builder(context, channelId)
            } else NotificationCompat.Builder(context)


        builder.setLargeIcon(
            BitmapFactory.decodeResource(
                context.resources, if (largeIcon == 0) R.mipmap.st_push else largeIcon
            )
        ) //设置自动收报机和通知中显示的大图标。
            .setSmallIcon(R.mipmap.st_push_small) // 小图标
            .setContentText(contentText) // 内容
            .setContentTitle(contentTitle) // 标题
            .setSubText(if (subText.nN().isEmpty()) null else subText) // APP名称的副标题
            .setPriority(PRIORITY_MAX) //设置优先级 PRIORITY_DEFAULT
            .setTicker(if (Ticker.isEmpty()) null else Ticker) // 设置通知首次弹出时，状态栏上显示的文本
            .setContent(view)
            .setWhen(System.currentTimeMillis()) // 设置通知发送的时间戳
            .setShowWhen(true)//设置是否显示时间戳
            .setAutoCancel(true)// 点击通知后通知在通知栏上消失
            .setDefaults(Notification.PRIORITY_HIGH)// 设置默认的提示音、振动方式、灯光等 使用的默认通知选项
            .setContentIntent(pendingIntent) // 设置通知的点击事件
        //锁屏状态下显示通知图标及标题 1、VISIBILITY_PUBLIC 在所有锁定屏幕上完整显示此通知/2、VISIBILITY_PRIVATE 隐藏安全锁屏上的敏感或私人信息/3、VISIBILITY_SECRET 不显示任何部分
//            .setVisibility(Notification.VISIBILITY_PUBLIC)//部分手机没效果
//            .setFullScreenIntent(pendingIntent, true)//悬挂式通知8.0需手动打开
//                .setColorized(true)
//                .setGroupSummary(true)//将此通知设置为一组通知的组摘要
//                .setGroup(NEW_GROUP)//使用组密钥
//                .setDeleteIntent(pendingIntent)//当用户直接从通知面板清除通知时 发送意图
//                .setFullScreenIntent(pendingIntent,true)
//                .setContentInfo("大文本")//在通知的右侧设置大文本。
//                .setContent(RemoteViews RemoteView)//设置自定义通知栏
//                .setColor(Color.parseColor("#ff0000"))
//                .setLights()//希望设备上的LED闪烁的argb值以及速率
//                .setTimeoutAfter(3000)//指定取消此通知的时间（如果尚未取消）。
        // 通知栏id
        manager.notify(notifyId, builder.build()); // build()方法需要的最低API为16 ,
    }


    /**
     * 判断应用渠道通知是否打开（适配8.0）
     * @return true 打开
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun openNotificationChannel(
        context: Context,
        manager: NotificationManager,
        channelId: String
    ): Boolean {
        //判断通知是否有打开
        if (!isNotificationPermissionOpen(context)) {
            toNotifySetting(context, null)
            return false
        }
        //判断渠道通知是否打开
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var channel = manager.getNotificationChannel(channelId)
            if (channel.importance == NotificationManager.IMPORTANCE_NONE) {
                //没打开调往设置界面
                toNotifySetting(context, channel.id)
                return false
            }
        }
        return true
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun openNotificationChannel(context: Context, channelId: String = NEW_MESSAGE): Boolean {
        //判断通知是否有打开
        var manager = context.getSystemService(NOTIFICATION_SERVICE) as (NotificationManager)
        if (!isNotificationPermissionOpen(context)) {
            return false
        }
        //判断渠道通知是否打开
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var channel = manager.getNotificationChannel(channelId)
            if (channel.importance == NotificationManager.IMPORTANCE_NONE) {
                return false
            }
        }
        return true
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun openSetting(context: Context) {
        //判断通知是否有打开
        var manager = context.getSystemService(NOTIFICATION_SERVICE) as (NotificationManager)
        if (!isNotificationPermissionOpen(context)) {
            toNotifySetting(context, null)
        }
        //判断渠道通知是否打开
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var channel = manager.getNotificationChannel(NEW_MESSAGE)
            if (channel.importance == NotificationManager.IMPORTANCE_NONE) {
                toNotifySetting(context, channel.id)
            }
        }
    }


    /**
     * 手动打开应用通知
     */
    fun toNotifySetting(context: Context, channelId: String?) {
        var intent = Intent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //适配 8.0及8.0以上(8.0需要先打开应用通知，再打开渠道通知)
            if (TextUtils.isEmpty(channelId)) {
                intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            } else {
                intent.action = Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//适配 5.0及5.0以上
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS;
            intent.putExtra("app_package", context.getPackageName())
            intent.putExtra("app_uid", context.getApplicationInfo().uid)
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {// 适配 4.4及4.4以上
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.data = Uri.fromParts("package", context.packageName, null)
        } else {
            intent.action = Settings.ACTION_SETTINGS
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }


    /**
     * 通知权限申请
     * @param context
     */
    fun requestNotify(context: Context) {
        /**
         * 跳到通知栏设置界面
         * @param context
         */
        val localIntent = Intent()
        ///< 直接跳转到应用通知设置的代码
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localIntent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            localIntent.putExtra(
                Settings.EXTRA_APP_PACKAGE,
                context.packageName
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
            Build.VERSION.SDK_INT < Build.VERSION_CODES.O
        ) {
            localIntent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            localIntent.putExtra("app_package", context.packageName)
            localIntent.putExtra("app_uid", context.applicationInfo.uid)
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            localIntent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            localIntent.addCategory(Intent.CATEGORY_DEFAULT)
            localIntent.data = Uri.parse("package:" + context.packageName)
        } else { ///< 4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                localIntent.data = Uri.fromParts(
                    "package",
                    context.packageName,
                    null
                )
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.action = Intent.ACTION_VIEW
                localIntent.setClassName(
                    "com.android.settings",
                    "com.android.setting.InstalledAppDetails"
                )
                localIntent.putExtra(
                    "com.android.settings.ApplicationPkgName",
                    context.packageName
                )
            }
        }
        context.startActivity(localIntent)
    }

    fun isNotificationPermissionOpen(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManagerCompat.from(context.applicationContext).importance != NotificationManager.IMPORTANCE_NONE
        } else NotificationManagerCompat.from(context.applicationContext)
            .areNotificationsEnabled()
    }

    @SuppressLint("WrongConstant")
    fun showResidentNotifyCation(
        context: Context,
        func: (builder: NotificationCompat.Builder, view: RemoteViews) -> Unit
    ) {

        var builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationCompat.Builder(context, WIDGET_MESSAGE)
            } else NotificationCompat.Builder(context)
        var contentView = RemoteViews(context.packageName, R.layout.notifycation_layout)
        var intent = Intent(context, MainActivity::class.java)
        intent.putExtra("from", "notify")
        var pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.build().contentIntent = pendingIntent

        builder.build().flags = Notification.FLAG_ONLY_ALERT_ONCE
        builder
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.mipmap.st_push
                )
            ) //设置自动收报机和通知中显示的大图标。
            .setContentTitle(context.resources.getString(R.string.app_name)) // 标题
            .setPriority(NotificationManager.IMPORTANCE_LOW) //设置优先级 PRIORITY_DEFAULT
            .setWhen(System.currentTimeMillis()) // 设置通知发送的时间戳
//            .setDeleteIntent(deletePending)
            .setShowWhen(false)//设置是否显示时间
            .setContentText("")
            .setOngoing(true)
            .setAutoCancel(false)// 点击通知后通知在通知栏上消失
            //锁屏状态下显示通知图标及标题 1、VISIBILITY_PUBLIC 在所有锁定屏幕上完整显示此通知/2、VISIBILITY_PRIVATE 隐藏安全锁屏上的敏感或私人信息/3、VISIBILITY_SECRET 不显示任何部分
            .setVisibility(Notification.VISIBILITY_SECRET)//部分手机没效果
//            .setFullScreenIntent(pendingIntent, false)//悬挂式通知8.0需手动打开
            .setContent(contentView)
            .setContentIntent(pendingIntent) // 设置通知的点击事件
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setGroup("normal")
            builder.setGroupSummary(false)
        }
//                .setColorized(true)
//                .setGroupSummary(true)//将此通知设置为一组通知的组摘要
//                .setGroup(NEW_GROUP)//使用组密钥
//                .setDeleteIntent(pendingIntent)//当用户直接从通知面板清除通知时 发送意图
//                .setFullScreenIntent(pendingIntent,true)
//                .setContentInfo("大文本")//在通知的右侧设置大文本。
//                .setColor(Color.parseColor("#ff0000"))
//                .setLights()//希望设备上的LED闪烁的argb值以及速率
//                .setTimeoutAfter(3000)//指定取消此通知的时间（如果尚未取消）。

        func(builder, contentView)
    }


    @SuppressLint("WrongConstant")
    fun showResidentNotifyCation(context: Context): NotificationCompat.Builder {

        var builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationCompat.Builder(context, WIDGET_MESSAGE)
            } else NotificationCompat.Builder(context)
        var contentView = RemoteViews(context.packageName, R.layout.notifycation_layout)

        var intent = Intent(context, MainActivity::class.java)
        intent.putExtra("from", "notify")
        var pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.build().contentIntent = pendingIntent
        builder.build().flags = Notification.FLAG_ONLY_ALERT_ONCE
        builder
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.mipmap.st_push
                )
            ) //设置自动收报机和通知中显示的大图标。
            .setContentTitle(context.resources.getString(R.string.app_name)) // 标题
            .setPriority(NotificationManager.IMPORTANCE_LOW) //设置优先级 PRIORITY_DEFAULT
            .setWhen(System.currentTimeMillis()) // 设置通知发送的时间戳
            .setShowWhen(false)//设置是否显示时间
            .setContentText("")
            .setOngoing(true)
            .setAutoCancel(false)// 点击通知后通知在通知栏上消失
            //锁屏状态下显示通知图标及标题 1、VISIBILITY_PUBLIC 在所有锁定屏幕上完整显示此通知/2、VISIBILITY_PRIVATE 隐藏安全锁屏上的敏感或私人信息/3、VISIBILITY_SECRET 不显示任何部分
            .setVisibility(Notification.VISIBILITY_SECRET)//部分手机没效果
            .setContent(contentView)
            .setContentIntent(pendingIntent) // 设置通知的点击事件
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setGroup("normal")
            builder.setGroupSummary(false)
        }
        return builder
    }
}