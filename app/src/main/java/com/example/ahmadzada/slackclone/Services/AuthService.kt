package com.example.ahmadzada.slackclone.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ahmadzada.slackclone.Utilities.CREATE_URL
import com.example.ahmadzada.slackclone.Utilities.LOGIN_URL
import com.example.ahmadzada.slackclone.Utilities.REGISTER_URL
import org.json.JSONException
import org.json.JSONObject

object AuthService {

    var userEmail = ""
    var authToken = ""
    var isLoggedIn = false

    fun registerUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {

        val json = JSONObject()
        json.put("email", email)
        json.put("password", password)
        val requestBody = json.toString()

        val registerRequest = object : StringRequest(Method.POST, REGISTER_URL, Response.Listener {
            complete(true)
        }, Response.ErrorListener {
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        Volley.newRequestQueue(context).add(registerRequest)
    }

    fun loginUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {

        val json = JSONObject()
        json.put("email", email)
        json.put("password", password)
        val requestBody = json.toString()

        val loginRequest = object : JsonObjectRequest(Method.POST, LOGIN_URL, null, Response.Listener { response ->
            try {
                this.userEmail = response.getString("user")
                this.authToken = response.getString("token")
                this.isLoggedIn = true

                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "EXC: " + e.toString())

                complete(false)
            }
        }, Response.ErrorListener {
                complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        Volley.newRequestQueue(context).add(loginRequest)
    }

    fun createUser(context: Context, name: String, email: String, avatarName: String, avatarColor: String, complete: (Boolean) -> Unit) {

        val json = JSONObject()
        json.put("name", name)
        json.put("email", email)
        json.put("avatarName", avatarName)
        json.put("avatarColor", avatarColor)
        val requestBody = json.toString()

        val createRequest = object : JsonObjectRequest(Method.POST, CREATE_URL, null, Response.Listener { response ->
            try {
                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.id = response.getString("_id")

                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "EXC: " + e.toString())

                complete(false)
            }
        }, Response.ErrorListener {
                complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map.put("Authorization", "Bearer ${this@AuthService.authToken}")
                return map
            }
        }

        Volley.newRequestQueue(context).add(createRequest)
    }
}