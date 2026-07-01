package com.andika.temudecision.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andika.temudecision.data.repository.SDSSRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: SDSSRepository
) : ViewModel() {

    private val _studyCount = MutableStateFlow(0)
    val studyCount: StateFlow<Int> = _studyCount.asStateFlow()

    private val _criteriaCount = MutableStateFlow(0)
    val criteriaCount: StateFlow<Int> = _criteriaCount.asStateFlow()

    private val _alternativeCount = MutableStateFlow(0)
    val alternativeCount: StateFlow<Int> = _alternativeCount.asStateFlow()

    private val _calculationCount = MutableStateFlow(0)
    val calculationCount: StateFlow<Int> = _calculationCount.asStateFlow()

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            repository.getAllStudies().collectLatest { studies ->
                _studyCount.value = studies.size
                
                // For simplicity, we just count all criteria/alternatives across all studies
                // In a real app, this might be filtered by the active study
                var critCount = 0
                var altCount = 0
                var calcCount = 0
                
                studies.forEach { study ->
                    // These should ideally be done with a more efficient query
                    // but for a small local DB it's fine for now.
                    repository.getCriteriaByStudy(study.id).collectLatest { critCount += it.size }
                    repository.getAlternativesByStudy(study.id).collectLatest { altCount += it.size }
                    repository.getCalculationsByStudy(study.id).collectLatest { calcCount += it.size }
                }
                
                _criteriaCount.value = critCount
                _alternativeCount.value = altCount
                _calculationCount.value = calcCount
            }
        }
    }
}
