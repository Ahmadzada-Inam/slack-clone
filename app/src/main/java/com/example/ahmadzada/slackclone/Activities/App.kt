package com.example.ahmadzada.slackclone.Activities

import android.app.Application
import com.example.ahmadzada.slackclone.Utilities.SharedPrefs

class App: Application() {

    companion object {
        lateinit var prefs: SharedPrefs
    }

    override fun onCreate() {
        prefs = SharedPrefs(applicationContext)
        super.onCreate()
    }
}