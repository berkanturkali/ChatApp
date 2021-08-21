package com.example.chatapp.viewmodel.homeflow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.filters.SmallTest
import com.example.chatapp.R
import com.example.chatapp.di.qualifiers.FakeChatRepoQ
import com.example.chatapp.repo.ChatRepo
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@SmallTest
class FragmentMessagesViewModelTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var mViewModel:FragmentMessagesViewModel


    @Inject
    @FakeChatRepoQ
    lateinit var repo:ChatRepo

    @Before
    fun setup(){
        hiltRule.inject()
        mViewModel = FragmentMessagesViewModel(repo)
    }

    @Test
    fun extractOwnNameTest(){
        val list = mutableListOf<String>()
        list.add("Name1")
        list.add("Name2")
        list.add("Name3")
        list.add("Name4")
        list.add("Name5")
        val extractedList = mViewModel.extractOwnName(list,"Name1")
        assertThat(!extractedList.contains("Name1"),`is`(true))
    }

    @Test
    fun getTypingInfoString_WithSingleItemList_ReturnsSingularFormResource(){
        val list = mutableListOf<String>()
        list.add("Name")
        val resource = mViewModel.getTypingInfoString(list)
        assertThat(resource,`is`(R.string.typingInfo))
    }

    @Test
    fun getTypingInfoString_WithMultipleItemsList_ReturnsPluralFormResource(){
        val list = mutableListOf<String>()
        list.add("Name")
        list.add("Another Name")
        val resource = mViewModel.getTypingInfoString(list)
        assertThat(resource,`is`(R.string.typingInfoPlural))
    }

    @Test
    fun extractFirstThreeAndReturnTest(){
        val list = mutableListOf<String>()
        list.add("Name1")
        list.add("Name2")
        list.add("Name3")
        list.add("Name4")
        list.add("Name5")
        val string  = mViewModel.extractFirstThreeAndReturn(list)
        assertThat(string, `is`("Name1, Name2, Name3, others"))
    }


}