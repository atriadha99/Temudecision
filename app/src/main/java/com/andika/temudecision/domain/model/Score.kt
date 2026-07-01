package com.andika.temudecision.domain.model

data class Score(
    val id: String,
    val alternativeId: String,
    val criteriaId: String,
    val value: Double
)
