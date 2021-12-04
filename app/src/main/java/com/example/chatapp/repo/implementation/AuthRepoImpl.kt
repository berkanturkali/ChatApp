package com.example.chatapp.repo.implementation

import com.example.chatapp.model.User
import com.example.chatapp.network.AuthApi
import com.example.chatapp.repo.abstraction.AuthRepo
import com.example.chatapp.repo.util.safeApiCall
import com.google.gson.JsonObject
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepoImpl @Inject constructor(
    private val api: AuthApi,
    private val retrofit: Retrofit,
) : AuthRepo {
    override suspend fun signup(user: User) = safeApiCall(retrofit) { api.signup(user) }
    override suspend fun login(credentials: JsonObject) =
        safeApiCall(retrofit) { api.login(credentials) }
}