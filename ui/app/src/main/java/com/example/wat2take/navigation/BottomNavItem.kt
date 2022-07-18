package com.example.wat2take.navigation

import androidx.annotation.DrawableRes
import com.example.wat2take.R

sealed class BottomNavItem(
    val title: String,
    @DrawableRes val icon: Int,
    val navRoute: String){

    object MyCourses : BottomNavItem("Courses", R.drawable.ic_courses,"myCourses")
    object MyCourseRecs: BottomNavItem("Recommendations", R.drawable.ic_course_recs,"courseRecs")
    object UploadTranscript: BottomNavItem("Upload", R.drawable.ic_transcript,"uploadTranscript")
}