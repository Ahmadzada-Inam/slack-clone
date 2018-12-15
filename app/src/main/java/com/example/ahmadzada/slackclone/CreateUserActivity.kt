package com.example.ahmadzada.slackclone

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
    }

    fun createProfileImageViewClick(v: View) {
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)

        val avatarPrefix = if (color == 0) "light" else "dark"
        this.userAvatar = "$avatarPrefix$avatar"

        val resourceId = resources.getIdentifier(this.userAvatar, "drawable", packageName)
        this.createProfileImageView.setImageResource(resourceId)
    }

    fun createGenerateColorBtnClick(v: View) {
        val random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)

        this.createProfileImageView.setBackgroundColor(Color.rgb(r, g, b))

        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() / 255

        this.avatarColor = "[$savedR, $savedG, $savedB]"
    }

    fun createCreateUserBtnClick(v: View) {

    }
}
