package com.example.cityaware.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @get:Query("select * from User")
    val all: List<User>

    @Query("select * from User where email = :email")
    fun getPostById(email: String?): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(user: User)

    @Delete
    fun delete(user: User)
}