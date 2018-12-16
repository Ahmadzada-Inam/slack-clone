package com.example.ahmadzada.slackclone.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ahmadzada.slackclone.Activities.App
import com.example.ahmadzada.slackclone.Utilities.CREATE_URL
import com.example.ahmadzada.slackclone.Utilities.FIND_USER_URL
import com.example.ahmadzada.slackclone.Utilities.LOGIN_URL
import com.example.ahmadzada.slackclone.Utilities.REGISTER_URL
import org.json.JSONException
import org.json.JSONObject

object AuthService {

    fun registerUser(email: String, password: String, complete: (Boolean) -> Unit) {
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

        App.prefs.requestQueue.add(registerRequest)
    }

    fun loginUser(email: String, password: String, complete: (Boolean) -> Unit) {
        val json = JSONObject()
        json.put("email", email)
        json.put("password", password)
        val requestBody = json.toString()

        val loginRequest = object : JsonObjectRequest(Method.POST, LOGIN_URL, null, Response.Listener { response ->
            try {
                App.prefs.userEmail = response.getString("user")
                App.prefs.authToken = response.getString("token")
                App.prefs.isLoggedIn = true

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

        App.prefs.requestQueue.add(loginRequest)
    }

    fun createUser(name: String, email: String, avatarName: String, avatarColor: String, complete: (Boolean) -> Unit) {
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
                map.put("Authorization", "Bearer ${App.prefs.authToken}")
                return map
            }
        }

        App.prefs.requestQueue.add(createRequest)
    }

    fun findUserByEmail(complete: (Boolean) -> Unit) {
        val findUserRequest = object : JsonObjectRequest(Method.GET, "$FIND_USER_URL${App.prefs.userEmail}", null, Response.Listener { response ->
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

            override fun getHeaders(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map.put("Authorization", "Bearer ${App.prefs.authToken}")
                return map
            }
        }

        App.prefs.requestQueue.add(findUserRequest)
    }
}