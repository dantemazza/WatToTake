package com.example.wat2take

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wat2take.models.AcquiredCourse
import com.example.wat2take.viewmodels.TranscriptDataStore
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.reflect.Type

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MyCoursesList(navController: NavController) {
    val context = LocalContext.current

    val dataStore = TranscriptDataStore(context)

    var courseListJson = dataStore.getCourseList.collectAsState(
        initial = TranscriptDataStore.DEFAULT_COURSES_VAL
    ).value;
    var courses = parseCourseListJSON(courseListJson)

    // Loading
    var loadingState = dataStore.getLoadingKey.collectAsState(initial = false).value
    Log.i("Loading", loadingState.toString())

        if (!loadingState) {
            if(courses.size != 0){
                Column(modifier = Modifier
                    .padding(bottom = 50.dp
                    )) {
                    Text(text = "My Courses",
                        textAlign = TextAlign.Left,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 20.dp),
                        style = MaterialTheme.typography.h5
                    )
                    Row(modifier = Modifier
                        .padding(horizontal = 24.dp)) {
                        Button(
                            onClick = {
                                GlobalScope.launch { dataStore.clearCourses() }
                            }) {
                            Text(text = "Clear courses")
                        }
                    }
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(
                            items = courses,
                            itemContent = {
                                CourseListItem(acquiredCourse = it)
                            }
                        )
                    }
                }
            }else{
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp, 0.dp, 0.dp, 0.dp)
                    ) {
                    Text(text = "Sorry, no courses stored on this device")
                    Button(onClick = {
                        navController.navigate("uploadTranscript")
                    }) {
                        Text(text = "Upload a transcript to our service")
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Text(
                        modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp),
                        text = "Please be patient as Wat2Take curates your courses...",
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
}

@Composable
fun CourseListItem(acquiredCourse: AcquiredCourse) {
    Log.i("Course", acquiredCourse.toString())
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
                Text(text = acquiredCourse.Course ?: "Course name unavailable", style = typography.h6)
                Text(text = acquiredCourse.Description ?: "Course description unavailable", style = typography.caption)
            }
            Column(modifier = Modifier
                .padding(16.dp)
                .weight(1f),
                horizontalAlignment = Alignment.End

            ) {
                Text(text = if(acquiredCourse.Grade != null)
                    "Grade: " + acquiredCourse.Grade else "Grade not available"
                , style = typography.body1)
            }
        }
    }
}

fun parseCourseListJSON(json: String): List<AcquiredCourse> {
    // Log.i("STRING: ", json)
    val gson = Gson()
    val type: Type = object : TypeToken<List<AcquiredCourse>>() {}.type
    try {
        val courseList: List<AcquiredCourse> = gson.fromJson(json, type)
        // Log.i("", courseList.toString())
        return courseList
    } catch (e: Exception) {
        Log.d("Caught Exception", e.stackTraceToString())
        return emptyList()
    }
}

fun courseListToJSON(courses: List<AcquiredCourse>): String {
    val gson = Gson()
    val type: Type = object : TypeToken<List<AcquiredCourse?>?>() {}.type
    val json = gson.toJson(courses, type)
    return json
}
