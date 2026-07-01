package com.andika.temudecision.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "calculations",
    foreignKeys = [ForeignKey(
        entity = StudyEntity::class,
        parentColumns = ["id"],
        childColumns = ["study_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class CalculationEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "study_id") val studyId: String,
    val method: String,
    val result: String, // JSON string
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)
