package com.andika.temudecision.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val icon: String, // String representation of icon (e.g., emoji or resource name)
    val description: String = "",
    val isDefault: Boolean = false
)
