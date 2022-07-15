package com.example.wat2take

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wat2take.data.Course
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

@Composable
fun MyCoursesList(navController: NavController) {
    val context = LocalContext.current
    val dataStore = TranscriptDataStore(context)
    var courseListJson = dataStore.getCourseList.collectAsState(
        initial = TranscriptDataStore.DEFAULT_COURSES_VAL
    ).value;
    Log.i("CourseObj", courseListJson)
    val courses = parseCourseListJSON(courseListJson)
    if(courses != null){
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(
                items = courses,
                itemContent = {
                    CourseListItem(course = it)
                }
            )
        }
    }
}

@Composable
fun CourseListItem(course: Course) {
    Log.i("Course", course.toString())
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        Row() {
            Column (modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterVertically)
                .weight(2f)
            ) {
                Text(text = course.Course ?: "Course name unavailable", style = typography.h6)
                Text(text = course.Description ?: "Course description unavailable", style = typography.caption)
            }
            Column(modifier = Modifier
                .padding(16.dp)
                .weight(1f),
                horizontalAlignment = Alignment.End

            ) {
                Text(text = if(course.grade != null)
                    "Grade: " + course.grade else "Grade not available"
                , style = typography.body1)
            }
        }
    }
}

fun parseCourseListJSON(json: String): List<Course> {
    // Log.i("STRING: ", json)
    val gson = Gson()
    val type: Type = object : TypeToken<List<Course>>() {}.type
    val courseList: List<Course> = gson.fromJson(json, type)
    // Log.i("", courseList.toString())
    return courseList
}

fun courseListToJSON(courses: List<Course>): String {
    val gson = Gson()
    val type: Type = object : TypeToken<List<Course?>?>() {}.type
    val json = gson.toJson(courses, type)
    return json
}
