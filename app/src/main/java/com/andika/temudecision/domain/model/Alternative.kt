package com.andika.temudecision.domain.model

data class Alternative(
    val id: String,
    val studyId: String,
    val name: String,
    val description: String = "",
    val category: String = ""
)
