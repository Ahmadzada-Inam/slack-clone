package com.example.ahmadzada.slackclone.Activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.ahmadzada.slackclone.R
import com.example.ahmadzada.slackclone.Services.AuthService
import com.example.ahmadzada.slackclone.Services.UserDataService
import com.example.ahmadzada.slackclone.Utilities.BROADCAST_USER_CHANGE_INTENT
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = object : ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)

                val moveFactor = channels_list.width * slideOffset
                main_view.translationX = moveFactor
            }
        }

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        LocalBroadcastManager.getInstance(this).registerReceiver(this.userDataChangeReceiver, IntentFilter(BROADCAST_USER_CHANGE_INTENT))
    }

    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            this@MainActivity.userNameNavHeader.text = UserDataService.name
            this@MainActivity.userEmailNavHeader.text = UserDataService.email
            if (AuthService.isLoggedIn) {
                val resourceId = this@MainActivity.resources.getIdentifier(UserDataService.avatarName, "drawable", this@MainActivity.packageName)
                this@MainActivity.userImageNavHeader.setImageResource(resourceId)
                this@MainActivity.userImageNavHeader.setBackgroundColor(UserDataService.parsedAvatarColor())
                this@MainActivity.loginBtnNavHeader.text = "Logout"
            } else {
                this@MainActivity.userImageNavHeader.setImageResource(R.drawable.profiledefault)
                this@MainActivity.userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
                this@MainActivity.loginBtnNavHeader.text = "Login"
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun loginBtnNavHeaderClick(v: View) {
        if (AuthService.isLoggedIn) {
            UserDataService.logout()
            LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(BROADCAST_USER_CHANGE_INTENT))
        } else {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    fun addChannelBtnClick(v: View) {

    }

    fun sendMessageBtnClick(v: View) {

    }
}
