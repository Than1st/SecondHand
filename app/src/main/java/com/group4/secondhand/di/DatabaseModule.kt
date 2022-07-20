package com.group4.secondhand.di

import android.content.Context
import androidx.room.Room
import com.group4.secondhand.data.database.DbHelper
import com.group4.secondhand.data.database.MyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext appContext: Context
    ): MyDatabase = Room.databaseBuilder(appContext,MyDatabase::class.java,"secondhand_db").fallbackToDestructiveMigration()
        .build()


    @Singleton
    @Provides
    fun provideDbHelper(database: MyDatabase): DbHelper {
        return DbHelper(database.productDao(),database.remoteKeysDao())
    }
}