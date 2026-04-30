package com.ndejje.garagelocationfinder.data.repository

import com.ndejje.garagelocationfinder.data.local.EmergencyDao
import com.ndejje.garagelocationfinder.data.model.EmergencyRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmergencyRepository @Inject constructor(
    private val emergencyDao: EmergencyDao
) {
    fun getAllRequests(): Flow<List<EmergencyRequest>> = emergencyDao.getAllEmergencyRequests()

    suspend fun sendEmergencyRequest(request: EmergencyRequest) {
        emergencyDao.insertEmergencyRequest(request)
    }

    suspend fun updateRequestStatus(id: String, status: String) {
        emergencyDao.updateStatus(id, status)
    }
}
