package com.example.chatapp.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long.getDate(pattern: String = Constants.TIME_PATTERN): String {
    return pattern.formatter().format(this)
}

fun String.formatter(): SimpleDateFormat {
    return SimpleDateFormat(this, Locale.getDefault())
}