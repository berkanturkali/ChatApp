package com.example.chatapp.viewmodel.homeflow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.chatapp.MainCoroutineRule
import com.example.chatapp.getOrAwaitValue
import com.example.chatapp.repo.FakeUserRepo
import com.example.chatapp.repo.abstraction.UserRepo
import com.example.chatapp.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class HomeFlowContainerViewModelTest {

    private lateinit var viewModel: HomeFlowContainerViewModel

    private lateinit var repo: UserRepo

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    @Before
    fun setup() {
        repo = FakeUserRepo()
        viewModel = HomeFlowContainerViewModel(repo)
    }

    @Test
    fun getMe_withSuccess_setsUserInfo() = mainCoroutineRule.runBlockingTest {
        viewModel.getMe()
        val value = viewModel.me.getOrAwaitValue()
        assertThat(value, instanceOf(Resource.Success::class.java))
    }

    @Test
    fun getMe_withError_returnsError() = mainCoroutineRule.runBlockingTest {
        (repo as FakeUserRepo).setFail(true)
        viewModel.getMe()
        val value = viewModel.me.getOrAwaitValue()
        assertThat(value, instanceOf(Resource.Error::class.java))
    }
}