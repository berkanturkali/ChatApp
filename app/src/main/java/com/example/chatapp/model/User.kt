package com.example.chatapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class User(
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String,
    val interests: MutableList<String> = mutableListOf(),
    val photo: String = "",
    var aboutMe:String = "",
    var _id:String = ""
):Parcelable