package com.example.chatapp.utils

import com.example.chatapp.model.ErrorResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit

fun <T> parseError(retrofit: Retrofit, response: Response<T>): String {
    val converter: Converter<ResponseBody, ErrorResponse> =
        retrofit.responseBodyConverter(ErrorResponse::class.java, arrayOfNulls<Annotation>(0))
    response.errorBody()?.let {
        converter.convert(it)?.let { response ->
            return response.message
        }
    } ?: return ""
}