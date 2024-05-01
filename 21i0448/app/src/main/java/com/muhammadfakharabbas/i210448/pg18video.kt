package com.muhammadfakharabbas.i210448

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class pg18video : AppCompatActivity() {
    private val VIDEO_CAPTURE_REQUEST_CODE = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pg18video)

        var cam_txt = findViewById<TextView>(R.id.photo_txt)
        var cam_btn = findViewById<ImageView>(R.id.cam_btn)
        var close_btn = findViewById<ImageButton>(R.id.close_btn)
        var video = findViewById<ImageView>(R.id.video)
        close_btn.setOnClickListener(){
            finish()
        }
        cam_txt.setOnClickListener(){
            val intent = Intent(this, pg17photo::class.java)
            startActivity(intent)
        }
        cam_btn.setOnClickListener(){
            val intent = Intent(this, pg17photo::class.java)
            startActivity(intent)
        }
        video.setOnClickListener {
            // Check camera permission before starting the video recording activity
            if (checkCameraPermission()) {
                startVideoCaptureActivity()
            } else {
                requestCameraPermission()
            }
        }
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            VIDEO_CAPTURE_REQUEST_CODE
        )
    }

    private fun startVideoCaptureActivity() {
        val captureVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(captureVideoIntent, VIDEO_CAPTURE_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == VIDEO_CAPTURE_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startVideoCaptureActivity()
            } else {
                // Handle permission denied
            }
        }
    }
}