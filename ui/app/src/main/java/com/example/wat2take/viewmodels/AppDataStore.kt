package com.example.wat2take.viewmodels

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class AppDataStore (private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("appData")
        const val TUTORIAL_APP_START_DESTINATION = "welcomePage"
        const val DEFAULT_APP_START_DESTINATION = "myCourses"
        val NETWORK_REQUEST_LOADING = booleanPreferencesKey("appNetworkRequestLoading")
        val NETWORK_REQUEST_ERROR = stringPreferencesKey("appNetworkRequestError")
        val STORAGE_PERMISSIONS_GRANTED = booleanPreferencesKey("appStoragePermission")
        val APP_START_DESTINATION = stringPreferencesKey("appStartDestination")
    }

    suspend fun setAppStartDestination(value: String) {
        context.dataStore.edit { preferences ->
            preferences[APP_START_DESTINATION] = value
        }
    }

    val getAppStartDestination = context.dataStore.data
        .map { preferences ->
            preferences[APP_START_DESTINATION] ?: TUTORIAL_APP_START_DESTINATION
        }

    suspend fun setAppLoading(key: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NETWORK_REQUEST_LOADING] = key
        }
    }

    val getAppLoading = context.dataStore.data
        .map { preferences ->
            preferences[NETWORK_REQUEST_LOADING] ?: false
        }

    suspend fun setNetworkError(value: String) {
        context.dataStore.edit { preferences ->
            preferences[NETWORK_REQUEST_ERROR] = value
        }
    }

    val getNetworkError = context.dataStore.data
        .map { preferences ->
            preferences[NETWORK_REQUEST_ERROR]
        }

    suspend fun setStoragePermissionGranted(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[STORAGE_PERMISSIONS_GRANTED] = value
        }
    }

    val getStoragePermissionsGranted = context.dataStore.data
        .map { preferences ->
            preferences[STORAGE_PERMISSIONS_GRANTED] ?: false
        }

    suspend fun resetNetworkData(){
        context.dataStore.edit {
            it.remove(NETWORK_REQUEST_LOADING)
            it.remove(NETWORK_REQUEST_ERROR)
        }
    }
}