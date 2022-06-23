package com.example.wat2take.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.wat2take.CourseList
import kotlinx.coroutines.flow.map

val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "COURSELIST")
class ImplRepository(private val context: Context) : Abstract {
    companion object{
        val COURSES = stringPreferencesKey("COURSES")
    }

    override suspend fun saveCourseList(courseList: CourseList) {
        context.dataStore.edit { courseLists ->
            courseLists[COURSES] = courseList.courses
        }

    }

    override suspend fun getCourseList() = context.dataStore.data.map { courseList ->
        CourseList(
            courses = courseList[COURSES]!!
        )
    }

}