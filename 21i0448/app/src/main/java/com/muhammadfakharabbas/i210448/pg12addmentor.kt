package com.muhammadfakharabbas.i210448

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.storage.StorageReference
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class pg12addmentor : AppCompatActivity() {

    private var dpUri: Uri? = null
    private var videoUri: Uri? = null

    private lateinit var dp: ImageView
    private lateinit var video: ImageView

    private lateinit var name: EditText
    private lateinit var desc: EditText
    private lateinit var fee: EditText
    private lateinit var status: EditText



    private lateinit var storageRef: StorageReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pg12addmentor)

        dp = findViewById(R.id.photo_btn)
        name = findViewById<EditText>(R.id.name)
        desc = findViewById<EditText>(R.id.desc)
        status = findViewById<EditText>(R.id.status)
        fee = findViewById<EditText>(R.id.fee)
        video = findViewById(R.id.video_btn)

        var back_btn = findViewById<Button>(R.id.back_btn)
        var upload_btn = findViewById<Button>(R.id.upload_btn)
        var video_btn1 = findViewById<Button>(R.id.video_btn2)
        var photo_btn1 = findViewById<Button>(R.id.photo_btn2)


        var home_btn = findViewById<ImageView>(R.id.home_img)
        var search_btn = findViewById<ImageView>(R.id.search_img)
        var add_btn = findViewById<ImageView>(R.id.add_img)
        var chat_btn = findViewById<ImageView>(R.id.chat_img)
        var profile_btn = findViewById<ImageView>(R.id.profile_img)


        // Other code...

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

        photo_btn1.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_REQUEST_CODE)
        }

        video_btn1.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            startActivityForResult(intent, VIDEO_REQUEST_CODE)
        }

        upload_btn.setOnClickListener {
         saveMentor()
        }
        // Other code...
    }

    private fun saveMentor() {
        val mentorName = name.text.toString()
        val mentorDesc = desc.text.toString()
        val mentorStatus = status.text.toString()
        val mentorFee = fee.text.toString()
        val dpUrl = dpUri.toString()

        //  image to a Base64 string
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, dpUri)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT)


        // Check if mentor name, description, and status are not empty
        if (mentorName.isNotEmpty() && mentorDesc.isNotEmpty() && mentorStatus.isNotEmpty()) {
            // Define the URL of the PHP script to save mentor data
            val saveMentorUrl = "http://192.168.1.6/smd/save_mentor.php"

            // Create a StringRequest with POST method to save mentor data
            val stringRequest = object : StringRequest(
                Request.Method.POST, saveMentorUrl,
                Response.Listener { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        if (jsonObject.has("message")) {
                            val successMessage = jsonObject.getString("message")
                            Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show()
                            // Navigate back to the home screen after mentor is added
                            val intent = Intent(this, pg7home::class.java)
                            startActivity(intent)
                        } else if (jsonObject.has("error")) {
                            val errorMessage = jsonObject.getString("error")
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
                    params["name"] = mentorName
                    params["description"] = mentorDesc
                    params["status"] = mentorStatus
                    params["fee"] = mentorFee
                    params["dpUrl"] = dpUri.toString()  // base64Image
                    return params
                }
            }

            // Add the string request to the request queue
            Volley.newRequestQueue(this).add(stringRequest)
        } else {
            // Show a message if any field is empty
            Toast.makeText(this, "One or more fields are empty", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val IMAGE_REQUEST_CODE = 1
        private const val VIDEO_REQUEST_CODE = 2
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_REQUEST_CODE) {
                dpUri = data?.data
                dp.setImageURI(dpUri)
            } else if (requestCode == VIDEO_REQUEST_CODE) {
                videoUri = data?.data
                video.setImageURI(videoUri)
            }
        }
    }
}
