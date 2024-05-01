package com.muhammadfakharabbas.i210448

import com.google.firebase.Timestamp

data class ChatRoomModel(
    var chatroomId: String = "",
    var userIds: List<String> = mutableListOf(),
    var lastMessageTimestamp: Timestamp? = null,
    var lastMessageSenderId: String = "",
    var lastMessage: String = ""
) {
    constructor(
        chatroomId: String,
        userIds: List<String>,
        lastMessageTimestamp: Timestamp?,
        lastMessageSenderId: String
    ) : this(chatroomId, userIds, lastMessageTimestamp, lastMessageSenderId, "")
}
