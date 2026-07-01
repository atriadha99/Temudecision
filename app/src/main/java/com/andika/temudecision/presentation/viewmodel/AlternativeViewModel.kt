package com.andika.temudecision.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andika.temudecision.data.entity.AlternativeEntity
import com.andika.temudecision.data.repository.SDSSRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlternativeViewModel @Inject constructor(
    private val repository: SDSSRepository
) : ViewModel() {

    private val _alternatives = MutableStateFlow<List<AlternativeEntity>>(emptyList())
    val alternatives: StateFlow<List<AlternativeEntity>> = _alternatives.asStateFlow()

    fun loadAlternatives(studyId: String) {
        viewModelScope.launch {
            repository.getAlternativesByStudy(studyId).collectLatest {
                _alternatives.value = it
            }
        }
    }

    fun addAlternative(studyId: String, name: String, category: String, description: String) {
        viewModelScope.launch {
            repository.insertAlternative(
                AlternativeEntity(
                    studyId = studyId,
                    name = name,
                    category = category,
                    description = description
                )
            )
        }
    }

    fun deleteAlternative(alternative: AlternativeEntity) {
        viewModelScope.launch {
            repository.deleteAlternative(alternative)
        }
    }
}
