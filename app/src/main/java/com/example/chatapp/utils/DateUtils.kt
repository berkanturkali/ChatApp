package com.example.chatapp.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

internal fun isYesterday(date: Long): Boolean {
    val d = Date(date)
    return DateUtils.isToday(d.time + DateUtils.DAY_IN_MILLIS)
}

internal fun isToday(date: Long): Boolean {
    val d = Date(date)
    return DateUtils.isToday(d.time)
}

fun Long.getDate(pattern: String = Constants.TIME_PATTERN): String {
    return pattern.formatter().format(this)
}

fun String.formatter(): SimpleDateFormat {
    return SimpleDateFormat(this, Locale.getDefault())
}