package com.example.chatapp.view.homeflow

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerActions.open
import androidx.test.espresso.contrib.NavigationViewActions.navigateTo
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.chatapp.R
import com.example.chatapp.launchFragmentInHiltContainer
import com.example.chatapp.repo.abstraction.UserRepo
import com.example.chatapp.viewmodel.homeflow.HomeFlowContainerViewModel
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@MediumTest
class HomeFlowContainerFragmentTest{

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo:UserRepo

    private lateinit var viewModel:HomeFlowContainerViewModel

    @Before
    fun setup(){
        hiltRule.inject()
        viewModel = HomeFlowContainerViewModel(repo)
    }

    @Test
    fun setUserInfo_whenGetMeSuccess(){
        launchFragmentInHiltContainer<HomeFlowContainerFragment> {  }
        onView(withId(R.id.drawer_layout))
            .perform(DrawerActions.open())
        onView(withId(R.id.fullNameTv)).check(matches(withText("John Doe")))
    }

    @Test
    fun clickLogoutButton_performLogout(){
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        launchFragmentInHiltContainer<HomeFlowContainerFragment> {
            navController.setGraph(R.navigation.nav_graph)
            navController.setCurrentDestination(R.id.homeFlowContainerFragment)
            Navigation.setViewNavController(requireView(),navController)
        }
        onView(withId(R.id.drawer_layout))
            .perform(open())

        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.logout))
        Truth.assertThat(navController.currentDestination?.id).isEqualTo(R.id.loginSignupOptionsFragment)
    }
}