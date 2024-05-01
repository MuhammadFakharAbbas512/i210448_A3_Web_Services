package com.muhammadfakharabbas.i210448

// Add imports for necessary classes
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akexorcist.screenshotdetection.ScreenshotDetectionDelegate
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

class pg15chat_person : AppCompatActivity(), ScreenshotDetectionDelegate.ScreenshotDetectionListener {
    /*// Define constants for request codes
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_AUDIO_CAPTURE = 2
    private val REQUEST_FILE_PICK = 3
*/
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatList: ArrayList<Chat>
    private lateinit var databaseRef: DatabaseReference
    private var firebaseUser: FirebaseUser? = null
    private lateinit var userId: String
    //var receiverIdGlobal = ""
    private var Datatype = 0
    /* private lateinit var dp: ImageView
     private lateinit var file: ImageView
     private lateinit var audio: ImageView*/

    // Define variables for storage reference and current file URI
    private var storageRef: StorageReference? = null
/*    private var fileUri: Uri? = null
    private var audioUri: Uri? = null
    private var dpUri: Uri? = null
    private var type: String = "msg"*/
    private lateinit var recvId: String

/*    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            dpUri = uri
          //  var img = findViewById<ImageView>(R.id.imgMsg).setImageURI(uri)
            type="img"
        }
    }
    // Function to get URI for a file
    private val pickFile = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            fileUri = uri
            findViewById<ImageView>(R.id.file).setImageURI(fileUri)
            type="file"
        }
    }

    // Function to get URI for an audio file
    private val pickAudio = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            audioUri = uri
            val mediaPlayer = MediaPlayer.create(this, uri)
            // mediaPlayer.start()
            type="audio"
        }
    }*/

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pg15chat_john_person)

        // Initialize storage reference
        storageRef = FirebaseStorage.getInstance().reference
        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        chatList = ArrayList()
        chatAdapter = ChatAdapter(this, chatList)
        chatRecyclerView.adapter = chatAdapter
        //chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        var intent = getIntent()
        var recvid = intent.getStringExtra("userId")
        recvId = recvid.toString()
        var mentor = intent.getStringExtra("mid")
        if(mentor != null){
            recvid  = mentor
        }
        var userName = intent.getStringExtra("userName")
        val msg = "Chat recv ID: ${recvid}, Name: ${userName}"
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()


        var tvUserName = findViewById<TextView>(R.id.tvUserName)
        tvUserName.text = userName
        var etMessage = findViewById<EditText>(R.id.msg)
        var imgProfile = findViewById<CircleImageView>(R.id.userImage)

        val backBtn = findViewById<Button>(R.id.back_btn)
        val btnSendMessage = findViewById<ImageView>(R.id.send)
        val camBtn = findViewById<ImageView>(R.id.cam_btn)
        val audio = findViewById<ImageView>(R.id.audio)     // implement for audio button to select and save audio file
        val dp = findViewById<ImageView>(R.id.img)          // implement for dp button to select and save image file
        val file = findViewById<ImageView>(R.id.file)       // implement for file button to select and save files (pdf,txt,.etc)
        val callBtn = findViewById<ImageView>(R.id.call_btn)
        val videoCallBtn = findViewById<ImageView>(R.id.videocall_btn)

        val homeBtn = findViewById<ImageView>(R.id.home_img)
        val searchBtn = findViewById<ImageView>(R.id.search_img)
        val addBtn = findViewById<ImageView>(R.id.add_img)
        val chatBtn = findViewById<ImageView>(R.id.chat_img)
        val profileBtn = findViewById<ImageView>(R.id.profile_img)

        var Typeflag = 0
        var url = ""

        backBtn.setOnClickListener {
            onBackPressed()
        }
        homeBtn.setOnClickListener {
            startActivity(Intent(this, pg7home::class.java))
        }

        searchBtn.setOnClickListener {
            startActivity(Intent(this, pg8find::class.java))
        }

        addBtn.setOnClickListener {
            startActivity(Intent(this, pg12addmentor::class.java))
        }

        chatBtn.setOnClickListener {
            startActivity(Intent(this, pg14chat::class.java))
        }

        profileBtn.setOnClickListener {
            startActivity(Intent(this, pg21profile::class.java))
        }

        videoCallBtn.setOnClickListener {
            startActivity(Intent(this, pg19videocall::class.java))
        }

        callBtn.setOnClickListener {
            var intent = Intent(this, pg20call::class.java)
            intent.putExtra("name", userName)
            intent.putExtra("id", recvid)
            startActivity(intent)
        }

        camBtn.setOnClickListener {
            startActivity(Intent(this, pg17photo::class.java))
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser

        // Get user ID from Firebase Authentication
        val currentUserID = firebaseUser?.uid
        //receiverIdGlobal = userId!!

        // Reference to Firebase Realtime Database
        val usersRef = FirebaseDatabase.getInstance().reference.child("users").child(currentUserID.toString())

        // Retrieve user data from Firebase Realtime Database

        usersRef!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user!!.profileImage == "") {
                    imgProfile.setImageResource(R.drawable.ic_launcher_foreground)
                } else {
                    var filename = user.profileImage
                    val storage = FirebaseStorage.getInstance()
                    val storageRef = storage.reference
                    val pathReference = storageRef.child("images/$filename")
                    pathReference.downloadUrl.addOnSuccessListener { Uri ->
                        val imageURL = Uri.toString()
                        Glide.with(this@pg15chat_person).load(imageURL).into(imgProfile)
                    }.addOnFailureListener {
                        // Handle any errors
                    }
                }
            }
        })
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val userName = dataSnapshot.child("name").value.toString()
                    userId = dataSnapshot.child("userId").value.toString()
                    // Start reading messages after getting user data
                //    readMessage(currentUserID!!, recvid.toString())
                    readMessage(firebaseUser!!.uid, recvId, chatRecyclerView)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })

        btnSendMessage.setOnClickListener {
            var message: String = etMessage.text.toString()

            if (message.isEmpty()) {
                Toast.makeText(this, "message is empty", Toast.LENGTH_SHORT).show()
                etMessage.setText("")
            } else {
                if (userId != null) {
                    sendMessage(firebaseUser!!.uid, recvId, message, Typeflag, url)
                }
                etMessage.setText("")

            }
        }

        // Attach Image
        var pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                var storageRef = FirebaseStorage.getInstance()
                var filename = System.currentTimeMillis().toString() + "msg.jpg"
                var ref = storageRef.getReference("images/$filename")
                ref.putFile(it)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show()
                        url = filename

                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to Upload", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        dp.setOnClickListener {
            Toast.makeText(this@pg15chat_person, "Attach Image", Toast.LENGTH_SHORT).show()
            pickImage.launch("image/*")
            Typeflag = 1

        }

        // Attach Video
        var pickVideo = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                // Handle the video URI
                var storageRef = FirebaseStorage.getInstance()
                var filename = System.currentTimeMillis().toString() + "video.mp4"
                var ref = storageRef.getReference("videos/$filename")
                ref.putFile(uri)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Video Uploaded", Toast.LENGTH_SHORT).show()
                        url = filename
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to Upload Video", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        file.setOnClickListener {
            Toast.makeText(this@pg15chat_person.applicationContext, "Attach Video", Toast.LENGTH_SHORT).show()
            pickVideo.launch(arrayOf("video/*"))
            Typeflag = 2
        }

        // Camera Attachment
        // Camera Attachment
        val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                // Convert the bitmap to a file
                val file = File(this@pg15chat_person.externalMediaDirs.first(), "${System.currentTimeMillis()}.jpg")
                file.outputStream().use {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
                }

                // Upload the file to Firebase Storage
                var storageRef = FirebaseStorage.getInstance()
                var filename = System.currentTimeMillis().toString() + "mssg.jpg"
                var ref = storageRef.getReference("images/$filename")
                ref.putFile(Uri.fromFile(file))
                    .addOnSuccessListener {
                        Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show()
                        url = filename
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to Upload", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        camBtn.setOnClickListener {
            takePicture.launch(null)
            Typeflag = 1
        }

        // Mic Attachment
        var mediaRecorder: MediaRecorder? = null
        var audioFile: File? = null


        audio.setOnClickListener {
            if (mediaRecorder == null) {
                mediaRecorder = MediaRecorder().apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                    audioFile = File(this@pg15chat_person.externalMediaDirs.first(), "${System.currentTimeMillis()}.3gp")
                    setOutputFile(audioFile?.absolutePath)
                    prepare()
                    start()
                }
                Toast.makeText(this, "Recording Started", Toast.LENGTH_SHORT).show()
            } else {
                mediaRecorder?.apply {
                    stop()
                    release()
                }
                mediaRecorder = null

                // Upload the audio file to Firebase Storage
                var storageRef = FirebaseStorage.getInstance()
                var filename = System.currentTimeMillis().toString() + "mssg.3gp"
                var ref = storageRef.getReference("audios/$filename")
                ref.putFile(Uri.fromFile(audioFile))
                    .addOnSuccessListener {
                        Toast.makeText(this, "Audio Uploaded", Toast.LENGTH_SHORT).show()
                        url = filename
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to Upload", Toast.LENGTH_SHORT).show()
                    }

                Typeflag = 3
                Log.d("Audio", Typeflag.toString())
                Toast.makeText(this, "Recording Stopped", Toast.LENGTH_SHORT).show()
            }
        }




    }

    private fun sendMessage(senderId: String, receiverId: String, message: String, Typeflag: Int, url: String) {
        var reference: DatabaseReference? = FirebaseDatabase.getInstance().getReference()
        var messageID = reference!!.push().key

        var hashMap: HashMap<String, String> = HashMap()
        hashMap.put("senderId", senderId)
        hashMap.put("receiverId", receiverId)
        hashMap.put("message", message)
        hashMap.put("messageID", messageID!!)
        hashMap.put("isSeen", "false")
        hashMap.put("time", System.currentTimeMillis().toString())
        hashMap.put("imageURL", "")
        hashMap.put("videoURL", "")
        hashMap.put("audioUrl", "")
        hashMap.put("messageType", Typeflag.toString())

        // notification
        NotificationHelper(this,message).Notification()
        val kh = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        kh.hideSoftInputFromWindow(currentFocus?.windowToken,0)
/*ok set Text msg*/

// file type

if (Typeflag == 1) {
    hashMap.put("imageURL", url)
}

if (Typeflag == 2) {
    hashMap.put("videoURL", url)
}

if (Typeflag == 3) {
    hashMap.put("audioUrl", url)
}

messageID?.let {
    reference.child("Chat").child(it).setValue(hashMap)
}


val userReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)
userReference.addValueEventListener(object : ValueEventListener {
    override fun onCancelled(error: DatabaseError) {
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        val user = snapshot.getValue(User::class.java)
        if (user != null) {
          //  sendNotification(receiverId, message, user.name, hashMap)
        }
    }
})



}

fun readMessage(senderId: String, receiverId: String, chatRecyclerView: RecyclerView) {
val databaseReference: DatabaseReference =
    FirebaseDatabase.getInstance().getReference("Chat")

databaseReference.addValueEventListener(object : ValueEventListener {
    override fun onCancelled(error: DatabaseError) {
        TODO("Not yet implemented")
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        chatList.clear()
        for (dataSnapShot: DataSnapshot in snapshot.children) {
            val chat = dataSnapShot.getValue(Chat::class.java)

            if (chat!!.senderId.equals(senderId) && chat!!.receiverId.equals(receiverId) ||
                chat!!.senderId.equals(receiverId) && chat!!.receiverId.equals(senderId)
            ) {
                chatList.add(chat)
            }
        }

        val chatAdapter = ChatAdapter(this@pg15chat_person, chatList)

        chatRecyclerView.adapter = chatAdapter
    }
})
}


/*private fun sendNotification(receiverId: String, message: String, senderName: String, data: Map<String, String> = emptyMap()) {


var databaseReference: DatabaseReference =
    FirebaseDatabase.getInstance().getReference("users").child(receiverId).child("fcmToken")

var token : String ?= null
databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
    override fun onCancelled(error: DatabaseError) {
        //To change body of created functions use File | Settings | File Templates.
    }
    override fun onDataChange(snapshot: DataSnapshot) {
        val token2 = snapshot.getValue(String::class.java)
        token2?.let { Log.d("Token2 Test", it) }
        val bodyJson = JSONObject()
        bodyJson.put("to", token2)

        val key = "AAAAKMiszvY:APA91bETnB3mp__RaSx0QQL1QJDVyc8X3eUO3CmoGlOgLZursbb1y2_a0d3zkpxZNPpeQJvh8yrEtpT5VafT-2_iTmqbu4O3Xk77l4OW_UyngSTZP6XN3zQ-SlXR-luaoONNHhHa_263"
        val url = "https://fcm.googleapis.com/fcm/send"
        bodyJson.put("notification",
            JSONObject().also { it.put("title", senderName )
                it.put("subtitle", "New Message")
                it.put("body", message)
                it.put("sound", "social_notification_sound.wav")
            } )
        bodyJson.put("data", JSONObject(data))

        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "key=$key")
            .post(
                bodyJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

            )
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(
            object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    println("Received data: ${response.body?.string()}")
                }

                override fun onFailure(call: Call, e: IOException) {
                    println(e.message.toString())
                }
            }
        )
    }
})
}*/

private val screenshotDetectionDelegate = ScreenshotDetectionDelegate(this, this)

override fun onStart() {
super.onStart()
screenshotDetectionDelegate.startScreenshotDetection()
}

override fun onStop() {
super.onStop()
screenshotDetectionDelegate.stopScreenshotDetection()

}

override fun onScreenCaptured(path: String) {
Toast.makeText(this, "Screenshot captured message", Toast.LENGTH_SHORT).show()
//  sendNotification(receiverIdGlobal, "Screenshot Captured", "Admin")
}

override fun onScreenCapturedWithDeniedPermission() {
Toast.makeText(this, "Screenshot captured message  with permission_denied_message", Toast.LENGTH_SHORT).show()
//  sendNotification(receiverIdGlobal, "Screenshot Captured", "Admin")
}
//

/*    override fun onScreenCaptured(path: String) {
    // Do something when screen was captured
    Toast.makeText(this@pg15chat_person, "Screenshot Captured", Toast.LENGTH_SHORT).show()
    sendNotification(receiverIdGlobal, "Screenshot Captured", "Admin")
}

override fun onScreenCapturedWithDeniedPermission() {
    // Do something when screen was captured but read external storage permission has denied
    Toast.makeText(this@pg15chat_person, "Screenshot Captured", Toast.LENGTH_SHORT).show()
    sendNotification(receiverIdGlobal, "Screenshot Captured", "Admin")
}*/
}

