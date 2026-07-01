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
import androidx.hilt.navigation.compose.hiltViewModel
import com.andika.temudecision.data.entity.AlternativeEntity
import com.andika.temudecision.presentation.components.GlassCard
import com.andika.temudecision.presentation.ui.theme.SDSSColors
import com.andika.temudecision.presentation.ui.theme.SDSSTypography
import com.andika.temudecision.presentation.viewmodel.AlternativeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlternativeManagementScreen(
    studyId: String,
    viewModel: AlternativeViewModel = hiltViewModel()
) {
    val alternatives by viewModel.alternatives.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(studyId) {
        viewModel.loadAlternatives(studyId)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = SDSSColors.Brand500,
                contentColor = Color.White
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add Alternative")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Manajemen Alternatif",
                style = SDSSTypography.headlineLarge
            )
            Text(
                "Daftar pilihan atau alternatif yang akan dibandingkan dalam studi kasus ini.",
                style = SDSSTypography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (alternatives.isEmpty()) {
                EmptyAlternativeState { showAddDialog = true }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(alternatives) { alternative ->
                        AlternativeItem(alternative, onDelete = { viewModel.deleteAlternative(alternative) })
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddAlternativeDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, cat, desc ->
                viewModel.addAlternative(studyId, name, cat, desc)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun AlternativeItem(alternative: AlternativeEntity, onDelete: () -> Unit) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(alternative.name, style = SDSSTypography.titleLarge)
                if (alternative.category.isNotBlank()) {
                    Surface(
                        color = SDSSColors.Brand50,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            alternative.category,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = SDSSTypography.labelSmall,
                            color = SDSSColors.Brand500
                        )
                    }
                }
                if (alternative.description.isNotBlank()) {
                    Text(
                        alternative.description,
                        style = SDSSTypography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp),
                        maxLines = 2
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
fun EmptyAlternativeState(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Rounded.People,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Belum Ada Alternatif",
            style = SDSSTypography.titleLarge
        )
        Text(
            "Tambahkan alternatif yang ingin Anda bandingkan nilainya.",
            style = SDSSTypography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onAddClick,
            colors = ButtonDefaults.buttonColors(containerColor = SDSSColors.Brand500),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Tambah Alternatif Pertama →")
        }
    }
}

@Composable
fun AddAlternativeDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Alternatif Baru") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Alternatif") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Kategori (Opsional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Deskripsi/Spesifikasi Ringkas") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = MaterialTheme.shapes.medium
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name, category, description) },
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
