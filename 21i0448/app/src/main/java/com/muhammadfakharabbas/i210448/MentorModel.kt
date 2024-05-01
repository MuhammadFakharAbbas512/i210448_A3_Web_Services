package com.muhammadfakharabbas.i210448

data class MentorModel(val mid:String, val name:String, val description:String, val status: String,  var dpUrl:String, val fee:String  )
{ // later include about, rate and review
    constructor():this("","","", "", "", "")
}
