package com.muhammadfakharabbas.i210448

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class pg7home : ScreenshotDetectionActivity() {

    private lateinit var mentorAdapter: MentorAdapter
    private lateinit var mentorRecyclerView: RecyclerView
    private lateinit var mentorList: ArrayList<MentorModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pg7home)
        var name = findViewById<TextView>(R.id.name)
        //name.setText(getIntent().getStringExtra("userName").toString())
        name.setText(intent.getStringExtra("userName"))

        mentorRecyclerView = findViewById(R.id.mentorRecyclerView)
        mentorList = ArrayList()
        mentorAdapter = MentorAdapter(this, mentorList)
        mentorRecyclerView.adapter = mentorAdapter
        mentorRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Fetch mentors from PHP web service
        fetchMentorsFromWebService()

        // Initialize UI elements and set click listeners
        initializeUI()
    }

    private fun initializeUI() {
        val bellBtn = findViewById<Button>(R.id.notification_btn)
        val logout = findViewById<Button>(R.id.logout)
        val homeBtn = findViewById<ImageView>(R.id.home_img)
        val searchBtn = findViewById<ImageView>(R.id.search_img)
        val addBtn = findViewById<ImageView>(R.id.add_img)
        val chatBtn = findViewById<ImageView>(R.id.chat_img)
        val profileBtn = findViewById<ImageView>(R.id.profile_img)

        logout.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            Toast.makeText(this, "Logged Out", Toast.LENGTH_LONG).show()
        }

        homeBtn.setOnClickListener {
            // Do nothing since we are already on the home screen

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

        bellBtn.setOnClickListener {
            val intent = Intent(this, pg24notification::class.java)
            startActivity(intent)
        }
    }

    private fun fetchMentorsFromWebService() {
        val mentorsUrl = "http://192.168.1.6/smd/get_mentors.php" // Replace with your actual URL

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, mentorsUrl, null,
            Response.Listener { response ->
                try {
                    // Clear existing mentor list
                    mentorList.clear()

                    // Parse the JSON response
                    for (i in 0 until response.length()) {
                        val mentorJson = response.getJSONObject(i)
                        val mentor = MentorModel(
                            mentorJson.getString("mid"),
                            mentorJson.getString("name"),
                            mentorJson.getString("description"),
                            mentorJson.getString("status"),
                            mentorJson.getString("dpUrl"),
                            mentorJson.getString("fee"),


                        )
                        mentorList.add(mentor)
                    }

                    // Notify the adapter of changes
                    mentorAdapter.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        // Add the request to the RequestQueue
        requestQueue.add(jsonArrayRequest)
    }
}
