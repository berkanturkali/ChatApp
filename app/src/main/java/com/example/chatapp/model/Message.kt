package com.example.chatapp.model

sealed class Message {
    data class TextMessage(
        val message: String,
        val sender: String,
        val room: String,
        var createdAt: Long = 0
    ) : Message()

    data class LogMessage(
        val type: String,
        val content: String
    ):Message()
}


