package com.andika.temudecision.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "alternatives",
    foreignKeys = [ForeignKey(
        entity = StudyEntity::class,
        parentColumns = ["id"],
        childColumns = ["study_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class AlternativeEntity(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    @ColumnInfo(name = "study_id") val studyId: String,
    val name: String,
    val description: String = "",
    val category: String = ""
)
