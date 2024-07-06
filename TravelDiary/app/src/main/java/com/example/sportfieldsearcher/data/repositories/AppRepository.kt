package com.example.sportfieldsearcher.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.sportfieldsearcher.data.models.Theme
import kotlinx.coroutines.flow.map

class AppRepository (
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val THEME_KEY = stringPreferencesKey("theme")
        private val USER_ID_KEY = stringPreferencesKey("userId")
    }

    val theme = dataStore.data.map { preferences ->
        try {
            Theme.valueOf(preferences[THEME_KEY] ?: "System")
        } catch (_: IllegalArgumentException) {
            Theme.System
        }
    }

    val userId = dataStore.data.map { preferences ->
        try {
            preferences[USER_ID_KEY]?.toInt()
        } catch (_: NumberFormatException) {
            null
        }
    }

    suspend fun setTheme(theme: Theme) = dataStore.edit { preferences -> preferences[THEME_KEY] = theme.toString() }
    suspend fun setUserId(userId: Int?) = dataStore.edit { preferences -> preferences[USER_ID_KEY] = userId.toString() }
}