package com.muhammadfakharabbas.i210448

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.database.*
import org.json.JSONException


class pg14chat : AppCompatActivity() {
    private lateinit var userAdapter: UserAdapter
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>

    private lateinit var databaseReference: DatabaseReference
    private lateinit var imgProfile: ImageView
    private lateinit var username: TextView

    private lateinit var userId: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pg14chat)

        userRecyclerView = findViewById(R.id.userRecyclerView)

        userList = ArrayList()
        userAdapter = UserAdapter(this, userList)
        userRecyclerView.adapter = userAdapter
        userRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        userId = getUserId().toString()
        getUsersList()

        username =  findViewById<TextView>(R.id.john_chat_txt)
        var john_community_btn = findViewById<ImageView>(R.id.john_community_btn)
        var john_chat_btn = findViewById<ImageView>(R.id.john_chat_btn)
        var john_chat_txt = findViewById<TextView>(R.id.john_chat_txt)

        var back_btn = findViewById<Button>(R.id.back_btn)
        var home_btn = findViewById<ImageView>(R.id.home_img)
        var search_btn = findViewById<ImageView>(R.id.search_img)
        var add_btn = findViewById<ImageView>(R.id.add_img)
        var chat_btn = findViewById<ImageView>(R.id.chat_img)
        var profile_btn = findViewById<ImageView>(R.id.profile_img)
        imgProfile = findViewById(R.id.user2)

        back_btn.setOnClickListener{
            val intent = Intent(this, pg7home::class.java)
            startActivity(intent)
        }
        home_btn.setOnClickListener{
            val intent = Intent(this, pg7home::class.java)
            startActivity(intent)
        }
        search_btn.setOnClickListener{
            val intent = Intent(this, pg8find::class.java)
            startActivity(intent)
        }
        add_btn.setOnClickListener{
            val intent = Intent(this, pg12addmentor::class.java)
            startActivity(intent)
        }
        chat_btn.setOnClickListener{
            val intent = Intent(this, pg14chat::class.java)
            startActivity(intent)
        }
        profile_btn.setOnClickListener{
            val intent = Intent(this, pg21profile::class.java)
            startActivity(intent)
        }
        john_community_btn.setOnClickListener{
            val intent = Intent(this, pg16chat_community::class.java)
            startActivity(intent)
        }
        john_chat_btn.setOnClickListener{
            val intent = Intent(this, pg15chat_person::class.java)
            startActivity(intent)
        }
        john_chat_txt.setOnClickListener{
            val intent = Intent(this, pg15chat_person::class.java)
            startActivity(intent)
        }

    }


    private fun getUsersList() {
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

