package com.ndejje.garagelocationfinder.data.local

import androidx.room.*
import com.ndejje.garagelocationfinder.data.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
}
