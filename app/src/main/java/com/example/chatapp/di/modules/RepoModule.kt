package com.example.chatapp.di.modules

import com.example.chatapp.repo.ChatRepo
import com.example.chatapp.repo.ChatRepoImpl
import com.example.chatapp.repo.UserRepo
import com.example.chatapp.repo.UserRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    abstract fun bindChatRepo(chatRepoImpl: ChatRepoImpl): ChatRepo

    @Binds
    abstract fun bindUserRepo(userRepoImpl: UserRepoImpl): UserRepo

}