package com.example.chatapp.module

import com.example.chatapp.FakeChatRepo
import com.example.chatapp.di.qualifiers.FakeChatRepoQ
import com.example.chatapp.repo.ChatRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @FakeChatRepoQ
    abstract fun bindFakeChatRepo(repo:FakeChatRepo):ChatRepo
}