package com.andika.temudecision.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "studies")
data class StudyEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val icon: String = "📁",
    val description: String,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)
