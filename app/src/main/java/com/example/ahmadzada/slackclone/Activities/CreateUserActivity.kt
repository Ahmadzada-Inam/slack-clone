package com.example.ahmadzada.slackclone.Activities

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.Toast
import com.example.ahmadzada.slackclone.R
import com.example.ahmadzada.slackclone.Services.AuthService
import com.example.ahmadzada.slackclone.Utilities.BROADCAST_USER_CHANGE_INTENT
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5]"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        this.createSpinner.visibility = View.INVISIBLE
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

        val userName = this.createUserNameTextField.text.toString()
        val userEmail = this.createEmailField.text.toString()
        val password = this.createPasswordTextField.text.toString()

        this.enableSpinner(true)

        if (userName.isNotEmpty() && userEmail.isNotEmpty() && password.isNotEmpty()) {
            AuthService.registerUser(userEmail, password) { registerSuccess ->
                if (registerSuccess) {
                    AuthService.loginUser(userEmail, password) { loginSuccess ->
                        if (loginSuccess) {
                            AuthService.createUser(userName, userEmail, this.userAvatar, this.avatarColor) { createSuccess ->
                                if (createSuccess) {
                                    val userChangedIntent = Intent(BROADCAST_USER_CHANGE_INTENT)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userChangedIntent)

                                    this.enableSpinner(false)
                                    finish()
                                } else {
                                    this.errorToast("Oops, Something went wrong, Pleae try again.")
                                }
                            }
                        } else {
                            this.errorToast("Oops, Something went wrong, Pleae try again.")
                        }
                    }
                } else {
                    this.errorToast("Oops, Something went wrong, Pleae try again.")
                }
            }
        } else {
            this.errorToast("Please fill in all the fields.")
        }
    }

    fun errorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        this.enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean) {

        this.createSpinner.visibility = if (enable) View.VISIBLE else View.INVISIBLE

        this.createProfileImageView.isEnabled = !enable
        this.createGenerateColorBtn.isEnabled = !enable
        this.createCreateUserBtn.isEnabled = !enable
    }
}
