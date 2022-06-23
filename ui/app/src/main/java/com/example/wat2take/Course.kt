package com.example.wat2take

data class Course(
    val id: Int,
    val name: String,
    val grade: Double,
    val title: String
)

data class CourseList(
    val courses: String
)
