package com.example.chatapp.utils

import android.view.View
import androidx.core.content.ContextCompat
import com.example.chatapp.R
import com.google.android.material.snackbar.Snackbar

fun View.showSnack(color: Int = R.color.colorDanger, message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        .setBackgroundTint(ContextCompat.getColor(this.context, color))
        .show()
}