package com.example.ahmadzada.slackclone.Services

import com.example.ahmadzada.slackclone.Activities.App

object UserDataService {

    var name = ""
    var email = ""
    var avatarName = ""
    var avatarColor = ""
    var id = ""

    fun logout() {
        this.name = ""
        this.email = ""
        this.avatarName = ""
        this.avatarColor = ""
        this.id = ""

        App.prefs.userEmail = ""
        App.prefs.authToken = ""
        App.prefs.isLoggedIn = false

        MessageService.clearMessages()
        MessageService.clearChannels()
    }
}