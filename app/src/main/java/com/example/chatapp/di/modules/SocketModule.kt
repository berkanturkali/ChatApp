package com.example.chatapp.di.modules

import com.example.chatapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SocketModule {

    @Singleton
    @Provides
    fun provideSocket(): Socket {
        return IO.socket(Constants.SOCKET_URL)
    }
}