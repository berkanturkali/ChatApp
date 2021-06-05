package com.example.chatapp.viewmodel.homeflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.User
import com.example.chatapp.repo.UserRepo
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


    fun getMe() {
        viewModelScope.launch(Dispatchers.Main) {
            _me.value = repo.getMe()
        }
    }
}