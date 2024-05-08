package com.example.cityaware.model

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cityaware.MyApplication

@Database(entities = [Post::class,User::class], version = 55)
abstract class AppLocalDbRepository : RoomDatabase() {
    abstract fun PostsDao(): PostsDao
    abstract fun userDao(): UserDao
}

object AppLocalDb {
    val appDb: AppLocalDbRepository
        get() = Room.databaseBuilder<AppLocalDbRepository>(
            MyApplication.Companion.myContext!!,
            AppLocalDbRepository::class.java,
            "dbPost.db"
        )
            .fallbackToDestructiveMigration()
            .build()
}