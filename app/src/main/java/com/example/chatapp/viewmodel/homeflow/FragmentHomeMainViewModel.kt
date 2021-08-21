package com.example.chatapp.viewmodel.homeflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.Message
import com.example.chatapp.model.User
import com.example.chatapp.repo.UserRepo
import com.example.chatapp.repo.UserRepoImpl
import com.example.chatapp.utils.Event
import com.example.chatapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentHomeMainViewModel @Inject constructor(
    private val repo: UserRepo
) : ViewModel() {

    private val _me = MutableLiveData<Resource<User>>()
    val me: LiveData<Resource<User>> get() = _me
    private val _update = MutableLiveData<Event<Boolean>>()
    val update: LiveData<Event<Boolean>> get() = _update
    private val _receiver = MutableLiveData<String>()
    val receiver: LiveData<String> get() = _receiver

    private val _message = MutableLiveData<Event<Message.TextMessage>>()
    val message:LiveData<Event<Message.TextMessage>> get() = _message


    fun getMe() {
        viewModelScope.launch(Dispatchers.Main) {
            _me.value = repo.getMe()
        }
    }

    fun updateList(update: Boolean) {
        _update.value = Event(update)
    }

    fun setReceiver(receiver: String?) {
        _receiver.value = receiver
    }
    fun setMessage(message:Message.TextMessage){
        _message.value = Event(message)
    }
}