package com.muhammadfakharabbas.i210448

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class pg23bookedsession : AppCompatActivity() {

    private lateinit var adapter: MentorAdapter
    private lateinit var mentorRecyclerView: RecyclerView
    private lateinit var mentorList: ArrayList<MentorModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pg23bookedsession)

        mentorRecyclerView = findViewById(R.id.search_rv)
        mentorList = ArrayList()
        adapter = MentorAdapter(this, mentorList)
        mentorRecyclerView.adapter = adapter
        mentorRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        var back_btn = findViewById<Button>(R.id.back_btn)

        back_btn.setOnClickListener{
            val intent = Intent(this, pg21profile::class.java)
            startActivity(intent)
        }

        fetchBookedMentors()
    }

    private fun fetchBookedMentors() {
        val url = "http://192.168.1.6/smd/get_booked_mentors.php"
       // val url = "http://192.168.1.6/smd/get_mentors.php"
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    for (i in 0 until response.length()) {
                        val mentorJson = response.getJSONObject(i)
                        val mentor = MentorModel(
                            mentorJson.getString("mid"),
                            mentorJson.getString("name"),
                            mentorJson.getString("description"),
                            mentorJson.getString("dpUrl"),
                            mentorJson.getString("fee"),
                            mentorJson.getString("bookedTime")
                        )
                        mentorList.add(mentor)
                    }

                    adapter.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()

                }
            },
            Response.ErrorListener { error ->
                // Handle error
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        // Add the request to the request queue
        Volley.newRequestQueue(this).add(jsonArrayRequest)
    }
}