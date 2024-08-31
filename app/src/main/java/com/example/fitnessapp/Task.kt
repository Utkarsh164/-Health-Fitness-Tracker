package com.example.fitnessapp

data class Task(
    val name: String = "",
    val goalDuration: String = "",
    val actualDuration: String = "",
    val userId: String = "",
    val day: Int = 0,   // Add day field
    val month: Int = 0, // Add month field
    val year: Int = 0,  // Add year field
    val id: String? = null
)


