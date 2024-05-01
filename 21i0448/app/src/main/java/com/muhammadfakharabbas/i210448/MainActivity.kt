package com.muhammadfakharabbas.i210448

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.akexorcist.screenshotdetection.ScreenshotDetectionDelegate
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.delay
import org.json.JSONObject

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(), ScreenshotDetectionDelegate.ScreenshotDetectionListener  {
        companion object {
            private const val REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 3009
        }
        private val screenshotDetectionDelegate = ScreenshotDetectionDelegate(this, this)

        @SuppressLint("CutPasteId", "MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //onc()
       //  waiter()
            Handler().postDelayed({
                // Check if user is already logged in
                var user_Id = getUserId()
                if (user_Id != null) {
                    // User is already logged in, fetch user data
                    val userRefUrl = "http://192.168.1.6/smd/splash.php"
                    val userRef = JSONObject().apply {
                        put("userId", user_Id)
                    }
                    fetchUserData(userRefUrl, userRef)
                } else {
                    // User is not logged in, navigate to signup screen
                    val intent = Intent(this, Signup::class.java)
                    startActivity(intent)
                    finish()
                }

            }, 5000) // Delay for 5 seconds (5000 milliseconds)




       var auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser   // Check if user is already logged in
        val userId = currentUser?.uid
        val database = FirebaseDatabase.getInstance().reference
        val userRef = database.child("users").child(userId.toString())
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@addOnCompleteListener
                }

                // get new FCM registration token
                val token = task.result

                // Log and toast
                val msg = "FCM Token: $token"
                Log.d("my token ", msg)
                Log.d(TAG, "Token was given")
                // Store the token in the User object
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()

                // Create a map with the new token
                val updates = hashMapOf<String, Any>("token" to token!!)

                // Update the user's data in the database
                userRef.updateChildren(updates)

            }

        Handler().postDelayed({
            if (currentUser != null) {
                // User is already logged in, navigate to home screen
                userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Retrieve user data from the database
                            val userName = dataSnapshot.child("name").getValue(String::class.java)

                            val intent = Intent(this@MainActivity, pg7home::class.java)
                            intent.putExtra("userName", userName)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@MainActivity, "User data not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("MainActivity", "Failed to read user data (name): ${databaseError.message}")
                        Toast.makeText(this@MainActivity, "Failed to read user data (name)", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                // User is not logged in, navigate to signup screen
                val intent = Intent(this, Signup::class.java)
                startActivity(intent)
            }
            finish() // Finish the Splash screen activity
        }, 5000) // Delay for 5 seconds (5000 milliseconds)


    }
    fun onc(){
        var txt_mentor = findViewById<TextView>(R.id.txt_mentor)
        var txt_me = findViewById<TextView>(R.id.txt_me)
        txt_mentor.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        txt_me.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
    suspend fun waiter(){
        delay(5000) // 5 secs delay
    }
    override fun onStart() {
        super.onStart()
        screenshotDetectionDelegate.startScreenshotDetection()
    }

    override fun onStop() {
        super.onStop()
        screenshotDetectionDelegate.stopScreenshotDetection()
    }
    override fun onScreenCaptured(path: String) {
        Log.d(TAG, "Screenshot captured: $path")
        NotificationHelper(this,"screen shot").Notification()
        val kh = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        kh.hideSoftInputFromWindow(currentFocus?.windowToken,0)

}

    override fun onScreenCapturedWithDeniedPermission() {
        Log.d(TAG, "Screenshot captured but was denied permission")
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.d("permission", "Permission is granted")
        } else {
            requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
    }

    /*override fun onScreenCapturedwithDeniedPermission() {

    }*/

    private fun getUserId(): String? {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userId", null)
    }
 /*   private fun getUserId(): String? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid
    }*/

    private fun fetchUserData(url: String, userRef: JSONObject) {
        // Create a JsonObjectRequest with POST method
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, userRef,
            Response.Listener { response ->
                try {
                    val success = response.getBoolean("success")
                    if (success) {
                        val userData = response.getJSONObject("userData")
                        val userName = userData.getString("name")
                        val intent = Intent(this@MainActivity, pg7home::class.java)
                        intent.putExtra("userName", userName)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@MainActivity, "User data not found", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this@MainActivity, "Error parsing JSON response", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                // Handle error response
                Toast.makeText(this@MainActivity, "Error: " + error.message, Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Error: " + error.message)
            })

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this@MainActivity).add(jsonObjectRequest)
    }



override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
when (requestCode) {
    REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION -> {
        if (grantResults.getOrNull(0) == PackageManager.PERMISSION_DENIED) {
            showReadExternalStoragePermissionDeniedMessage()
        }
    }
    else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
}
}

private fun checkReadExternalStoragePermission() {
if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
    requestReadExternalStoragePermission()
}
}

private fun requestReadExternalStoragePermission() {
ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION)
}

private fun showReadExternalStoragePermissionDeniedMessage() {
Toast.makeText(this, "Read external storage permission has denied", Toast.LENGTH_SHORT).show()
}



}