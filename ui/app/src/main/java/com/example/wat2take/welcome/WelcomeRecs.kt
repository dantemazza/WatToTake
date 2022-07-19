package com.example.wat2take.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import com.example.wat2take.viewmodels.AppDataStore
import com.example.wat2take.viewmodels.CoursesViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun WelcomeRecs(navController: NavController) {
    val context = LocalContext.current
    val dataStore = AppDataStore(context)
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
                            painter = painterResource(id = R.drawable.recommendations),
                            contentDescription = "",
                            modifier = Modifier.fillMaxHeight(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Text(
                        text = "3. Wat2Take then gives you course recommendations, it's as easy as that!",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 32.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Light
                    )
                    Button(onClick = {
                        GlobalScope.launch {
                            dataStore.setAppStartDestination(
                                AppDataStore.DEFAULT_APP_START_DESTINATION
                            )
                        }
                        navController.navigate("myCourses");
                    }) {
                        Text(text = "Finish", fontSize = 18.sp)
                    }
                }
            }
        }
    )
}