package com.example.chatapp.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.chatapp.BuildConfig
import com.example.chatapp.R

fun ImageView.load(
    url: String,
) {
    Glide
        .with(context)
        .load("${BuildConfig.BASE_URL}/$url")
        .error(R.mipmap.ic_launcher_round)
        .into(this)
}