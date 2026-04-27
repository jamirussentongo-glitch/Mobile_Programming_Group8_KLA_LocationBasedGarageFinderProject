package com.ndejje.garagelocationfinder.di

import android.content.Context
import androidx.room.Room
import com.ndejje.garagelocationfinder.data.local.BookingDao
import com.ndejje.garagelocationfinder.data.local.GarageDao
import com.ndejje.garagelocationfinder.data.local.GarageDatabase
import com.ndejje.garagelocationfinder.data.local.UserDao
import com.ndejje.garagelocationfinder.data.remote.GarageApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGarageDatabase(@ApplicationContext context: Context): GarageDatabase {
        return Room.databaseBuilder(
            context,
            GarageDatabase::class.java,
            "garage_db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideGarageDao(db: GarageDatabase): GarageDao = db.garageDao()

    @Provides
    fun provideUserDao(db: GarageDatabase): UserDao = db.userDao()

    @Provides
    fun provideBookingDao(db: GarageDatabase): BookingDao = db.bookingDao()

    @Provides
    @Singleton
    fun provideGarageApi(): GarageApi {
        return Retrofit.Builder()
            .baseUrl("https://api.example.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GarageApi::class.java)
    }
}
