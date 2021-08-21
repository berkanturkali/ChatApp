package com.example.chatapp.viewmodel.authflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.TokenResponse
import com.example.chatapp.repo.UserRepoImpl
import com.example.chatapp.utils.Event
import com.example.chatapp.utils.Resource
import com.example.chatapp.utils.isValidEmail
import com.example.chatapp.utils.isValidPassword
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentLoginViewModel @Inject constructor(
    private val repo:UserRepoImpl
) : ViewModel() {

    private val _isValid = MutableLiveData<Event<Boolean>>()
    val isValid: LiveData<Event<Boolean>>
        get() = _isValid

    private val _loginInfo = MutableLiveData<Event<Resource<TokenResponse>>>()
    val loginInfo: LiveData<Event<Resource<TokenResponse>>>
        get() = _loginInfo

    fun validate(email: String, password: String) {
        _isValid.value = Event(email.isValidEmail() && password.isValidPassword())
    }

    fun login(credentials: JsonObject) {
        viewModelScope.launch(Dispatchers.Main) {
            _loginInfo.value = Event(repo.login(credentials))
        }
    }
}