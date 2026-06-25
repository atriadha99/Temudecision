package com.andika.temudecision.di

import android.content.Context
import androidx.room.Room
import com.andika.temudecision.data.database.AppDatabase
import com.andika.temudecision.data.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "smart_choice_db"
        ).build()
    }

    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao()

    @Provides
    fun provideCriterionDao(db: AppDatabase): CriterionDao = db.criterionDao()

    @Provides
    fun provideAlternativeDao(db: AppDatabase): AlternativeDao = db.alternativeDao()

    @Provides
    fun provideAlternativeValueDao(db: AppDatabase): AlternativeValueDao = db.alternativeValueDao()

    @Provides
    fun provideDecisionResultDao(db: AppDatabase): DecisionResultDao = db.decisionResultDao()
}
