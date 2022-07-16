package com.example.wat2take.data

data class AcquiredCourse(
    val Course: String,
    val Grade: Double,
    val Description: String
)

data class RecommendationGroup(
    val Requirement_Name : String,
    val Courses: List<String>,
    val Num_Requirements: Number
)
