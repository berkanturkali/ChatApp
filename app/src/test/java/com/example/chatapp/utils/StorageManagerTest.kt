package com.example.chatapp.utils

import android.content.Context
import android.content.SharedPreferences
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class StorageManagerTest {

    @Test
    fun setTokenAndMailTest() {
        val sharedPrefs = mockk<SharedPreferences>(relaxed = true)
        val sharedPrefsEditor =
            mockk<SharedPreferences.Editor>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        every { context.getSharedPreferences(any(), any()) }
            .returns(sharedPrefs)
        every { sharedPrefs.edit() }
            .returns(sharedPrefsEditor)
        every { sharedPrefsEditor.putString(any(), any()) }
            .returns(sharedPrefsEditor)
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
        val email = "john@doe.com"
        val fullname = "John Doe"
        val storageManager = StorageManager(context)
        storageManager.setTokenAndMail(token, email, fullname)
        verify {
            sharedPrefsEditor.putString(eq(Constants.TOKEN_KEY),eq(token))
            sharedPrefsEditor.putString(eq(Constants.EMAIL),eq(email))
            sharedPrefsEditor.putString(eq(Constants.FULL_NAME),eq(fullname))
        }
        verify { sharedPrefsEditor.commit() }
    }
}