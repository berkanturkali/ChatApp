package com.example.chatapp.viewmodel.homeflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.chatapp.model.User
import com.example.chatapp.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FragmentUsersViewModel @Inject constructor(repo: UserRepo) : ViewModel() {
    var users: Flow<PagingData<User>>? = repo.users().cachedIn(viewModelScope)
}