package com.example.test.dxworkspace.presentation.utils

import java.text.SimpleDateFormat
import java.util.*

fun getTimeInMillisFromString(dateTimeString: String): Long {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("UTC")

    try {
        val date = format.parse(dateTimeString)
        return date?.time ?: 0
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return 0
}
fun getTimeDDMMYYYYHHMMFromString(dateTimeString: String): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val df = SimpleDateFormat("HH:mm \n dd/MM/yy", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("UTC")

    try {
        val date = format.parse(dateTimeString)
        return df.format(date!!)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return ""
}

fun getTimeDDMMYYYYHHMMFromStringOneLine(dateTimeString: String): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val df = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("UTC")

    try {
        val date = format.parse(dateTimeString)
        return df.format(date!!)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return ""
}

fun getDateTimer() : String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    return format.format(Date())
}

fun getDateDDMMYYYYHHMM(date: Date): String {
    val df = SimpleDateFormat("HH:mm \n dd/MM/yyyy", Locale.getDefault())
    return df.format(date)
}
fun convertToDate(dateString: String): Date? {
    val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return try {
        format.parse(dateString)
    } catch (e: Exception) {
       Date()
    }
}

fun getDateYYYYMMDDHHMMSS(date : Date) : String  {
    val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    return df.format(date)
}