package com.example.wat2take

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TranscriptDataStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("courseList")
        const val DEFAULT_COURSES_VAL = "[]"
        val COURSE_LIST_KEY = stringPreferencesKey("courses")
        val MY_COURSES_LOADING = booleanPreferencesKey("myCoursesLoadingPrefKey")
        val STORAGE_PERMISSIONS_GRANTED = booleanPreferencesKey("transcriptUploadStoragePermission")
    }

    suspend fun setLoadingKey(key: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[MY_COURSES_LOADING] = key
        }
    }

    val getLoadingKey = context.dataStore.data
        .map { preferences ->
            preferences[MY_COURSES_LOADING] ?: false
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

    //get course list
    val getCourseList: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[COURSE_LIST_KEY] ?: DEFAULT_COURSES_VAL;
        }

    //save course list into datastore
    suspend fun saveCourseList(courseJson: String) {
        context.dataStore.edit { it.clear() }
        context.dataStore.edit { preferences ->
            preferences[COURSE_LIST_KEY] = courseJson
        }
    }

    suspend fun clearCourses(){
        context.dataStore.edit { it.clear() }
    }
}