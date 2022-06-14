package com.group4.secondhand.di

import android.content.Context
import com.group4.secondhand.data.datastore.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {
    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context) = UserPreferences(context)
}