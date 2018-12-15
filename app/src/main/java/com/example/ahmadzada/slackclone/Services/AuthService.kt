package com.example.ahmadzada.slackclone.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
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
                Log.d("EXCEPTION", e.toString())

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
}