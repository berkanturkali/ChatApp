package com.example.chatapp.viewmodel.homeflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.di.qualifiers.ChatRepoQ
import com.example.chatapp.model.Room
import com.example.chatapp.repo.ChatRepo
import com.example.chatapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentRoomsViewModel @Inject constructor(
    @ChatRepoQ private val repo: ChatRepo
) : ViewModel() {

    private val _rooms = MutableLiveData<Resource<List<Room>>>()
    val rooms: LiveData<Resource<List<Room>>> get() = _rooms

    fun getRooms() {
        viewModelScope.launch(Dispatchers.Main) {
            _rooms.value = repo.getRooms()
        }
    }
}