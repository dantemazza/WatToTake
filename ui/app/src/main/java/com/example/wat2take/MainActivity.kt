package com.example.wat2take

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wat2take.Global.Companion.START_DESTINATION
import com.example.wat2take.ui.theme.Wat2TakeTheme
import com.example.wat2take.welcome.WelcomeCourses
import com.example.wat2take.welcome.WelcomePage
import com.example.wat2take.welcome.WelcomeUpload

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Wat2TakeTheme {
                Main()
            }
        }
    }
}

@Composable
fun Main() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        if (START_DESTINATION == "welcomePage") {
            // Tutorial
            composable("welcomePage") { WelcomePage(navController) }
            composable("welcomeUpload") { WelcomeUpload(navController) }
            composable("welcomeCourses") { WelcomeCourses(navController) }
        }
        // Main Pages
        composable("home") { Home(navController) }
        composable("myCourses") { MyCoursesList(navController)}
        composable("uploadTranscript") { UploadTranscript(navController) }
        composable("courseRecs") { MyCourseRecs(navController = navController)}
    }
}
