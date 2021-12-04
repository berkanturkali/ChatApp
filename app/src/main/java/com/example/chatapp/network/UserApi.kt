package com.example.chatapp.network

import com.example.chatapp.model.User
import retrofit2.Response
import retrofit2.http.GET

interface UserApi {

    @GET("user/me")
    suspend fun getMe(): Response<User>
}