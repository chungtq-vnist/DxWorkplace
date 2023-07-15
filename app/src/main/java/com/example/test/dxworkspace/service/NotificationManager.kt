package com.example.test.dxworkspace.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.example.test.dxworkspace.DxApplication
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.local.preferences.AppPreferences
import com.example.test.dxworkspace.presentation.ui.home.HomeActivity
import com.example.test.dxworkspace.presentation.ui.splash.SplashActivity
import com.example.test.dxworkspace.presentation.utils.common.Constants
import javax.inject.Inject

class NotificationManager constructor(private val context: Context) {

    var sharedPreferences =  AppPreferences.customPrefs(DxApplication.getInstance(), Constants.APLogin.SHARE_PREFERENCES)

    var notificationManager: NotificationManager? = null

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun create(adminChannel: NotificationChannel) {
        notificationManager!!.createNotificationChannel(adminChannel)
    }

    @SuppressLint("InvalidWakeLockTag")
    fun customNotification(
        header: String?,
        subTitle: String?,
        notifyId: String?,
        dataType: Int?,
        object_id: String,
    ) {

        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val chanelId = context.getString(R.string.chanel_id)
        val chanelName = context.getString(R.string.chanel_message)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(chanelId, chanelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager!!.createNotificationChannel(channel)
        }

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(context, chanelId)
            .setChannelId(chanelId)
            .setContentTitle(header)
            .setContentText(subTitle)
            .setPriority(NotificationManagerCompat.IMPORTANCE_MAX)
            .setDefaults(Notification.DEFAULT_ALL)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setSound(alarmSound)
            .setVibrate(longArrayOf(0, 100, 200, 300))
            .setShowWhen(true)
            .setStyle((NotificationCompat.BigTextStyle()).bigText(subTitle))
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_logo_app)
        builder.setSmallIcon(getNotificationIcon(builder))

        val intent : Intent
        if (!sharedPreferences.getBoolean(Constants.APLogin.IS_LOGGED,true)){
            intent = Intent(context, SplashActivity::class.java)
        }else{
            if(DxApplication.appIsRunning)
            intent = Intent(context, HomeActivity::class.java)
            else intent = Intent(context, SplashActivity::class.java)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val type = dataType ?: 0
//            if (action.contains(TYPE_KITCHEN)) {
//            TYPE_KITCHEN
//        } else if (action.contains(TYPE_BOOKING)) {
//            TYPE_BOOKING
//        } else if (action.goToNotiCart()) {
//            TYPE_CART
//        } else if (action.goToNotiOrder()){
//            TYPE_ORDER
//        } else if(action.goToQrOrder()){
//            TYPE_QR_ORDER
//        }
//        else{
//            TYPE_SYSTEM
//        }
        intent.putExtras(
            bundleOf(
                Pair("NOTIFY_ID", notifyId),
                Pair("OBJECT_ID", object_id),
                Pair("SUB_TITLE", subTitle),
                Pair("HEADER",header),
                Pair("DATA_TYPE", type)

            )
        )
//        AppPreferences.clientIdNotification = notifyId?:""
//        AppPreferences.typeNotification = type
//        AppPreferences.objectIdNotification = object_id
//        AppPreferences.subtitleNotification = subTitle?:""


        val notiId: Int = (System.currentTimeMillis() / 1000).toInt()
//        val pIntent = PendingIntent.getActivity(context, notiId, intent, PendingIntent.FLAG_ONE_SHOT)
        val pIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(context, notiId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(context, notiId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        builder.setContentIntent(pIntent)

        notificationManager!!.notify(notiId, builder.build())
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

        val isScreenOn = powerManager.isInteractive
        if (!isScreenOn) {
            val wl = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE, "MH24_SCREENLOCK")
            wl.acquire(2000)
            val wlCpu = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MH24_SCREENLOCK")
            wlCpu.acquire(2000)
        }
    }

    fun notificationForeground(): Notification {
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val chanelId = "Running Background"
        val chanelName = "Running Background"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(chanelId, chanelName, NotificationManager.IMPORTANCE_LOW)
            channel.setShowBadge(false)
            notificationManager!!.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, chanelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setChannelId(chanelId)
            .setPriority(NotificationManagerCompat.IMPORTANCE_LOW)
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentTitle(DxApplication.getInstance().getString(R.string.app_name))
            .setContentText(DxApplication.getInstance().getString(R.string.app_name))

        return builder.build()
    }

//    private fun createOnDismissedIntent(context: Context, notificationId: Int, notifyId: String): PendingIntent {
//        val intent = Intent(context, NotificationDismissedReceiver::class.java)
//        val bundle = Bundle()
//        bundle.putString(NOTIFY_ID, notifyId)
//        intent.putExtras(bundle)
//        return PendingIntent.getBroadcast(context, notificationId, intent, 0)
//    }

    private fun getNotificationIcon(notificationBuilder: NotificationCompat.Builder): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            val color = ContextCompat.getColor(context, R.color.sp_color_ic_launcher);
//            notificationBuilder.color = color
            return R.drawable.ic_logo_app
        }
        return R.drawable.ic_logo_app
    }
}