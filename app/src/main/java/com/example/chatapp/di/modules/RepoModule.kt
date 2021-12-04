package com.example.chatapp.di.modules

import com.example.chatapp.repo.abstraction.AuthRepo
import com.example.chatapp.repo.abstraction.ChatRepo
import com.example.chatapp.repo.abstraction.UserRepo
import com.example.chatapp.repo.implementation.AuthRepoImpl
import com.example.chatapp.repo.implementation.ChatRepoImpl
import com.example.chatapp.repo.implementation.UserRepoImpl
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
    abstract fun bindAuthRepo(authRepoImpl: AuthRepoImpl): AuthRepo

    @Binds
    abstract fun bindUserRepo(userRepoImpl: UserRepoImpl): UserRepo

}