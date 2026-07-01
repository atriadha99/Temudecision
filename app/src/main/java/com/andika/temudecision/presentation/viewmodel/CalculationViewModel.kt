package com.andika.temudecision.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andika.temudecision.data.repository.SDSSRepository
import com.andika.temudecision.domain.model.*
import com.andika.temudecision.domain.usecase.*
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalculationViewModel @Inject constructor(
    private val repository: SDSSRepository,
    private val calculateSAW: CalculateSAW,
    private val calculateWP: CalculateWP,
    private val calculateTOPSIS: CalculateTOPSIS,
    private val calculateSMART: CalculateSMART,
    private val calculateProfileMatching: CalculateProfileMatching,
    private val calculateAHP: CalculateAHP,
    private val calculateMOORA: CalculateMOORA
) : ViewModel() {

    private val _rankingResults = MutableStateFlow<List<RankingResult>>(emptyList())
    val rankingResults: StateFlow<List<RankingResult>> = _rankingResults.asStateFlow()

    private val _selectedMethod = MutableStateFlow("SAW")
    val selectedMethod: StateFlow<String> = _selectedMethod.asStateFlow()

    fun calculate(studyId: String, method: String) {
        _selectedMethod.value = method
        viewModelScope.launch {
            val criteriaEntities = repository.getCriteriaByStudy(studyId).first()
            val alternativeEntities = repository.getAlternativesByStudy(studyId).first()
            val scoreEntities = repository.getScoresByStudy(studyId).first()

            val criteria = criteriaEntities.map { 
                Criteria(it.id, it.studyId, it.name, it.weight, it.type, it.targetValue, it.isCoreFactor) 
            }
            val alternatives = alternativeEntities.map { 
                Alternative(it.id, it.studyId, it.name, it.description, it.category) 
            }
            val scores = scoreEntities.map { 
                Score(it.id, it.alternativeId, it.criteriaId, it.value) 
            }

            val results = when (method) {
                "SAW" -> calculateSAW(alternatives, criteria, scores)
                "WP" -> calculateWP(alternatives, criteria, scores)
                "TOPSIS" -> calculateTOPSIS(alternatives, criteria, scores)
                "SMART" -> calculateSMART(alternatives, criteria, scores)
                "Profile Matching" -> calculateProfileMatching(alternatives, criteria, scores)
                "AHP" -> calculateAHP(alternatives, criteria, scores)
                "MOORA" -> calculateMOORA(alternatives, criteria, scores)
                else -> emptyList()
            }
            _rankingResults.value = results
        }
    }
}
