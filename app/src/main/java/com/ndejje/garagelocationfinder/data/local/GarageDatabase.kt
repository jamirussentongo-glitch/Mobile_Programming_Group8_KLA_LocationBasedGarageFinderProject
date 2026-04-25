package com.ndejje.garagelocationfinder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ndejje.garagelocationfinder.data.model.Garage
import com.ndejje.garagelocationfinder.data.model.User

@Database(entities = [Garage::class, User::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class GarageDatabase : RoomDatabase() {
    abstract fun garageDao(): GarageDao
    abstract fun userDao(): UserDao
}
