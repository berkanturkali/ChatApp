package com.example.chatapp.repo

import com.example.chatapp.model.TokenResponse
import com.example.chatapp.model.User
import com.example.chatapp.utils.Resource
import com.google.gson.JsonObject

interface UserRepo:Repo {

    suspend fun signup(user: User):Resource<String>

    suspend fun login(credentials:JsonObject):Resource<TokenResponse>

    suspend fun getMe():Resource<User>
}