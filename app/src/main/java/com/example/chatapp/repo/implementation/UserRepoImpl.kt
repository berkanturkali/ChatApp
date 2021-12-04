package com.example.chatapp.repo.implementation

import com.example.chatapp.model.User
import com.example.chatapp.network.UserApi
import com.example.chatapp.repo.abstraction.UserRepo
import com.example.chatapp.repo.util.safeApiCall
import com.example.chatapp.utils.Resource
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepoImpl @Inject constructor(
    private val api: UserApi,
    private val retrofit: Retrofit,
) : UserRepo {

    override suspend fun getMe(): Resource<User>  = safeApiCall(retrofit){
        api.getMe()
    }
}