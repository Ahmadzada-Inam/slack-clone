package com.example.ahmadzada.slackclone.Activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.ahmadzada.slackclone.R
import com.example.ahmadzada.slackclone.Services.AuthService
import com.example.ahmadzada.slackclone.Utilities.BROADCAST_USER_CHANGE_INTENT
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        this.loginSpinner.visibility = View.INVISIBLE
    }

    fun loginLoginBtnClick(v: View) {
        val userEmail = this.loginEmailTextField.text.toString()
        val userPassword = this.loginPasswordTextField.text.toString()
        this.enableSpinner(true)

        this.hideKeyboard()
        if (userEmail.isNotEmpty() && userPassword.isNotEmpty()) {
            AuthService.loginUser(this, userEmail, userPassword) { loginSuccess ->
                if (loginSuccess) {
                    AuthService.findUserByEmail(this) { findUserSuccess ->
                        if (findUserSuccess) {
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
           this.errorToast("Please fill in all the fields.")
        }
    }

    fun loginSignupBtnClick(v: View) {
        val createUserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()
    }

    fun errorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        this.enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean) {
        this.loginSpinner.visibility = if (enable) View.VISIBLE else View.INVISIBLE

        this.loginLoginBtn.isEnabled = !enable
        this.loginSignupBtn.isEnabled = !enable
    }

    fun hideKeyboard() {
        val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(this.currentFocus.windowToken, 0)
        }
    }
}