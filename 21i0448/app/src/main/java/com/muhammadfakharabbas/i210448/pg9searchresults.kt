package com.muhammadfakharabbas.i210448

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
import org.json.JSONException

class pg9searchresults : AppCompatActivity() {
    private lateinit var mentorAdapter: MentorAdapter
    private lateinit var mentorRecyclerView: RecyclerView
    private lateinit var mentorList: ArrayList<MentorModel>
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pg9searchresults)
        mentorRecyclerView = findViewById(R.id.search_rv)
        mentorList = ArrayList()
        mentorAdapter = MentorAdapter(this, mentorList)
        mentorRecyclerView.adapter = mentorAdapter
        mentorRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        var back_btn = findViewById<Button>(R.id.back_btn)
        var home_btn = findViewById<ImageView>(R.id.home_img)
        var search_btn = findViewById<ImageView>(R.id.search_img)
        var add_btn = findViewById<ImageView>(R.id.add_img)
        var chat_btn = findViewById<ImageView>(R.id.chat_img)
        var profile_btn = findViewById<ImageView>(R.id.profile_img)

        var mentorName = findViewById<TextView>(R.id.mentorname)
        var mentorDesc = findViewById<TextView>(R.id.mentordesc)
        var mentorStatus = findViewById<TextView>(R.id.mentorstatus)
        var imgUser = findViewById<ImageView>(R.id.mentorimg)
        var fee = findViewById<TextView>(R.id.mentorfee)

        var mid = intent.getStringExtra("mid").toString()

        imgUser.setOnClickListener{
            Intent(this, pg10mentor::class.java)
            intent.putExtra("mid",mid)
            startActivity(intent)
        }
        mentorName.setOnClickListener{
            Intent(this, pg10mentor::class.java)
            intent.putExtra("mid",mid)
            startActivity(intent)
        }

        back_btn.setOnClickListener{
            val intent = Intent(this, pg8find::class.java)
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

    }

    private fun getMentorDetails(mid: String) {
        val searchUrl = "http://192.168.1.6/smd/search_result.php?mid=$mid" // Replace with actual URL


        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        // Create a JSONObject with the search term

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, searchUrl, null,
            Response.Listener { response ->
                try {
                    // Clear existing mentor list
                    mentorList.clear()

                    // Parse the JSON response and update mentorList
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
                // Log.d("Error ${error.message}", error.toString())
            }
        )

        // Add the request to the RequestQueue
        requestQueue.add(jsonArrayRequest)
    }

}
