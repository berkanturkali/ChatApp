package com.example.chatapp.view.authflow

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.chatapp.R
import com.example.chatapp.launchFragmentInHiltContainer
import com.example.chatapp.repo.FakeAuthRepo
import com.example.chatapp.repo.abstraction.AuthRepo
import com.example.chatapp.viewmodel.authflow.SignupFragmentViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SignupFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: AuthRepo

    private lateinit var viewModel: SignupFragmentViewModel


    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = SignupFragmentViewModel(repo)
    }

    @Test
    fun displayErrorMessage_whenEmailIsInvalid() {
        launchFragmentInHiltContainer<SignupFragment> {}
        Espresso.onView(withId(R.id.fn_et)).perform(typeText("John"))
        Espresso.onView(withId(R.id.ln_et)).perform(typeText("Doe"))
        Espresso.onView(withId(R.id.email_et)).perform(typeText(" "))
        Espresso.onView(withId(R.id.pw_et)).perform(typeText("1234567"), closeSoftKeyboard())
        Espresso.onView(withId(R.id.signup_btn)).perform(click())
        onView(allOf(withId(com.google.android.material.R.id.snackbar_text),
            withText(com.example.chatapp.utils.InputType.EMAIL.message)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun displayErrorMessage_whenFirstnameIsInvalid() {
        launchFragmentInHiltContainer<SignupFragment> {}
        Espresso.onView(withId(R.id.fn_et)).perform(typeText(""))
        Espresso.onView(withId(R.id.ln_et)).perform(typeText("Doe"))
        Espresso.onView(withId(R.id.email_et)).perform(typeText("john@doe.com"))
        Espresso.onView(withId(R.id.pw_et)).perform(typeText("1234567"), closeSoftKeyboard())
        Espresso.onView(withId(R.id.signup_btn)).perform(click())
        onView(allOf(withId(com.google.android.material.R.id.snackbar_text),
            withText(com.example.chatapp.utils.InputType.FIRSTNAME.message)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun displayErrorMessage_whenLastnameIsInvalid() {
        launchFragmentInHiltContainer<SignupFragment> {}
        Espresso.onView(withId(R.id.fn_et)).perform(typeText("John"))
        Espresso.onView(withId(R.id.ln_et)).perform(typeText(""))
        Espresso.onView(withId(R.id.email_et)).perform(typeText("john@doe.com"))
        Espresso.onView(withId(R.id.pw_et)).perform(typeText("1234567"), closeSoftKeyboard())
        Espresso.onView(withId(R.id.signup_btn)).perform(click())
        onView(allOf(withId(com.google.android.material.R.id.snackbar_text),
            withText(com.example.chatapp.utils.InputType.LASTNAME.message)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun displayErrorMessage_whenPasswordIsInvalid() {
        launchFragmentInHiltContainer<SignupFragment> {}
        Espresso.onView(withId(R.id.fn_et)).perform(typeText("John"))
        Espresso.onView(withId(R.id.ln_et)).perform(typeText("Doe"))
        Espresso.onView(withId(R.id.email_et)).perform(typeText("john@doe.com"))
        Espresso.onView(withId(R.id.pw_et)).perform(typeText(""), closeSoftKeyboard())
        Espresso.onView(withId(R.id.signup_btn)).perform(click())
        onView(allOf(withId(com.google.android.material.R.id.snackbar_text),
            withText(com.example.chatapp.utils.InputType.PASSWORD.message)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun displaySuccessMessage_thenNavigateToLoginFragment_whenSignupIsSuccess() {
        val navController = mockk<NavController>(relaxed = true)
        launchFragmentInHiltContainer<SignupFragment> {
            Navigation.setViewNavController(this.requireView(), navController)
        }
        Espresso.onView(withId(R.id.fn_et)).perform(typeText("John"))
        Espresso.onView(withId(R.id.ln_et)).perform(typeText("Doe"))
        Espresso.onView(withId(R.id.email_et)).perform(typeText("john@doe.com"))
        Espresso.onView(withId(R.id.pw_et)).perform(typeText("1234567"), closeSoftKeyboard())
        Espresso.onView(withId(R.id.signup_btn)).perform(click())
        (repo as FakeAuthRepo).setFail(false)
        onView(allOf(withId(com.google.android.material.R.id.snackbar_text),
            withText("Success")))
            .check(matches(isDisplayed()))
        verify { navController.navigate(R.id.action_signupFragment_to_loginFragment) }
    }

    @Test
    fun displayErrorMessage_whenSignupIsFail() {
        launchFragmentInHiltContainer<SignupFragment> {}
        Espresso.onView(withId(R.id.fn_et)).perform(typeText("John"))
        Espresso.onView(withId(R.id.ln_et)).perform(typeText("Doe"))
        Espresso.onView(withId(R.id.email_et)).perform(typeText("john@doe.com"))
        Espresso.onView(withId(R.id.pw_et)).perform(typeText("1234567"), closeSoftKeyboard())
        (repo as FakeAuthRepo).setFail(true)
        Espresso.onView(withId(R.id.signup_btn)).perform(click())
        onView(allOf(withId(com.google.android.material.R.id.snackbar_text),
            withText("Error")))
            .check(matches(isDisplayed()))
    }
}