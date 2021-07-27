package com.example.chatapp.viewmodel.homeflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.Message
import com.example.chatapp.repo.ChatRepo
import com.example.chatapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentPrivateMessagesViewModel @Inject constructor(
    private val repo: ChatRepo
) : ViewModel() {

    private val _history = MutableLiveData<Resource<List<Message.TextMessage>>>()
    val history: LiveData<Resource<List<Message.TextMessage>>> get() = _history

    fun getHistory(room: String, isPrivate: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            _history.value = repo.getHistory(room, isPrivate)
        }
    }
}