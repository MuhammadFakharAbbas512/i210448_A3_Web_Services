package com.muhammadfakharabbas.i210448

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONException
import org.json.JSONObject


class pg13calendar : AppCompatActivity() {
    //private lateinit var selectedTimeView: TextView
    private lateinit var name: TextView
    private lateinit var img: ImageView
    private lateinit var fee: TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pg13calander)

        var t0  = findViewById<TextView>(R.id.AM10)
        var t1  = findViewById<TextView>(R.id.AM11)
        var t2  = findViewById<TextView>(R.id.PM12)
        var t3  = findViewById<TextView>(R.id.PM1)
        var t4  = findViewById<TextView>(R.id.PM2)
        var t5  = findViewById<TextView>(R.id.PM5)

        var back_btn = findViewById<Button>(R.id.back_btn)
        var msg_btn = findViewById<ImageButton>(R.id.msg_btn)
        var call_btn = findViewById<ImageButton>(R.id.call_btn)
        var videocall_btn = findViewById<ImageButton>(R.id.videocall_btn)
        var bookappointment_btn = findViewById<Button>(R.id.bookappointment_btn)


         name =  findViewById<TextView>(R.id.mentorname)
         img =  findViewById<ImageView>(R.id.mentorimg)
         fee =  findViewById<TextView>(R.id.mentorfee)

        val intent = intent
        val mid = intent.getStringExtra("mid")
        val mentorId = intent.getStringExtra("mid")
        // Retrieve mentor details from the database using the mentor ID
        getMentorDetails(mid)
        //default setting
        var selectedTime = t1.text.toString()

        t0.setOnClickListener {
           // selectTime(t0)
            val time = listOf(t0, t1, t2, t3, t4, t5)
            for (i in 0 until 6) {
                time[i].backgroundTintList = ContextCompat.getColorStateList(this, R.color.original_background_tint)
            }
            // Set the selected time view and change its backgroundTint
            t0.backgroundTintList = ContextCompat.getColorStateList(this, R.color.selected_background_tint)
            selectedTime = t0.text.toString()
        }
        t1.setOnClickListener {
           // selectTime(t1)
            val time = listOf(t0, t1, t2, t3, t4, t5)
            for (i in 0 until 6) {
                time[i].backgroundTintList = ContextCompat.getColorStateList(this, R.color.original_background_tint)
            }
            // Set the selected time view and change its backgroundTint
            t1.backgroundTintList = ContextCompat.getColorStateList(this, R.color.selected_background_tint)
            selectedTime = t1.text.toString()
        }
        t2.setOnClickListener {
           // selectTime(t2)
            val time = listOf(t0, t1, t2, t3, t4, t5)
            for (i in 0 until 6) {
                time[i].backgroundTintList = ContextCompat.getColorStateList(this, R.color.original_background_tint)
            }
            // Set the selected time view and change its backgroundTint
            t2.backgroundTintList = ContextCompat.getColorStateList(this, R.color.selected_background_tint)
            selectedTime = t2.text.toString()
        }
        t3.setOnClickListener {
         //   selectTime(t3)
            val time = listOf(t0, t1, t2, t3, t4, t5)
            for (i in 0 until 6) {
                time[i].backgroundTintList = ContextCompat.getColorStateList(this, R.color.original_background_tint)
            }
            // Set the selected time view and change its backgroundTint
            t3.backgroundTintList = ContextCompat.getColorStateList(this, R.color.selected_background_tint)
            selectedTime = t3.text.toString()
        }
        t4.setOnClickListener {
           // selectTime(t4)
            val time = listOf(t0, t1, t2, t3, t4, t5)
            for (i in 0 until 6) {
                time[i].backgroundTintList = ContextCompat.getColorStateList(this, R.color.original_background_tint)
            }
            // Set the selected time view and change its backgroundTint
            t4.backgroundTintList = ContextCompat.getColorStateList(this, R.color.selected_background_tint)
            selectedTime = t4.text.toString()
        }
        t5.setOnClickListener {
          //  selectTime(t5)
            val time = listOf(t0, t1, t2, t3, t4, t5)
            for (i in 0 until 6) {
                time[i].backgroundTintList = ContextCompat.getColorStateList(this, R.color.original_background_tint)
            }
            // Set the selected time view and change its backgroundTint
            t5.backgroundTintList = ContextCompat.getColorStateList(this, R.color.selected_background_tint)
            selectedTime = t5.text.toString()
        }
        back_btn.setOnClickListener{
            val intent = Intent(this, pg10mentor::class.java)
            intent.putExtra("mid",mid)
            startActivity(intent)
        }
        msg_btn.setOnClickListener{
            val intent = Intent(this, pg15chat_person::class.java)
            intent.putExtra("mid",mid)
            startActivity(intent)
        }
        call_btn.setOnClickListener{
            val intent = Intent(this, pg20call::class.java)
            startActivity(intent)
        }
        videocall_btn.setOnClickListener{
            val intent = Intent(this, pg19videocall::class.java)
            startActivity(intent)
        }
        bookappointment_btn.setOnClickListener{
            // Check if a time slot is selected
            if (t1 == null || t1 == null || t2 == null || t3 == null || t4 == null || t5 == null) {
                Toast.makeText(this, "Please select a time slot", Toast.LENGTH_SHORT).show()

            } else {
                // Save booking data to the database
                saveBookingData(selectedTime,mid)
                Toast.makeText(this, "Saving time slot for booking", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun saveBookingData(selectedTime: String, mentorId: String?) {
        // Check if mentorId is not null
        if (!mentorId.isNullOrBlank()) {
            // Define the URL of the PHP script to book appointment
            val bookAppointmentUrl = "http://192.168.1.6/smd/book_appointment.php"

            // Create a StringRequest with POST method to book appointment
            val stringRequest = object : StringRequest(
                Method.POST, bookAppointmentUrl,
                Response.Listener { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        if (jsonObject.has("message")) {
                            val successMessage = jsonObject.getString("message")
                            Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show()
                        } else if (jsonObject.has("error")) {
                            val errorMessage = jsonObject.getString("error")
                            // Check if sql_error is present in the response
                            if (jsonObject.has("sql_error")) {
                                val sqlErrorMessage = jsonObject.getString("sql_error")
                                Log.e(TAG, "SQL Error: $sqlErrorMessage")
                            }
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    // Handle error
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["mentorId"] = mentorId
                    params["selectedTime"] = selectedTime
                    return params
                }
            }

            // Add the string request to the request queue
            Volley.newRequestQueue(this).add(stringRequest)
        } else {
            Toast.makeText(this, "Mentor ID is null", Toast.LENGTH_LONG).show()
            Log.e(TAG, "Mentor ID is null")
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
                        fee.text = mentorDesc

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
    companion object {
        private const val TAG = "pg13calendar"
    }
}
