package com.ndejje.garagelocationfinder.data.repository

import com.ndejje.garagelocationfinder.data.local.BookingDao
import com.ndejje.garagelocationfinder.data.model.Booking
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingRepository @Inject constructor(
    private val bookingDao: BookingDao
) {
    fun getAllBookings(): Flow<List<Booking>> = bookingDao.getAllBookings()

    suspend fun addBooking(booking: Booking) {
        bookingDao.insertBooking(booking)
    }

    suspend fun cancelBooking(booking: Booking) {
        bookingDao.deleteBooking(booking)
    }
}
