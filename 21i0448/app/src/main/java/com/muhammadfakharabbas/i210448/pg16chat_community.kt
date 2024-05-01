package com.muhammadfakharabbas.i210448

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class pg16chat_community : AppCompatActivity() {


    private var storageRef: StorageReference? = null
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatList: ArrayList<Chat>
    private lateinit var databaseRef: DatabaseReference
    private var firebaseUser: FirebaseUser? = null
    private lateinit var userId: String
    private var fileUri: Uri? = null
    private var audioUri: Uri? = null
    private var dpUri: Uri? = null
    private var type: String = "msg"
    private lateinit var recvId: String


    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            dpUri = uri
         //   var img = findViewById<ImageView>(R.id.imgMsg).setImageURI(uri)
            type = "img"
        }
    }

    // Function to get URI for a file
    private val pickFile = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            fileUri = uri
            findViewById<ImageView>(R.id.file).setImageURI(fileUri)
            type = "file"
        }
    }

    // Function to get URI for an audio file
    private val pickAudio = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            audioUri = uri
            val mediaPlayer = MediaPlayer.create(this, uri)
            // mediaPlayer.start()
            type = "audio"
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pg16chat_community)


        var intent = getIntent()
        var recvid = intent.getStringExtra("userId")
        var userName = intent.getStringExtra("userName")
        val audio = findViewById<ImageView>(R.id.audio)     // implement for audio button to select and save audio file
        val dp = findViewById<ImageView>(R.id.img)          // implement for dp button to select and save image file
        val file = findViewById<ImageView>(R.id.file)
        var etMessage = findViewById<EditText>(R.id.msg)
        val btnSendMessage = findViewById<ImageView>(R.id.send)
        var back_btn = findViewById<Button>(R.id.back_btn)
        var cam_btn = findViewById<ImageView>(R.id.cam_btn)
        var call_btn = findViewById<ImageView>(R.id.call_btn)
        var videocall_btn = findViewById<ImageView>(R.id.videocall_btn)

        var home_btn = findViewById<ImageView>(R.id.home_img)
        var search_btn = findViewById<ImageView>(R.id.search_img)
        var add_btn = findViewById<ImageView>(R.id.add_img)
        var chat_btn = findViewById<ImageView>(R.id.chat_img)
        var profile_btn = findViewById<ImageView>(R.id.profile_img)

        back_btn.setOnClickListener {
            val intent = Intent(this, pg14chat::class.java)
            startActivity(intent)
        }
        home_btn.setOnClickListener {
            val intent = Intent(this, pg7home::class.java)
            startActivity(intent)
        }
        search_btn.setOnClickListener {
            val intent = Intent(this, pg8find::class.java)
            startActivity(intent)
        }
        add_btn.setOnClickListener {
            val intent = Intent(this, pg12addmentor::class.java)
            startActivity(intent)
        }
        chat_btn.setOnClickListener {
            val intent = Intent(this, pg14chat::class.java)
            startActivity(intent)
        }
        profile_btn.setOnClickListener {
            val intent = Intent(this, pg21profile::class.java)
            startActivity(intent)
        }
        videocall_btn.setOnClickListener {
            val intent = Intent(this, pg19videocall::class.java)
            startActivity(intent)
        }
        call_btn.setOnClickListener {
            val intent = Intent(this, pg20call::class.java)
            startActivity(intent)
        }
        cam_btn.setOnClickListener {
            val intent = Intent(this, pg17photo::class.java)
            intent.putExtra("name", userName)
            intent.putExtra("id", recvid)
            startActivity(intent)
            audio.setOnClickListener {
                pickAudio.launch("audio/*")
            }

            dp.setOnClickListener {
                pickImage.launch("image/*")
            }

            file.setOnClickListener {
                pickFile.launch("*/*")
            }

            firebaseUser = FirebaseAuth.getInstance().currentUser

            // Get user ID from Firebase Authentication
            val currentUserID = firebaseUser?.uid

            // Reference to Firebase Realtime Database
            val usersRef = FirebaseDatabase.getInstance().reference.child("users")
                .child(currentUserID.toString())

            // Retrieve user data from Firebase Realtime Database
            usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val userName = dataSnapshot.child("name").value.toString()
                        userId = dataSnapshot.child("userId").value.toString()
                        // Start reading messages after getting user data
                        readMessage(currentUserID!!, recvid.toString())
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })

            btnSendMessage.setOnClickListener {
                val message: String = etMessage.text.toString().trim()
                if (message.isEmpty()) {
                    Toast.makeText(this, "Message is empty", Toast.LENGTH_SHORT).show()

                    if (type == "audio") {
                        sendAudioMessage(firebaseUser?.uid.toString(), recvId)
                    }
                    if (type == "img") {
                        sendImageMessage(firebaseUser?.uid.toString(), recvId)
                    }
                    if (type == "file") {
                        sendFileMessage(firebaseUser?.uid.toString(), recvId)
                    }
                } else {
                    sendMessage(currentUserID!!, recvid.toString(), message)
                    etMessage.setText("")
                    Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show()
                    // Handle notification sending
                }
            }

            // Rest of the code remains unchanged
        }

        fun sendAudioMessage(toString: String, recvId: String) {

        }

        fun sendImageMessage(toString: String, recvId: String) {

        }

        fun sendFileMessage(toString: String, recvId: String) {

        }

        // Functions to send and recv only text:



        // Modify onActivityResult to handle audio and file selection
        /* private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                 if (result.resultCode == Activity.RESULT_OK) {
                     val intent = result.data
                     intent?.data?.let { uri ->
                         if (requestCode == REQUEST_AUDIO_CAPTURE) {
                             // Send audio message
                             sendAudioMessage(currentUserID!!, recvid.toString(), uri)
                         } else if (requestCode == REQUEST_FILE_PICK) {
                             // Send file message
                             sendFileMessage(currentUserID!!, recvid.toString(), uri)
                         }
                     }
                 }
             }*/
    }

    private fun sendMessage(senderId: String, receiverId: String, message: String) {

        val reference = FirebaseDatabase.getInstance().getReference("Chat")
        val hashMap = HashMap<String, String>()
        hashMap["senderId"] = senderId
        hashMap["receiverId"] = receiverId
        hashMap["message"] = message
        hashMap["timestamp"] = System.currentTimeMillis().toString()
        if (type == "audio") {
            sendAudioMessage(firebaseUser?.uid.toString(), recvId)
        }
        if (type == "img") {
            sendImageMessage(firebaseUser?.uid.toString(), recvId)
        }
        if (type == "file") {
            sendFileMessage(firebaseUser?.uid.toString(), recvId)
        }
        reference.push().setValue(hashMap)
    }

    private fun sendFileMessage(toString: String, recvId: String) {
        val storageRef =
            FirebaseStorage.getInstance().reference.child("Chats/files/${System.currentTimeMillis()}")

        // Upload file to Firebase Storage
        storageRef.putFile(fileUri!!)
            .addOnSuccessListener { taskSnapshot ->
                // Get the download URL of the uploaded file
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Save the download URL in Realtime Database
                    val databaseRef =
                        FirebaseDatabase.getInstance().reference.child("Chat/files")
                    val messageMap = HashMap<String, Any>()
                    val messageId = databaseRef.push().key // Generate a unique key
                    messageMap["msgid"] = messageId.toString()
                    messageMap["fileUrl"] = uri.toString()
                    messageMap["messagetype"] = "file"
                    messageMap["timestamp"] = System.currentTimeMillis().toString()
                    // You can also add senderId and receiverId if needed
                    databaseRef.push().setValue(messageMap)
                    Toast.makeText(this, "sending file ", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                Toast.makeText(
                    this,
                    "Failed to send file message: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun sendImageMessage(toString: String, recvId: String) {
        // Get reference to Firebase Storage
        val storageRef =
            FirebaseStorage.getInstance().reference.child("Chats/images/${System.currentTimeMillis()}")

        // Upload image file to Firebase Storage
        storageRef.putFile(dpUri!!)
            .addOnSuccessListener { taskSnapshot ->
                // Get the download URL of the uploaded image
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Save the download URL in Realtime Database
                    val databaseRef =
                        FirebaseDatabase.getInstance().reference.child("Chat/images")
                    val messageMap = HashMap<String, Any>()
                    val messageId = databaseRef.push().key // Generate a unique key
                    messageMap["msgid"] = messageId.toString()
                    messageMap["imageUrl"] = uri.toString()
                    messageMap["messagetype"] = "image"
                    messageMap["timestamp"] = System.currentTimeMillis().toString()
                    // You can also add senderId and receiverId if needed
                    databaseRef.push().setValue(messageMap)
                    val imageView = ImageView(this@pg16chat_community)
                    imageView.setImageURI(dpUri)
                    val layoutParams = LinearLayout.LayoutParams(200, 200)
                    imageView.layoutParams = layoutParams
                    // Add the ImageView to your layout
                    // For example, if you have a LinearLayout with id 'container':
                    var layoutUser = findViewById<LinearLayout>(R.id.layoutUser)
                    layoutUser.addView(imageView)

                    Toast.makeText(this, "sending image ", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                Toast.makeText(
                    this,
                    "Failed to send image message ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun sendAudioMessage(sendId: String, recvId: String) {
        // Get reference to Firebase Storage
        val storageRef =
            FirebaseStorage.getInstance().reference.child("Chats/audios/${System.currentTimeMillis()}")

        // Upload audio file to Firebase Storage
        storageRef.putFile(audioUri!!)
            .addOnSuccessListener { taskSnapshot ->
                // Get the download URL of the uploaded audio
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Save the download URL in Realtime Database
                    val databaseRef =
                        FirebaseDatabase.getInstance().reference.child("Chat/audios")
                    val messageMap = HashMap<String, Any>()
                    val messageId = databaseRef.push().key // Generate a unique key
                    messageMap["msgid"] = messageId.toString()
                    messageMap["audioUrl"] = uri.toString()
                    messageMap["messagetype"] = "audio"
                    messageMap["timestamp"] = System.currentTimeMillis()
                    // You can also add senderId and receiverId if needed
                    databaseRef.push().setValue(messageMap)
                    Toast.makeText(this, "sending audio message: ", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                Toast.makeText(
                    this,
                    "Failed to send audio message: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun readMessage(senderId: String, receiverId: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(Chat::class.java)
                    if ((chat!!.senderId == senderId && chat.receiverId == senderId) ||
                        (chat.senderId == receiverId && chat.receiverId == senderId)
                    ) {
                        chatList.add(chat)

                    }
                }
                chatAdapter = ChatAdapter(this@pg16chat_community, chatList)
                chatRecyclerView.adapter = chatAdapter
                chatAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}