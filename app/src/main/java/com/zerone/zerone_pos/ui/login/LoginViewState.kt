package com.zerone.zerone_pos.ui.login

data class LoginViewState(
    val loading: Boolean? = false,
    val error: Exception? = null,
    val data: MutableList<UserData>? = null
)