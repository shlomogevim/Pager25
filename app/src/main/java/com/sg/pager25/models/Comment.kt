package com.sg.pager25.models

import com.google.firebase.Timestamp

data class Comment(
    val commntId:String="",
    val postId:String="",
    val text:String="",
    val userName:String="",
    val userId:String="",
    val timestamp: Timestamp?=null
)
