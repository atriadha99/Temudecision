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
            "sdss_db"
        ).fallbackToDestructiveMigration() // For development simplicity
        .build()
    }

    @Provides
    fun provideStudyDao(db: AppDatabase): StudyDao = db.studyDao()

    @Provides
    fun provideCriteriaDao(db: AppDatabase): CriteriaDao = db.criteriaDao()

    @Provides
    fun provideAlternativeDao(db: AppDatabase): AlternativeDao = db.alternativeDao()

    @Provides
    fun provideScoreDao(db: AppDatabase): ScoreDao = db.scoreDao()

    @Provides
    fun provideCalculationDao(db: AppDatabase): CalculationDao = db.calculationDao()
}
