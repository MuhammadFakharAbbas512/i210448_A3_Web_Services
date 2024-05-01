package com.muhammadfakharabbas.i210448
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class pg5forgotpass : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pg5forgotpass)

        val login_btn = findViewById<TextView>(R.id.login_btn)
        val send_btn = findViewById<Button>(R.id.send_btn)
        val back_btn = findViewById<Button>(R.id.back_btn)
        val emailEditText = findViewById<EditText>(R.id.email)

        send_btn.setOnClickListener {
            val email = emailEditText.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Email field cannot be empty.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Replace with the URL of your forgot password PHP script
            val forgotPasswordUrl = "http://192.168.1.6/smd/forgotpassword.php"

            val emailJson = JSONObject()
            emailJson.put("email", email)
            val requestQueue: RequestQueue = Volley.newRequestQueue(this)
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, forgotPasswordUrl, emailJson,
                Response.Listener { response ->
                    val success = response.getBoolean("success")
                    val message = response.getString("message")
                    if (success) {
                        // Email exists, proceed to password reset activity
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, pg6resetpass::class.java)
                        intent.putExtra("email", email)
                        startActivity(intent)
                    } else {
                        // Email does not exist
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )

            // Add the request to the RequestQueue
            requestQueue.add(jsonObjectRequest)
        }

        login_btn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        back_btn.setOnClickListener {
            finish() // Go back to the previous activity
        }
    }
}
