package com.muhammadfakharabbas.i210448

data class User(
    var userId: String = "",
    var city: String = "",
    var country: String = "",
    var email: String = "",
    var name: String = "",
    var profileImage: String = "",
    val fcmToken : String = ""
)
{
    constructor():this("","", "","","","","")
    fun setDataFromMap(userData: HashMap<String, Any>) {
        userId = userData["userId"].toString()
        city = userData["city"].toString()
        country = userData["country"].toString()
        email = userData["email"].toString()
        name = userData["name"].toString()
        profileImage = userData["profileImage"].toString()

    }
}