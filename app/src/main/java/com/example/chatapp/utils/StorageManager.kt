package com.example.chatapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.chatapp.utils.Constants.EMAIL
import com.example.chatapp.utils.Constants.PREF_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageManager @Inject constructor(@ApplicationContext context: Context) {
    private var preferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = preferences.edit()

    fun setTokenAndMail(token: String,email:String) {
        editor.putString(PREF_NAME, token)
        editor.putString(EMAIL,email)
        editor.commit()
    }

    fun getToken() = preferences.getString(PREF_NAME, "")

    fun getEmail() = preferences.getString(EMAIL, "")

    fun clearSharedPref() {
        editor.clear()
        editor.apply()
    }
}