package com.example.chatapp.utils

import androidx.core.util.PatternsCompat
import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.trim() = text.toString().trim()

fun TextInputEditText.text() = text.toString()

fun String.isValidEmail(): Boolean =
    this.isNotEmpty() && PatternsCompat.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword(): Boolean =
    this.isNotEmpty() && this.length > 6

fun String.isValid(): Boolean = this.isNotEmpty()

enum class InputType(val message: String) {
    EMAIL("Please provide a valid email."),
    PASSWORD("Password length must be greater than 6 characters."),
    FIRSTNAME("Please provide a valid firstname."),
    LASTNAME("Please provide a valid lastname."),
    ROOM("Room name must not be empty.")
}
