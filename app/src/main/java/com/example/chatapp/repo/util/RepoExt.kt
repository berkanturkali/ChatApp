package com.example.chatapp.repo.util

import com.example.chatapp.model.ErrorResponse
import com.example.chatapp.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException


suspend fun <T> safeApiCall(
    retrofit: Retrofit,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> Response<T>,
    ): Resource<T> {
    return try {
        val response = withContext(dispatcher) { apiCall.invoke() }
        if (response.isSuccessful) {
            Resource.Success(response.body())
        } else {
            Resource.Error(response.parseError(retrofit))
        }
    } catch (throwable: Throwable) {
        throwable.printStackTrace()
        when (throwable) {
            is TimeoutCancellationException -> {
                Resource.Error("Timeout")
            }
            is IOException -> {
                Resource.Error(throwable.localizedMessage ?: "")
            }
            is HttpException -> {
                val errorResponse = throwable.response()
                Resource.Error(errorResponse?.parseError(retrofit) ?: "")
            }
            else ->{
                throwable.printStackTrace()
                Resource.Error("unknown error")
            }
        }
    }
}

private fun <T> Response<T>.parseError(retrofit: Retrofit): String {
    val converter: Converter<ResponseBody, ErrorResponse> =
        retrofit.responseBodyConverter(ErrorResponse::class.java, arrayOfNulls<Annotation>(0))
      errorBody()?.let {
        converter.convert(it)?.let { response ->
            return response.message
        }
    } ?: return ""
}