package com.ndejje.garagelocationfinder.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val garageId: String,
    val userName: String,
    val rating: Float,
    val comment: String,
    val imageUrls: List<String> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)
