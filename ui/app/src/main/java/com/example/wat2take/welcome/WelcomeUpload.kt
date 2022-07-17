package com.example.wat2take.welcome

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wat2take.Global
import com.example.wat2take.Global.Companion.APP_NAME

@Composable
fun WelcomeUpload(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = APP_NAME) },
                navigationIcon = if (navController.previousBackStackEntry != null) {
                    {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                } else null
            )
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
                        text = "1. Upload your transcript",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 32.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Light
                    )
                    Button(onClick = {
                        navController.navigate("WelcomeCourses");
                    }) {
                        Text(text = "Next", fontSize = 18.sp)
                    }
                }
            }
        }
    )
}