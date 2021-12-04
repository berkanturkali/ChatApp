package com.example.chatapp.viewmodel.authflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.User
import com.example.chatapp.repo.abstraction.AuthRepo
import com.example.chatapp.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupFragmentViewModel @Inject constructor(
     private val repo: AuthRepo,
) : ViewModel() {

    private val _message = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>> get() = _message

    private val _signupInfo = MutableLiveData<Event<Resource<String>>>()
    val signupInfo: LiveData<Event<Resource<String>>>
        get() = _signupInfo

    fun isValid(email: String, password: String, fn: String, ln: String): Boolean {
        return if (!email.isValidEmail()) {
            _message.value = Event(InputType.EMAIL.message)
            false
        } else if (!password.isValidPassword()) {
            _message.value = Event(InputType.PASSWORD.message)
            false
        } else if (!fn.isValid()) {
            _message.value = Event(InputType.FIRSTNAME.message)
            false
        } else if (!ln.isValid()) {
            _message.value = Event(InputType.LASTNAME.message)
            false
        } else {
            true
        }
    }

    fun signup(user: User) {
        viewModelScope.launch(Dispatchers.Main) {
            _signupInfo.value = Event(repo.signup(user))
        }
    }
}