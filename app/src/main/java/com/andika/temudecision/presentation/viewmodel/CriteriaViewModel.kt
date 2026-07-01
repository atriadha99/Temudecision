package com.andika.temudecision.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andika.temudecision.data.entity.CriteriaEntity
import com.andika.temudecision.data.repository.SDSSRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CriteriaViewModel @Inject constructor(
    private val repository: SDSSRepository
) : ViewModel() {

    private val _criteria = MutableStateFlow<List<CriteriaEntity>>(emptyList())
    val criteria: StateFlow<List<CriteriaEntity>> = _criteria.asStateFlow()

    private val _totalWeight = MutableStateFlow(0.0)
    val totalWeight: StateFlow<Double> = _totalWeight.asStateFlow()

    fun loadCriteria(studyId: String) {
        viewModelScope.launch {
            repository.getCriteriaByStudy(studyId).collectLatest { list ->
                _criteria.value = list
                _totalWeight.value = list.sumOf { it.weight }
            }
        }
    }

    fun addCriterion(studyId: String, name: String, weight: Double, type: String) {
        viewModelScope.launch {
            repository.insertCriteria(
                CriteriaEntity(
                    studyId = studyId,
                    name = name,
                    weight = weight,
                    type = type
                )
            )
        }
    }

    fun deleteCriterion(criteria: CriteriaEntity) {
        viewModelScope.launch {
            repository.deleteCriteria(criteria)
        }
    }
    
    fun normalizeWeights() {
        if (_criteria.value.isEmpty()) return
        val currentTotal = _totalWeight.value
        if (currentTotal == 0.0) return
        
        viewModelScope.launch {
            _criteria.value.forEach { crit ->
                repository.insertCriteria(crit.copy(weight = crit.weight / currentTotal))
            }
        }
    }
}
