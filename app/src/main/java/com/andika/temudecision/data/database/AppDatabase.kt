package com.andika.temudecision.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andika.temudecision.data.dao.*
import com.andika.temudecision.data.entity.*

@Database(
    entities = [
        StudyEntity::class,
        CriteriaEntity::class,
        AlternativeEntity::class,
        ScoreEntity::class,
        CalculationEntity::class
    ],
    version = 2, // Incremented version because of schema change
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studyDao(): StudyDao
    abstract fun criteriaDao(): CriteriaDao
    abstract fun alternativeDao(): AlternativeDao
    abstract fun scoreDao(): ScoreDao
    abstract fun calculationDao(): CalculationDao
}
