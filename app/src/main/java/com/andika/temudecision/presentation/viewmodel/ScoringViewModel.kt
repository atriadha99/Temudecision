package com.andika.temudecision.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andika.temudecision.data.entity.*
import com.andika.temudecision.data.repository.SDSSRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoringViewModel @Inject constructor(
    private val repository: SDSSRepository
) : ViewModel() {

    private val _criteria = MutableStateFlow<List<CriteriaEntity>>(emptyList())
    val criteria: StateFlow<List<CriteriaEntity>> = _criteria.asStateFlow()

    private val _alternatives = MutableStateFlow<List<AlternativeEntity>>(emptyList())
    val alternatives: StateFlow<List<AlternativeEntity>> = _alternatives.asStateFlow()

    private val _scores = MutableStateFlow<List<ScoreEntity>>(emptyList())
    val scores: StateFlow<List<ScoreEntity>> = _scores.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    fun loadData(studyId: String) {
        viewModelScope.launch {
            repository.getCriteriaByStudy(studyId).collectLatest { _criteria.value = it }
        }
        viewModelScope.launch {
            repository.getAlternativesByStudy(studyId).collectLatest { _alternatives.value = it }
        }
        viewModelScope.launch {
            repository.getScoresByStudy(studyId).collectLatest { _scores.value = it }
        }
    }

    fun updateScore(alternativeId: String, criteriaId: String, value: Double) {
        val currentScores = _scores.value.toMutableList()
        val existingIndex = currentScores.indexOfFirst { it.alternativeId == alternativeId && it.criteriaId == criteriaId }
        
        if (existingIndex != -1) {
            currentScores[existingIndex] = currentScores[existingIndex].copy(value = value)
        } else {
            currentScores.add(ScoreEntity(alternativeId = alternativeId, criteriaId = criteriaId, value = value))
        }
        _scores.value = currentScores
    }

    fun saveScores() {
        viewModelScope.launch {
            _isSaving.value = true
            repository.insertScores(_scores.value)
            _isSaving.value = false
        }
    }
}
