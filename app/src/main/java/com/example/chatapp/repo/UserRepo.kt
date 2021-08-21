package com.example.chatapp.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.chatapp.model.User
import com.example.chatapp.network.ChatApi
import com.example.chatapp.utils.BaseService
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepo @Inject constructor(
    private val api: ChatApi
) : BaseService() {
    suspend fun signup(user: User) = apiCall { api.signup(user) }
    suspend fun login(credentials : JsonObject) = apiCall { api.login(credentials) }
    suspend fun getMe() = apiCall { api.getMe() }
}