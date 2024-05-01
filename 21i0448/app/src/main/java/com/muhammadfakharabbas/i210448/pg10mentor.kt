package com.muhammadfakharabbas.i210448

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONException

class pg10mentor : AppCompatActivity() {
    private lateinit var name: TextView
    private lateinit var desc: TextView
    private lateinit var img: ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pg10mentorpg)
        var back_btn = findViewById<Button>(R.id.back_btn)
        var review_btn = findViewById<Button>(R.id.review_btn)
        var join_btn = findViewById<Button>(R.id.community_btn)
        var booksession_btn = findViewById<Button>(R.id.booksession_btn)

         name =  findViewById<TextView>(R.id.mentorname)
         img =  findViewById<ImageView>(R.id.mentorimg)
         desc =  findViewById<TextView>(R.id.mentordesc)
        var rate = findViewById<TextView>(R.id.mentorrate)


        val intent = intent
        val mid = intent.getStringExtra("mid")
        Toast.makeText(this, "mid: $mid", Toast.LENGTH_LONG).show()

        // Retrieve mentor details from the database using the mentor ID
        getMentorDetails(mid)

        back_btn.setOnClickListener{
            val intent = Intent(this, pg7home::class.java)
            intent.putExtra("mid",mid)
            startActivity(intent)
        }
        review_btn.setOnClickListener{
            val intent = Intent(this, pg11review::class.java)
            intent.putExtra("mid",mid)
            startActivity(intent)
        }
        join_btn.setOnClickListener{
            val intent = Intent(this, pg16chat_community::class.java) //pg16
            intent.putExtra("mid",mid)
            startActivity(intent)
        }
        booksession_btn.setOnClickListener{
            val intent = Intent(this, pg13calendar::class.java)
            intent.putExtra("mid",mid)
            startActivity(intent)
        }



    }

    private fun getMentorDetails(mid: String?) {
        val mentorDetailsUrl = "http://192.168.1.6/smd/search_result.php?mid=$mid" // Replace with actual URL

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, mentorDetailsUrl, null,
            Response.Listener { response ->
                try {
                    // Check for errors in the response
                    if (response.getJSONObject(0).has("error")) {
                        val errorMessage = response.getJSONObject(0).getString("error")
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    } else {
                        // Parse mentor details from JSON response
                        val mentorJson = response.getJSONObject(0)
                        val mentorName = mentorJson.getString("name")
                        val mentorDesc = mentorJson.getString("description")
                        val mentorImg = mentorJson.getString("dpUrl")

                        // Set mentor data to the corresponding views
                        name.text = mentorName
                        desc.text = mentorDesc

                        // Load mentor image using Glide or any other image loading library
                        if (!mentorImg.isNullOrEmpty()) {
                            Glide.with(this)
                                .load(mentorImg)
                                .placeholder(R.drawable.profile_image) // Placeholder image while loading
                                .error(R.drawable.profile_image) // Error image if loading fails
                                .into(img)
                        }
                    }
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