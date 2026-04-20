package com.ndejje.garagelocationfinder.data.local

import androidx.room.*
import com.ndejje.garagelocationfinder.data.model.Garage
import kotlinx.coroutines.flow.Flow

@Dao
interface GarageDao {
    @Query("SELECT * FROM garages")
    fun getAllGarages(): Flow<List<Garage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGarages(garages: List<Garage>)

    @Query("SELECT * FROM garages WHERE id = :id")
    suspend fun getGarageById(id: String): Garage?
}
