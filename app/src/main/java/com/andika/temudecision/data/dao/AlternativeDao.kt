package com.andika.temudecision.data.dao

import androidx.room.*
import com.andika.temudecision.data.entity.AlternativeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlternativeDao {
    @Query("SELECT * FROM alternatives WHERE study_id = :studyId")
    fun getAlternativesByStudy(studyId: String): Flow<List<AlternativeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlternative(alternative: AlternativeEntity)

    @Update
    suspend fun updateAlternative(alternative: AlternativeEntity)

    @Delete
    suspend fun deleteAlternative(alternative: AlternativeEntity)
}
