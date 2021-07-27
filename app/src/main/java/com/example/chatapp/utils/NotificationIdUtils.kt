package com.example.chatapp.utils

import java.util.concurrent.atomic.AtomicInteger

fun getId():Int{
    val id = AtomicInteger(0)
    return id.incrementAndGet()
}