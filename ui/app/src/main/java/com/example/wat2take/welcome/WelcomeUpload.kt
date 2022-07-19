package com.example.wat2take.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wat2take.R

@Composable
fun WelcomeUpload(navController: NavController) {
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
                    Image(
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop,
                        painter = painterResource(id = R.drawable.transcript),
                        contentDescription = ""
                    )
                    Text(
                        text = "1. Upload your downloaded transcript from Quest",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 32.dp),
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