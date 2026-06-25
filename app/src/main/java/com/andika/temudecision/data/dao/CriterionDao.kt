package com.andika.temudecision.data.dao

import androidx.room.*
import com.andika.temudecision.data.entity.CriterionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CriterionDao {
    @Query("SELECT * FROM criteria WHERE categoryId = :categoryId")
    fun getCriteriaByCategoryId(categoryId: Long): Flow<List<CriterionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCriterion(criterion: CriterionEntity): Long

    @Delete
    suspend fun deleteCriterion(criterion: CriterionEntity)
}
