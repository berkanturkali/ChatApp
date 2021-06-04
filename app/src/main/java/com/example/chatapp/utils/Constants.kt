package com.example.chatapp.utils

import com.example.chatapp.BuildConfig


object Constants {
    const val BASE_URL = BuildConfig.BASE_URL
    const val SOCKET_URL = BuildConfig.SOCKET_URL
    const val TIME_OUT = 3000L
    const val CONNECTION_TIMEOUT: Long = 30L
    const val READ_TIMEOUT: Long = 30L
    const val WRITE_TIMEOUT: Long = 30L
    const val PREF_NAME = "token"
    const val NETWORK_PAGE_SIZE = 50
    const val INITIAL_PAGE = 1
    const val EMAIL = "email"
    const val FULL_NAME = "fullname"
    const val TIME_PATTERN = "HH:mm"
    const val DATE_PATTERN = "dd/MM/yyyy"
}