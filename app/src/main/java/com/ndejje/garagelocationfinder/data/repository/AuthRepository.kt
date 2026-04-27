package com.ndejje.garagelocationfinder.data.repository

import com.ndejje.garagelocationfinder.data.local.UserDao
import com.ndejje.garagelocationfinder.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val userDao: UserDao
) {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    suspend fun login(email: String, password: String): User? {
        val user = userDao.getUserByEmail(email)
        return if (user?.password == password) {
            _currentUser.value = user
            user
        } else {
            null
        }
    }

    suspend fun register(user: User) {
        userDao.insertUser(user)
        _currentUser.value = user
    }

    fun logout() {
        _currentUser.value = null
    }
}
