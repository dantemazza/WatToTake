package com.example.wat2take

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavController
import com.example.wat2take.data.AcquiredCourse
import com.example.wat2take.data.CourseRec
import com.example.wat2take.data.RecommendationGroup
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


@Composable
fun MyCourseRecs(navController: NavController) {
    val context = LocalContext.current
    val dataStore = TranscriptDataStore(context)
    var courseListJson = dataStore.getCourseRecsList.collectAsState(
        initial = TranscriptDataStore.DEFAULT_COURSES_VAL
    ).value
    var courses = parseCourseListRecsJSON(courseListJson)
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
                    CourseRecGroupListItem(recommendationGroup = it)
                }
            )
        }
    }
}

@Composable
fun CourseRecGroupListItem(recommendationGroup: RecommendationGroup) {
    Log.i("Rec Group", recommendationGroup.toString())
    Text(text = "${recommendationGroup.Requirement_Name}: Required ${recommendationGroup.Num_Requirements}",
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h3
    )
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(
            items = recommendationGroup.Courses,
            itemContent = {
                    CourseRecListItem(courseRec = it)
            }
        )
    }
}

@Composable
fun CourseRecListItem(courseRec: CourseRec) {
    Log.i("Course Rec", courseRec.toString())
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
                .weight(1f)
            ) {
                Text(text = courseRec.course_code ?: "Course code unavailable", style = MaterialTheme.typography.h6)
            }
            Column(modifier = Modifier
                .padding(16.dp)
                .weight(2f),
                horizontalAlignment = Alignment.End

            ) {
                Text(text = courseRec.course_title ?: "Course title unavailable", style = MaterialTheme.typography.body1)
            }
        }
    }
}

fun parseCourseListRecsJSON(json: String): List<RecommendationGroup> {
    // Our Return value, list we build
    var recGroupsReturn : List<RecommendationGroup> = emptyList()
    // Log.i("STRING: ", json)
    val gson = Gson()
    // Take apart rec groups first
    val jelement: JsonElement = JsonParser().parse(json)
    val recGroups = jelement.asJsonArray
    Log.i("Rec Groups Json", recGroups.toString())

    // String to JSON courses
    val courseRecType: Type = object : TypeToken<List<CourseRec>>() {}.type
    for (recGroup in recGroups) {
        val recGroupObject = recGroup.asJsonObject
        Log.i("Require_Name to string", recGroupObject.get("Requirement_Name").asString)
        val courseList: List<CourseRec> = gson.fromJson(json, courseRecType)
        Log.i("Require_Courses list", courseList.toString())
        Log.i("Require_Num int", recGroupObject.get("Num_Requirements").asInt.toString())
        val recGroupBuilder : RecommendationGroup = RecommendationGroup(
            Requirement_Name = recGroupObject.get("Requirement_Name").asString,
            Courses = courseList,
            Num_Requirements = recGroupObject.get("Num_Requirements").asInt
        )
        recGroupsReturn = recGroupsReturn + recGroupBuilder
    }

    return recGroupsReturn
}
