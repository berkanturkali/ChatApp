package com.example.chatapp.repo

import com.example.chatapp.model.Message
import com.example.chatapp.model.Room
import com.example.chatapp.utils.Resource
import okhttp3.MultipartBody

interface ChatRepo:Repo {

    suspend fun addRoom(room:Room, body: MultipartBody.Part?): Resource<String>

    suspend fun getRooms():Resource<List<Room>>

    suspend fun getHistory(room:String):Resource<List<Message.TextMessage>>
}