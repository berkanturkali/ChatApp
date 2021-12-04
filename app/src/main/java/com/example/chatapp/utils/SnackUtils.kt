package com.example.chatapp.utils

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.chatapp.R
import com.google.android.material.snackbar.Snackbar

fun Fragment.showSnack(message: String, color: Int = R.color.colorDanger) {
    Snackbar.make(this.requireView(), message, Snackbar.LENGTH_LONG).run {
        setBackgroundTint(ContextCompat.getColor(this@showSnack.requireContext(), color))
        show()
    }
}