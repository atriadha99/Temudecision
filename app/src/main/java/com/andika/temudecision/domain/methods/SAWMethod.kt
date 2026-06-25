package com.andika.temudecision.domain.methods

import com.andika.temudecision.domain.model.DSSAlternative
import com.andika.temudecision.domain.model.DSSCriterion
import com.andika.temudecision.domain.model.RankingResult

class SAWMethod {
    fun calculate(
        alternatives: List<DSSAlternative>,
        criteria: List<DSSCriterion>
    ): List<RankingResult> {
        if (alternatives.isEmpty() || criteria.isEmpty()) return emptyList()

        // 1. Find Max/Min for normalization
        val maxValues = mutableMapOf<Long, Double>()
        val minValues = mutableMapOf<Long, Double>()

        criteria.forEach { criterion ->
            val values = alternatives.map { it.scores[criterion.id] ?: 0.0 }
            maxValues[criterion.id] = values.maxOrNull() ?: 1.0
            minValues[criterion.id] = values.minOrNull() ?: 0.0
        }

        // 2. Normalize and calculate final score
        return alternatives.map { alt ->
            var totalScore = 0.0
            criteria.forEach { criterion ->
                val x = alt.scores[criterion.id] ?: 0.0
                val r = if (criterion.isBenefit) {
                    x / (maxValues[criterion.id] ?: 1.0)
                } else {
                    (minValues[criterion.id] ?: 0.0) / x
                }
                totalScore += criterion.weight * r
            }
            RankingResult(alt.id, alt.name, totalScore)
        }.sortedByDescending { it.score }
            .mapIndexed { index, result -> result.copy(rank = index + 1) }
    }
}
