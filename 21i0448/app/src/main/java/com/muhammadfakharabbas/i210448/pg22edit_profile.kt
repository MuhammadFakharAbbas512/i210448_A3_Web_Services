package com.muhammadfakharabbas.i210448

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class pg22edit_profile : AppCompatActivity() {

    private var dpUri: Uri? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            dpUri = uri
            findViewById<ImageView>(R.id.img).setImageURI(uri)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pg22editprofile)

        val dp = findViewById<ImageView>(R.id.img)
        val name = findViewById<EditText>(R.id.name)
        val email = findViewById<EditText>(R.id.email)
        val num = findViewById<EditText>(R.id.contact)
        val country = findViewById<EditText>(R.id.country)
        val city = findViewById<EditText>(R.id.city)
        var back_btn = findViewById<Button>(R.id.back_btn)
        var upload_btn = findViewById<Button>(R.id.uploadprofile_btn)

        dp.setOnClickListener {
            pickImage.launch("image/*")
        }

        back_btn.setOnClickListener {
            val intent = Intent(this, pg21profile::class.java)
            startActivity(intent)
        }

      /*  var auth = FirebaseAuth.getInstance()
        // retrieving current user data from database
        val currentUser = auth.currentUser
        val userId = currentUser?.uid
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId.toString())
*/
        val userId = getUserId()


        upload_btn.setOnClickListener {
            if (name != null && email != null && num != null && country != null && city != null) {
                val url = "http://192.168.1.6/smd/edit_profile.php"
                if (name.text.isBlank() || email.text.isBlank() || num.text.isBlank() || country.text.isBlank() || city.text.isBlank()) {
                    Toast.makeText(this@pg22edit_profile, "One or more views are null", Toast.LENGTH_SHORT).show()
                }
                // Create JSON object to hold user data
                val postData = JSONObject().apply {
                    put("userId", userId)
                    put("name", name.text.toString())
                    put("email", email.text.toString())
                    put("phone", num.text.toString())
                    put("country", country.text.toString())
                    put("city", city.text.toString())
                }
                Log.d("PostData", postData.toString())
                //      Toast.makeText(this@Signup, postData.toString(), Toast.LENGTH_SHORT).show()

                // Create a JsonObjectRequest with POST method
                val requestQueue = Volley.newRequestQueue(this)

                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.POST, url, postData,
                    Response.Listener { response ->
                        try {
                            val success = response.getBoolean("success")
                            val message = response.getString("message")
                            Log.d("MESSAGE", message.toString())

                            if (success) {
                                // Signup successful
                                //   Toast.makeText(this@Signup,"Success", Toast.LENGTH_SHORT).show()
                                Toast.makeText(this@pg22edit_profile, message, Toast.LENGTH_LONG).show()
                                val intent = Intent(this@pg22edit_profile, pg21profile::class.java)
                                intent.putExtra("phone", num.text.toString())
                                startActivity(intent)
                                finish()
                            } else {
                                // Signup failed "Failure"
                                //  Toast.makeText(this@Signup,"Failure", Toast.LENGTH_SHORT).show()
                                Toast.makeText(this@pg22edit_profile, message, Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: JSONException) {

                            Toast.makeText(this@pg22edit_profile, "Error parsing JSON response", Toast.LENGTH_SHORT).show()
                            e.printStackTrace()}
                    },
                    Response.ErrorListener { error: VolleyError ->
                        // Handle error response
                        Toast.makeText(this@pg22edit_profile, "Error: " + error.message, Toast.LENGTH_SHORT).show()
                        Log.e("Signup", "Error: " + error.message)
                    })

                // Add the request to the RequestQueue
                Volley.newRequestQueue(this@pg22edit_profile).add(jsonObjectRequest)
            }
            }
        }
    private fun getUserId(): String? {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userId", null)
        }
    }


