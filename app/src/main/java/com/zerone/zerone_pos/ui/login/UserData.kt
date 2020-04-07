package com.zerone.zerone_pos.ui.login

data class UserData (val id:Int,
                     val role:Int,
                     val name:String,
                     val email:String,
                     val phone:String,
                     val merchant_name:String,
                     val merchant_business:String,
                     val merchant_city:String?=null,
                     val merchant_address:String?=null,
                     val merchant_logo:String?=null,
                     val image:String?=null
)