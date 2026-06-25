package com.andika.temudecision.domain.methods

import com.andika.temudecision.domain.model.DSSAlternative
import com.andika.temudecision.domain.model.DSSCriterion
import com.andika.temudecision.domain.model.RankingResult

class ProfileMatchingMethod {
    // Gap weight mapping
    private fun getGapWeight(gap: Double): Double {
        return when (gap) {
            0.0 -> 5.0
            1.0 -> 4.5
            -1.0 -> 4.0
            2.0 -> 3.5
            -2.0 -> 3.0
            3.0 -> 2.5
            -3.0 -> 2.0
            4.0 -> 1.5
            -4.0 -> 1.0
            else -> 1.0
        }
    }

    fun calculate(
        alternatives: List<DSSAlternative>,
        criteria: List<DSSCriterion>,
        targetValues: Map<Long, Double> // Target for each criterion
    ): List<RankingResult> {
        if (alternatives.isEmpty() || criteria.isEmpty()) return emptyList()

        return alternatives.map { alt ->
            var totalWeight = 0.0
            criteria.forEach { criterion ->
                val target = targetValues[criterion.id] ?: 3.0 // Default target 3
                val actual = alt.scores[criterion.id] ?: 0.0
                val gap = actual - target
                totalWeight += getGapWeight(gap)
            }
            val finalScore = totalWeight / criteria.size
            RankingResult(alt.id, alt.name, finalScore)
        }.sortedByDescending { it.score }
            .mapIndexed { index, result -> result.copy(rank = index + 1) }
    }
}
