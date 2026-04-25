package com.ndejje.garagelocationfinder.data.repository

import com.ndejje.garagelocationfinder.data.local.UserDao
import com.ndejje.garagelocationfinder.data.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun login(email: String, password: String): User? {
        val user = userDao.getUserByEmail(email)
        return if (user?.password == password) user else null
    }

    suspend fun register(user: User) {
        userDao.insertUser(user)
    }
}
