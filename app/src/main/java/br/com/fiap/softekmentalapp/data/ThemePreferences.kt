package br.com.fiap.softekmentalapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

class ThemePreferences(private val dataStore: DataStore<Preferences>) {
    companion object {
        val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
    }

    val isDarkTheme: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[DARK_THEME_KEY] ?: false
        }

    suspend fun setDarkTheme(isDarkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = isDarkTheme
        }
    }
}