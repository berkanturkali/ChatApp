package com.example.chatapp.repo.implementation


import com.example.chatapp.model.Room
import com.example.chatapp.network.ChatApi
import com.example.chatapp.repo.abstraction.ChatRepo
import com.example.chatapp.repo.util.safeApiCall
import okhttp3.MultipartBody
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "ChatRepoImpl"

@Singleton
class ChatRepoImpl @Inject constructor(
    private val api: ChatApi,
    private val retrofit: Retrofit,
) : ChatRepo {
    override suspend fun addRoom(room: Room, body: MultipartBody.Part?) =
        safeApiCall(retrofit) { api.addRoom(room, body) }

    override suspend fun getRooms() = safeApiCall(retrofit) { api.rooms() }
    override suspend fun getHistory(room: String) = safeApiCall(retrofit){api.getHistory(room)}


}