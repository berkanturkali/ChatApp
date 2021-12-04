package com.example.chatapp.viewmodel.authflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.TokenResponse
import com.example.chatapp.repo.abstraction.AuthRepo
import com.example.chatapp.utils.*
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginFragmentViewModel @Inject constructor(
    private val repo: AuthRepo,
    private val storageManager: StorageManager,
) : ViewModel() {

    private val _message = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>> get() = _message

    private val _loginInfo = MutableLiveData<Event<Resource<TokenResponse>>>()

    val loginInfo: LiveData<Event<Resource<TokenResponse>>>
        get() = _loginInfo

    fun isValid(email: String, password: String) =
        if (!email.isValidEmail()) {
            _message.value = Event(InputType.EMAIL.message)
            false
        } else if (!password.isValidPassword()) {
            _message.value = Event(InputType.PASSWORD.message)
            false
        } else {
            true
        }

    fun login(credentials: JsonObject) {
        viewModelScope.launch(Dispatchers.Main) {
            _loginInfo.value = Event(repo.login(credentials))
        }
    }

    fun setTokenAndUserInfo(token: String, email: String, fullname: String) {
        storageManager.setTokenAndMail(token, email, fullname)
    }
}