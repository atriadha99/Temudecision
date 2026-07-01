package com.andika.temudecision.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andika.temudecision.data.entity.CriteriaEntity
import com.andika.temudecision.presentation.components.GlassCard
import com.andika.temudecision.presentation.ui.theme.SDSSColors
import com.andika.temudecision.presentation.ui.theme.SDSSTypography
import com.andika.temudecision.presentation.viewmodel.CriteriaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CriteriaManagementScreen(
    studyId: String,
    viewModel: CriteriaViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val criteria by viewModel.criteria.collectAsState()
    val totalWeight by viewModel.totalWeight.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(studyId) {
        viewModel.loadCriteria(studyId)
    }

    Scaffold(
        floatingActionButton = {
            if (selectedTab == 0) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = SDSSColors.Brand500,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = "Add Criterion")
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Daftar Kriteria") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("🧠 Matriks AHP") }
                )
            }

            when (selectedTab) {
                0 -> CriteriaListTab(
                    criteria = criteria,
                    totalWeight = totalWeight,
                    onDelete = { viewModel.deleteCriterion(it) },
                    onNormalize = { viewModel.normalizeWeights() }
                )
                1 -> AHPMatrixTab()
            }
        }
    }

    if (showAddDialog) {
        AddCriterionDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, weight, type ->
                viewModel.addCriterion(studyId, name, weight, type)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun CriteriaListTab(
    criteria: List<CriteriaEntity>,
    totalWeight: Double,
    onDelete: (CriteriaEntity) -> Unit,
    onNormalize: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Weight validation alert
        if (criteria.isNotEmpty()) {
            val isWeightValid = Math.abs(totalWeight - 1.0) < 0.001
            Surface(
                color = if (isWeightValid) Color(0xFFECFDF5) else Color(0xFFFFFBEB),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        if (isWeightValid) Icons.Rounded.CheckCircle else Icons.Rounded.Warning,
                        contentDescription = null,
                        tint = if (isWeightValid) SDSSColors.Success else SDSSColors.Warning
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            if (isWeightValid) "Jumlah bobot sudah ideal (1.0)" else "Jumlah bobot tidak sama dengan 1.0 (Total: ${String.format("%.2f", totalWeight)})",
                            style = SDSSTypography.titleMedium,
                            color = if (isWeightValid) Color(0xFF065F46) else Color(0xFF92400E)
                        )
                    }
                    if (!isWeightValid) {
                        TextButton(onClick = onNormalize) {
                            Text("Normalkan", color = Color(0xFFB45309))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(criteria) { criterion ->
                CriterionItem(criterion, onDelete = { onDelete(criterion) })
            }
        }
    }
}

@Composable
fun CriterionItem(criterion: CriteriaEntity, onDelete: () -> Unit) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(criterion.name, style = SDSSTypography.titleLarge)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = if (criterion.type.lowercase() == "benefit") Color(0xFFD1FAE5) else Color(0xFFFEE2E2),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            criterion.type.uppercase(),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = SDSSTypography.labelSmall,
                            color = if (criterion.type.lowercase() == "benefit") Color(0xFF065F46) else Color(0xFF991B1B)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Bobot: ${String.format("%.4f", criterion.weight)}",
                        style = SDSSTypography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Rounded.Delete, contentDescription = null, tint = SDSSColors.Error)
            }
        }
    }
}

@Composable
fun AHPMatrixTab() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Fitur AHP Matrix akan segera hadir", style = SDSSTypography.bodyLarge)
    }
}

@Composable
fun AddCriterionDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Double, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var weightStr by remember { mutableStateOf("0.25") }
    var type by remember { mutableStateOf("benefit") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Kriteria Baru") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Kriteria") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    value = weightStr,
                    onValueChange = { weightStr = it },
                    label = { Text("Bobot Awal (0-1)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                
                Text("Tipe Kriteria", style = SDSSTypography.labelLarge)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = type == "benefit", onClick = { type = "benefit" })
                    Text("Benefit")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(selected = type == "cost", onClick = { type = "cost" })
                    Text("Cost")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name, weightStr.toDoubleOrNull() ?: 0.0, type) },
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = SDSSColors.Brand500)
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
