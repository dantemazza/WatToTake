package com.example.wat2take

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wat2take.data.AcquiredCourse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.reflect.Type

@Composable
fun MyCourseRecs(navController: NavController) {
    val context = LocalContext.current
    val dataStore = TranscriptDataStore(context)
    var courseListJson = dataStore.getCourseList.collectAsState(
        initial = TranscriptDataStore.DEFAULT_COURSES_VAL
    ).value
    var courses = parseCourseListJSON(courseListJson)
    Column() {
        Text(text = "Course Recommendations",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5
        )
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(
                items = courses,
                itemContent = {
                    CourseRecListItem(acquiredCourse = it)
                }
            )
        }
    }
}

@Composable
fun CourseRecListItem(acquiredCourse: AcquiredCourse) {
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
                Text(text = acquiredCourse.Course ?: "Course name unavailable", style = MaterialTheme.typography.h6)
                Text(text = acquiredCourse.Description ?: "Course description unavailable", style = MaterialTheme.typography.caption)
            }
            Column(modifier = Modifier
                .padding(16.dp)
                .weight(1f),
                horizontalAlignment = Alignment.End

            ) {
                Text(text = "Type: NSE", style = MaterialTheme.typography.body1)
            }
        }
    }
}