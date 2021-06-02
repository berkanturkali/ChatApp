package com.example.chatapp.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

abstract class BaseService {
    @Inject
    lateinit var retrofit: Retrofit
    protected suspend fun <T : Any> apiCall(
        call: suspend () -> Response<T>
    ): Resource<T> {
        val response: Response<T>
        try {
            withContext(Dispatchers.IO) {
                response = call()
            }
        } catch (t: Throwable) {
            return Resource.Error(t.localizedMessage!!.toString())
        }
        return if (!response.isSuccessful) {
            Resource.Error(parseError(retrofit, response))
        } else {
            Resource.Success(response.body())
        }
    }
}