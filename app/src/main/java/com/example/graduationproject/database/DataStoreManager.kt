package com.example.graduationproject

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_preferences")

class DataStoreManager(private val context: Context) {

    companion object {
        val USER_EMAIL_KEY = stringPreferencesKey("USER_EMAIL")
        val USER_PASSWORD_KEY = stringPreferencesKey("USER_PASSWORD")
        val KEEP_SIGNED_IN_KEY = booleanPreferencesKey("KEEP_SIGNED_IN")
    }

    val userEmail: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_EMAIL_KEY]
        }

    val userPassword: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_PASSWORD_KEY]
        }

    val keepSignedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[KEEP_SIGNED_IN_KEY] ?: false
        }

    suspend fun saveUserCredentials(email: String, password: String, keepSignedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL_KEY] = email
            preferences[USER_PASSWORD_KEY] = password
            preferences[KEEP_SIGNED_IN_KEY] = keepSignedIn
        }
    }

    suspend fun clearUserCredentials() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_EMAIL_KEY)
            preferences.remove(USER_PASSWORD_KEY)
            preferences.remove(KEEP_SIGNED_IN_KEY)
        }
    }
}
