package com.example.chatapp.viewmodel.homeflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.Room
import com.example.chatapp.repo.ChatRepoImpl
import com.example.chatapp.utils.Event
import com.example.chatapp.utils.Resource
import com.example.chatapp.utils.isValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

private const val TAG = "FragmentAddRoomViewMode"
@HiltViewModel
class FragmentAddRoomViewModel @Inject constructor(
    private val repo: ChatRepoImpl
) : ViewModel() {

    private val _isValid = MutableLiveData<Event<Boolean>>()
    val isValid: LiveData<Event<Boolean>>
        get() = _isValid

    private val _roomInfo = MutableLiveData<Event<Resource<String>>>()
    val roomInfo: LiveData<Event<Resource<String>>> get() = _roomInfo

    fun validate(room: String) {
        _isValid.value = Event(room.isValid())
    }

    fun addRoom(room: Room,body:MultipartBody.Part?) {
        viewModelScope.launch(Dispatchers.Main) {
            _roomInfo.value = Event(repo.addRoom(room,body))
        }
    }
}