package com.andika.temudecision.data.repository

import com.andika.temudecision.data.dao.*
import com.andika.temudecision.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SDSSRepository @Inject constructor(
    private val studyDao: StudyDao,
    private val criteriaDao: CriteriaDao,
    private val alternativeDao: AlternativeDao,
    private val scoreDao: ScoreDao,
    private val calculationDao: CalculationDao
) {
    // Studies
    fun getAllStudies(): Flow<List<StudyEntity>> = studyDao.getAllStudies()
    suspend fun getStudyById(id: String) = studyDao.getStudyById(id)
    suspend fun insertStudy(study: StudyEntity) = studyDao.insertStudy(study)
    suspend fun deleteStudy(study: StudyEntity) = studyDao.deleteStudy(study)

    // Criteria
    fun getCriteriaByStudy(studyId: String) = criteriaDao.getCriteriaByStudy(studyId)
    suspend fun insertCriteria(criteria: CriteriaEntity) = criteriaDao.insertCriteria(criteria)
    suspend fun deleteCriteria(criteria: CriteriaEntity) = criteriaDao.deleteCriteria(criteria)

    // Alternatives
    fun getAlternativesByStudy(studyId: String) = alternativeDao.getAlternativesByStudy(studyId)
    suspend fun insertAlternative(alternative: AlternativeEntity) = alternativeDao.insertAlternative(alternative)
    suspend fun deleteAlternative(alternative: AlternativeEntity) = alternativeDao.deleteAlternative(alternative)

    // Scores
    fun getScoresByStudy(studyId: String) = scoreDao.getScoresByStudy(studyId)
    suspend fun insertScores(scores: List<ScoreEntity>) = scoreDao.insertScores(scores)

    // Calculations
    fun getCalculationsByStudy(studyId: String) = calculationDao.getCalculationsByStudy(studyId)
    suspend fun insertCalculation(calculation: CalculationEntity) = calculationDao.insertCalculation(calculation)
}
