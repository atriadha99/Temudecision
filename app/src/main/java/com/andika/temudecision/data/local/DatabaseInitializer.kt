package com.andika.temudecision.data.local

import com.andika.temudecision.data.dao.*
import com.andika.temudecision.data.entity.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseInitializer @Inject constructor(
    private val studyDao: StudyDao,
    private val criteriaDao: CriteriaDao,
    private val alternativeDao: AlternativeDao,
    private val scoreDao: ScoreDao
) {
    suspend fun seedIfNeeded() {
        val existing = studyDao.getAllStudies().first()
        if (existing.isNotEmpty()) return

        // Study 1: Pemilihan Laptop Terbaik
        val laptopStudyId = "study_laptop"
        studyDao.insertStudy(
            StudyEntity(
                id = laptopStudyId,
                name = "Pemilihan Laptop Terbaik",
                icon = "💻",
                description = "Mencari laptop dengan performa dan harga paling optimal untuk produktivitas."
            )
        )

        val criteria = listOf(
            CriteriaEntity(id = "c1", studyId = laptopStudyId, name = "Harga", weight = 0.3, type = "cost"),
            CriteriaEntity(id = "c2", studyId = laptopStudyId, name = "RAM", weight = 0.2, type = "benefit"),
            CriteriaEntity(id = "c3", studyId = laptopStudyId, name = "Storage", weight = 0.2, type = "benefit"),
            CriteriaEntity(id = "c4", studyId = laptopStudyId, name = "Processor", weight = 0.3, type = "benefit")
        )
        criteria.forEach { criteriaDao.insertCriteria(it) }

        val alternatives = listOf(
            AlternativeEntity(id = "a1", studyId = laptopStudyId, name = "MacBook Air M2", category = "Laptop"),
            AlternativeEntity(id = "a2", studyId = laptopStudyId, name = "ThinkPad X1", category = "Laptop"),
            AlternativeEntity(id = "a3", studyId = laptopStudyId, name = "Dell XPS 13", category = "Laptop"),
            AlternativeEntity(id = "a4", studyId = laptopStudyId, name = "Zenbook 14 OLED", category = "Laptop")
        )
        alternatives.forEach { alternativeDao.insertAlternative(it) }

        val scores = listOf(
            ScoreEntity(alternativeId = "a1", criteriaId = "c1", value = 4.0),
            ScoreEntity(alternativeId = "a1", criteriaId = "c2", value = 3.0),
            ScoreEntity(alternativeId = "a1", criteriaId = "c3", value = 3.0),
            ScoreEntity(alternativeId = "a1", criteriaId = "c4", value = 5.0),
            
            ScoreEntity(alternativeId = "a2", criteriaId = "c1", value = 3.0),
            ScoreEntity(alternativeId = "a2", criteriaId = "c2", value = 5.0),
            ScoreEntity(alternativeId = "a2", criteriaId = "c3", value = 4.0),
            ScoreEntity(alternativeId = "a2", criteriaId = "c4", value = 4.0),
            
            ScoreEntity(alternativeId = "a3", criteriaId = "c1", value = 2.0),
            ScoreEntity(alternativeId = "a3", criteriaId = "c2", value = 4.0),
            ScoreEntity(alternativeId = "a3", criteriaId = "c3", value = 5.0),
            ScoreEntity(alternativeId = "a3", criteriaId = "c4", value = 4.0),
            
            ScoreEntity(alternativeId = "a4", criteriaId = "c1", value = 5.0),
            ScoreEntity(alternativeId = "a4", criteriaId = "c2", value = 3.0),
            ScoreEntity(alternativeId = "a4", criteriaId = "c3", value = 3.0),
            ScoreEntity(alternativeId = "a4", criteriaId = "c4", value = 3.0)
        )
        scoreDao.insertScores(scores)
    }

    suspend fun getStudyName(id: String): String? {
        return studyDao.getStudyById(id)?.name
    }
}
