package com.example.chatapp.view.authflow

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.chatapp.R
import com.example.chatapp.launchFragmentInHiltContainer
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LoginSignupOptionsFragmentTest {


    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun testNavigationToLoginScreen() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        launchFragmentInHiltContainer<LoginSignupOptionsFragment> {
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(this.requireView(), navController)
        }

        Espresso.onView(ViewMatchers.withId(R.id.login_btn)).perform(ViewActions.click())
        Truth.assertThat(navController.currentDestination?.id).isEqualTo(R.id.loginFragment)
    }

    @Test
    fun testNavigationToSignupScreen() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        launchFragmentInHiltContainer<LoginSignupOptionsFragment> {
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(this.requireView(),navController)
        }

        Espresso.onView(ViewMatchers.withId(R.id.signup_btn)).perform(ViewActions.click())
        Truth.assertThat(navController.currentDestination?.id).isEqualTo(R.id.signupFragment)

    }
}