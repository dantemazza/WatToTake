package com.example.wat2take

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch


@Composable
fun Home(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = TranscriptDataStore(context)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Welcome to Wat2Take!",
                fontSize = 36.sp,
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 32.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Button(onClick = {
                scope.launch {
                    dataStore.saveCourseList()
                }
                navController.navigate("uploadTranscript");
            }) {
                Text(text = "Upload my transcript", fontSize = 18.sp)
            }
            Button(onClick = {
                scope.launch {
                    dataStore.saveCourseList()
                }
                navController.navigate("myCourses");
            }) {
                Text(text = "Go to my courses", fontSize = 18.sp)
            }
        }
    }
}