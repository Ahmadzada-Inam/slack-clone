package com.example.ahmadzada.slackclone.Services

import android.graphics.Color
import android.support.v4.content.LocalBroadcastManager
import java.util.*

object UserDataService {

    var name = ""
    var email = ""
    var avatarName = ""
    var avatarColor = ""
    var id = ""

    fun parsedAvatarColor() : Int {
        val strippedColor = this.avatarColor.replace("[", "")
                .replace("]", "").replace(",", "")

        var r = 0
        var g = 0
        var b = 0

        val scanner = Scanner(strippedColor)
        if (scanner.hasNext()) {
            r = (scanner.nextDouble() * 255).toInt()
            g = (scanner.nextDouble() * 255).toInt()
            b = (scanner.nextDouble() * 255).toInt()
        }

        return Color.rgb(r, g, b)
    }

    fun logout() {
        this.name = ""
        this.email = ""
        this.avatarName = ""
        this.avatarColor = ""
        this.id = ""

        AuthService.userEmail = ""
        AuthService.authToken = ""
        AuthService.isLoggedIn = false
    }
}