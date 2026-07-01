package com.andika.temudecision.domain.model

data class Criteria(
    val id: String,
    val studyId: String,
    val name: String,
    val weight: Double,
    val type: String, // "benefit" | "cost"
    val targetValue: Double = 3.0,
    val isCoreFactor: Boolean = true
)
