package com.example.fitnessapp.API

data class MyData(
    val `data`: List<Data>,
    val next: String,
    val total: Int
)