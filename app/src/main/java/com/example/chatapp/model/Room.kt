package com.example.chatapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Room(
    val name: String,
    var createdBy: String = "",
    var image: String = ""
) : Parcelable
