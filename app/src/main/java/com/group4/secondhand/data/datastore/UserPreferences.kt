package com.group4.secondhand.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import java.util.concurrent.Flow

class UserPreferences(private val context: Context) {
    companion object{
        private const val USER_PREF = "USER_PREF"
        private val ON_BOARDING_KEY = booleanPreferencesKey("ON_BOARDING_KEY")
        const val DEF_ON_BOARDING = true
        val Context.datastore by preferencesDataStore(USER_PREF)
    }

}