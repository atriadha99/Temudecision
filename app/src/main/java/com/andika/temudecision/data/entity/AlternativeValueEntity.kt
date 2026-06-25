package com.andika.temudecision.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "alternative_values",
    foreignKeys = [
        ForeignKey(
            entity = AlternativeEntity::class,
            parentColumns = ["id"],
            childColumns = ["alternativeId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CriterionEntity::class,
            parentColumns = ["id"],
            childColumns = ["criterionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AlternativeValueEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val alternativeId: Long,
    val criterionId: Long,
    val value: Double
)
