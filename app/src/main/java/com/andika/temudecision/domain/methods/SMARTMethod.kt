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

        val maxValues = mutableMapOf<Long, Double>()
        val minValues = mutableMapOf<Long, Double>()

        criteria.forEach { criterion ->
            val values = alternatives.map { it.scores[criterion.id] ?: 0.0 }
            maxValues[criterion.id] = values.maxOrNull() ?: 1.0
            minValues[criterion.id] = values.minOrNull() ?: 0.0
        }

        return alternatives.map { alt ->
            var totalScore = 0.0
            criteria.forEach { criterion ->
                val x = alt.scores[criterion.id] ?: 0.0
                val min = minValues[criterion.id] ?: 0.0
                val max = maxValues[criterion.id] ?: 1.0
                
                // Utility: u_ij = (x_ij - x_min) / (x_max - x_min)
                val u = if (max - min != 0.0) {
                    if (criterion.isBenefit) {
                        (x - min) / (max - min)
                    } else {
                        (max - x) / (max - min)
                    }
                } else {
                    1.0
                }
                totalScore += u * criterion.weight
            }
            RankingResult(alt.id, alt.name, totalScore)
        }.sortedByDescending { it.score }
            .mapIndexed { index, result -> result.copy(rank = index + 1) }
    }
}
