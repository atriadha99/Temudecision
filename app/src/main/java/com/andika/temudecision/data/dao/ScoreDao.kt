package com.andika.temudecision.data.dao

import androidx.room.*
import com.andika.temudecision.data.entity.ScoreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {
    @Query("SELECT * FROM scores WHERE alternative_id = :alternativeId")
    fun getScoresByAlternative(alternativeId: String): Flow<List<ScoreEntity>>

    @Query("""
        SELECT * FROM scores 
        WHERE alternative_id IN (SELECT id FROM alternatives WHERE study_id = :studyId)
    """)
    fun getScoresByStudy(studyId: String): Flow<List<ScoreEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(score: ScoreEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScores(scores: List<ScoreEntity>)

    @Query("DELETE FROM scores WHERE alternative_id = :alternativeId AND criteria_id = :criteriaId")
    suspend fun deleteScore(alternativeId: String, criteriaId: String)
}
