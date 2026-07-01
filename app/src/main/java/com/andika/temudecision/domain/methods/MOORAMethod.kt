package com.andika.temudecision.domain.methods

import com.andika.temudecision.domain.model.DSSAlternative
import com.andika.temudecision.domain.model.DSSCriterion
import com.andika.temudecision.domain.model.RankingResult
import kotlin.math.sqrt

class MOORAMethod {
    fun calculate(
        alternatives: List<DSSAlternative>,
        criteria: List<DSSCriterion>
    ): List<RankingResult> {
        if (alternatives.isEmpty() || criteria.isEmpty()) return emptyList()

        // 1. Normalization: r_ij = x_ij / sqrt(sum(x_ij^2))
        val denominators = criteria.associate { criterion ->
            val sumSq = alternatives.sumOf { (it.scores[criterion.id] ?: 0.0).let { v -> v * v } }
            criterion.id to sqrt(sumSq)
        }

        // 2. Calculation of Yi = sum(benefit) - sum(cost)
        return alternatives.map { alt ->
            var benefitSum = 0.0
            var costSum = 0.0
            
            criteria.forEach { criterion ->
                val x = alt.scores[criterion.id] ?: 0.0
                val den = denominators[criterion.id] ?: 1.0
                val normalized = if (den == 0.0) 0.0 else x / den
                val weighted = normalized * criterion.weight
                
                if (criterion.isBenefit) {
                    benefitSum += weighted
                } else {
                    costSum += weighted
                }
            }
            
            RankingResult(alt.id, alt.name, benefitSum - costSum)
        }.sortedByDescending { it.score }
            .mapIndexed { index, result -> result.copy(rank = index + 1) }
    }
}
