package com.ndejje.garagelocationfinder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ndejje.garagelocationfinder.data.model.Booking
import com.ndejje.garagelocationfinder.data.repository.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val repository: BookingRepository
) : ViewModel() {

    val bookings: StateFlow<List<Booking>> = repository.getAllBookings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun bookGarage(garageId: String, garageName: String, userName: String, service: String) {
        viewModelScope.launch {
            val date = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
            val booking = Booking(
                garageId = garageId,
                garageName = garageName,
                userName = userName,
                service = service,
                date = date,
                status = "Pending"
            )
            repository.addBooking(booking)
        }
    }

    fun cancelBooking(booking: Booking) {
        viewModelScope.launch {
            repository.cancelBooking(booking)
        }
    }
}
