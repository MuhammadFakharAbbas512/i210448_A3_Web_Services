package com.muhammadfakharabbas.i210448

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


open class ScreenshotDetectionActivity : AppCompatActivity(), ScreenshotDetectionDelegate.ScreenshotDetectionListener {
    companion object {
        private const val REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 3009
    }

    private val screenshotDetectionDelegate = ScreenshotDetectionDelegate(this, this)

    override fun onStart() {
        super.onStart()
        screenshotDetectionDelegate.startScreenshotDetection()
    }

    override fun onStop() {
        super.onStop()
        screenshotDetectionDelegate.stopScreenshotDetection()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onScreenCapturedwithDeniedPermission()
        //checkReadExternalStoragePermission()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted, start screenshot detection
                    Toast.makeText(this, "Permission was granted, start screenshot detection", Toast.LENGTH_SHORT).show()
                    screenshotDetectionDelegate.startScreenshotDetection()
                } else {
                    // Permission denied, show a message or handle accordingly
                  //  Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    showReadExternalStoragePermissionDeniedMessage()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


    override fun onScreenCaptured(path: String) {
        Toast.makeText(this, "Screenshot captured message", Toast.LENGTH_SHORT).show()

    }

    override fun onScreenCapturedwithDeniedPermission() {
        //
        Toast.makeText(this, "Screenshot captured message with permission_denied_message", Toast.LENGTH_SHORT).show()

    }

    private fun checkReadExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            Toast.makeText(this, "Requesting Read_external_storage_permission", Toast.LENGTH_SHORT).show()
            requestReadExternalStoragePermission()
        } else {
            // Permission already granted, you can start screenshot detection directly if needed
            screenshotDetectionDelegate.startScreenshotDetection()
        }
    }

    private fun requestReadExternalStoragePermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION)
    }

    private fun showReadExternalStoragePermissionDeniedMessage() {
        Toast.makeText(this, "read_external_storage_permission_denied_message", Toast.LENGTH_SHORT).show()
    }
}