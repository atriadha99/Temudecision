package com.andika.temudecision.domain.methods

import com.andika.temudecision.domain.model.DSSAlternative
import com.andika.temudecision.domain.model.DSSCriterion
import com.andika.temudecision.domain.model.RankingResult
import kotlin.math.pow

class WPMethod {
    fun calculate(
        alternatives: List<DSSAlternative>,
        criteria: List<DSSCriterion>
    ): List<RankingResult> {
        if (alternatives.isEmpty() || criteria.isEmpty()) return emptyList()

        // 1. Normalize weights
        val totalWeight = criteria.sumOf { it.weight }
        val normWeights = criteria.associate { 
            it.id to (it.weight / totalWeight) * (if (it.isBenefit) 1 else -1)
        }

        // 2. Calculate S vector
        val sVector = alternatives.map { alt ->
            var s = 1.0
            criteria.forEach { criterion ->
                val x = alt.scores[criterion.id] ?: 1.0
                val w = normWeights[criterion.id] ?: 0.0
                s *= x.pow(w)
            }
            alt.id to s
        }

        val totalS = sVector.sumOf { it.second }

        // 3. Calculate V vector (ranking)
        return alternatives.map { alt ->
            val s = sVector.first { it.first == alt.id }.second
            val v = s / totalS
            RankingResult(alt.id, alt.name, v)
        }.sortedByDescending { it.score }
            .mapIndexed { index, result -> result.copy(rank = index + 1) }
    }
}
