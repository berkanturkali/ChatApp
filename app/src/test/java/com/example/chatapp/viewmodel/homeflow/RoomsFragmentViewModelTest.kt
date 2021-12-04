package com.example.chatapp.viewmodel.homeflow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.chatapp.MainCoroutineRule
import com.example.chatapp.getOrAwaitValue
import com.example.chatapp.repo.FakeChatRepo
import com.example.chatapp.repo.abstraction.ChatRepo
import com.example.chatapp.utils.Resource
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class RoomsFragmentViewModelTest {


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: RoomsFragmentViewModel

    private lateinit var repo: ChatRepo

    @Before
    fun setup() {
        repo = FakeChatRepo()
        viewModel = RoomsFragmentViewModel(repo)
    }

    @Test
    fun getRooms_Success_returnsRoomsList() = mainCoroutineRule.runBlockingTest {
        (repo as FakeChatRepo).setFail(false)
        viewModel.getRooms()
        val value = viewModel.rooms.getOrAwaitValue()
        Truth.assertThat(value.data!!.size).isEqualTo(100)
    }

    @Test
    fun getRooms_Error_returnsError(){
        (repo as FakeChatRepo).setFail(true)
        viewModel.getRooms()
        val value = viewModel.rooms.getOrAwaitValue()
        Truth.assertThat(value).isInstanceOf(Resource.Error::class.java)
    }
}