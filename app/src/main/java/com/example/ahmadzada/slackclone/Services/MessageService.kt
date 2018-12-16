package com.example.ahmadzada.slackclone.Services

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.example.ahmadzada.slackclone.Activities.App
import com.example.ahmadzada.slackclone.Models.Channel
import com.example.ahmadzada.slackclone.Models.Message
import com.example.ahmadzada.slackclone.Utilities.GET_CHANNELS_URL
import com.example.ahmadzada.slackclone.Utilities.GET_CHANNEL_MESSAGE_URL
import org.json.JSONException

object MessageService {

    val channels = ArrayList<Channel>()
    val messages = ArrayList<Message>()

    fun appendChannel(channel: Channel) {
        this.channels.add(channel)
    }

    fun appendMessage(message: Message) {
        this.messages.add(message)
    }

    fun findAllChannels(complete: (Boolean) -> Unit) {
        val channelRequest = object : JsonArrayRequest(Method.GET, GET_CHANNELS_URL, null, Response.Listener { response ->
            try {
                for (x in 0 until response.length()) {
                    val channelJson = response.getJSONObject(x)
                    val channelName = channelJson.getString("name")
                    val channelDescription = channelJson.getString("description")
                    val channelId = channelJson.getString("_id")

                    val channel = Channel(channelId, channelName, channelDescription)
                    this.appendChannel(channel)

                    complete(true)
                }
            } catch (e: JSONException) {
                Log.d("JSON", "EXC: " + e.toString())
                complete(false)
            }
        }, Response.ErrorListener { error ->
            Log.d("ERRORC", error.toString())
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map.put("Authorization", "Bearer ${App.prefs.authToken}")
                return map
            }
        }

        App.prefs.requestQueue.add(channelRequest)
    }

    fun getMessages(channelId: String, complete: (Boolean) -> Unit) {
        val messageRequest = object : JsonArrayRequest(Method.GET, "$GET_CHANNEL_MESSAGE_URL$channelId", null, Response.Listener { response ->
            this.clearMessages()
            println(response)
            try {
                for (x in 0 until response.length()) {
                    val json = response.getJSONObject(x)
                    val messageBody = json.getString("messageBody")
                    val channelId = json.getString("channelId")
                    val id = json.getString("_id")
                    val username = json.getString("userName")
                    val userAvatarName = json.getString("userAvatar")
                    val userAvatarColor = json.getString("userAvatarColor")
                    val timestamp = json.getString("timeStamp")

                    val message = Message(messageBody, username, channelId, userAvatarName, userAvatarColor, id, timestamp)
                    this.appendMessage(message)
                }
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "EXC: " + e.toString())
                complete(false)
            }
        }, Response.ErrorListener { error ->
            Log.d("ERRORM", error.toString())
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map.put("Authorization", "Bearer ${App.prefs.authToken}")
                return map
            }
        }

        App.prefs.requestQueue.add(messageRequest)
    }

    fun clearMessages() {
        this.messages.clear()
    }

    fun clearChannels() {
        this.channels.clear()
    }
}