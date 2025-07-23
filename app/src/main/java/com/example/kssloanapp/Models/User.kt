package com.example.kssloanapp.Models

data class Users(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val emailid: String = "",
    val phoneNumber: String = "",
    val profileImage: String = "" // Base64 format
)
