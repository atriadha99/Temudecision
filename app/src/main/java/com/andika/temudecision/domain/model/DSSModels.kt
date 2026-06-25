package com.andika.temudecision.domain.model

data class DSSAlternative(
    val id: Long,
    val name: String,
    val scores: Map<Long, Double> // Criterion ID to value
)

data class DSSCriterion(
    val id: Long,
    val name: String,
    val weight: Double,
    val isBenefit: Boolean
)

data class RankingResult(
    val alternativeId: Long,
    val alternativeName: String,
    val score: Double,
    val rank: Int = 0
)

data class StepDetail(
    val title: String,
    val description: String,
    val matrix: List<List<String>> = emptyList()
)
