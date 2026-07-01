package com.andika.temudecision.domain.methods

import com.andika.temudecision.domain.model.DSSAlternative
import com.andika.temudecision.domain.model.DSSCriterion
import com.andika.temudecision.domain.model.RankingResult

class ProfileMatchingMethod {
    
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
        targetValues: Map<Long, Double> = emptyMap()
    ): List<RankingResult> {
        if (alternatives.isEmpty() || criteria.isEmpty()) return emptyList()

        return alternatives.map { alt ->
            var coreFactor = 0.0
            var secondaryFactor = 0.0
            var cfCount = 0
            var sfCount = 0
            
            criteria.forEach { criterion ->
                val x = alt.scores[criterion.id] ?: 0.0
                val target = targetValues[criterion.id] ?: 3.0 // Default target
                val gap = x - target
                val weight = getGapWeight(gap)
                
                // Assuming first half are core factors for simplicity if not specified
                // In a real app, this would be a property of the criterion
                val isCore = criteria.indexOf(criterion) < criteria.size / 2
                
                if (isCore) {
                    coreFactor += weight
                    cfCount++
                } else {
                    secondaryFactor += weight
                    sfCount++
                }
            }
            
            val ncf = if (cfCount > 0) coreFactor / cfCount else 0.0
            val nsf = if (sfCount > 0) secondaryFactor / sfCount else 0.0
            
            // Total = 60% Core + 40% Secondary
            val total = (0.6 * ncf) + (0.4 * nsf)
            
            RankingResult(alt.id, alt.name, total)
        }.sortedByDescending { it.score }
            .mapIndexed { index, result -> result.copy(rank = index + 1) }
    }
}
