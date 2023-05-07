package com.example.test.dxworkspace.core.extensions

import android.util.Log

inline fun justTry(block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        Log.d("TAG", "justTry: ${e.message} ")
    }
}
