package com.muhammadfakharabbas.i210448

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

object FirebaseUtil {

    // Method to get current user ID from FirebaseAuth
    fun currentUserId(): String? {
        return FirebaseAuth.getInstance().uid
    }

    // Method to check if a user is logged in
    fun isLoggedIn(): Boolean {
        return currentUserId() != null
    }

    // Method to get reference to current user details in Realtime Database
    fun currentUserDetails(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference("users").child(currentUserId()!!)
    }

    // Method to get reference to all users in Realtime Database
    fun allUserDatabaseReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference("users")
    }

    // Method to get reference to a specific chatroom in Realtime Database
    fun getChatroomReference(chatroomId: String): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference("chatrooms").child(chatroomId)
    }

    // Method to get reference to messages in a specific chatroom in Realtime Database
    fun getChatroomMessageReference(chatroomId: String): DatabaseReference {
        return getChatroomReference(chatroomId).child("chats")
    }

    // Method to generate chatroom ID based on user IDs
    fun getChatroomId(userId1: String, userId2: String): String {
        return if (userId1.hashCode() < userId2.hashCode()) {
            "$userId1" + "_" + "$userId2"
        } else {

            "$userId2" + "_" + "$userId1"
        }
    }

    // Method to get reference to all chatrooms in Realtime Database
    fun allChatroomDatabaseReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference("chatrooms")
    }

    // Method to get reference to the other user from a chatroom in Realtime Database
    fun getOtherUserFromChatroom(userIds: List<String>): DatabaseReference {
        return if (userIds[0] == currentUserId()) {
            allUserDatabaseReference().child(userIds[1])
        } else {
            allUserDatabaseReference().child(userIds[0])
        }
    }

    // Method to convert Timestamp to string format
    fun timestampToString(timestamp: Long): String {
        return SimpleDateFormat("HH:MM").format(Date(timestamp))
    }

    // Method to logout user from FirebaseAuth
    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}
