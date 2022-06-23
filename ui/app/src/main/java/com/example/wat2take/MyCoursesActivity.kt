package com.example.wat2take

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.wat2take.ui.theme.Wat2TakeTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type

class MyCoursesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_courses)
        setContent {
            Wat2TakeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MyCoursesList()
                }
            }
        }
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "courses")

@Composable
fun MyCoursesList() {
    val courseListJson: String = "[{\n" +
            "  \"id\": 0,\n" +
            "  \"name\": \"ECE240\",\n" +
            "  \"grade\": 51.2,\n" +
            "  \"title\": \"Circuits 2\"\n" +
            "},\n" +
            "{\n" +
            "  \"id\": 1,\n" +
            "  \"name\": \"ECE160\",\n" +
            "  \"grade\": 62.3,\n" +
            "  \"title\": \"Electromagnetic Physics\"\n" +
            "}\n" +
            "]"
    val courses = parseCourseListJSON(courseListJson)
    val coursesBackToJson = courseListToJSON(courses)
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

@Composable
fun CourseListItem(course: Course) {
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
                Text(text = course.name, style = typography.h6)
                Text(text = course.title, style = typography.caption)
            }
            Column(modifier = Modifier
                .padding(16.dp)
                .weight(1f),
                horizontalAlignment = Alignment.End

            ) {
                Text(text = "Grade: " + course.grade, style = typography.body1)
            }
        }
    }
}

fun parseCourseListJSON(json: String): List<Course> {
    val gson = Gson()
    val type: Type = object : TypeToken<List<Course?>?>() {}.type
    val courseList: List<Course> = gson.fromJson(json, type)
    for (course in courseList) {
        Log.i("Course Details", course.name + "-" + course.title)
    }
    return courseList
}

fun courseListToJSON(courses: List<Course>): String {
    val gson = Gson()
    val type: Type = object : TypeToken<List<Course?>?>() {}.type
    val json = gson.toJson(courses, type)
    Log.i("TO JSON Result: ", json)
    return json
}

class Course(
    val id: Int,
    val name: String,
    val grade: Double,
    val title: String
)
