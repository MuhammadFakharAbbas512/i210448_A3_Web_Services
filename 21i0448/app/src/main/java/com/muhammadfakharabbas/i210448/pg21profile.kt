package com.muhammadfakharabbas.i210448

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class pg21profile : AppCompatActivity() {
    private var userName: TextView? = null
    private var userImg: ImageView? = null
    private var userCity: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pg21myprofile)

        userName = findViewById<TextView>(R.id.userName)
        userImg = findViewById<ImageView>(R.id.img)
        userCity = findViewById<TextView>(R.id.city)
        val backBtn = findViewById<Button>(R.id.back_btn)
        var homeBtn = findViewById<ImageView>(R.id.home_img)
        var searchBtn = findViewById<ImageView>(R.id.search_img)
        var addBtn = findViewById<ImageView>(R.id.add_img)
        var chatBtn = findViewById<ImageView>(R.id.chat_img)
        var profileBtn = findViewById<ImageView>(R.id.profile_img)

        var editProfilePicBtn = findViewById<ImageView>(R.id.editprofilepic_btn)
        var editProfileBtn = findViewById<ImageView>(R.id.editprofile_btn)
        var bookedSessionsBtn = findViewById<Button>(R.id.booked_sessions)
        var johnBtn = findViewById<ImageButton>(R.id.john_btn)

        backBtn.setOnClickListener {
            val intent = Intent(this, pg7home::class.java)
            startActivity(intent)
        }

        editProfilePicBtn.setOnClickListener {
            val intent = Intent(this, pg22edit_profile::class.java)
            startActivity(intent)
        }

        editProfileBtn.setOnClickListener {
            val intent = Intent(this, pg22edit_profile::class.java)
            startActivity(intent)
        }

        bookedSessionsBtn.setOnClickListener {
            val intent = Intent(this, pg23bookedsession::class.java)
            startActivity(intent)
        }

        johnBtn.setOnClickListener {
            val intent = Intent(this, pg10mentor::class.java)
            startActivity(intent)
        }

        homeBtn.setOnClickListener {
            val intent = Intent(this, pg7home::class.java)
            startActivity(intent)
        }

        searchBtn.setOnClickListener {
            val intent = Intent(this, pg8find::class.java)
            startActivity(intent)
        }

        addBtn.setOnClickListener {
            val intent = Intent(this, pg12addmentor::class.java)
            startActivity(intent)
        }

        chatBtn.setOnClickListener {
            val intent = Intent(this, pg14chat::class.java)
            startActivity(intent)
        }

        profileBtn.setOnClickListener {
            val intent = Intent(this, pg21profile::class.java)
            startActivity(intent)
        }

        val userId = getUserId()
        if (!userId.isNullOrEmpty()) {
            fetchUserProfile(userId)
        }
    }

    private fun fetchUserProfile(userId: String) {
        val url = "http://192.168.1.6/smd/get_profile.php?userId=$userId"
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)

        Toast.makeText(this, "Fetching", Toast.LENGTH_SHORT).show()
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    if (response.length() > 0) {
                        val user = response.getJSONObject(0)
                        val name = user.getString("name")
                        val city = user.getString("city")

                        userName?.text = name
                        userCity?.text = city
                    } else {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Log.e("Volley Error", error.toString())
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        requestQueue.add(jsonArrayRequest)
    }

    private fun getUserId(): String? {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userId", null)
    }
}
