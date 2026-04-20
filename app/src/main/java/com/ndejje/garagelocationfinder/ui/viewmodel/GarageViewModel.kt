package com.ndejje.garagelocationfinder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ndejje.garagelocationfinder.data.model.Garage
import com.ndejje.garagelocationfinder.data.repository.GarageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GarageViewModel @Inject constructor(
    private val repository: GarageRepository
) : ViewModel() {

    val garages: StateFlow<List<Garage>> = repository.getGarages()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        refreshGarages()
    }

    fun refreshGarages() {
        viewModelScope.launch {
            repository.refreshGarages()
        }
    }

    suspend fun getGarageById(id: String): Garage? {
        return repository.getGarageById(id)
    }
}
