package com.example.chatapp.viewmodel.homeflow

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.R
import com.example.chatapp.model.Message
import com.example.chatapp.repo.ChatRepoImpl
import com.example.chatapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentMessagesViewModel @Inject constructor(
    private val repo: ChatRepoImpl
) : ViewModel() {

    private val _history = MutableLiveData<Resource<List<Message.TextMessage>>>()
    val history: LiveData<Resource<List<Message.TextMessage>>> get() = _history

    fun getHistory(room: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _history.value = repo.getHistory(room)
        }
    }

    fun extractOwnName(nameList: List<String>, myName: String): List<String> {
        return nameList.filter {
            it != myName
        }
    }

    fun getTypingInfoString(list: List<String>): Int {
        return if (list.size == 1) {
            R.string.typingInfo
        } else {
            R.string.typingInfoPlural
        }
    }

    fun extractFirstThreeAndReturn(list: List<String>): String {
        val firstThree = list.take(3).toMutableList()
        firstThree.add(firstThree.lastIndex + 1, "others")
        return TextUtils.join(", ", firstThree)
    }
}