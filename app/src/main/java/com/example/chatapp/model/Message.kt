package com.example.chatapp.model

sealed class Message {

    data class TextMessage(
        val message: String,
        val sender: String,
        val room: String,
        var createdAt: Long = 0,
        val receiver: String? = null,
        val isPrivate: Boolean = false,
        var _id: String? = null,
    ) : Message()

    data class LogMessage(
        val type: String,
        val content: String,
    ) : Message()
}


