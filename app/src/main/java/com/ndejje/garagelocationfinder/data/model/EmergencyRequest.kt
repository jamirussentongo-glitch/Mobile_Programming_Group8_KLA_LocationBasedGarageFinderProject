package com.ndejje.garagelocationfinder.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emergency_requests")
data class EmergencyRequest(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val userId: String,
    val userName: String,
    val userPhone: String,
    val latitude: Double,
    val longitude: Double,
    val status: String = "PENDING", // PENDING, ACCEPTED, RESOLVED
    val timestamp: Long = System.currentTimeMillis()
)
