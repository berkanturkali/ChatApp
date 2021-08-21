package com.example.chatapp.utils

import android.text.TextUtils

fun List<String>.separateItems(): String {
    return TextUtils.join(", ", this)
}