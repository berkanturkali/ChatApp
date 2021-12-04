package com.example.chatapp.repo.abstraction

import com.example.chatapp.model.User
import com.example.chatapp.utils.Resource

interface UserRepo {

    suspend fun getMe():Resource<User>
}