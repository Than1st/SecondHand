package com.group4.secondhand.di

import com.group4.secondhand.data.Repository
import com.group4.secondhand.data.api.ApiHelper
import com.group4.secondhand.data.database.DbHelper
import com.group4.secondhand.data.database.MyDatabase
import com.group4.secondhand.data.datastore.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @ViewModelScoped
    @Provides
    fun provideRepositoryy(
        apiHelper: ApiHelper,
        userPreferences: UserPreferences,
        dbHelper: DbHelper,
        database: MyDatabase
    ) = Repository(apiHelper, userPreferences,dbHelper,database)
}