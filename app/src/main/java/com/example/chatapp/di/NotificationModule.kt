package com.example.chatapp.di

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.chatapp.utils.Constants.NOTIFICATION_CHANNEL_ID
import com.example.chatapp.utils.Constants.PENDING_INTENT_REQUEST_CODE
import com.example.chatapp.view.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {


    @Provides
    @Singleton
    fun providePendingIntent(
        @ApplicationContext context: Context
    ): PendingIntent {
        return PendingIntent.getActivity(
            context, PENDING_INTENT_REQUEST_CODE,
            Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context,
        intent: PendingIntent
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).setContentIntent(intent)
    }

    @Singleton
    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

}