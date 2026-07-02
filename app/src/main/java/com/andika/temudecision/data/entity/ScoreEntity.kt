package com.andika.temudecision.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "scores",
    foreignKeys = [
        ForeignKey(
            entity = AlternativeEntity::class,
            parentColumns = ["id"],
            childColumns = ["alternative_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CriteriaEntity::class,
            parentColumns = ["id"],
            childColumns = ["criteria_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["alternative_id", "criteria_id"], unique = true)]
)
data class ScoreEntity(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    @ColumnInfo(name = "alternative_id") val alternativeId: String,
    @ColumnInfo(name = "criteria_id") val criteriaId: String,
    val value: Double = 0.0
)
