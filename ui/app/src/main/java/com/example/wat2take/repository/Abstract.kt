package com.example.wat2take.repository

import com.example.wat2take.CourseList
import kotlinx.coroutines.flow.Flow

interface Abstract {
    suspend fun saveCourseList(courseList: CourseList)
    suspend fun getCourseList(): Flow<CourseList>
}