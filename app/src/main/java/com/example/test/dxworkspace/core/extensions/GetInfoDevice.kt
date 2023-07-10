package com.example.test.dxworkspace.core.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.content.res.AssetManager
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets


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

    @Throws(IOException::class)
    fun loadJSONFromAsset(context: Context, jsonFileName: String?): String? {
        val manager = context.assets
        val inputStream: InputStream = manager.open(jsonFileName!!)
        val size: Int = inputStream.available()
        val buffer = ByteArray(size)
        val `in`: Int = inputStream.read(buffer)
        inputStream.close()
        return String(buffer, StandardCharsets.UTF_8)
    }
}