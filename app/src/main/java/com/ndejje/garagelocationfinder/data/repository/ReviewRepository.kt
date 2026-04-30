package com.ndejje.garagelocationfinder.data.repository

import com.ndejje.garagelocationfinder.data.local.ReviewDao
import com.ndejje.garagelocationfinder.data.model.Review
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepository @Inject constructor(
    private val reviewDao: ReviewDao
) {
    fun getReviewsForGarage(garageId: String): Flow<List<Review>> =
        reviewDao.getReviewsForGarage(garageId)

    suspend fun addReview(review: Review) {
        reviewDao.insertReview(review)
    }
}
