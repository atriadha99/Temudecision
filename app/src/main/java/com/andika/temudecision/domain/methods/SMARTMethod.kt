package com.andika.temudecision.domain.methods

import com.andika.temudecision.domain.model.DSSAlternative
import com.andika.temudecision.domain.model.DSSCriterion
import com.andika.temudecision.domain.model.RankingResult

class SMARTMethod {
    fun calculate(
        alternatives: List<DSSAlternative>,
        criteria: List<DSSCriterion>
    ): List<RankingResult> {
        if (alternatives.isEmpty() || criteria.isEmpty()) return emptyList()

        // 1. Normalize weights
        val totalWeight = criteria.sumOf { it.weight }
        val normWeights = criteria.associate { it.id to (it.weight / totalWeight) }

        // 2. Find Min/Max for utility values
        val maxValues = mutableMapOf<Long, Double>()
        val minValues = mutableMapOf<Long, Double>()
        criteria.forEach { criterion ->
            val values = alternatives.map { it.scores[criterion.id] ?: 0.0 }
            maxValues[criterion.id] = values.maxOrNull() ?: 1.0
            minValues[criterion.id] = values.minOrNull() ?: 0.0
        }

        // 3. Calculate Utility and Final Score
        return alternatives.map { alt ->
            var totalUtility = 0.0
            criteria.forEach { criterion ->
                val x = alt.scores[criterion.id] ?: 0.0
                val min = minValues[criterion.id] ?: 0.0
                val max = maxValues[criterion.id] ?: 1.0
                
                val utility = if (max == min) 1.0 else if (criterion.isBenefit) {
                    (x - min) / (max - min)
                } else {
                    (max - x) / (max - min)
                }
                
                totalUtility += (normWeights[criterion.id] ?: 0.0) * utility
            }
            RankingResult(alt.id, alt.name, totalUtility)
        }.sortedByDescending { it.score }
            .mapIndexed { index, result -> result.copy(rank = index + 1) }
    }
}
