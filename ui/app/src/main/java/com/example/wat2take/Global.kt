package com.example.wat2take

import android.app.Application
import androidx.compose.ui.graphics.Color

class Global: Application() {
    companion object {
        var APP_NAME = "Wat2Take"
        var WATERLOO_YELLOW = Color.hsl(46f, 1f, 0.65f)
        var SERVER_URL = "https://6a30-192-159-178-206.ngrok.io/transcript"
    }
}