package com.ndejje.garagelocationfinder.data

import com.ndejje.garagelocationfinder.data.model.Garage

object GarageProvider {
    val garageList = listOf(
        Garage(
            id = "1",
            name = "City Auto Care - Kampala Central",
            address = "Kampala Road, Central",
            latitude = 0.3136,
            longitude = 32.5811,
            services = listOf("Oil Change", "Brake Repair", "General Service"),
            rating = 4.5f,
            contact = "+256700111222",
            imageUrl = "https://images.unsplash.com/photo-1486262715619-67b85e0b08d3?w=400",
            isOpen = true
        ),
        Garage(
            id = "2",
            name = "Wandegeya Mechanics",
            address = "Bombo Road, Wandegeya",
            latitude = 0.3308,
            longitude = 32.5739,
            services = listOf("Engine Tuning", "Body Work"),
            rating = 4.2f,
            contact = "+256701222333",
            imageUrl = "https://images.unsplash.com/photo-1517524008410-b44336d29a07?w=400",
            isOpen = true
        ),
        Garage(
            id = "3",
            name = "Nakawa Express Garage",
            address = "Old Port Bell Rd, Nakawa",
            latitude = 0.3283,
            longitude = 32.6186,
            services = listOf("Tyre Repair", "Wheel Alignment"),
            rating = 4.7f,
            contact = "+256702333444",
            imageUrl = "https://images.unsplash.com/photo-1507702553912-a15641e827c8?w=400",
            isOpen = false
        ),
        Garage(
            id = "4",
            name = "Ntinda Auto Clinic",
            address = "Kira Road, Ntinda",
            latitude = 0.3546,
            longitude = 32.6108,
            services = listOf("Diagnostics", "Battery Replacement", "Oil Change"),
            rating = 4.8f,
            contact = "+256703444555",
            imageUrl = "https://images.unsplash.com/photo-1530046339160-ce3e5b0c7a2f?w=400",
            isOpen = true
        ),
        Garage(
            id = "5",
            name = "Kololo Premium Garage",
            address = "Acacia Avenue, Kololo",
            latitude = 0.3371,
            longitude = 32.5908,
            services = listOf("Luxury Car Service", "AC Repair"),
            rating = 4.9f,
            contact = "+256704555666",
            imageUrl = "https://images.unsplash.com/photo-1599256621730-535171e28e50?w=400",
            isOpen = true
        ),
        Garage(
            id = "6",
            name = "Kibuli Tech Garage",
            address = "Kibuli Road",
            latitude = 0.3069,
            longitude = 32.5967,
            services = listOf("Suspension Repair", "Brake Pads"),
            rating = 4.3f,
            contact = "+256705666777",
            imageUrl = "https://images.unsplash.com/photo-1615906650593-ad41c02120c7?w=400",
            isOpen = true
        ),
        Garage(
            id = "7",
            name = "Mengo Auto Hub",
            address = "Mengo Hill Road",
            latitude = 0.3014,
            longitude = 32.5658,
            services = listOf("Transmission Repair", "Clutch Fix"),
            rating = 4.1f,
            contact = "+256706777888",
            imageUrl = "https://images.unsplash.com/photo-1619642751034-765dfdf7c58e?w=400",
            isOpen = false
        ),
        Garage(
            id = "8",
            name = "Bukoto Service Center",
            address = "Old Kira Road, Bukoto",
            latitude = 0.3506,
            longitude = 32.5986,
            services = listOf("Car Wash", "Interior Detailing"),
            rating = 4.6f,
            contact = "+256707888999",
            imageUrl = "https://images.unsplash.com/photo-1601362840469-51e4d8d59085?w=400",
            isOpen = true
        ),
        Garage(
            id = "9",
            name = "Makindye Master Mechanics",
            address = "Makindye Road",
            latitude = 0.2867,
            longitude = 32.5858,
            services = listOf("Electrical Repairs", "Wiring"),
            rating = 4.4f,
            contact = "+256708999000",
            imageUrl = "https://images.unsplash.com/photo-1562426509-50580bacfef3?w=400",
            isOpen = true
        ),
        Garage(
            id = "10",
            name = "Rubaga Professional Garage",
            address = "Rubaga Road",
            latitude = 0.3053,
            longitude = 32.5539,
            services = listOf("Exhaust Repair", "Muffler Service"),
            rating = 4.0f,
            contact = "+256709000111",
            imageUrl = "https://images.unsplash.com/photo-1503376780353-7e6692767b70?w=400",
            isOpen = true
        )
    )
}
