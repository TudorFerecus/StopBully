package com.example.bullyapp

data class CommentData(
    val content: String,
    val created: String,
    val id: Int,
    val last_modified: String,
    val likes: Int,
    val post_id: Int
)
