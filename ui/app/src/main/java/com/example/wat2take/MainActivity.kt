package com.example.wat2take

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wat2take.navigation.BottomNav
import com.example.wat2take.ui.theme.Wat2TakeTheme
import com.example.wat2take.viewmodels.AppDataStore
import com.example.wat2take.welcome.WelcomeCourses
import com.example.wat2take.welcome.WelcomePage
import com.example.wat2take.welcome.WelcomeRecs
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
    val dataStore = AppDataStore(context)
    val appStartDestination = dataStore
        .getAppStartDestination
        .collectAsState(initial = AppDataStore.TUTORIAL_APP_START_DESTINATION).value

    LaunchedEffect(Unit, block = {
        Log.i("Launched Effect", "called")
        dataStore.resetNetworkData()
    })
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNav(navController = navController)}
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)){
            NavHost(navController = navController, startDestination = appStartDestination) {
                composable("welcomePage") { WelcomePage(navController) }
                composable("welcomeUpload") { WelcomeUpload(navController) }
                composable("welcomeCourses") { WelcomeCourses(navController) }
                composable("welcomeRecs") { WelcomeRecs(navController) }

                // Main Pages
                composable("myCourses") { MyCoursesList(navController) }
                composable("uploadTranscript") { UploadTranscript(navController) }
                composable("courseRecs") { MyCourseRecs(navController = navController) }
            }
        }
    }
}
