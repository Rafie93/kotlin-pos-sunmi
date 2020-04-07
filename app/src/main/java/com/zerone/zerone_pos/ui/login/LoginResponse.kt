package com.zerone.zerone_pos.ui.login

data class LoginResponse(
    val success : Boolean,
    var message:String,
    var token:String,
    var data: UserData
)