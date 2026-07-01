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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andika.temudecision.data.entity.StudyEntity
import com.andika.temudecision.presentation.components.GlassCard
import com.andika.temudecision.presentation.ui.theme.SDSSColors
import com.andika.temudecision.presentation.ui.theme.SDSSTypography
import com.andika.temudecision.presentation.viewmodel.StudyViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyManagementScreen(
    onStudyClick: (String) -> Unit,
    viewModel: StudyViewModel = hiltViewModel()
) {
    val studies by viewModel.studies.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = SDSSColors.Brand500,
                contentColor = Color.White
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add Study")
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
                "Manajemen Studi Kasus",
                style = SDSSTypography.headlineLarge
            )
            Text(
                "Kelola studi kasus / permasalahan SPK yang ingin Anda analisis.",
                style = SDSSTypography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (studies.isEmpty()) {
                EmptyStudyState { showAddDialog = true }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(studies) { study ->
                        StudyItem(
                            study = study,
                            onClick = { onStudyClick(study.id) },
                            onDelete = { viewModel.deleteStudy(study) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddStudyDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, desc ->
                viewModel.addStudy(name, desc)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun StudyItem(
    study: StudyEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(SDSSColors.Brand50, MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Text(study.icon, fontSize = 24.sp)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    study.name,
                    style = SDSSTypography.titleLarge
                )
                Text(
                    study.description,
                    style = SDSSTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
                Text(
                    "Dibuat: ${dateFormat.format(Date(study.createdAt))}",
                    style = SDSSTypography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            IconButton(onClick = onClick) {
                Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = SDSSColors.Brand500)
            }
            
            IconButton(onClick = onDelete) {
                Icon(Icons.Rounded.Delete, contentDescription = null, tint = SDSSColors.Error)
            }
        }
    }
}

@Composable
fun EmptyStudyState(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Rounded.HelpOutline,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Tidak Ada Studi Kasus",
            style = SDSSTypography.titleLarge
        )
        Text(
            "Buat studi kasus baru untuk memulai analisis.",
            style = SDSSTypography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onAddClick,
            colors = ButtonDefaults.buttonColors(containerColor = SDSSColors.Brand500),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Mulai Buat Studi Kasus →")
        }
    }
}

@Composable
fun AddStudyDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Studi Kasus Baru") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Studi Kasus") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Deskripsi") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = MaterialTheme.shapes.medium
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name, description) },
                enabled = name.length >= 3 && description.length >= 10,
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
