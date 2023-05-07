package com.example.test.dxworkspace.presentation.utils.common

import android.content.Context
import com.example.test.dxworkspace.DxApplication
import com.google.gson.Gson

object SharedPreferencesHelper {
    private var shared =
        DxApplication.getInstance().getSharedPreferences(Constants.APLogin.SHARE_PREFERENCES, Context.MODE_PRIVATE)

    fun <T> get(key: String, type: Class<T>): T {
        return when (type) {

            String::class.java -> {
                shared?.getString(key, "") as T
            }
            Boolean::class.java -> {
                shared?.getBoolean(key, false) as T
            }
            Float::class.java -> {
                shared?.getFloat(key, Float.MAX_VALUE) as T
            }
            Int::class.java -> {
                shared?.getInt(key, Int.MAX_VALUE) as T
            }
            Long::class.java -> {
                shared?.getLong(key, Long.MAX_VALUE) as T
            }
            else -> {
                val jsonString = shared?.getString(key, "")
                Gson().fromJson<T>(jsonString, type)
            }
        }

    }

    fun <T> get(key: String, type: Class<T>, default: T): T {
        return when (type) {

            String::class.java -> {
                shared?.getString(key, default as String) as T
            }
            Boolean::class.java -> {
                shared?.getBoolean(key, (default as? Boolean) ?: false) as T
            }
            Float::class.java -> {
                shared?.getFloat(key, Float.MAX_VALUE) as T
            }
            Int::class.java -> {
                shared?.getInt(key, Int.MAX_VALUE) as T
            }
            Long::class.java -> {
                shared?.getLong(key, Long.MAX_VALUE) as T
            }
            else -> {
                val jsonString = shared?.getString(key, "")
                Gson().fromJson<T>(jsonString, type)
            }
        }

    }

    fun <T> put(key: String, data: T) {
        val editor = shared?.edit()
        when (data) {
            is String -> {
                editor?.putString(key, data as String)
            }

            is Boolean -> {
                editor?.putBoolean(key, data as Boolean)
            }
            is Float -> {
                editor?.putFloat(key, data as Float)
            }
            is Int -> {
                editor?.putInt(key, data as Int)
            }
            is Long -> {
                editor?.putLong(key, data as Long)
            }
            else -> {
                editor?.putString(key, Gson().toJson(data))
            }

        }
        editor?.apply()
    }
}