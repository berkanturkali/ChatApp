package com.example.chatapp.viewmodel.authflow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.chatapp.MainCoroutineRule
import com.example.chatapp.getOrAwaitValue
import com.example.chatapp.model.User
import com.example.chatapp.repo.FakeAuthRepo
import com.example.chatapp.repo.abstraction.AuthRepo
import com.example.chatapp.utils.InputType
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class SignupFragmentViewModelTest {

    private lateinit var viewModel: SignupFragmentViewModel
    private lateinit var repo:AuthRepo

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        repo = FakeAuthRepo()
        viewModel = SignupFragmentViewModel(repo)
    }

    @Test
    fun validate_InvalidEmail_returnsFalse() =mainCoroutineRule.runBlockingTest{
        val isValid = viewModel.isValid("", "1234567", "John", "Doe")
        val message = viewModel.message.getOrAwaitValue()
        assertThat(isValid, `is`(false))
        Truth.assertThat(message.peekContent()).isEqualTo(InputType.EMAIL.message)
    }

    @Test
    fun validate_InvalidPassword_returnsFalse() = mainCoroutineRule.runBlockingTest {
        val isValid = viewModel.isValid("test@test.com", "12345", "John", "Doe")
        val message = viewModel.message.getOrAwaitValue()
        assertThat(isValid, `is`(false))
        Truth.assertThat(message.peekContent()).isEqualTo(InputType.PASSWORD.message)
    }

    @Test
    fun validate_EmptyFirstname_returnsFalse() = mainCoroutineRule.runBlockingTest{
        val isValid = viewModel.isValid("test@test.com", "1234567", "", "Doe")
        val message = viewModel.message.getOrAwaitValue()
        assertThat(isValid, `is`(false))
        Truth.assertThat(message.peekContent()).isEqualTo(InputType.FIRSTNAME.message)
    }

    @Test
    fun validate_EmptyLastname_returnFalse() =mainCoroutineRule.runBlockingTest{
        val isValid = viewModel.isValid("test@test.com", "1234567", "John", "")
        val message = viewModel.message.getOrAwaitValue()
        assertThat(isValid, `is`(false))
        Truth.assertThat(message.peekContent()).isEqualTo(InputType.LASTNAME.message)
    }

    @Test
    fun validate_ValidFields_returnsTrue() {
        val isValid = viewModel.isValid("test@test.com", "1234567", "John", "Doe")
        assertThat(isValid, `is`(true))
    }

    @Test
    fun signup_Success_returnsSuccessMessage() = mainCoroutineRule.runBlockingTest {
        val user = User("John", "Doe", "john@doe.com", "1234567")
        viewModel.signup(user)
        val value = viewModel.signupInfo.getOrAwaitValue()
        Truth.assertThat(value.peekContent().data).isEqualTo("Success")
    }

    @Test
    fun signup_Error_returnsErrorMessage() = mainCoroutineRule.runBlockingTest {
        val user = User("John", "Doe", "john@doe.com", "1234567")
        (repo as FakeAuthRepo).setFail(true)
        viewModel.signup(user)
        val value = viewModel.signupInfo.getOrAwaitValue()
        Truth.assertThat(value.peekContent().message).isEqualTo("Error")
    }
}