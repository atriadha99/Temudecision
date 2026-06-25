package com.andika.temudecision.presentation.screen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andika.temudecision.domain.methods.AIDecisionAssistant
import com.andika.temudecision.data.entity.CriterionEntity
import com.andika.temudecision.presentation.viewmodel.DecisionViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(
    categoryId: Long,
    categoryName: String,
    onBack: () -> Unit,
    viewModel: DecisionViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Kriteria", "Alternatif", "Perhitungan")

    LaunchedEffect(categoryId) {
        viewModel.loadCategoryData(categoryId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoryName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTab) {
                0 -> CriteriaTab(categoryId, viewModel)
                1 -> AlternativesTab(categoryId, viewModel)
                2 -> CalculationTab(categoryId, viewModel)
            }
        }
    }
}

@Composable
fun CriteriaTab(categoryId: Long, viewModel: DecisionViewModel) {
    val criteria by viewModel.criteria.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Daftar Kriteria", style = MaterialTheme.typography.titleMedium)
            Button(onClick = { /* TODO: Implement AHP Weight Selection */ }) {
                Text("Gunakan AHP")
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(criteria) { criterion ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = criterion.name, style = MaterialTheme.typography.titleMedium)
                                Text(text = "Bobot: ${criterion.weight}", style = MaterialTheme.typography.bodySmall)
                            }
                            Text(
                                text = if (criterion.isBenefit) "Benefit" else "Cost",
                                color = if (criterion.isBenefit) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
            
            FloatingActionButton(
                onClick = { viewModel.addCriterion(categoryId, "Kriteria Baru", 1.0, true) },
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Criterion")
            }
        }
    }
}

@Composable
fun AlternativesTab(categoryId: Long, viewModel: DecisionViewModel) {
    val alternatives by viewModel.alternatives.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(alternatives) { alternative ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = alternative.name,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { viewModel.addAlternative(categoryId, "Alternatif Baru") },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Alternative")
        }
    }
}

@Composable
fun CalculationTab(categoryId: Long, viewModel: DecisionViewModel) {
    val results by viewModel.rankingResults.collectAsState()
    val methods = listOf("SAW", "WP", "TOPSIS", "MOORA", "SMART", "Profile Matching")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Pilih Metode:", style = MaterialTheme.typography.titleMedium)
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            methods.forEach { method ->
                Button(onClick = { viewModel.calculate(categoryId, method) }) {
                    Text(method)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Hasil Ranking:", style = MaterialTheme.typography.titleMedium)
        
        if (results.isNotEmpty()) {
            val explanation = AIDecisionAssistant().explainResult(results, "Metode Terpilih")
            Text(
                text = explanation,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.secondary
            )
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(results) { result ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${result.rank}. ${result.alternativeName}")
                        Text(String.format(Locale.getDefault(), "%.4f", result.score))
                    }
                }
            }
        }
    }
}
