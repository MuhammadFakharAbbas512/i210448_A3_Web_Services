package com.muhammadfakharabbas.i210448

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class pg17photo : AppCompatActivity() {

    private val CAMERA_PERMISSION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pg17photo)

        // Find views
        var close_btn = findViewById<ImageButton>(R.id.close_btn)
        var video_txt = findViewById<TextView>(R.id.video_txt)
        var video_btn = findViewById<ImageView>(R.id.video_btn)
        var cam = findViewById<ImageView>(R.id.cam)
        // Set click listeners
        close_btn.setOnClickListener {
            finish()
        }
        video_txt.setOnClickListener {
            val intent = Intent(this, pg18video::class.java)
            startActivity(intent)
        }
        video_btn.setOnClickListener(){
            val intent = Intent(this, pg18video::class.java)
            startActivity(intent)
        }
        cam.setOnClickListener {
            // Check camera permission before starting the activity
            if (checkCameraPermission()) {
                startCameraActivity()
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
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    private fun startCameraActivity() {
        val localVideoViewContainer = findViewById<FrameLayout>(R.id.local_video_view_container)
        val previewView = PreviewView(this)

        // Add previewView to localVideoViewContainer
        localVideoViewContainer.addView(previewView)

        // Initialize CameraX
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            // Get the camera provider
            val cameraProvider = cameraProviderFuture.get()

            // Set up the preview use case
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            // Select back camera as the default camera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            // Unbind any previous use cases before binding new ones
            cameraProvider.unbindAll()

            // Bind the camera preview use case to the lifecycle
            cameraProvider.bindToLifecycle(this, cameraSelector, preview)
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraActivity()
            } else {
                // Handle permission denied
            }
        }
    }
}
