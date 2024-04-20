package com.example.cityaware.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface PostsDao {
   @get:Query("select * from Post")
   val all: List<Post?>?

   @Query("select * from Post where id = :postId")
   fun getPostById(postId: String?): Post?

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun insertAll(vararg posts: Post?)

   @Delete
   fun delete(post: Post?)
}