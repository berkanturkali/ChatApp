package com.example.chatapp.network

import com.example.chatapp.model.Message
import com.example.chatapp.model.Room
import com.example.chatapp.model.TokenResponse
import com.example.chatapp.model.User
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApi {

    @POST("account/signup")
    suspend fun signup(@Body user: User): Response<String>

    @POST("account/login")
    suspend fun login(@Body credentials: JsonObject): Response<TokenResponse>

    @GET("account/me")
    suspend fun getMe(): Response<User>

    @POST("chat/room/new")
    suspend fun addRoom(
        @Body room: Room
    ): Response<String>

    @GET("chat/room/all")
    suspend fun rooms(): Response<List<Room>>

    @GET("chat/room/{room}")
    suspend fun getHistory(
        @Path("room") room: String
    ): Response<List<Message.TextMessage>>

}