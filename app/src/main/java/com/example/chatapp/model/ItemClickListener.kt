package com.example.chatapp.model

interface ItemClickListener<T> {

    fun onItemClick(item:T)
}

interface OnItemLongClickListener<T>{

    fun onItemLongClickListener(item:T)
}