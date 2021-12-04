package com.example.chatapp.view.authflow

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.chatapp.R
import com.example.chatapp.launchFragmentInHiltContainer
import com.example.chatapp.repo.abstraction.AuthRepo
import com.example.chatapp.utils.InputType
import com.example.chatapp.utils.StorageManager
import com.example.chatapp.viewmodel.authflow.LoginFragmentViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: AuthRepo
    private lateinit var viewModel: LoginFragmentViewModel

    private val storageManager = mockk<StorageManager>()

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = LoginFragmentViewModel(repo, storageManager)
    }

    @Test
    fun displayErrorMessage_whenEmailIsInvalid() {
        launchFragmentInHiltContainer<LoginFragment> { }
        Espresso.onView(ViewMatchers.withId(R.id.email_et)).perform(ViewActions.typeText(" "))
        Espresso.onView(ViewMatchers.withId(R.id.pw_et)).perform(ViewActions.typeText("1234567"),
            ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.login_btn)).perform(ViewActions.click())
        Espresso.onView(CoreMatchers.allOf(ViewMatchers.withId(com.google.android.material.R.id.snackbar_text),
            ViewMatchers.withText(InputType.EMAIL.message)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun displayErrorMessage_whenPasswordIsInvalid() {
        launchFragmentInHiltContainer<LoginFragment> { }
        Espresso.onView(ViewMatchers.withId(R.id.email_et))
            .perform(ViewActions.typeText("john@doe.com "))
        Espresso.onView(ViewMatchers.withId(R.id.pw_et)).perform(ViewActions.typeText("123"),
            ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.login_btn)).perform(ViewActions.click())
        Espresso.onView(CoreMatchers.allOf(ViewMatchers.withId(com.google.android.material.R.id.snackbar_text),
            ViewMatchers.withText(InputType.PASSWORD.message)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun navigateToHome_whenLoginSuccess() {
        val navController = mockk<NavController>(relaxed = true)
        launchFragmentInHiltContainer<LoginFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }
        Espresso.onView(ViewMatchers.withId(R.id.email_et))
            .perform(ViewActions.typeText("john@doe.com "))
        Espresso.onView(ViewMatchers.withId(R.id.pw_et)).perform(ViewActions.typeText("1234567"),
            ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.login_btn)).perform(ViewActions.click())
        verify { navController.navigate(R.id.action_loginFragment_to_homeFlowContainerFragment) }
    }
}