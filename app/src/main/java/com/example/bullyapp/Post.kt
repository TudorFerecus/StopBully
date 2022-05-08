package com.example.bullyapp

data class Post(
    val caption: String,
    val content: String,
    val created: String,
    val id: Int,
    val last_modified: String,
    val likes: Int,
    val user_id: Int
)