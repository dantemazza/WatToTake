package com.example.wat2take

import android.app.Application

class Global: Application() {
    companion object {
        var APP_NAME = "Wat2Take"
        var START_DESTINATION = "home" // "home" for home and "welcomePage" for welcome
    }
}