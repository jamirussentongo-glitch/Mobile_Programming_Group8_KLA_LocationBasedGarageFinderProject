package com.ndejje.garagelocationfinder.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "garages")
data class Garage(
    @PrimaryKey val id: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val services: List<String>,
    val rating: Float,
    val contact: String,
    val imageUrl: String,
    val isOpen: Boolean = true,
    val imageRes: Int? = null,
    val distance: String? = null,
    val isVerified: Boolean = false // Added for community trust
)

@Entity(tableName = "recently_viewed")
data class RecentlyViewedGarage(
    @PrimaryKey val id: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val garageId: String,
    val garageName: String,
    val userName: String,
    val service: String,
    val date: String,
    val status: String // Pending, Completed, Cancelled
)
