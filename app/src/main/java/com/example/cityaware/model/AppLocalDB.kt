package com.example.cityaware.model

import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.cityaware.MyApplication


@Database(entities = [Post::class], version = 55)
abstract class AppLocalDbRepository() : RoomDatabase() {
    abstract fun PostsDao(): PostsDao?
}


object AppLocalDb {
    val getAppDb: AppLocalDbRepository
        get() = databaseBuilder(
            MyApplication.myContext,
            AppLocalDbRepository::class.java,
            "dbPost.db"
        )
            .fallbackToDestructiveMigration()
            .build()
}


