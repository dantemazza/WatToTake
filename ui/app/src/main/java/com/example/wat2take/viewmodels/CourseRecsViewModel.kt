package com.example.wat2take.viewmodels

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CourseRecsViewModel(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences>
            by preferencesDataStore("courseRecsList")
        const val DEFAULT_COURSE_RECS_VAL = "[]"
        val MY_COURSE_RECS_KEY = stringPreferencesKey("courseRecs")
    }

    val getCourseRecsList: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[MY_COURSE_RECS_KEY] ?: DEFAULT_COURSE_RECS_VAL
        }

    suspend fun saveCoursRecsList(courseRecsJson: String) {
        context.dataStore.edit {
            it.remove(MY_COURSE_RECS_KEY)
        }
        context.dataStore.edit { preferences ->
            preferences[MY_COURSE_RECS_KEY] = courseRecsJson
        }
    }

    suspend fun clearCourseRecs(){
        context.dataStore.edit {
            it.remove(MY_COURSE_RECS_KEY)
        }
    }
}