package com.andika.temudecision.data.dao

import androidx.room.*
import com.andika.temudecision.data.entity.AlternativeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlternativeDao {
    @Query("SELECT * FROM alternatives WHERE categoryId = :categoryId")
    fun getAlternativesByCategoryId(categoryId: Long): Flow<List<AlternativeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlternative(alternative: AlternativeEntity): Long

    @Delete
    suspend fun deleteAlternative(alternative: AlternativeEntity)
}
