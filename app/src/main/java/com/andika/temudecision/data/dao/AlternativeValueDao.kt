package com.andika.temudecision.data.dao

import androidx.room.*
import com.andika.temudecision.data.entity.AlternativeValueEntity

@Dao
interface AlternativeValueDao {
    @Query("""
        SELECT av.* FROM alternative_values av
        JOIN alternatives a ON av.alternativeId = a.id
        WHERE a.categoryId = :categoryId
    """)
    suspend fun getValuesByCategoryId(categoryId: Long): List<AlternativeValueEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertValue(value: AlternativeValueEntity)

    @Query("DELETE FROM alternative_values WHERE alternativeId = :alternativeId")
    suspend fun deleteValuesByAlternativeId(alternativeId: Long)
}
