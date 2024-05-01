package com.muhammadfakharabbas.i210448

data class Chat(var senderId: String = "", var receiverId: String = "", var message : String = "", var messageID : String = "",
                var isSeen : String = "", var time : String = "", var imageURL : String = "",
                var videoURL : String = "",  var audioUrl : String = "", var messageType : String) {
    constructor() : this("", "", "", "", "", "", "", "", "", "")

}


/*

data class ChatMessageModel(var msgId:String,var senderId:String = "", var receiverId:String = "", var message:String = "",var messagetype:String = "", var imageURL:String = "",var audioURL:String = "",var videoURL:String = "",var timestamp: Int)//Timestamp?)
{
    constructor():this("","","", "","","","", "",0)
}
*/

