package com.example.fitnessapp

data class Task(
    val name: String = "",
    val goalDuration: String = "",
    val actualDuration: String = "",
    val userId: String = "",
    val id: String? = null // Optional, used for identifying tasks
)
