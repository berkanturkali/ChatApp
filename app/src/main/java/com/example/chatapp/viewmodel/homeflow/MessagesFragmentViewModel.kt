package com.example.chatapp.viewmodel.homeflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.R
import com.example.chatapp.model.Message
import com.example.chatapp.repo.abstraction.ChatRepo
import com.example.chatapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MessagesFragmentViewMod"

@HiltViewModel
class MessagesFragmentViewModel @Inject constructor(
    private val repo: ChatRepo,
) : ViewModel() {

    private val _history = MutableLiveData<Resource<List<Message.TextMessage>>>()

    val history:LiveData<Resource<List<Message.TextMessage>>> get() = _history

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
        val it = firstThree.iterator()
        if(!it.hasNext()){
            return ""
        }
        val sb = StringBuilder()
        sb.append(it.next())
        while (it.hasNext()){
            sb.append(", ")
            sb.append(it.next())
        }
        return sb.toString()
    }

    fun getHistory(room:String){
        viewModelScope.launch(Dispatchers.Main) {
            _history.value = repo.getHistory(room)
        }
    }
}