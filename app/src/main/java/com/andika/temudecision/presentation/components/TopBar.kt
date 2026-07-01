package com.andika.temudecision.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.andika.temudecision.presentation.ui.theme.SDSSColors
import com.andika.temudecision.presentation.ui.theme.SDSSTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    activeStudyName: String?,
    isDarkMode: Boolean,
    onMenuClick: () -> Unit,
    onToggleDarkMode: () -> Unit,
    onStudySelectorClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onStudySelectorClick() }
            ) {
                Text(
                    text = "Studi Kasus: ",
                    style = SDSSTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = activeStudyName ?: "Pilih Studi Kasus",
                    style = SDSSTypography.titleMedium,
                    color = if (activeStudyName != null) SDSSColors.Brand500 else MaterialTheme.colorScheme.onSurface
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Rounded.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            if (activeStudyName != null) {
                Surface(
                    color = SDSSColors.Brand50,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Rounded.BarChart,
                            contentDescription = null,
                            tint = SDSSColors.Brand500,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Model Loaded",
                            style = SDSSTypography.labelSmall,
                            color = SDSSColors.Brand500
                        )
                    }
                }
            }
            IconButton(onClick = onToggleDarkMode) {
                Icon(
                    if (isDarkMode) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
                    contentDescription = "Toggle Dark Mode"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}
