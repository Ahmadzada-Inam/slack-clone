package com.example.ahmadzada.slackclone.Utilities

import android.content.Context
import android.content.SharedPreferences
import com.android.volley.toolbox.Volley

class SharedPrefs(context: Context) {

    val PREFS_FILENAME = "prefs"
    val prefs :SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    val IS_LOGGED_IN = "isLoggedIn"
    val USER_EMAIL = "userEmail"
    val AUTH_TOKEN = "authToken"

    var isLoggedIn: Boolean
        get() = this.prefs.getBoolean(this.IS_LOGGED_IN, false)
        set(value) = this.prefs.edit().putBoolean(this.IS_LOGGED_IN, value).apply()

    var userEmail: String
        get() = this.prefs.getString(this.USER_EMAIL, "")
        set(value) = this.prefs.edit().putString(this.USER_EMAIL, value).apply()

    var authToken: String
        get() = this.prefs.getString(this.AUTH_TOKEN, "")
        set(value) = this.prefs.edit().putString(this.AUTH_TOKEN, value).apply()

    val requestQueue = Volley.newRequestQueue(context)
}