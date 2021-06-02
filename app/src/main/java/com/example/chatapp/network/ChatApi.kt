package com.example.chatapp.network

import com.example.chatapp.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApi {

    @POST("account/signup")
    suspend fun signup(@Body user: User): Response<String>
}