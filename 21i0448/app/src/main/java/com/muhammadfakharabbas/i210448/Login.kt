package com.muhammadfakharabbas.i210448

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val email = findViewById<EditText>(R.id.email)
        val pass = findViewById<EditText>(R.id.pass)
        val login_btn = findViewById<Button>(R.id.login_btn)
        val signup_btn = findViewById<TextView>(R.id.signup_btn)
        val forgotpass_btn = findViewById<TextView>(R.id.forgot_txt)

        login_btn.setOnClickListener {

            val url = "http://192.168.1.6/smd/login.php"

            // Create JSON object to hold login data
            val postData = JSONObject().apply {
                put("email", email.text.toString())
                put("password", pass.text.toString())
            }

            // Create a JsonObjectRequest with POST method
            val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, postData,
                Response.Listener { response ->
                    try {
                        val success = response.getBoolean("success")
                        val message = response.getString("message")
                        if (success) {
                            // Login successful
                            val user = response.getJSONObject("user")
                            val userId = user.getString("userId")
                            saveUserIdToPreferences(userId) // Save userId to SharedPreferences

                            val userName = user.getString("name")
                            val intent = Intent(this@Login, pg7home::class.java)
                            intent.putExtra("userName", userName)
                            startActivity(intent)
                            Toast.makeText(this@Login, "Login Successful", Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            // Login failed
                            Toast.makeText(this@Login, message, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(this@Login, "Error parsing JSON response", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error: VolleyError ->
                    // Handle error response
                    Toast.makeText(this@Login, "Error: " + error.message, Toast.LENGTH_SHORT).show()
                    Log.e("Login", "Error: " + error.message)
                })

            // Add the request to the RequestQueue
            Volley.newRequestQueue(this@Login).add(jsonObjectRequest)
        }

        signup_btn.setOnClickListener {
            val intent = Intent(this@Login, Signup::class.java)
            startActivity(intent)
        }

        forgotpass_btn.setOnClickListener {
            val intent = Intent(this@Login, pg5forgotpass::class.java)
            startActivity(intent)
        }
    }
    private fun saveUserIdToPreferences(userId: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("userId", userId).apply()
    }

}
