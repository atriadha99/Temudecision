package com.andika.temudecision.data.repository

import com.andika.temudecision.data.dao.*
import com.andika.temudecision.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DecisionRepository @Inject constructor(
    private val categoryDao: CategoryDao,
    private val criterionDao: CriterionDao,
    private val alternativeDao: AlternativeDao,
    private val alternativeValueDao: AlternativeValueDao,
    private val decisionResultDao: DecisionResultDao
) {
    fun getAllCategories(): Flow<List<CategoryEntity>> = categoryDao.getAllCategories()
    
    suspend fun insertCategory(category: CategoryEntity) = categoryDao.insertCategory(category)
    
    fun getCriteria(categoryId: Long): Flow<List<CriterionEntity>> = criterionDao.getCriteriaByCategoryId(categoryId)
    
    fun getAlternatives(categoryId: Long): Flow<List<AlternativeEntity>> = alternativeDao.getAlternativesByCategoryId(categoryId)
    
    suspend fun getValues(categoryId: Long): List<AlternativeValueEntity> = alternativeValueDao.getValuesByCategoryId(categoryId)

    suspend fun insertAlternative(alternative: AlternativeEntity) = alternativeDao.insertAlternative(alternative)
    
    suspend fun insertCriterion(criterion: CriterionEntity) = criterionDao.insertCriterion(criterion)
    
    suspend fun insertValue(value: AlternativeValueEntity) = alternativeValueDao.insertValue(value)

    suspend fun saveResult(result: DecisionResultEntity) = decisionResultDao.insertResult(result)

    fun getHistory(): Flow<List<DecisionResultEntity>> = decisionResultDao.getAllResults()
}
