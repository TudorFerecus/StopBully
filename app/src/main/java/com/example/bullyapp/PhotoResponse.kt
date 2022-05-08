package com.example.bullyapp

data class PhotoResponse(
    val has_face: Boolean,
    val id: Int,
    val image_url: String,
    val user_id: Int
)