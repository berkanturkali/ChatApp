package com.example.chatapp.repo

import com.example.chatapp.model.User
import com.example.chatapp.network.ChatApi
import com.example.chatapp.utils.BaseService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepo @Inject constructor(
    private val api: ChatApi
) : BaseService() {
    suspend fun signup(user: User) = apiCall { api.signup(user) }
}