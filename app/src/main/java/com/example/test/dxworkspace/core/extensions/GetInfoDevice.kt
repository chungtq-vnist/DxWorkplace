package com.example.test.dxworkspace.core.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings

/**
 * Created by greenlove on 2019-05-11
 */
object GetInfoDevice {

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String = try {
        Settings.Secure.getString(context.contentResolver,
                Settings.Secure.ANDROID_ID)
    } catch (e: Exception) {
        e.printStackTrace()
        "device not found id"
    }

    fun getDeviceName() = "${Build.MANUFACTURER}/${Build.DEVICE}/${Build.MODEL}"

    fun getOsVersion(): String = Build.VERSION.RELEASE
}