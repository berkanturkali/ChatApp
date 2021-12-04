package com.example.chatapp.utils

interface UIController {

    fun showProgress(isShow:Boolean)
}

interface ItemClickListener<T>{

    fun onItemClick(item:T)
}