package com.example.chatapp.model

sealed class MessageModel {

    data class MessageItem(val message: Message):MessageModel()

    data class SeparatorItem(val date:String):MessageModel()
}