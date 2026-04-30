package com.ndejje.garagelocationfinder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ndejje.garagelocationfinder.data.model.EmergencyRequest
import com.ndejje.garagelocationfinder.data.repository.EmergencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmergencyViewModel @Inject constructor(
    private val repository: EmergencyRepository
) : ViewModel() {

    val emergencyRequests: StateFlow<List<EmergencyRequest>> = repository.getAllRequests()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun sendEmergencyRequest(userId: String, userName: String, userPhone: String, lat: Double, lng: Double) {
        viewModelScope.launch {
            val request = EmergencyRequest(
                userId = userId,
                userName = userName,
                userPhone = userPhone,
                latitude = lat,
                longitude = lng
            )
            repository.sendEmergencyRequest(request)
        }
    }

    fun updateStatus(id: String, status: String) {
        viewModelScope.launch {
            repository.updateRequestStatus(id, status)
        }
    }
}
