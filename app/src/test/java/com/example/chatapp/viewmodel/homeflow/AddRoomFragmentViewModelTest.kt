package com.example.chatapp.viewmodel.homeflow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.chatapp.MainCoroutineRule
import com.example.chatapp.getOrAwaitValue
import com.example.chatapp.model.Room
import com.example.chatapp.repo.FakeChatRepo
import com.example.chatapp.repo.abstraction.ChatRepo
import com.example.chatapp.utils.InputType
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
class AddRoomFragmentViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: AddRoomFragmentViewModel

    private lateinit var repo: ChatRepo

    @Before
    fun setup() {
        repo = FakeChatRepo()
        viewModel = AddRoomFragmentViewModel(repo)
    }

    @Test
    fun isValid_withInvalidRoomName_ReturnsFalse() = mainCoroutineRule.runBlockingTest {
        val room = ""
        val isValid = viewModel.isValid(room)
        val value = viewModel.message.getOrAwaitValue()
        Truth.assertThat(isValid).isFalse()
        Truth.assertThat(value.peekContent()).isEqualTo(InputType.ROOM.message)
    }

    @Test
    fun isValid_withValidRoomName_ReturnsTrue(){
        val room = "Some Room"
        val isValid = viewModel.isValid(room)
        Truth.assertThat(isValid).isTrue()
    }

    @Test
    fun addRoom_withSuccess_returnsSuccessMessage() = mainCoroutineRule.runBlockingTest {
        val room = "Room"
        viewModel.addRoom(Room(room),null)
        val value = viewModel.roomInfo.getOrAwaitValue()
        Truth.assertThat(value.peekContent().data).isEqualTo("$room is created successfully.")
    }
}