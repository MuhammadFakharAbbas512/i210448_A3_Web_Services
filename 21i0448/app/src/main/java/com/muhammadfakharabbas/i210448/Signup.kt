package com.muhammadfakharabbas.i210448

import android.annotation.SuppressLint
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

class Signup : AppCompatActivity() {


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
        var name=findViewById<EditText>(R.id.name)
        var email=findViewById<EditText>(R.id.emailet)
        var pass=findViewById<EditText>(R.id.pass)
        var num = findViewById<EditText>(R.id.num)
        var country = findViewById<EditText>(R.id.country)
        var city = findViewById<EditText>(R.id.city)
        var login_btn = findViewById<TextView>(R.id.login_btn)
        var signup_btn = findViewById<Button>(R.id.signup_btn)


        signup_btn.setOnClickListener {
            val url = "http://192.168.1.6/smd/signup.php"
            if (name.text.isBlank() || email.text.isBlank() || pass.text.isBlank() || num.text.isBlank() || country.text.isBlank() || city.text.isBlank()) {
                Toast.makeText(this@Signup, "One or more views are null", Toast.LENGTH_SHORT).show()
            }
            // Create JSON object to hold user data
            val postData = JSONObject().apply {
                put("name", name.text.toString())
                put("email", email.text.toString())
                put("password", pass.text.toString())
                put("phone", num.text.toString())
                put("country", country.text.toString())
                put("city", city.text.toString())
            }
            Log.d("PostData", postData.toString())
      //      Toast.makeText(this@Signup, postData.toString(), Toast.LENGTH_SHORT).show()

            // Create a JsonObjectRequest with POST method
            val requestQueue = Volley.newRequestQueue(this)

            val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, postData,
                Response.Listener { response ->
                    try {
                        val success = response.getBoolean("success")
                        val message = response.getString("message")
                        Log.d("MESSAGE", message.toString())

                        if (success) {
                            // Signup successful
                         //   Toast.makeText(this@Signup,"Success", Toast.LENGTH_SHORT).show()
                            Toast.makeText(this@Signup, message, Toast.LENGTH_LONG).show()
                            val intent = Intent(this@Signup, pg4Verify::class.java)
                            intent.putExtra("phone", num.text.toString())
                            startActivity(intent)
                            finish()
                        } else {
                            // Signup failed "Failure"
                          //  Toast.makeText(this@Signup,"Failure", Toast.LENGTH_SHORT).show()
                            Toast.makeText(this@Signup, message, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException) {

                        Toast.makeText(this@Signup, "Error parsing JSON response", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()}
                },
                Response.ErrorListener { error: VolleyError ->
                    // Handle error response
                    Toast.makeText(this@Signup, "Error: " + error.message, Toast.LENGTH_SHORT).show()
                    Log.e("Signup", "Error: " + error.message)
                })

            // Add the request to the RequestQueue
            Volley.newRequestQueue(this@Signup).add(jsonObjectRequest)
        }

        login_btn.setOnClickListener {
            val intent = Intent(this@Signup, Login::class.java)
            startActivity(intent)
        }
    }
}
