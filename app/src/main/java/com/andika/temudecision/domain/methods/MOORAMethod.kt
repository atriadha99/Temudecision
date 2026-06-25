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

        // 1. Normalization
        val dividers = criteria.associate { criterion ->
            val sumSq = alternatives.sumOf { (it.scores[criterion.id] ?: 0.0).let { v -> v * v } }
            criterion.id to sqrt(sumSq)
        }

        // 2. Optimization Value
        return alternatives.map { alt ->
            var benefitSum = 0.0
            var costSum = 0.0
            
            criteria.forEach { criterion ->
                val x = alt.scores[criterion.id] ?: 0.0
                val divider = dividers[criterion.id] ?: 1.0
                val norm = if (divider == 0.0) 0.0 else x / divider
                val weighted = norm * criterion.weight
                
                if (criterion.isBenefit) benefitSum += weighted else costSum += weighted
            }
            
            RankingResult(alt.id, alt.name, benefitSum - costSum)
        }.sortedByDescending { it.score }
            .mapIndexed { index, result -> result.copy(rank = index + 1) }
    }
}
