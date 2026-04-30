package com.ndejje.garagelocationfinder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ndejje.garagelocationfinder.data.model.Review
import com.ndejje.garagelocationfinder.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val repository: ReviewRepository
) : ViewModel() {

    fun getReviewsForGarage(garageId: String): StateFlow<List<Review>> {
        return repository.getReviewsForGarage(garageId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun addReview(garageId: String, userName: String, rating: Float, comment: String, imageUrls: List<String> = emptyList()) {
        viewModelScope.launch {
            val review = Review(
                garageId = garageId,
                userName = userName,
                rating = rating,
                comment = comment,
                imageUrls = imageUrls
            )
            repository.addReview(review)
        }
    }
}
