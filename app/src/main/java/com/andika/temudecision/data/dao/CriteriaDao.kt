package com.andika.temudecision.data.dao

import androidx.room.*
import com.andika.temudecision.data.entity.CriteriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CriteriaDao {
    @Query("SELECT * FROM criteria WHERE study_id = :studyId")
    fun getCriteriaByStudy(studyId: String): Flow<List<CriteriaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCriteria(criteria: CriteriaEntity)

    @Update
    suspend fun updateCriteria(criteria: CriteriaEntity)

    @Delete
    suspend fun deleteCriteria(criteria: CriteriaEntity)
}
