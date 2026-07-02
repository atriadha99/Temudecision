package com.andika.temudecision.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.andika.temudecision.presentation.ui.theme.SDSSColors

@Composable
fun BottomNavBar(
    activeRoute: String,
    hasActiveStudy: Boolean,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        NavigationBarItem(
            selected = activeRoute == "dashboard",
            onClick = { onNavigate("dashboard") },
            icon = { Icon(Icons.Rounded.Dashboard, contentDescription = "Dashboard") },
            label = { Text("Dash") }
        )
        NavigationBarItem(
            selected = activeRoute == "studies",
            onClick = { onNavigate("studies") },
            icon = { Icon(Icons.Rounded.FolderOpen, contentDescription = "Studies") },
            label = { Text("Studi") }
        )
        NavigationBarItem(
            selected = activeRoute == "criteria",
            onClick = { onNavigate("criteria") },
            icon = { Icon(Icons.Rounded.Checklist, contentDescription = "Criteria") },
            label = { Text("Kriteria") },
            enabled = hasActiveStudy
        )
        NavigationBarItem(
            selected = activeRoute == "scores",
            onClick = { onNavigate("scores") },
            icon = { Icon(Icons.Rounded.TableChart, contentDescription = "Scores") },
            label = { Text("Nilai") },
            enabled = hasActiveStudy
        )
        NavigationBarItem(
            selected = activeRoute == "calculate",
            onClick = { onNavigate("calculate") },
            icon = { Icon(Icons.Rounded.Calculate, contentDescription = "Calculate") },
            label = { Text("Hasil") },
            enabled = hasActiveStudy
        )
    }
}
