package com.example.chatapp.repo

import com.example.chatapp.model.User
import com.example.chatapp.repo.abstraction.UserRepo
import com.example.chatapp.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeUserRepo @Inject constructor() : UserRepo {

    private var shouldFail = false

    override suspend fun getMe(): Resource<User> {
        return if (!shouldFail) {
            val user = User(firstname = "John", lastname = "Doe", email = "john@doe.com", "1234567")
            Resource.Success(user)
        } else {
            Resource.Error("")
        }
    }

    fun setFail(shouldFail: Boolean) {
        this.shouldFail = shouldFail
    }
}