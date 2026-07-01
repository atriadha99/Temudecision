package com.andika.temudecision.domain.model

data class CalculationResult(
    val id: String,
    val studyId: String,
    val method: String,
    val resultJson: String,
    val createdAt: Long
)
