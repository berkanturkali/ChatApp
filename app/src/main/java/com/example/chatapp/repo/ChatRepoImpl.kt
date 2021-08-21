package com.example.chatapp.repo


import com.example.chatapp.model.Message
import com.example.chatapp.model.Room
import com.example.chatapp.network.ChatApi
import com.example.chatapp.utils.Resource
import com.example.chatapp.utils.apiCall
import okhttp3.MultipartBody
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepoImpl @Inject constructor(
    private val api: ChatApi,
    private val retrofit: Retrofit
):ChatRepo {
    override suspend fun addRoom(room: Room, body: MultipartBody.Part?) =
        apiCall(retrofit) { api.addRoom(room, body) }

    override suspend fun getRooms() = apiCall(retrofit) { api.rooms() }

    override suspend fun getHistory(room: String): Resource<List<Message.TextMessage>> =
        apiCall(retrofit) { api.getHistory(room) }
}