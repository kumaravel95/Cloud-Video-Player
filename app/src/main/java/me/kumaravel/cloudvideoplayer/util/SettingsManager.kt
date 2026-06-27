package me.kumaravel.cloudvideoplayer.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsManager(private val context: Context) {
    private val CONFIG_URL_KEY = stringPreferencesKey("movies_config_url")

    // Default to your current hardcoded URL
    val configUrl: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[CONFIG_URL_KEY] ?: Constants.MOVIES_CONFIG_URL
    }

    suspend fun saveConfigUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[CONFIG_URL_KEY] = url
        }
    }
}