package com.lovely.deer.ext

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue


val Context.displayMetrics: DisplayMetrics get() = resources.displayMetrics

fun Context.dpToPx(dp: Int): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),displayMetrics).toInt()


/**
 * 디바이스 너비.
 **/
fun Context.getDeviceWidth(): Int {
    return displayMetrics.widthPixels
}

/**
 * 디바이스 높이.
 **/
fun Context.getDeviceHeight(): Int {
    return displayMetrics.heightPixels
}


/**
 * 앱의 실행 위치.
 **/
fun Context.isAppRunInBackground(): Boolean {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val appProcesses = activityManager.runningAppProcesses
    for (appProcess in appProcesses) {
        if (appProcess.processName == packageName) {
            // return true -> Run in background
            // return false - > Run in foreground
            return appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND
        }
    }
    return false
}