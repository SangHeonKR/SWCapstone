package com.example.swcapstone.models

data class Photo(
    val imageUrl: String = "",
    val fileName: String = "",
    val name: String = "",
    val calories: Int = 0,
    val carbohydrate: Int = 0,
    val fat: Int = 0,
    val protein: Int = 0,
    val sugar: Int = 0,
    val timestamp: Long = 0L,
)

