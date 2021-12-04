package com.example.chatapp.viewmodel.authflow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.chatapp.MainCoroutineRule
import com.example.chatapp.getOrAwaitValue
import com.example.chatapp.repo.FakeAuthRepo
import com.example.chatapp.repo.abstraction.AuthRepo
import com.example.chatapp.utils.InputType
import com.example.chatapp.utils.StorageManager
import com.google.common.truth.Truth
import com.google.gson.JsonObject
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class LoginFragmentViewModelTest {

    private lateinit var viewModel: LoginFragmentViewModel

    private lateinit var repo: AuthRepo

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val storageManager = mockk<StorageManager>()

    @Before
    fun setup() {
        repo = FakeAuthRepo()
        viewModel = LoginFragmentViewModel(repo, storageManager)
    }


    @Test
    fun validate_InvalidEmail_returnsFalse() =mainCoroutineRule.runBlockingTest{
        val isValid = viewModel.isValid("", "1234567")
        val message = viewModel.message.getOrAwaitValue()
        MatcherAssert.assertThat(isValid, `is`(false))
        Truth.assertThat(message.peekContent()).isEqualTo(InputType.EMAIL.message)
    }

    @Test
    fun validate_InvalidPassword_returnsFalse() =mainCoroutineRule.runBlockingTest{
        val isValid = viewModel.isValid("john@doe.com", "123")
        val message = viewModel.message.getOrAwaitValue()
        MatcherAssert.assertThat(isValid, `is`(false))
        Truth.assertThat(message.peekContent()).isEqualTo(InputType.PASSWORD.message)
    }

    @Test
    fun validate_withValidCredentials_returnsTrue() {
        val isValid = viewModel.isValid("john@doe.com", "1234567")
        assertThat(isValid, `is`(true))
    }

    @Test
    fun login_withValidCredentials_Success() = mainCoroutineRule.runBlockingTest {
        val credentials = JsonObject()
        credentials.addProperty("email", "john@doe.com")
        credentials.addProperty("password", "1234567")
        viewModel.login(credentials)
        val value = viewModel.loginInfo.getOrAwaitValue()
        Truth.assertThat(value.peekContent().data!!.fullname).isEqualTo("John Doe")
    }

    @Test
    fun login_withFail_returnsError() =mainCoroutineRule.runBlockingTest{
        val credentials = JsonObject()
        credentials.addProperty("email", "john@doe.com")
        credentials.addProperty("password", "1234567")
        (repo as FakeAuthRepo).setFail(true)
        viewModel.login(credentials)
        val value = viewModel.loginInfo.getOrAwaitValue()
        Truth.assertThat(value.peekContent().message).isEqualTo("Error")
    }
}