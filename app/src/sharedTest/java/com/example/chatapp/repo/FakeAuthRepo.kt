package com.example.chatapp.repo

import com.example.chatapp.model.TokenResponse
import com.example.chatapp.model.User
import com.example.chatapp.repo.abstraction.AuthRepo
import com.example.chatapp.utils.Resource
import com.google.gson.JsonObject
import javax.inject.Inject

class FakeAuthRepo @Inject constructor() : AuthRepo {

    private var shouldFail: Boolean = false

    override suspend fun signup(user: User): Resource<String> {
        return if (!shouldFail) {
            Resource.Success("Success")
        } else {
            Resource.Error("Error")
        }
    }

    override suspend fun login(credentials: JsonObject): Resource<TokenResponse> {
        return if (!shouldFail) {
            Resource.Success(TokenResponse("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",
                "john@doe.com",
                "John Doe"))
        } else {
            Resource.Error("Error")
        }
    }

    fun setFail(shouldFail: Boolean) {
        this.shouldFail = shouldFail
    }
}