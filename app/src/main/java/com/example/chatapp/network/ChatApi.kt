package com.example.chatapp.network

import com.example.chatapp.model.Message
import com.example.chatapp.model.Room
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ChatApi {

    @Multipart
    @POST("chat/room/new")
    suspend fun addRoom(
        @Part("room") room: Room,
        @Part image: MultipartBody.Part? = null,
    ): Response<String>

    @GET("chat/room/rooms")
    suspend fun rooms(): Response<List<Room>>

    @GET("chat/room/history/{room}")
    suspend fun getHistory(
        @Path("room") room: String
    ): Response<List<Message.TextMessage>>
}