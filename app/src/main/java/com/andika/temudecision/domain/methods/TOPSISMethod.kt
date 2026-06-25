package com.andika.temudecision.domain.methods

import com.andika.temudecision.domain.model.DSSAlternative
import com.andika.temudecision.domain.model.DSSCriterion
import com.andika.temudecision.domain.model.RankingResult
import kotlin.math.sqrt

class TOPSISMethod {
    fun calculate(
        alternatives: List<DSSAlternative>,
        criteria: List<DSSCriterion>
    ): List<RankingResult> {
        if (alternatives.isEmpty() || criteria.isEmpty()) return emptyList()

        // 1. Calculate divider for normalization
        val dividers = criteria.associate { criterion ->
            val sumSq = alternatives.sumOf { (it.scores[criterion.id] ?: 0.0).let { v -> v * v } }
            criterion.id to sqrt(sumSq)
        }

        // 2. Weighted Normalized Matrix
        val weightedNormalized = alternatives.map { alt ->
            alt.id to criteria.associate { criterion ->
                val x = alt.scores[criterion.id] ?: 0.0
                val divider = dividers[criterion.id] ?: 1.0
                val norm = if (divider == 0.0) 0.0 else x / divider
                criterion.id to (norm * criterion.weight)
            }
        }

        // 3. Ideal Solutions
        val positiveIdeal = criteria.associate { criterion ->
            val values = weightedNormalized.map { it.second[criterion.id] ?: 0.0 }
            val best = if (criterion.isBenefit) values.maxOrNull() else values.minOrNull()
            criterion.id to (best ?: 0.0)
        }

        val negativeIdeal = criteria.associate { criterion ->
            val values = weightedNormalized.map { it.second[criterion.id] ?: 0.0 }
            val worst = if (criterion.isBenefit) values.minOrNull() else values.maxOrNull()
            criterion.id to (worst ?: 0.0)
        }

        // 4. Distances and Preference
        return alternatives.map { alt ->
            val altWeighted = weightedNormalized.first { it.first == alt.id }.second
            
            var dPlus = 0.0
            var dMinus = 0.0
            
            criteria.forEach { criterion ->
                val v = altWeighted[criterion.id] ?: 0.0
                val ap = positiveIdeal[criterion.id] ?: 0.0
                val am = negativeIdeal[criterion.id] ?: 0.0
                
                dPlus += (v - ap).let { it * it }
                dMinus += (v - am).let { it * it }
            }
            
            dPlus = sqrt(dPlus)
            dMinus = sqrt(dMinus)
            
            val preference = if (dPlus + dMinus == 0.0) 0.0 else dMinus / (dPlus + dMinus)
            
            RankingResult(alt.id, alt.name, preference)
        }.sortedByDescending { it.score }
            .mapIndexed { index, result -> result.copy(rank = index + 1) }
    }
}
