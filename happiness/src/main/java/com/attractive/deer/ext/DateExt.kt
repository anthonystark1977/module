package com.attractive.deer.ext

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


@SuppressLint("SimpleDateFormat")
val YYYY_MM_DD_HH_MM = SimpleDateFormat("yyyy-MM-dd HH:mm")

@SuppressLint("SimpleDateFormat")
val YYYY_MM_DD_T_HH_MM = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

const val SEC = 60
const val MIN = 60
const val HOUR = 24
const val DAY = 30
const val MONTH = 12


fun Long.parseTime(): String {
    if (TimeUnit.MILLISECONDS.toHours(this) == 0L) {
        // append only min and hours
        val FORMAT = "%02d:%02d"
        return String.format(
            FORMAT, TimeUnit.MILLISECONDS.toMinutes(this) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(this)
            ),
            TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(this)
            )
        )
    } else {
        val FORMAT = "%02d:%02d:%02d"
        return String.format(
            FORMAT,
            TimeUnit.MILLISECONDS.toHours(this),
            TimeUnit.MILLISECONDS.toMinutes(this) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(this)
            ),
            TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(this)
            )
        )
    }
}


fun Date.getBeforeSpecificTime(
    cntYear: Int?,
    cntMonth: Int?,
    cntDay: Int?,
    cntHourOfDay: Int?,
    cntMinute: Int?,
    cntSecond: Int?
): String {
    val cal = Calendar.getInstance().apply { time = this@getBeforeSpecificTime }
    YYYY_MM_DD_HH_MM.format(cal.time)
    cal.add(Calendar.YEAR, cntYear ?: 0)
    cal.add(Calendar.MONTH, cntMonth ?: 0)
    cal.add(Calendar.DATE, cntDay ?: 0)
    cal.add(Calendar.HOUR_OF_DAY, cntHourOfDay ?: 0)
    cal.add(Calendar.MINUTE, cntMinute ?: 0)
    cal.add(Calendar.SECOND, cntSecond ?: 0)
    return YYYY_MM_DD_HH_MM.format(cal.time)
}


fun getMonthsDifference(date1: Date, date2: Date): Int {
    val month1 = date1.year * 12 + date1.month
    val month2 = date2.year * 12 + date2.month
    return month2 - month1
}


@SuppressLint("SimpleDateFormat")
fun Date.getCurrentTimeInKorea(format: String): String =
    SimpleDateFormat(format).apply { timeZone = TimeZone.getTimeZone("Asia/Seoul") }.format(this)


fun String.getDateFromatedString(): Long {
    val date: Date? = YYYY_MM_DD_T_HH_MM.parse(this)
    return date?.time ?: 0L
}

fun Long.formatTimeString(): String {
    val curTime = System.currentTimeMillis()
    var diffTime = (curTime - this) / 1000
    var msg: String? = null
    when {
        diffTime < SEC -> msg = "방금 전"
        SEC.let { diffTime /= it; diffTime } < MIN -> msg = diffTime.toString() + "분 전"
        MIN.let { diffTime /= it; diffTime } < HOUR -> msg = diffTime.toString() + "시간 전"
        HOUR.let { diffTime /= it; diffTime } < DAY -> msg = diffTime.toString() + "일 전"
        DAY.let { diffTime /= it; diffTime } < MONTH -> msg = diffTime.toString() + "달 전"
        else -> msg = diffTime.toString() + "년 전"
    }
    return msg
}
