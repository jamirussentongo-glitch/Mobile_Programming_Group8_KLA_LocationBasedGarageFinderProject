package com.ndejje.garagelocationfinder.data.local

import androidx.room.*
import com.ndejje.garagelocationfinder.data.model.Booking
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY date DESC")
    fun getAllBookings(): Flow<List<Booking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: Booking)

    @Delete
    suspend fun deleteBooking(booking: Booking)
}
