package com.example.test.dxworkspace.core.exception

import android.content.Context
import com.google.gson.Gson
import com.example.test.dxworkspace.core.extensions.justTry

sealed class Failure {
    class ServerError(val code: Int = 404, val message: String = "Not Found!") : Failure()
    class ResponseError(errorCode: Int = 0, message: String = "") : Failure()
    class Error(val message: String = "Không xác định!") : Failure()
    class NetworkConnection : Failure()

    companion object {
        const val TYPE_NOT_FOUND = 0
        const val TYPE_NO_UNREACH = 1
        const val TYPE_UNAUTHORIZE = 2
        const val TYPE_ACTIVATED = 3
        const val TYPE_ERROR_REQUEST = 4
        const val TYPE_ACTIVE = 5
        const val TYPE_CODE_NETWORK = 12321
        const val TYPE_UNKNOW = 123456

        fun getTypeError(failure: Failure): Int {
            if (failure is ServerError) {
                if (failure.message.contains("error") && (failure.message.contains("Not Found") || failure.message.contains("doesn't exist"))) {
                    return TYPE_NOT_FOUND
                } else if (failure.message.contains("Unable to resolve host")) {
                    return TYPE_NO_UNREACH
                } else if (failure.message.contains("activated")) {
                    return TYPE_ACTIVATED
                } else if (failure.code == 422) {
                    return TYPE_ERROR_REQUEST
                } else if (failure.code == TYPE_CODE_NETWORK) {
                    return TYPE_CODE_NETWORK
                } else if (failure.code == TYPE_UNKNOW) {
                    return TYPE_UNKNOW
                }
            }
            return TYPE_UNAUTHORIZE
        }
    }

//    fun getErrorMsg(gson: Gson, context: Context): String {
//        var ms = "Unknown"
//        if (this is ServerError) {
//            justTry {
//                val apiError = gson.fromJson(this.message, ApiError::class.java)
//                ms = gson.fromJson(apiError.errors?.ahamove?.firstOrNull()
//                        ?: "{}", AhamoveErr::class.java).getDescBill(context)
//                        ?: apiError.errors?.base?.firstOrNull() ?: apiError.errors?.cart?.firstOrNull()
//                                ?: "Unknown"
//            }
//        }
//        return ms
//    }
}
