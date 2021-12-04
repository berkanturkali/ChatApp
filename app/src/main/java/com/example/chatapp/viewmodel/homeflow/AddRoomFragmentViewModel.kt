package com.example.chatapp.viewmodel.homeflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.Room
import com.example.chatapp.repo.abstraction.ChatRepo
import com.example.chatapp.utils.Event
import com.example.chatapp.utils.InputType
import com.example.chatapp.utils.Resource
import com.example.chatapp.utils.isValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

private const val TAG = "FragmentAddRoomViewMode"

@HiltViewModel
class AddRoomFragmentViewModel @Inject constructor(
    private val repo: ChatRepo,
) : ViewModel() {

    private val _message = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>> get() = _message

    private val _roomInfo = MutableLiveData<Event<Resource<String>>>()
    val roomInfo: LiveData<Event<Resource<String>>> get() = _roomInfo

    fun isValid(room: String) =
        if (!room.isValid()) {
            _message.value = Event(InputType.ROOM.message)
            false
        } else {
            true
        }

    fun addRoom(room: Room, body: MultipartBody.Part?) {
        viewModelScope.launch(Dispatchers.Main) {
            _roomInfo.value = Event(repo.addRoom(room, body))
        }
    }
}