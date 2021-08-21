package com.example.chatapp.di.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ChatRepoQ

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FakeChatRepoQ