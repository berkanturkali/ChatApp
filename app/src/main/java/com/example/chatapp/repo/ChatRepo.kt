package com.example.chatapp.repo


import com.example.chatapp.model.Room
import com.example.chatapp.network.ChatApi
import com.example.chatapp.utils.BaseService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepo @Inject constructor(
    private val api: ChatApi
) : BaseService() {
    suspend fun addRoom(room: Room) = apiCall { api.addRoom(room) }

    suspend fun getRooms() = apiCall { api.rooms() }

    suspend fun getHistory(room:String) = apiCall { api.getHistory(room) }
}