package com.andika.temudecision.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "decision_results",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DecisionResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val categoryId: Long,
    val date: Long = System.currentTimeMillis(),
    val methodUsed: String,
    val bestAlternativeName: String,
    val detailsJson: String // Serialized details for the explanation
)
