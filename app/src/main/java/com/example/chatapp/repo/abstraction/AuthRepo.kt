package com.example.chatapp.repo.abstraction

import com.example.chatapp.model.TokenResponse
import com.example.chatapp.model.User
import com.example.chatapp.utils.Resource
import com.google.gson.JsonObject

interface AuthRepo {

    suspend fun signup(user: User):Resource<String>

    suspend fun login(credentials:JsonObject):Resource<TokenResponse>
}