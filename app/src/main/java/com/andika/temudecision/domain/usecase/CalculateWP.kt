package com.andika.temudecision.domain.usecase

import com.andika.temudecision.domain.methods.WPMethod
import com.andika.temudecision.domain.model.*
import javax.inject.Inject

class CalculateWP @Inject constructor() {
    private val method = WPMethod()

    operator fun invoke(
        alternatives: List<Alternative>,
        criteria: List<Criteria>,
        scores: List<Score>
    ): List<RankingResult> {
        val dssAlternatives = alternatives.map { alt ->
            DSSAlternative(
                id = alt.id.hashCode().toLong(),
                name = alt.name,
                scores = scores.filter { it.alternativeId == alt.id }
                    .associate { it.criteriaId.hashCode().toLong() to it.value }
            )
        }

        val dssCriteria = criteria.map { crit ->
            DSSCriterion(
                id = crit.id.hashCode().toLong(),
                name = crit.name,
                weight = crit.weight,
                isBenefit = crit.type.lowercase() == "benefit"
            )
        }

        return method.calculate(dssAlternatives, dssCriteria)
    }
}
