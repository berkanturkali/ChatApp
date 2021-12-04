package com.example.chatapp.viewmodel.homeflow

import com.example.chatapp.R
import com.example.chatapp.repo.FakeChatRepo
import com.example.chatapp.repo.abstraction.ChatRepo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class MessagesFragmentViewModelTest {


    private lateinit var viewModel: MessagesFragmentViewModel


    private lateinit var repo: ChatRepo

    @Before
    fun setup() {
        repo = FakeChatRepo()
        viewModel = MessagesFragmentViewModel(repo)
    }

    @Test
    fun extractOwnNameTest() {
        val list = mutableListOf<String>()
        list.add("Name1")
        list.add("Name2")
        list.add("Name3")
        list.add("Name4")
        list.add("Name5")
        val extractedList = viewModel.extractOwnName(list, "Name1")
        assertThat(!extractedList.contains("Name1"), `is`(true))
    }

    @Test
    fun getTypingInfoString_WithSingleItemList_ReturnsSingularFormResource() {
        val list = mutableListOf<String>()
        list.add("Name")
        val resource = viewModel.getTypingInfoString(list)
        assertThat(resource, `is`(R.string.typingInfo))
    }

    @Test
    fun getTypingInfoString_WithMultipleItemsList_ReturnsPluralFormResource() {
        val list = mutableListOf<String>()
        list.add("Name")
        list.add("Another Name")
        val resource = viewModel.getTypingInfoString(list)
        assertThat(resource, `is`(R.string.typingInfoPlural))
    }

    @Test
    fun extractFirstThreeAndReturnTest() {
        val list = mutableListOf<String>()
        list.add("Name1")
        list.add("Name2")
        list.add("Name3")
        list.add("Name4")
        list.add("Name5")
        list.add("Name6")
        list.add("Name7")
        list.add("Name8")
        list.add("Name9")
        val string = viewModel.extractFirstThreeAndReturn(list)
        assertThat(string, `is`("Name1, Name2, Name3, others"))
    }


}