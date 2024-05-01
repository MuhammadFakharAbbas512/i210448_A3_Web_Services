package com.muhammadfakharabbas.i210448

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig

class pg20call : AppCompatActivity() {

    private val appId = "128af5b50628492297d792ec586231b9"
    private val channelName = "testing"
    private var token : String? = null
    private val uid = 0
    private var isJoined = false
    private var agoraEngine: RtcEngine? = null
    private val PERMISSION_REQ_ID = 22
    private val REQUESTED_PERMISSIONS = arrayOf<String>(
        Manifest.permission.RECORD_AUDIO
    )

    private fun checkSelfPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            REQUESTED_PERMISSIONS[0]
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun showMessage(message: String?) {
        runOnUiThread {
            Toast.makeText(
                applicationContext,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupAudioSDKEngine() {
        try {
            val config = RtcEngineConfig()
            config.mContext = baseContext
            config.mAppId = appId
            config.mEventHandler = mRtcEventHandler
            agoraEngine = RtcEngine.create(config)
            agoraEngine!!.enableAudio()
        } catch (e: Exception) {
            showMessage(e.toString())
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pg20call)

        var name = findViewById<TextView>(R.id.mentorname)
        var img = findViewById<ImageView>(R.id.mentorimg)

        var intent = getIntent()
        var recvid = intent.getStringExtra("id")
        name.text = intent.getStringExtra("name")


        token = "007eJxTYDi6dcWplxeNei+Ibs5bHb9ih8H5D2q2eZ8eaas+yp83zdhdgSHJNMnI0NLCxCDNxNDExNQgKc001TzNICU1OcXczMQwufXk19SGQEaGk4kxrIwMEAjiszOUpBaXZOalMzAAAI61I5Y="
        if (!checkSelfPermission()) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, PERMISSION_REQ_ID)
        }
        setupAudioSDKEngine()
        var close_btn = findViewById<ImageView>(R.id.close_btn)

        close_btn.setOnClickListener(){
           /* val intent = Intent(this, pg15chat_person::class.java)
            startActivity(intent)*/
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //   agoraEngine!!.leaveChannel()
        agoraEngine?.leaveChannel()
        RtcEngine.destroy()
        agoraEngine = null
        //  agoraEngine!!.release()
    }

    private val mRtcEventHandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {
        override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
            isJoined = true
            showMessage("Joined Channel $channel")
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            showMessage("Remote user offline $uid $reason")
        }
    }

    fun joinChannel(view: View) {
        if (checkSelfPermission()) {
            val options = ChannelMediaOptions()
            options.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
            options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
            agoraEngine!!.joinChannel(token, channelName, uid, options)
        } else {
            Toast.makeText(applicationContext, "Permissions not granted", Toast.LENGTH_SHORT).show()
        }
    }

    fun leaveChannel(view: View) {
        if (!isJoined) {
            showMessage("Join a channel first")
        } else {
            agoraEngine!!.leaveChannel()
            showMessage("You left the channel")
            isJoined = false
        }
    }
}

















