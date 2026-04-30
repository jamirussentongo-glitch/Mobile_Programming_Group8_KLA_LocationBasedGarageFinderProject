package com.ndejje.garagelocationfinder.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ndejje.garagelocationfinder.data.model.EmergencyRequest
import kotlinx.coroutines.flow.Flow

@Dao
interface EmergencyDao {
    @Query("SELECT * FROM emergency_requests ORDER BY timestamp DESC")
    fun getAllEmergencyRequests(): Flow<List<EmergencyRequest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmergencyRequest(request: EmergencyRequest)

    @Query("UPDATE emergency_requests SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: String)
}
