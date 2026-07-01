package com.andika.temudecision.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andika.temudecision.data.entity.StudyEntity
import com.andika.temudecision.data.repository.SDSSRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudyViewModel @Inject constructor(
    private val repository: SDSSRepository
) : ViewModel() {

    val studies: StateFlow<List<StudyEntity>> = repository.getAllStudies()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addStudy(name: String, description: String) {
        viewModelScope.launch {
            repository.insertStudy(
                StudyEntity(
                    name = name,
                    description = description
                )
            )
        }
    }

    fun deleteStudy(study: StudyEntity) {
        viewModelScope.launch {
            repository.deleteStudy(study)
        }
    }
}
