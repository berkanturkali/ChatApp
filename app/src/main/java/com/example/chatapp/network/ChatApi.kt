package com.example.chatapp.network

import com.example.chatapp.model.Message
import com.example.chatapp.model.Room
import com.example.chatapp.model.TokenResponse
import com.example.chatapp.model.User
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ChatApi {

    @POST("account/signup")
    suspend fun signup(@Body user: User): Response<String>

    @POST("account/login")
    suspend fun login(@Body credentials: JsonObject): Response<TokenResponse>

    @GET("account/me")
    suspend fun getMe(): Response<User>

    @Multipart
    @POST("chat/room/new")
    suspend fun addRoom(
        @Part("room") room: Room,
        @Part image: MultipartBody.Part? = null
    ): Response<String>

    @GET("chat/room/all")
    suspend fun rooms(): Response<List<Room>>

    @GET("chat/room/{room}")
    suspend fun getHistory(
        @Path("room") room: String
    ): Response<List<Message.TextMessage>>
}