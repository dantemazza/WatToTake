package com.example.wat2take

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.compose.material.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
    val context = LocalContext.current
    val dataStore = TranscriptDataStore(context)
    val appStartDestination = dataStore
        .getAppStartDestination
        .collectAsState(initial = TranscriptDataStore.TUTORIAL_APP_START_DESTINATION).value
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNav(navController = navController)}
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)){
            NavHost(navController = navController, startDestination = appStartDestination) {
                composable("welcomePage") { WelcomePage(navController) }
                composable("welcomeUpload") { WelcomeUpload(navController) }
                composable("welcomeCourses") { WelcomeCourses(navController) }
                // Main Pages
                composable("home") { Home(navController) }
                composable("myCourses") { MyCoursesList(navController) }
                composable("uploadTranscript") { UploadTranscript(navController) }
                composable("courseRecs") { MyCourseRecs(navController = navController) }
            }
        }
    }
}
