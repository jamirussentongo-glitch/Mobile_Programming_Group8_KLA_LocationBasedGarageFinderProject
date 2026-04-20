package com.ndejje.garagelocationfinder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ndejje.garagelocationfinder.data.model.Garage

@Database(entities = [Garage::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class GarageDatabase : RoomDatabase() {
    abstract fun garageDao(): GarageDao
}
