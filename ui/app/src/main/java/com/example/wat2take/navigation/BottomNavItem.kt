package com.example.wat2take.navigation

import androidx.annotation.DrawableRes
import com.example.wat2take.R

sealed class BottomNavItem(
    val title: String,
    @DrawableRes val icon: Int,
    val navRoute: String){

    object MyCourses : BottomNavItem("My Courses", R.drawable.ic_courses,"myCourses")
    object MyCourseRecs: BottomNavItem("My Course Recs", R.drawable.ic_course_recs,"courseRecs")
    object UploadTranscript: BottomNavItem("Upload Transcript", R.drawable.ic_transcript,"uploadTranscript")
}