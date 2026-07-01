package com.andika.temudecision.domain.methods

import com.andika.temudecision.domain.model.DSSAlternative
import com.andika.temudecision.domain.model.DSSCriterion
import com.andika.temudecision.domain.model.RankingResult

class AHPMethod {
    // AHP in this context applies the weights derived from pairwise comparison
    // and uses a SAW-like approach for the final ranking.
    fun calculate(
        alternatives: List<DSSAlternative>,
        criteria: List<DSSCriterion>
    ): List<RankingResult> {
        if (alternatives.isEmpty() || criteria.isEmpty()) return emptyList()

        // 1. Normalization (Linear scale)
        val maxValues = criteria.associate { criterion ->
            criterion.id to (alternatives.maxOfOrNull { it.scores[criterion.id] ?: 0.0 } ?: 1.0)
        }

        // 2. Ranking
        return alternatives.map { alt ->
            var totalScore = 0.0
            criteria.forEach { criterion ->
                val x = alt.scores[criterion.id] ?: 0.0
                val max = maxValues[criterion.id] ?: 1.0
                val normalized = if (max == 0.0) 0.0 else x / max
                totalScore += normalized * criterion.weight
            }
            RankingResult(alt.id, alt.name, totalScore)
        }.sortedByDescending { it.score }
            .mapIndexed { index, result -> result.copy(rank = index + 1) }
    }
}
