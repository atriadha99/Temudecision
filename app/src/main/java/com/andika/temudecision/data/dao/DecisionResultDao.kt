package com.andika.temudecision.data.dao

import androidx.room.*
import com.andika.temudecision.data.entity.DecisionResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DecisionResultDao {
    @Query("SELECT * FROM decision_results ORDER BY date DESC")
    fun getAllResults(): Flow<List<DecisionResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: DecisionResultEntity)
}
