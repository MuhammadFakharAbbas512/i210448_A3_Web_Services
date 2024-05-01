package com.muhammadfakharabbas.i210448

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONException
import org.json.JSONObject

class pg11review : AppCompatActivity() {

    private lateinit var mid: String
    private lateinit var uid: String
    private lateinit var review: EditText
    private lateinit var oneStar: ImageView
    private lateinit var twoStar: ImageView
    private lateinit var threeStar: ImageView
    private lateinit var fourStar: ImageView
    private lateinit var fiveStar: ImageView
    private lateinit var name: TextView
   // private lateinit var fee: TextView
    private lateinit var img: ImageView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pg11review)

         name = findViewById<TextView>(R.id.mentorname)
         img = findViewById<ImageView>(R.id.mentorimg)
         //fee = findViewById<TextView>(R.id.mentorfee)

        var back_btn = findViewById<Button>(R.id.back_btn)

        val intent = intent

        mid = intent.getStringExtra("mid").toString()
        uid = getUserId().toString()
        Toast.makeText(this, "Uid: $uid, mid $mid", Toast.LENGTH_LONG).show()

        // Retrieve mentor details from the database using the mentor ID
        getMentorDetails(mid)

        back_btn.setOnClickListener {
            val intent = Intent(this, pg10mentor::class.java)
            startActivity(intent)
        }
        review = findViewById(R.id.review)
        oneStar = findViewById(R.id.star1)
        twoStar = findViewById(R.id.star2)
        threeStar = findViewById(R.id.star3)
        fourStar = findViewById(R.id.star4)
        fiveStar = findViewById(R.id.star5)

        oneStar.setOnClickListener { setStarRating(1) }
        twoStar.setOnClickListener { setStarRating(2) }
        threeStar.setOnClickListener { setStarRating(3) }
        fourStar.setOnClickListener { setStarRating(4) }
        fiveStar.setOnClickListener { setStarRating(5) }

        val feedbackBtn = findViewById<Button>(R.id.feedback_btn)
        feedbackBtn.setOnClickListener { saveReview() }


    }

    private var rating = 0

    private fun setStarRating(selectedRating: Int) {
        rating = selectedRating
        val stars = listOf(oneStar, twoStar, threeStar, fourStar, fiveStar)
        for (i in 0 until selectedRating) {
            stars[i].setImageResource(R.drawable.star_rate)
        }
        for (i in selectedRating until 5) {
            stars[i].setImageResource(R.drawable.baseline_star_outline_24)
        }
    }
    private fun saveReview() {
        // Get review text
        val reviewText = review.text.toString().trim()
        Toast.makeText(this, "review: $reviewText", Toast.LENGTH_LONG).show()

        if (reviewText.isNotEmpty()) {
            val userId = uid
            val mentorId = mid
            val rating =   rating.toString()
            Toast.makeText(this, "userid: $uid, menid $mid", Toast.LENGTH_LONG).show()
           Toast.makeText(this, "rating: $rating", Toast.LENGTH_LONG).show()

            // Define the URL of the save_review.php script
            val saveReviewUrl = "http://192.168.1.6/smd/save_review.php"

            // Create a JSONObject with review data
            val reviewData = JSONObject()
            reviewData.put("userId", userId)
            reviewData.put("mentorId", mentorId)
            reviewData.put("rating", rating)
            reviewData.put("review", reviewText)

            // Create a request queue
            val requestQueue = Volley.newRequestQueue(this)

            // Create a JsonObjectRequest with POST method
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, saveReviewUrl, reviewData,
                Response.Listener { response ->
                    try {
                        // Check for success message in the response
                        if (response.has("message")) {
                            val successMessage = response.getString("message")
                            Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show()
                            // Close the activity after saving the review
                            finish()
                        } else if (response.has("error")) {
                            // Display error message if any
                            val errorMessage = response.getString("error")
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    // Handle error
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )

            // Add the request to the request queue
            requestQueue.add(jsonObjectRequest)
        } else {
            // If review text is empty, show a message
            Toast.makeText(this, "Please enter a review", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUserId(): String? {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userId", null)
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
                        val mentorFee = mentorJson.getString("fee")
                        val mentorImg = mentorJson.getString("dpUrl")

                        // Set mentor data to the corresponding views
                        name.text = mentorName
                    //    fee.text = mentorFee

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
