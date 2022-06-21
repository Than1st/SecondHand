package com.group4.secondhand.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val context: Context) {
    companion object{
        private const val USER_PREF = "USER_PREF"
        private val TOKEN_KEY = stringPreferencesKey("TOKEN_KEY")
        const val DEFAULT_TOKEN = "default_token"
        val Context.datastore by preferencesDataStore(USER_PREF)
    }

    suspend fun setToken(token: String){
        context.datastore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    fun getToken() : Flow<String> {
        return context.datastore.data.map { preferences ->
                preferences[TOKEN_KEY] ?: DEFAULT_TOKEN
        }
    }

    suspend fun deleteToken() {
        context.datastore.edit { preferences ->
           preferences.clear()
        }
    }

}