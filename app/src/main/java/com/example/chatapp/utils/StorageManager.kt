package com.example.chatapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.chatapp.utils.Constants.EMAIL
import com.example.chatapp.utils.Constants.FULL_NAME
import com.example.chatapp.utils.Constants.TOKEN_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageManager @Inject constructor(@ApplicationContext context: Context) {
    private var preferences: SharedPreferences =
        context.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = preferences.edit()

    fun setTokenAndMail(token: String,email:String,fullname:String) {
        editor.putString(TOKEN_KEY, token)
        editor.putString(EMAIL,email)
        editor.putString(FULL_NAME,fullname)
        editor.commit()
    }

    fun getToken() = preferences.getString(TOKEN_KEY, "")

    fun getEmail() = preferences.getString(EMAIL, "")

    fun getFullname() = preferences.getString(FULL_NAME,"")

    fun clearSharedPref() {
        editor.clear()
        editor.apply()
    }
}