package com.example.wat2take

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import kotlinx.coroutines.launch


@Composable
fun Home(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = TranscriptDataStore(context)
    Button(onClick = {
        scope.launch {
            dataStore.saveCourseList()
        }
        navController.navigate("myCourses");
    }) {
        Text(text = "Go to my courses")
    }
}