package com.example.wat2take

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.wat2take.ui.theme.Wat2TakeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Wat2TakeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column() {
                        Greeting("Android")
                        MyCoursesButton()
                    }
                }
            }
        }
    }
}

@Composable
fun MyCoursesButton() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = TranscriptDataStore(context)
    Button(onClick = {
        scope.launch {
            dataStore.saveCourseList()
        }
        context.startActivity(Intent(context, MyCoursesActivity::class.java))
    }) {
        Text(text = "My Courses")
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Wat2TakeTheme {
        Greeting("Android")
    }
}
