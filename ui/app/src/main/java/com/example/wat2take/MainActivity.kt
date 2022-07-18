package com.example.wat2take

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.compose.material.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wat2take.Global.Companion.START_DESTINATION
import com.example.wat2take.navigation.BottomNav
import com.example.wat2take.navigation.BottomNavItem
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
    Scaffold(
        bottomBar = { BottomNav(navController = navController)}
    ) { padding ->
        NavHost(navController = navController, startDestination = START_DESTINATION) {
            if (START_DESTINATION == "welcomePage") {
                // Tutorial
                composable("welcomePage") { WelcomePage(navController) }
                composable("welcomeUpload") { WelcomeUpload(navController) }
                composable("welcomeCourses") { WelcomeCourses(navController) }
            }
            // Main Pages
            composable("home") { Home(navController) }
            composable("myCourses") { MyCoursesList(navController) }
            composable("uploadTranscript") { UploadTranscript(navController) }
            composable("courseRecs") { MyCourseRecs(navController = navController) }
        }
    }
}
