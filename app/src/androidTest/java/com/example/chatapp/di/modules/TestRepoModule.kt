package com.example.chatapp.di.modules

import com.example.chatapp.repo.FakeAuthRepo
import com.example.chatapp.repo.FakeChatRepo
import com.example.chatapp.repo.FakeUserRepo
import com.example.chatapp.repo.abstraction.AuthRepo
import com.example.chatapp.repo.abstraction.ChatRepo
import com.example.chatapp.repo.abstraction.UserRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepoModule::class]
)
interface TestRepoModule {

    @Binds
    fun bindFakeChatRepo(repo: FakeChatRepo): ChatRepo

    @Singleton
    @Binds
    fun bindFakeAuthRepo(repo:FakeAuthRepo):AuthRepo

    @Binds
    fun bindFakeUserRepo(repo:FakeUserRepo):UserRepo
}