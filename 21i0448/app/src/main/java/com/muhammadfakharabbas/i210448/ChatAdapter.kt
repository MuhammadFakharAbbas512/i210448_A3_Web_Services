package com.muhammadfakharabbas.i210448
import android.app.Dialog
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter(private val context: Context, private val chatList: ArrayList<Chat>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_RIGHT = 1

    private val MESSSAGE_TYPE_IMAGE = 1
    private val MESSSAGE_TYPE_VIDEO = 2
    private val MESSSAGE_TYPE_AUDIO = 3

    var firebaseUser: FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == MESSAGE_TYPE_RIGHT) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_right, parent, false)
            return ViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_left, parent, false)
            return ViewHolder(view)
        }

    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val chat = chatList[position]
        loadProfilePicture(chat.senderId, holder.imgUser)
        var mediaPlayer: MediaPlayer?= null

        when(chat.messageType.toInt()) {
            MESSSAGE_TYPE_IMAGE -> {
                holder.imageMessage.visibility = View.VISIBLE
                holder.videoMessage.visibility = View.GONE
                holder.audioMessage.visibility = View.GONE
                val filename = chat.imageURL
                val storage = FirebaseStorage.getInstance()
                val storageRef = storage.reference
                val pathReference = storageRef.child("images/$filename")
                pathReference.downloadUrl.addOnSuccessListener { Uri ->
                    val imageURL = Uri.toString()
                    Glide.with(context).load(imageURL).into(holder.imageMessage)
                }.addOnFailureListener {
                    // Handle any errors
                }
            }

            MESSSAGE_TYPE_VIDEO -> {
                holder.videoMessage.visibility = View.VISIBLE
                holder.imageMessage.visibility = View.GONE
                holder.audioMessage.visibility = View.GONE
                val filename = chat.videoURL
                val storage = FirebaseStorage.getInstance()
                val storageRef = storage.reference
                val pathReference = storageRef.child("videos/$filename")
                pathReference.downloadUrl.addOnSuccessListener { Uri ->
                    val videoURL = Uri.toString()
                    holder.videoMessage.setVideoPath(videoURL)
                    holder.videoMessage.start()
                }.addOnFailureListener {
                    // Handle any errors
                }
            }

            MESSSAGE_TYPE_AUDIO -> {
                holder.audioMessage.visibility = View.VISIBLE
                holder.imageMessage.visibility = View.GONE
                holder.videoMessage.visibility = View.GONE

                val filename = chat.audioUrl
                val storage = FirebaseStorage.getInstance()
                val storageRef = storage.reference
                val pathReference = storageRef.child("audios/$filename")
                pathReference.downloadUrl.addOnSuccessListener { Uri ->
                    val audioURL = Uri.toString()
                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(audioURL)
                        prepareAsync()
                        setOnPreparedListener {
                            // Start the MediaPlayer when it's ready
                            start()
                        }
                    }
                }.addOnFailureListener {
                    // Handle any errors
                }
            }

            else -> {
                holder.imageMessage.visibility = View.GONE
                holder.videoMessage.visibility = View.GONE
                holder.audioMessage.visibility = View.GONE
            }
        }


        holder.txtUserName.text = chat.message

        val currentTime = System.currentTimeMillis()
        val timeDifference = currentTime - chat.time.toLong()
        val isEditable = timeDifference < 5 * 60 * 1000 // minutes in milliseconds

        if (isEditable && chat.senderId == FirebaseAuth.getInstance().currentUser!!.uid)
        {
            holder.txtUserName.setOnLongClickListener {
                val popup = PopupMenu(context, holder.txtUserName)
                popup.inflate(R.menu.popup_menu)
                popup.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.delete -> {
                            val ref = FirebaseDatabase.getInstance().getReference("Chat")
                                .child(chatList[position].messageID)
                            Log.d("AdapterDM", ref.toString())
                            ref.removeValue()
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Message Deleted", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Failed to delete message", Toast.LENGTH_SHORT).show()
                                }
                            chatList.removeAt(position)
                            notifyItemRemoved(position)
                            true
                        }

                        R.id.edit -> {
                            val ref = FirebaseDatabase.getInstance().getReference("Chat")
                                .child(chatList[position].messageID).child("message")

                            showEditMessagePopup(chatList[position].message,ref,position)
                            true
                        }

                        else -> false
                    }
                }
                popup.show()
                true
            }
        }

        // Set a click listener for the audio button
        holder.audioMessage.setOnClickListener {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.pause()
                holder.audioMessage.text = "Play Audio"
            } else {
                mediaPlayer!!.start()
                holder.audioMessage.text = "Pause Audio"
            }
        }


        holder.videoMessage.setOnClickListener {
            holder.videoMessage.start()
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtUserName: TextView = view.findViewById(R.id.tvMessage)
        val imageMessage : ImageView = view.findViewById(R.id.imageMessage)
        val videoMessage : VideoView = view.findViewById(R.id.videoMessage)
        val audioMessage : Button = view.findViewById(R.id.audioMessage)
        val imgUser: CircleImageView = view.findViewById(R.id.userImage)

    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        if (chatList[position].senderId == firebaseUser!!.uid) {
            return MESSAGE_TYPE_RIGHT
        } else {
            return MESSAGE_TYPE_LEFT
        }

    }

    private fun loadProfilePicture(userID: String, imgUser: CircleImageView) {
        val reference = FirebaseDatabase.getInstance().getReference("Users").child(userID)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    if (user.profileImage == "") {
                        imgUser.setImageResource(R.drawable.ic_launcher_foreground)
                    } else {
                        val filename = user.profileImage
                        val storage = FirebaseStorage.getInstance()
                        val storageRef = storage.reference
                        val pathReference = storageRef.child("images/$filename")
                        pathReference.downloadUrl.addOnSuccessListener { Uri ->
                            val imageURL = Uri.toString()
                            Glide.with(context).load(imageURL).into(imgUser)
                        }.addOnFailureListener {
                            // Handle any errors
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })

    }

    private fun showEditMessagePopup(message : String, ref : DatabaseReference, position: Int) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.edit_message)
        val editTextMessage = dialog.findViewById<EditText>(R.id.editTextMessage)
        val btnSave = dialog.findViewById<Button>(R.id.btnSave)

        // Set the existing message to the EditText
        editTextMessage.setText(message)

        // Save button click listener
        btnSave.setOnClickListener {
            val editedMessage = editTextMessage.text.toString()
            ref.setValue(editedMessage)
            chatList[position].message = editedMessage
            notifyItemChanged(position)
            // Handle the edited message (e.g., update in database)
            Toast.makeText(context, "Message saved: $editedMessage", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }
}