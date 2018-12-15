package com.example.ahmadzada.slackclone.Activities

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ahmadzada.slackclone.R
import com.example.ahmadzada.slackclone.Services.AuthService
import com.example.ahmadzada.slackclone.Services.UserDataService
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
        var userName = this.createUserNameTextField.text.toString()
        val userEmail = this.createEmailField.text.toString()
        val password = this.createPasswordTextField.text.toString()

        AuthService.registerUser(this, userEmail, password) { registerSuccess ->
            if (registerSuccess) {
                AuthService.loginUser(this, userEmail, password) { loginSuccess ->
                    if (loginSuccess) {
                        AuthService.createUser(this, userName, userEmail, this.userAvatar, this.avatarColor) { createSuccess ->
                            if (createSuccess) {
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }
}
