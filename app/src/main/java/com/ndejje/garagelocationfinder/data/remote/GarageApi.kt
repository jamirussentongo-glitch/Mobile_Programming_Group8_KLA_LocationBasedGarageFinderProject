package com.ndejje.garagelocationfinder.data.remote

import com.ndejje.garagelocationfinder.data.model.Garage
import retrofit2.http.GET

interface GarageApi {
    @GET("garages")
    suspend fun getGarages(): List<Garage>
}
