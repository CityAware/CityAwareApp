package com.example.cityaware.model

import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.cityaware.MyApplication
import com.example.cityaware.model.PostsDao;


@Database(entities = [Post::class], version = 55)
abstract class AppLocalDbRepository() : RoomDatabase() {
    abstract fun PostsDao (): PostsDao?
}


object AppLocalDb {
    val appDb: AppLocalDbRepository
        get() = databaseBuilder(
            MyApplication.myContext!!,
            AppLocalDbRepository::class.java,
            "dbPost.db"
        )
            .fallbackToDestructiveMigration()
            .build()
}

