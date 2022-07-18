package com.example.wat2take.welcome

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wat2take.Global
import com.example.wat2take.TranscriptDataStore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun WelcomeCourses(navController: NavController) {
    val context = LocalContext.current
    val dataStore = TranscriptDataStore(context)
    Scaffold(
        topBar = {
            WelcomeTopBar(navController = navController)
        },
        content = { padding ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "2. See your courses",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 32.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Light
                    )
                    Button(onClick = {
                        GlobalScope.launch {
                            dataStore.setAppStartDestination(
                                TranscriptDataStore.DEFAULT_APP_START_DESTINATION
                            )
                        }
                        navController.navigate("Home");
                    }) {
                        Text(text = "Finish", fontSize = 18.sp)
                    }
                }
            }
        }
    )
}