package com.example.mad_exam_04

data class Task(
    var id: Int,         // ID is mutable because it's assigned when inserting
    val title: String,
    val content: String,
    val date: String,
    val startTime: String,
    val endTime: String
)
data class User(val username: String, val password: String)

