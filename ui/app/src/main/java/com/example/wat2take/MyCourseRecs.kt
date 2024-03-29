package com.example.wat2take

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wat2take.models.CourseRec
import com.example.wat2take.models.RecommendationGroup
import com.example.wat2take.viewmodels.AppDataStore
import com.example.wat2take.viewmodels.CourseRecsViewModel
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


@Composable
fun MyCourseRecs(navController: NavController) {
    val context = LocalContext.current

    val appDataStore = AppDataStore(context)

    val courseRecsDataStore = CourseRecsViewModel(context)
    var courseListJson = courseRecsDataStore.getCourseRecsList.collectAsState(
        initial = CourseRecsViewModel.DEFAULT_COURSE_RECS_VAL
    ).value
    var recommendationGroups = parseCourseListRecsJSON(courseListJson)

    // Convert Rec Groups into VerboseCourseRecs for our cards
    var courses = emptyList<VerboseCourseRec>()
    for( recGroup in recommendationGroups) {
        val name = recGroup.Requirement_Name
        val nums_reqs = recGroup.Num_Requirements
        for(course in recGroup.Courses) {
            val verboseCourseRec = VerboseCourseRec(
                course_code = course.course_code,
                course_title = course.course_title,
                Requirement_Name = name,
                Num_Requirements = nums_reqs
            )
            courses = courses + verboseCourseRec
        }
    }

    val loadingState = appDataStore.getAppLoading.collectAsState(initial = false).value
    Log.i("Loading", loadingState.toString())

    val error = appDataStore.getNetworkError.collectAsState(initial = null).value

    Log.i("errorState", error ?: "")

    if (!loadingState) {
        if(error != null && error != ""){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp),
                        text = "ERROR:",
                        fontSize = 28.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp),
                        text = error,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Red
                    )
                }
            }
        }else if (courses.size != 0){
            Column(modifier = Modifier
                .padding(bottom = 50.dp
                )) {
                Text(text = "Course Recommendations",
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    style = MaterialTheme.typography.h5
                )
                Text(text = "Below are your personally recommended courses!",
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 5.dp),
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.h5
                )
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(
                        items = courses,
                        itemContent = {
                            CourseRecGroupListItem(verboseCourseRec = it)
                        }
                    )
                }
            }
        } else {
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
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

data class VerboseCourseRec (
    val course_code: String,
    val course_title: String,
    val Requirement_Name : String,
    val Num_Requirements: Number
 )

@Composable
fun CourseRecGroupListItem(verboseCourseRec: VerboseCourseRec) {
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
                Text(text = verboseCourseRec.course_code, style = MaterialTheme.typography.h6, textAlign = TextAlign.Left)
            }
            Column(modifier = Modifier
                .padding(16.dp)
                .weight(2f),

            ) {
                Text(text = if (verboseCourseRec.course_title != "")
                    verboseCourseRec.course_title else "No Course Title"
                , style = MaterialTheme.typography.body2, textAlign = TextAlign.Left)
                Text(text = "${verboseCourseRec.Requirement_Name}, Num Req: ${verboseCourseRec.Num_Requirements}", style = MaterialTheme.typography.caption, textAlign = TextAlign.Left)
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
    try {
        val jelement: JsonElement = JsonParser().parse(json)
        val recGroups = jelement.asJsonArray
        Log.i("Rec Groups Json", recGroups.toString())

        // String to JSON courses
        val courseRecType: Type = object : TypeToken<List<CourseRec>>() {}.type
        for (recGroup in recGroups) {
//        Log.i("RecGroupElement", recGroup.toString())
            val recGroupObject = recGroup.asJsonObject
//        Log.i("RecGroupObject", recGroupObject.toString())
            Log.i("Require_Name to string", recGroupObject.get("Requirement_Name").asString)
            Log.i("Req_Courses",recGroupObject.get("Courses").toString() )
            val courseList: List<CourseRec> = gson.fromJson(recGroupObject.get("Courses"), courseRecType)
            Log.i("Require_Courses list", courseList.toString())
            Log.i("Require_Num int", recGroupObject.get("Num_Requirements").asInt.toString())
            val recGroupBuilder : RecommendationGroup = RecommendationGroup(
                Requirement_Name = recGroupObject.get("Requirement_Name").asString,
                Courses = courseList,
                Num_Requirements = recGroupObject.get("Num_Requirements").asInt
            )
            recGroupsReturn = recGroupsReturn + recGroupBuilder
        }
    } catch (e: JsonSyntaxException) {
        Log.d("JSON_EXCEPTION", "Something went wrong parsing JSON for CourseListRecs: ${e.stackTraceToString()}")
    }

    return recGroupsReturn
}
