package com.example.datastorage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class UserManager(private val context: Context) {

    private val Context.userStore: DataStore<Preferences> by preferencesDataStore(name = "user")

    companion object {
        val USER_NAME_KEY = stringPreferencesKey("USER_NAME")
        val USER_AGE_KEY = intPreferencesKey("USER_AGE")
        val USER_AUTHENTICATED_KEY = booleanPreferencesKey("USER_AUTHENTICATED")
    }

    suspend fun saveUserData(name: String, age: Int, authenticated: Boolean) {
        context.userStore.edit {
            it[USER_NAME_KEY] = name
            it[USER_AGE_KEY] = age
            it[USER_AUTHENTICATED_KEY] = authenticated
        }
    }

    suspend fun getUserData(): User {
        val prefs = context.userStore.data.first()

        return User(
            name = prefs[USER_NAME_KEY] ?: "",
            age = prefs[USER_AGE_KEY] ?: 0,
            authenticated = prefs[USER_AUTHENTICATED_KEY] ?: false
        )
    }

}