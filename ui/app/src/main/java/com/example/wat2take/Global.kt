package com.example.wat2take

import android.app.Application
import androidx.compose.ui.graphics.Color

class Global: Application() {
    companion object {
        var APP_NAME = "Wat2Take"
        var START_DESTINATION = "myCourses" // "home" for home and "welcomePage" for welcome
        var WATERLOO_YELLOW = Color.hsl(46f, 1f, 0.65f)
    }
}