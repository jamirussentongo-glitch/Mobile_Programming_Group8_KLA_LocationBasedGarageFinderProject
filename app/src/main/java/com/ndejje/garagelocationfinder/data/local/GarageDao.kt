package com.ndejje.garagelocationfinder.data.local

import androidx.room.*
import com.ndejje.garagelocationfinder.data.model.Garage
import com.ndejje.garagelocationfinder.data.model.RecentlyViewedGarage
import kotlinx.coroutines.flow.Flow

@Dao
interface GarageDao {
    @Query("SELECT * FROM garages")
    fun getAllGarages(): Flow<List<Garage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGarages(garages: List<Garage>)

    @Query("SELECT * FROM garages WHERE id = :id")
    suspend fun getGarageById(id: String): Garage?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentlyViewed(recent: RecentlyViewedGarage)

    @Query("""
        SELECT garages.* FROM garages 
        INNER JOIN recently_viewed ON garages.id = recently_viewed.id 
        ORDER BY recently_viewed.timestamp DESC LIMIT 10
    """)
    fun getRecentlyViewedGarages(): Flow<List<Garage>>
}
