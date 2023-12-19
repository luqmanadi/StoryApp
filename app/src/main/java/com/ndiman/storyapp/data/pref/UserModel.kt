package com.ndiman.storyapp.data.pref

data class UserModel(
    val name: String,
    val token: String,
    val email: String,
    val isLogin: Boolean = false
)
