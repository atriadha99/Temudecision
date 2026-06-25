package com.andika.temudecision.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andika.temudecision.data.entity.*
import com.andika.temudecision.data.repository.DecisionRepository
import com.andika.temudecision.domain.methods.*
import com.andika.temudecision.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DecisionViewModel @Inject constructor(
    private val repository: DecisionRepository
) : ViewModel() {

    val categories: StateFlow<List<CategoryEntity>> = repository.getAllCategories().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    private val _criteria = MutableStateFlow<List<CriterionEntity>>(emptyList())
    val criteria: StateFlow<List<CriterionEntity>> = _criteria

    private val _alternatives = MutableStateFlow<List<AlternativeEntity>>(emptyList())
    val alternatives: StateFlow<List<AlternativeEntity>> = _alternatives

    private val _rankingResults = MutableStateFlow<List<RankingResult>>(emptyList())
    val rankingResults: StateFlow<List<RankingResult>> = _rankingResults

    fun loadCategoryData(categoryId: Long) {
        viewModelScope.launch {
            repository.getCriteria(categoryId).collect { _criteria.value = it }
        }
        viewModelScope.launch {
            repository.getAlternatives(categoryId).collect { _alternatives.value = it }
        }
    }

    fun addCategory(name: String, icon: String) {
        viewModelScope.launch {
            repository.insertCategory(CategoryEntity(name = name, icon = icon))
        }
    }

    fun addCriterion(categoryId: Long, name: String, weight: Double, isBenefit: Boolean) {
        viewModelScope.launch {
            repository.insertCriterion(CriterionEntity(categoryId = categoryId, name = name, weight = weight, isBenefit = isBenefit))
        }
    }

    fun addAlternative(categoryId: Long, name: String) {
        viewModelScope.launch {
            repository.insertAlternative(AlternativeEntity(categoryId = categoryId, name = name))
        }
    }

    fun calculate(
        categoryId: Long,
        method: String
    ) {
        viewModelScope.launch {
            val currentCriteria = _criteria.value
            val currentAlternatives = _alternatives.value
            val currentValues = repository.getValues(categoryId)

            val dssCriteria = currentCriteria.map { DSSCriterion(it.id, it.name, it.weight, it.isBenefit) }
            val dssAlternatives = currentAlternatives.map { alt ->
                val scores = currentValues.filter { it.alternativeId == alt.id }
                    .associate { it.criterionId to it.value }
                DSSAlternative(alt.id, alt.name, scores)
            }

            val results = when (method) {
                "SAW" -> SAWMethod().calculate(dssAlternatives, dssCriteria)
                "WP" -> WPMethod().calculate(dssAlternatives, dssCriteria)
                "TOPSIS" -> TOPSISMethod().calculate(dssAlternatives, dssCriteria)
                "MOORA" -> MOORAMethod().calculate(dssAlternatives, dssCriteria)
                "SMART" -> SMARTMethod().calculate(dssAlternatives, dssCriteria)
                "Profile Matching" -> {
                    val targets = dssCriteria.associate { it.id to 3.0 } // Default target
                    ProfileMatchingMethod().calculate(dssAlternatives, dssCriteria, targets)
                }
                else -> emptyList()
            }
            
            _rankingResults.value = results
        }
    }
}
