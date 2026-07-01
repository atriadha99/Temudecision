package com.andika.temudecision.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andika.temudecision.presentation.ui.theme.SDSSColors
import com.andika.temudecision.presentation.ui.theme.SDSSTypography
import com.andika.temudecision.presentation.viewmodel.ScoringViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoringMatrixScreen(
    studyId: String,
    viewModel: ScoringViewModel = hiltViewModel()
) {
    val criteria by viewModel.criteria.collectAsState()
    val alternatives by viewModel.alternatives.collectAsState()
    val scores by viewModel.scores.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    LaunchedEffect(studyId) {
        viewModel.loadData(studyId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Input Penilaian (Matrix)") },
                actions = {
                    Button(
                        onClick = { viewModel.saveScores() },
                        enabled = !isSaving,
                        colors = ButtonDefaults.buttonColors(containerColor = SDSSColors.Brand500),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Rounded.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Simpan")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Matriks Penilaian",
                style = SDSSTypography.headlineLarge
            )
            Text(
                "Isikan nilai untuk setiap alternatif pada setiap kriteria yang ada.",
                style = SDSSTypography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (criteria.isEmpty() || alternatives.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Lengkapi Kriteria & Alternatif terlebih dahulu.", style = SDSSTypography.bodyLarge)
                }
            } else {
                Box(modifier = Modifier.weight(1f)) {
                    ScoringTable(
                        criteria = criteria,
                        alternatives = alternatives,
                        scores = scores,
                        onScoreChange = { altId, critId, value ->
                            viewModel.updateScore(altId, critId, value)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ScoringTable(
    criteria: List<com.andika.temudecision.data.entity.CriteriaEntity>,
    alternatives: List<com.andika.temudecision.data.entity.AlternativeEntity>,
    scores: List<com.andika.temudecision.data.entity.ScoreEntity>,
    onScoreChange: (String, String, Double) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.horizontalScroll(scrollState)) {
        // Header Row
        Row(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)) {
            TableCell("Alt / Kriteria", width = 150.dp, isHeader = true)
            criteria.forEach { criterion ->
                TableCell(criterion.name, width = 100.dp, isHeader = true)
            }
        }

        // Data Rows
        LazyColumn {
            items(alternatives) { alternative ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TableCell(alternative.name, width = 150.dp)
                    criteria.forEach { criterion ->
                        val score = scores.find { it.alternativeId == alternative.id && it.criteriaId == criterion.id }
                        val scoreValue = score?.value?.toString() ?: ""
                        
                        Box(modifier = Modifier.width(100.dp).padding(4.dp)) {
                            OutlinedTextField(
                                value = scoreValue,
                                onValueChange = { 
                                    it.toDoubleOrNull()?.let { value ->
                                        onScoreChange(alternative.id, criterion.id, value)
                                    }
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = SDSSTypography.bodyMedium.copy(textAlign = TextAlign.Center),
                                singleLine = true,
                                shape = MaterialTheme.shapes.small
                            )
                        }
                    }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        }
    }
}

@Composable
fun TableCell(text: String, width: Dp, isHeader: Boolean = false) {
    Box(
        modifier = Modifier
            .width(width)
            .padding(12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            style = if (isHeader) SDSSTypography.titleMedium else SDSSTypography.bodyMedium,
            fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1
        )
    }
}
