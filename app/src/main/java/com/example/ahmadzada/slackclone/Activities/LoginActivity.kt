package com.example.ahmadzada.slackclone.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.Toast
import com.example.ahmadzada.slackclone.R
import com.example.ahmadzada.slackclone.Services.AuthService
import com.example.ahmadzada.slackclone.Utilities.BROADCAST_USER_CHANGE_INTENT
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginLoginBtnClick(v: View) {
        val userEmail = this.loginEmailTextField.text.toString()
        val userPassword = this.loginPasswordTextField.text.toString()

        if (userEmail.isNotEmpty() && userPassword.isNotEmpty()) {
            AuthService.loginUser(this, userEmail, userPassword) { loginSuccess ->
                if (loginSuccess) {
                    AuthService.findUserByEmail(this) { findUserSuccess ->
                        val userChangedIntent = Intent(BROADCAST_USER_CHANGE_INTENT)
                        LocalBroadcastManager.getInstance(this).sendBroadcast(userChangedIntent)
                        finish()
                    }
                }
            }
        } else {
            Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_LONG).show()
        }
    }

    fun loginSignupBtnClick(v: View) {
        val createUserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()
    }
}