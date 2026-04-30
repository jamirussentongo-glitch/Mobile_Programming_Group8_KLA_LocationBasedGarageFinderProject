package com.ndejje.garagelocationfinder.ui.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.ndejje.garagelocationfinder.data.model.Garage
import com.ndejje.garagelocationfinder.data.repository.GarageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GarageViewModel @Inject constructor(
    private val repository: GarageRepository
) : ViewModel() {

    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation: StateFlow<LatLng?> = _userLocation

    val garages: StateFlow<List<Garage>> = combine(
        repository.getGarages(),
        _userLocation
    ) { garageList, location ->
        if (location == null) {
            garageList
        } else {
            garageList.map { garage ->
                val distance = calculateDistance(
                    location.latitude, location.longitude,
                    garage.latitude, garage.longitude
                )
                garage.copy(distance = String.format("%.1f km", distance / 1000))
            }.sortedBy {
                // Extract numeric distance for sorting
                it.distance?.replace(" km", "")?.toDoubleOrNull() ?: Double.MAX_VALUE
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        refreshGarages()
    }

    fun setUserLocation(latLng: LatLng) {
        _userLocation.value = latLng
    }

    fun refreshGarages() {
        viewModelScope.launch {
            repository.refreshGarages()
        }
    }

    suspend fun getGarageById(id: String): Garage? {
        return repository.getGarageById(id)
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0]
    }
}
