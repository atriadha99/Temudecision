package com.andika.temudecision.data.dao

import androidx.room.*
import com.andika.temudecision.data.entity.CalculationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CalculationDao {
    @Query("SELECT * FROM calculations WHERE study_id = :studyId ORDER BY created_at DESC")
    fun getCalculationsByStudy(studyId: String): Flow<List<CalculationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalculation(calculation: CalculationEntity)

    @Delete
    suspend fun deleteCalculation(calculation: CalculationEntity)
}
