package com.andika.temudecision.data.dao

import androidx.room.*
import com.andika.temudecision.data.entity.StudyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyDao {
    @Query("SELECT * FROM studies ORDER BY created_at DESC")
    fun getAllStudies(): Flow<List<StudyEntity>>

    @Query("SELECT * FROM studies WHERE id = :id")
    suspend fun getStudyById(id: String): StudyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudy(study: StudyEntity)

    @Update
    suspend fun updateStudy(study: StudyEntity)

    @Delete
    suspend fun deleteStudy(study: StudyEntity)
}
