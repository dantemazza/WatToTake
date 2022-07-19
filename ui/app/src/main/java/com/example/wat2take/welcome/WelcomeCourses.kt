package com.example.wat2take.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wat2take.R
import com.example.wat2take.viewmodels.TranscriptDataStore
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
                    Card(
                        modifier = Modifier.size(250.dp, 400.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.courses),
                            contentDescription = "",
                            modifier = Modifier.fillMaxHeight(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Text(
                        text = "2. See the courses you have already taken",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 32.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Light
                    )
                    Button(onClick = {
                        GlobalScope.launch {
                            dataStore.setAppStartDestination(
                                TranscriptDataStore.DEFAULT_APP_START_DESTINATION
                            )
                        }
                        navController.navigate("welcomeRecs");
                    }) {
                        Text(text = "Next", fontSize = 18.sp)
                    }
                }
            }
        }
    )
}