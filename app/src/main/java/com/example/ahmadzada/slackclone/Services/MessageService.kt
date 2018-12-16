package com.example.ahmadzada.slackclone.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.ahmadzada.slackclone.Models.Channel
import com.example.ahmadzada.slackclone.Utilities.GET_CHANNELS_URL
import org.json.JSONException

object MessageService {

    val channels = ArrayList<Channel>()

    fun append(channel: Channel) {
        this.channels.add(channel)
    }

    fun findAllChannels(context: Context, complete: (Boolean) -> Unit) {
        val channelRequest = object : JsonArrayRequest(Method.GET, GET_CHANNELS_URL, null, Response.Listener { response ->
            for (x in 0 until response.length()) {
                try {
                    val channelJson = response.getJSONObject(x)
                    val channelName = channelJson.getString("name")
                    val channelDescription = channelJson.getString("description")
                    val channelId = channelJson.getString("_id")

                    val channel = Channel(channelId, channelName, channelDescription)
                    this.append(channel)

                    complete(true)
                } catch (e: JSONException) {
                    Log.d("JSON", "EXC: " + e.toString())
                    complete(false)
                }
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR", error.toString())
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map.put("Authorization", "Bearer ${AuthService.authToken}")
                return map
            }
        }

        Volley.newRequestQueue(context).add(channelRequest)
    }
}