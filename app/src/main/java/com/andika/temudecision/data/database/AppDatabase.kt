package com.andika.temudecision.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andika.temudecision.data.dao.*
import com.andika.temudecision.data.entity.*

@Database(
    entities = [
        CategoryEntity::class,
        CriterionEntity::class,
        AlternativeEntity::class,
        AlternativeValueEntity::class,
        DecisionResultEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun criterionDao(): CriterionDao
    abstract fun alternativeDao(): AlternativeDao
    abstract fun alternativeValueDao(): AlternativeValueDao
    abstract fun decisionResultDao(): DecisionResultDao
}
