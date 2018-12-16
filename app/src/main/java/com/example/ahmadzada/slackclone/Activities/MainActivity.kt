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
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import com.example.ahmadzada.slackclone.Models.Channel
import com.example.ahmadzada.slackclone.R
import com.example.ahmadzada.slackclone.Services.AuthService
import com.example.ahmadzada.slackclone.Services.MessageService
import com.example.ahmadzada.slackclone.Services.UserDataService
import com.example.ahmadzada.slackclone.Utilities.BROADCAST_USER_CHANGE_INTENT
import com.example.ahmadzada.slackclone.Utilities.SOCKET_URL
import io.socket.client.IO
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

    val socket = IO.socket(SOCKET_URL)
    lateinit var channelsAdapter: ArrayAdapter<Channel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        this.socket.connect()
        this.socket.on("channelCreated", this.newChannelListener)

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

        this.setupAdapter()
    }

    override fun onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(this.userDataChangeReceiver, IntentFilter(BROADCAST_USER_CHANGE_INTENT))
        super.onResume()
    }

    override fun onDestroy() {
        this.socket.disconnect()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.userDataChangeReceiver)
        super.onDestroy()
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

                MessageService.findAllChannels(this@MainActivity) { completed ->
                    if (completed) {
                        this@MainActivity.channelsAdapter.notifyDataSetChanged()
                    }
                }
            } else {
                this@MainActivity.userImageNavHeader.setImageResource(R.drawable.profiledefault)
                this@MainActivity.userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
                this@MainActivity.loginBtnNavHeader.text = "Login"
            }
        }
    }
    
    private val newChannelListener = Emitter.Listener { args ->
        runOnUiThread {
            val channelName = args[0] as String
            val channelDescription = args[1] as String
            val channelId = args[2] as String

            val newChannel = Channel(channelId, channelName, channelDescription)
            MessageService.append(newChannel)
            this@MainActivity.channelsAdapter.notifyDataSetChanged()
        }
    }

    fun setupAdapter() {
        this.channelsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, MessageService.channels)
        this.channels_list.adapter = this.channelsAdapter
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
            val userChangedIntent = Intent(BROADCAST_USER_CHANGE_INTENT)
            LocalBroadcastManager.getInstance(this).sendBroadcast(userChangedIntent)
        } else {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    fun addChannelBtnClick(v: View) {
        if (AuthService.isLoggedIn) {
            val dialogBuilder = AlertDialog.Builder(this)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.add_channel_dialog, null)

            dialogBuilder.setView(dialogView)
                    .setPositiveButton("Add") { dialogInterface, i ->
                        val channelNameTextField = dialogView.findViewById<EditText>(R.id.addChannelNameTextField)
                        val channelDescTextField = dialogView.findViewById<EditText>(R.id.addChannelDescTextField)
                        val channelName = channelNameTextField.text.toString()
                        val channelDesc = channelDescTextField.text.toString()

                        this@MainActivity.socket.emit("newChannel", channelName, channelDesc)
                    }
                    .setNegativeButton("Cancel") { dialogInterface, i -> }
                    .show()
        }
    }

    fun sendMessageBtnClick(v: View) {
        this.hideKeyboard()
    }

    fun hideKeyboard() {
        val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(this.currentFocus.windowToken, 0)
        }
    }
}
