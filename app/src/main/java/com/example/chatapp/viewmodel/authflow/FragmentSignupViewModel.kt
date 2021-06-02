package com.example.chatapp.viewmodel.authflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.User
import com.example.chatapp.repo.UserRepo
import com.example.chatapp.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentSignupViewModel @Inject constructor(
    private val repo: UserRepo
) : ViewModel() {
    private val _isValid = MutableLiveData<Event<Boolean>>()
    val isValid: LiveData<Event<Boolean>>
        get() = _isValid

    private val _signupInfo = MutableLiveData<Event<Resource<String>>>()
    val signupInfo: LiveData<Event<Resource<String>>>
        get() = _signupInfo

    private val _interests = MutableLiveData<MutableList<String>>(mutableListOf())
    val interests: LiveData<MutableList<String>> get() = _interests

    fun validate(email: String, password: String, fn: String, ln: String) {
        _isValid.value =
            Event(email.isValidEmail() && password.isValidPassword() && fn.isValid() && ln.isValid())
    }

    fun signup(user: User) {
        viewModelScope.launch(Dispatchers.Main) {
            _signupInfo.value = Event(repo.signup(user))
        }
    }

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = value
    }

    fun addInterests(interest: String) {
        _interests.value?.add(interest)
        _interests.notifyObserver()
    }

    fun removeInterest(interest: String) {
        _interests.value?.remove(interest)
        _interests.notifyObserver()
    }
}