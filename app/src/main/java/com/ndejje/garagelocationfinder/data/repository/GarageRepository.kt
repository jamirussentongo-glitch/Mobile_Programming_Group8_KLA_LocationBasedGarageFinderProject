package com.ndejje.garagelocationfinder.data.repository

import com.ndejje.garagelocationfinder.data.local.GarageDao
import com.ndejje.garagelocationfinder.data.model.Garage
import com.ndejje.garagelocationfinder.data.remote.GarageApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GarageRepository @Inject constructor(
    private val api: GarageApi,
    private val dao: GarageDao
) {
    fun getGarages(): Flow<List<Garage>> = dao.getAllGarages()

    suspend fun refreshGarages() {
        try {
            val dummyGarages = listOf(
                Garage("1", "City Auto Care - Kampala Central", "Kampala Road, Central", 0.3136, 32.5811, listOf("Oil Change", "Brake Repair", "General Service"), 4.5f, "+256700111222", "https://images.unsplash.com/photo-1486262715619-67b85e0b08d3?w=400"),
                Garage("2", "Wandegeya Mechanics", "Bombo Road, Wandegeya", 0.3308, 32.5739, listOf("Engine Tuning", "Body Work"), 4.2f, "+256701222333", "https://images.unsplash.com/photo-1517524008410-b44336d29a07?w=400"),
                Garage("3", "Nakawa Express Garage", "Old Port Bell Rd, Nakawa", 0.3283, 32.6186, listOf("Tyre Repair", "Wheel Alignment"), 4.7f, "+256702333444", "https://images.unsplash.com/photo-1507702553912-a15641e827c8?w=400"),
                Garage("4", "Ntinda Auto Clinic", "Kira Road, Ntinda", 0.3546, 32.6108, listOf("Diagnostics", "Battery Replacement", "Oil Change"), 4.8f, "+256703444555", "https://images.unsplash.com/photo-1530046339160-ce3e5b0c7a2f?w=400"),
                Garage("5", "Kololo Premium Garage", "Acacia Avenue, Kololo", 0.3371, 32.5908, listOf("Luxury Car Service", "AC Repair"), 4.9f, "+256704555666", "https://images.unsplash.com/photo-1599256621730-535171e28e50?w=400"),
                Garage("6", "Kibuli Tech Garage", "Kibuli Road", 0.3069, 32.5967, listOf("Suspension Repair", "Brake Pads"), 4.3f, "+256705666777", "https://images.unsplash.com/photo-1615906650593-ad41c02120c7?w=400"),
                Garage("7", "Mengo Auto Hub", "Mengo Hill Road", 0.3014, 32.5658, listOf("Transmission Repair", "Clutch Fix"), 4.1f, "+256706777888", "https://images.unsplash.com/photo-1619642751034-765dfdf7c58e?w=400"),
                Garage("8", "Bukoto Service Center", "Old Kira Road, Bukoto", 0.3506, 32.5986, listOf("Car Wash", "Interior Detailing"), 4.6f, "+256707888999", "https://images.unsplash.com/photo-1601362840469-51e4d8d59085?w=400"),
                Garage("9", "Makindye Master Mechanics", "Makindye Road", 0.2867, 32.5858, listOf("Electrical Repairs", "Wiring"), 4.4f, "+256708999000", "https://images.unsplash.com/photo-1562426509-50580bacfef3?w=400"),
                Garage("10", "Rubaga Professional Garage", "Rubaga Road", 0.3053, 32.5539, listOf("Exhaust Repair", "Muffler Service"), 4.0f, "+256709000111", "https://images.unsplash.com/photo-1503376780353-7e6692767b70?w=400")
            )
            dao.insertGarages(dummyGarages)
        } catch (e: Exception) {
            // Handle error
        }
    }

    suspend fun getGarageById(id: String): Garage? = dao.getGarageById(id)
}
