package com.example.chatapp.utils

import com.example.chatapp.viewmodel.MainActivityViewModel


inline fun <reified T> handleResource(
    activityViewModel: MainActivityViewModel? = null,
    resource: Resource<T>,
    successFunc: () -> Unit,
    errorFunc: () -> Unit
) {
    when (resource) {
        is Resource.Loading -> activityViewModel?.showProgress(true)
        is Resource.Success -> {
            activityViewModel?.showProgress(false)
            successFunc()
        }
        is Resource.Error -> {
            activityViewModel?.showProgress(false)
            errorFunc()
        }
    }
}