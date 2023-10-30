package com.example.imagevideo.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImage(images: List<ImageEntity>)
    // Change the return type to Long

    @Query("SELECT * FROM images")
    fun getAllImages(): Flow<List<ImageEntity>>
}

