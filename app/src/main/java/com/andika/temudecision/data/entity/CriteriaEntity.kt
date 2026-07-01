package com.andika.temudecision.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "criteria",
    foreignKeys = [ForeignKey(
        entity = StudyEntity::class,
        parentColumns = ["id"],
        childColumns = ["study_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class CriteriaEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "study_id") val studyId: String,
    val name: String,
    val weight: Double,  // 0.0 - 1.0
    val type: String,    // "benefit" | "cost"
    @ColumnInfo(name = "target_value") val targetValue: Double = 3.0,
    @ColumnInfo(name = "is_core_factor") val isCoreFactor: Boolean = true
)
