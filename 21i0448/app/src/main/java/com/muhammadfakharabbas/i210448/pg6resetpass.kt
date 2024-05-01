package com.muhammadfakharabbas.i210448
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class pg6resetpass : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pg6resetpass)

        val login_btn = findViewById<TextView>(R.id.login_btn)
        val reset_btn = findViewById<Button>(R.id.resetpass_btn)
        val back_btn = findViewById<Button>(R.id.back_btn)
        val pass = findViewById<TextView>(R.id.pass)

        val email = intent.getStringExtra("email")

        login_btn.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        reset_btn.setOnClickListener{
            val newPassword = pass.text.toString().trim()

            // Check if the new password is empty
            if (newPassword.isEmpty()) {
                Toast.makeText(this, "Password field cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Replace with the URL of your reset password PHP script
            val resetPasswordUrl = "http://192.168.1.6/smd/resetpassword.php"
            val emailJson = JSONObject()
            emailJson.put("email", email)
            emailJson.put("password", newPassword)
            val requestQueue: RequestQueue = Volley.newRequestQueue(this)
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, resetPasswordUrl, emailJson,
                Response.Listener { response ->
                    val success = response.getBoolean("success")
                    val message = response.getString("message")
                    if (success) {
                        // Password reset successful
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                    } else {
                        // Password reset failed
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )

            // add request to the RequestQueue
            requestQueue.add(jsonObjectRequest)
        }
        back_btn.setOnClickListener{
            finish() // Go back to the previous activity
        }
    }
}
