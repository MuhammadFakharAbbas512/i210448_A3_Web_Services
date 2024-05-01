package com.muhammadfakharabbas.i210448

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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
import org.json.JSONObject

class pg8find : AppCompatActivity() {

    private lateinit var mentorAdapter: MentorAdapter
    private lateinit var mentorRecyclerView: RecyclerView
    private lateinit var mentorList: ArrayList<MentorModel>

    private lateinit var msg: String
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pg8find)
        mentorRecyclerView = findViewById(R.id.search_rv)
        mentorList = ArrayList()
        mentorAdapter = MentorAdapter(this, mentorList)
        mentorRecyclerView.adapter = mentorAdapter
        mentorRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        getMentorsList()

        var home_btn = findViewById<ImageView>(R.id.home_img)
        var search_btn = findViewById<ImageView>(R.id.search_img)
        var add_btn = findViewById<ImageView>(R.id.add_img)
        var chat_btn = findViewById<ImageView>(R.id.chat_img)
        var profile_btn = findViewById<ImageView>(R.id.profile_img)

        var search = findViewById<ImageView>(R.id.search)
        var rv = findViewById<RecyclerView>(R.id.search_rv)
        var searchInput = findViewById<EditText>(R.id.search_edtxt)
        searchInput.requestFocus()
        var back_btn = findViewById<Button>(R.id.back_btn)


        var img1 = findViewById<ImageView>(R.id.img1)
        var img2 = findViewById<ImageView>(R.id.img2)
        var img3 = findViewById<ImageView>(R.id.img3)
        var img11 = findViewById<ImageView>(R.id.img11)
        var img22 = findViewById<ImageView>(R.id.img22)
        var img33 = findViewById<ImageView>(R.id.img33)

        search.setOnClickListener{

            var str = searchInput.text.toString()
            if (str.isEmpty() || str.length <3){
                Toast.makeText(this, "Field cannot be empty/Enter more terms", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            search(str)


        }

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
        getMentorsList()
    }

    private fun search(str: String) {
        val searchUrl = "http://192.168.1.6/smd/search_mentor.php?search=$str" // Replace with actual URL
        var mid = ""
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        // Create a JSONObject with the search term
        val jsonBody = JSONObject()
        jsonBody.put("search", str)

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, searchUrl, null,
            Response.Listener { response ->
                try {
                    // Clear existing mentor list
                    mentorList.clear()

                    // Parse the JSON response and update mentorList
                    for (i in 0 until response.length()) {

                        val mentorJson = response.getJSONObject(i)
                        mid = mentorJson.getString("mid")
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
        /*var intent = Intent(this, pg9searchresults::class.java)
        intent.putExtra(mid, str)
        startActivity(intent)*/
    }

    private fun getMentorsList() {
        val mentorsUrl = "http://192.168.1.6/smd/get_mentors.php" // Replace with actual URL

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, mentorsUrl, null,
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
            }
        )

        // Add the request to the RequestQueue
        requestQueue.add(jsonArrayRequest)
    }
}
